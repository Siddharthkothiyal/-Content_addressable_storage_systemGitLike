package minigit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a test directory
            Path testDir = Paths.get("test_repo");
            Files.createDirectories(testDir);

            // Initialize repository
            Repository repo = new Repository(testDir.toString());
            repo.initRepo();
            System.out.println("Repository initialized");

            // Create a test file
            Path testFile = testDir.resolve("test.txt");
            Files.writeString(testFile, "Initial content");
            
            // Add and commit the file
            repo.addFile("test.txt");
            String firstCommit = repo.commit("Initial commit");
            System.out.println("First commit: " + firstCommit);

            // Modify the file
            Files.writeString(testFile, "Modified content");
            repo.addFile("test.txt");
            String secondCommit = repo.commit("Modified test.txt");
            System.out.println("Second commit: " + secondCommit);

            // Show commit history
            System.out.println("\nCommit History:");
            repo.logHistory();

            // Checkout first commit
            System.out.println("\nChecking out first commit...");
            repo.checkout(firstCommit);
            System.out.println("Content after checkout: " + Files.readString(testFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}