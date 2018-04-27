import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final TST<Boolean> dictionary;
    private boolean[][] seen;
    private BoggleBoard board;
    private Set<String> validWords;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TST<>();
        for (String word: dictionary) this.dictionary.put(word, true);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        validWords = new HashSet<>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                seen = new boolean[board.rows() + 1][board.cols() + 1];
                dfs(i, j, new StringBuilder());
            }
        }

        ArrayList<String> ans = new ArrayList<>();
        ans.addAll(validWords);
        Collections.sort(ans);
        return ans;
    }

    private void dfs(int i, int j, StringBuilder currWord) {
        seen[i][j] = true;
        char letter = board.getLetter(i, j);
        currWord.append(letter == 'Q' ? "QU" : letter);
        int poss = 0;

//        System.out.println(currWord);
        if (currWord.length() >= 3 && dictionary.contains(currWord.toString())) validWords.add(currWord.toString());
        for (String s: dictionary.keysWithPrefix(currWord.toString())) {
            poss++;
            break;
        }

        if (poss == 0) {
            seen[i][j] = false;
            return;
        }

        if (i > 0 && j > 0 && !seen[i - 1][j - 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i - 1, j - 1, copy);
        }
        if (i > 0 && !seen[i - 1][j]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i - 1, j, copy);
        }
        if (i > 0 && j < board.cols() - 1 && !seen[i - 1][j + 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i - 1, j + 1, copy);
        }
        if (j > 0 && !seen[i][j - 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i, j - 1, copy);
        }
        if (j < board.cols() - 1 && !seen[i][j + 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i, j + 1, copy);
        }
        if (i < board.rows() - 1 && j > 0 && !seen[i + 1][j - 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i + 1, j - 1, copy);
        }
        if (i < board.rows() - 1 && !seen[i + 1][j]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i + 1, j, copy);
        }
        if (i < board.rows() - 1 && j < board.cols() - 1 && !seen[i + 1][j + 1]) {
            StringBuilder copy = new StringBuilder(currWord);
            dfs(i + 1, j + 1, copy);
        }
        seen[i][j] = false;
    }

    public int scoreOf(String word) {
        if (!dictionary.contains(word) || word.length() < 3) return 0;
        if (word.length() == 3) return 1;
        if (word.length() == 7) return 5;
        if (word.length() >= 8) return 11;
        return word.length() - 3;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
