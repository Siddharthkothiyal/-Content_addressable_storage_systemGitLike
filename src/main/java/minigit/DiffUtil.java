package minigit;

import java.util.*;

public class DiffUtil {
    public static List<String> computeDiff(List<String> file1Lines, List<String> file2Lines) {
        List<String> diff = new ArrayList<>();
        int[][] lcs = computeLCSMatrix(file1Lines, file2Lines);
        
        int i = file1Lines.size();
        int j = file2Lines.size();
        
        Stack<String> changes = new Stack<>();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && file1Lines.get(i-1).equals(file2Lines.get(j-1))) {
                i--;
                j--;
            } else if (j > 0 && (i == 0 || lcs[i][j-1] >= lcs[i-1][j])) {
                changes.push("+ " + file2Lines.get(j-1));
                j--;
            } else if (i > 0 && (j == 0 || lcs[i][j-1] < lcs[i-1][j])) {
                changes.push("- " + file1Lines.get(i-1));
                i--;
            }
        }
        
        while (!changes.isEmpty()) {
            diff.add(changes.pop());
        }
        
        return diff;
    }
    
    private static int[][] computeLCSMatrix(List<String> file1Lines, List<String> file2Lines) {
        int m = file1Lines.size();
        int n = file2Lines.size();
        int[][] lcs = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (file1Lines.get(i-1).equals(file2Lines.get(j-1))) {
                    lcs[i][j] = lcs[i-1][j-1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i-1][j], lcs[i][j-1]);
                }
            }
        }
        
        return lcs;
    }
}