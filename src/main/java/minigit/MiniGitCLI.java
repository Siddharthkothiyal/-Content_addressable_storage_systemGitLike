package minigit;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class MiniGitCLI {
    private Repository repository;
    private final Path workingDir;

    public MiniGitCLI(String workingDir) {
        this.workingDir = Paths.get(workingDir).toAbsolutePath();
        this.repository = new Repository(workingDir);
    }

    public void executeCommand(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            switch (args[0]) {
                case "init":
                    init();
                    break;
                case "add":
                    if (args.length < 2) {
                        System.out.println("Error: File path required for add command");
                        return;
                    }
                    add(args[1]);
                    break;
                case "commit":
                    if (args.length < 3 || !args[1].equals("-m")) {
                        System.out.println("Error: Commit message required (-m \"message\")");
                        return;
                    }
                    commit(args[2]);
                    break;
                case "log":
                    log();
                    break;
                case "diff":
                    if (args.length < 3) {
                        System.out.println("Error: Two file paths required for diff command");
                        return;
                    }
                    diff(args[1], args[2]);
                    break;
                case "checkout":
                    if (args.length < 2) {
                        System.out.println("Error: Commit hash required for checkout");
                        return;
                    }
                    checkout(args[1]);
                    break;
                default:
                    System.out.println("Unknown command: " + args[0]);
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void init() throws IOException {
        repository.initRepo();
        System.out.println("MiniGit initialized.");
    }

    private void add(String filepath) throws IOException {
        repository.addFile(filepath);
        System.out.println("Added " + filepath + " to staging.");
    }

    private void commit(String message) throws IOException {
        String hash = repository.commit(message);
        System.out.println("[Commit " + hash.substring(0, 8) + "] " + message);
    }

    private void log() throws IOException {
        repository.logHistory();
    }

    private void diff(String file1, String file2) throws IOException {
        Path path1 = workingDir.resolve(file1);
        Path path2 = workingDir.resolve(file2);

        if (!Files.exists(path1) || !Files.exists(path2)) {
            System.out.println("Error: One or both files do not exist");
            return;
        }

        List<String> lines1 = Files.readAllLines(path1);
        List<String> lines2 = Files.readAllLines(path2);
        List<String> diff = DiffUtil.computeDiff(lines1, lines2);

        System.out.println("Diff between " + file1 + " and " + file2 + ":");
        for (String line : diff) {
            System.out.println(line);
        }
    }

    private void checkout(String commitHash) throws IOException {
        repository.checkout(commitHash);
        System.out.println("Checked out commit: " + commitHash);
    }

    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  init                    Initialize new repository");
        System.out.println("  add <file>             Add file to staging area");
        System.out.println("  commit -m \"message\"    Create a new commit");
        System.out.println("  log                    Show commit history");
        System.out.println("  diff <file1> <file2>   Show differences between files");
        System.out.println("  checkout <commit-hash>  Restore to a previous commit");
    }
}