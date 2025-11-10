package minigit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Commit {
    private String commitHash;
    private String parentHash;
    private String message;
    private String treeHash;
    private LocalDateTime timestamp;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Commit(String parentHash, String message, String treeHash) {
        this.parentHash = parentHash;
        this.message = message;
        this.treeHash = treeHash;
        this.timestamp = LocalDateTime.now();
    }

    public String serialize() {
        StringBuilder content = new StringBuilder();
        content.append("tree ").append(treeHash).append("\n");
        if (parentHash != null) {
            content.append("parent ").append(parentHash).append("\n");
        }
        content.append("message ").append(message).append("\n");
        content.append("date ").append(timestamp.format(formatter)).append("\n");
        return content.toString();
    }

    public static Commit deserialize(String content, String hash) {
        String[] lines = content.split("\n");
        String treeHash = null;
        String parentHash = null;
        String message = null;
        LocalDateTime date = null;

        for (String line : lines) {
            if (line.startsWith("tree ")) {
                treeHash = line.substring(5);
            } else if (line.startsWith("parent ")) {
                parentHash = line.substring(7);
            } else if (line.startsWith("message ")) {
                message = line.substring(8);
            } else if (line.startsWith("date ")) {
                date = LocalDateTime.parse(line.substring(5), formatter);
            }
        }

        if (treeHash == null || message == null || date == null) {
            throw new IllegalArgumentException("Invalid commit content");
        }

        Commit commit = new Commit(parentHash, message, treeHash);
        commit.setCommitHash(hash);
        commit.setTimestamp(date);
        return commit;
    }

    // Getters and setters
    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getMessage() {
        return message;
    }

    public String getTreeHash() {
        return treeHash;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}