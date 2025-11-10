package minigit;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Repository {
    private final Path rootPath;
    private final Path gitDir;
    private final Path objectsDir;
    private final Map<String, String> stagingArea;
    private String head;

    public Repository(String path) {
        this.rootPath = Paths.get(path).toAbsolutePath();
        this.gitDir = rootPath.resolve(".minigit");
        this.objectsDir = gitDir.resolve("objects");
        this.stagingArea = new HashMap<>();
        this.head = null;
    }

    public void initRepo() throws IOException {
        if (Files.exists(gitDir)) {
            throw new IllegalStateException("Repository already initialized");
        }

        Files.createDirectories(objectsDir);
        head = null;
        writeRef("HEAD", head);
    }

    public void addFile(String filepath) throws IOException {
        Path absolutePath = rootPath.resolve(filepath);
        if (!Files.exists(absolutePath)) {
            throw new IllegalArgumentException("File does not exist: " + filepath);
        }

        // Create blob from file content
        String content = Files.readString(absolutePath);
        String hash = createBlob(content);
        stagingArea.put(filepath, hash);
    }

    public String commit(String message) throws IOException {
        if (stagingArea.isEmpty()) {
            throw new IllegalStateException("Nothing to commit");
        }

        // Create tree object from staging area
        StringBuilder treeContent = new StringBuilder();
        for (Map.Entry<String, String> entry : stagingArea.entrySet()) {
            treeContent.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        String treeHash = createBlob(treeContent.toString());

        // Create commit object
        Commit commit = new Commit(head, message, treeHash);
        String commitContent = commit.serialize();
        String commitHash = createBlob(commitContent);
        commit.setCommitHash(commitHash);
        
        head = commitHash;
        writeRef("HEAD", head);
        stagingArea.clear();

        return commitHash;
    }

    public void logHistory() throws IOException {
        String currentCommit = head;
        while (currentCommit != null) {
            String commitContent = readObject(currentCommit);
            Commit commit = Commit.deserialize(commitContent, currentCommit);
            
            System.out.println("Commit: " + commit.getCommitHash());
            System.out.println("Message: " + commit.getMessage());
            System.out.println("Date: " + commit.getTimestamp());
            System.out.println();

            currentCommit = commit.getParentHash();
        }
    }

    public void checkout(String commitHash) throws IOException {
        String commitContent = readObject(commitHash);
        Commit commit = Commit.deserialize(commitContent, commitHash);
        String treeHash = commit.getTreeHash();

        // Read tree and restore files
        String treeContent = readObject(treeHash);
        try (Scanner scanner = new Scanner(treeContent)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                String filepath = parts[0];
                String blobHash = parts[1];
                
                String content = readObject(blobHash);
                Path filePath = rootPath.resolve(filepath);
                Files.writeString(filePath, content);
            }
        }

        head = commitHash;
        writeRef("HEAD", head);
    }

    private String createBlob(String content) throws IOException {
        String hash = sha1(content);
        Path objectPath = objectsDir.resolve(hash);
        Files.writeString(objectPath, content);
        return hash;
    }

    private String readObject(String hash) throws IOException {
        Path objectPath = objectsDir.resolve(hash);
        return Files.readString(objectPath);
    }

    private void writeRef(String refName, String hash) throws IOException {
        Path refFile = gitDir.resolve(refName);
        if (hash == null) {
            Files.deleteIfExists(refFile);
        } else {
            Files.writeString(refFile, hash);
        }
    }

    private String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
}