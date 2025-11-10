package minigit;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Index {
    private final Map<String, String> stagedFiles; // filepath → blobHash
    private final Path indexFile;
    
    public Index(Path gitDir) {
        this.stagedFiles = new HashMap<>();
        this.indexFile = gitDir.resolve("index");
    }
    
    public void addFile(String filePath, String blobHash) {
        stagedFiles.put(filePath, blobHash);
        try {
            saveIndex();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save index", e);
        }
    }
    
    public void removeFile(String filePath) {
        stagedFiles.remove(filePath);
        try {
            saveIndex();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save index", e);
        }
    }
    
    public Set<String> listFiles() {
        return new HashSet<>(stagedFiles.keySet());
    }
    
    public Map<String, String> getStagedFiles() {
        return new HashMap<>(stagedFiles);
    }
    
    public void clear() {
        stagedFiles.clear();
        try {
            saveIndex();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save index", e);
        }
    }
    
    public void saveIndex() throws IOException {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry : stagedFiles.entrySet()) {
            content.append(entry.getKey())
                   .append(" ")
                   .append(entry.getValue())
                   .append("\n");
        }
        Files.writeString(indexFile, content.toString());
    }
    
    public void loadIndex() throws IOException {
        stagedFiles.clear();
        if (!Files.exists(indexFile)) {
            return;
        }
        
        List<String> lines = Files.readAllLines(indexFile);
        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length == 2) {
                stagedFiles.put(parts[0], parts[1]);
            }
        }
    }
    
    public boolean hasChanges() {
        return !stagedFiles.isEmpty();
    }
}