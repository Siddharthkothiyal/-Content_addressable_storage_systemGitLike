package minigit;

import java.io.*;
import java.nio.file.*;

public class Blob {
    private String filePath;
    private byte[] content;
    private String blobHash;
    
    public Blob(String filePath, byte[] content) {
        this.filePath = filePath;
        this.content = content;
        this.blobHash = computeSHA1();
    }
    
    public String computeSHA1() {
        return HashUtil.generateSHA1(content);
    }
    
    public void saveBlob(Path objectsDir) throws IOException {
        Path blobPath = objectsDir.resolve(blobHash);
        Files.write(blobPath, content);
    }
    
    public static Blob loadBlob(Path objectsDir, String hash) throws IOException {
        Path blobPath = objectsDir.resolve(hash);
        byte[] content = Files.readAllBytes(blobPath);
        return new Blob("", content);
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public byte[] getContent() {
        return content;
    }
    
    public String getBlobHash() {
        return blobHash;
    }
    
    public String getContentAsString() {
        return new String(content);
    }
}