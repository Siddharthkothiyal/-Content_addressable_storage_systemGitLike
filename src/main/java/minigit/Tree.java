package minigit;

import java.util.*;

public class Tree {
    private List<Entry> entries;
    
    public Tree() {
        this.entries = new ArrayList<>();
    }
    
    public static class Entry {
        private final String mode; // "100644" for file, "040000" for directory
        private final String type; // "blob" or "tree"
        private final String hash;
        private final String name;
        
        public Entry(String mode, String type, String hash, String name) {
            this.mode = mode;
            this.type = type;
            this.hash = hash;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return String.format("%s %s %s %s", mode, type, hash, name);
        }
    }
    
    public void addEntry(String mode, String type, String hash, String name) {
        entries.add(new Entry(mode, type, hash, name));
    }
    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : entries) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
    
    public static Tree deserialize(String content) {
        Tree tree = new Tree();
        Scanner scanner = new Scanner(content);
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length == 4) {
                tree.addEntry(parts[0], parts[1], parts[2], parts[3]);
            }
        }
        scanner.close();
        
        return tree;
    }
    
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}