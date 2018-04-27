package epuzzle;

import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private final int[][] board;
    private int hamming;
    private int manhattan;
    private final int n;
    private boolean isGoal;
    private final int[] blank;

    public Board(int[][] board){
        this.board = new int[board.length][board[0].length];
        for (int i = 0; i < this.board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, this.board[i].length);
        }

        this.n = board.length;
        this.blank = new int[2];
        this.isGoal = true;
        hamming = 0;
        manhattan = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (board[i][j] != 0 && (n*i + j + 1) % (n*n) != board[i][j]){
                    hamming++;
                    isGoal = false;
                }
                int finalJ = (board[i][j] - 1) % n;
                int finalI = (board[i][j] - 1)/n;
                if (board[i][j] != 0) manhattan += Math.abs(finalI - i) + Math.abs(finalJ - j);
                if (board[i][j] == 0){
                    blank[0] = i;
                    blank[1] = j;
                }
            }
        }
    }

    public int dimension(){
        return n;
    }

    public int hamming(){
        return hamming;
    }

    public int manhattan(){
        return manhattan;
    }

    public boolean isGoal(){
        return isGoal;
    }

    public Board twin(){
        int i1 = 0;
        int j1 = 0;
        int i2 = 0;
        int j2 = 0;

        int[][] newBoard = new int[n][n];
        boolean found1 = false;
        boolean found2 = false;
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] = board[i][j];
                if (!found1 && board[i][j] != 0){
                    i1 = i; j1 = j;
                    found1 = true;
                }
                else if (found1 && !found2 && board[i][j] != 0){
                    i2 = i; j2 = j;
                    found2 = true;
                }
            }
        }

        exch(newBoard, i1, j1, i2, j2);
        return new Board(newBoard);
    }

    public boolean equals(Object y) {
        return y != null && y.getClass() == this.getClass() && this.toString().equals(y.toString());
    }

    public Iterable<Board> neighbors(){
        ArrayList<Board> boards = new ArrayList<>();

        Board neighbor = move("up");
        if (neighbor != null) boards.add(neighbor);

        neighbor = move("down");
        if (neighbor != null) boards.add(neighbor);

        neighbor = move("left");
        if (neighbor != null) boards.add(neighbor);

        neighbor = move("right");
        if (neighbor != null) boards.add(neighbor);

        return boards;
    }

    public String toString(){
        StringBuilder ans = new StringBuilder();
        ans.append(n).append("\n");
        for (int[] aBoard : board) {
            for (int anABoard : aBoard) ans.append(anABoard).append(" ");
            ans.append("\n");
        }

        return ans.toString();
    }

    private Board move(String direction){
        int[][] newBoard = new int[n][n];
        for (int i = 0; i < newBoard.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, newBoard[i].length);
        }

        switch (direction) {
            case "up":
                if (blank[0] - 1 >= 0) {
                    exch(newBoard, blank[0], blank[1], blank[0] - 1, blank[1]);
                    return new Board(newBoard);
                } else return null;
            case "down":
                if (blank[0] + 1 < n) {
                    exch(newBoard, blank[0], blank[1], blank[0] + 1, blank[1]);
                    return new Board(newBoard);
                } else return null;
            case "left":
                if (blank[1] - 1 >= 0) {
                    exch(newBoard, blank[0], blank[1], blank[0], blank[1] - 1);
                    return new Board(newBoard);
                } else return null;
            default:
                if (blank[1] + 1 < n){
                    exch(newBoard, blank[0], blank[1], blank[0], blank[1] + 1);
                    return new Board(newBoard);
                } else return null;
        }
    }

    private void exch(int[][] board, int i1, int j1, int i2, int j2){
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dim = scanner.nextInt();
        int[][] ba = new int[dim][dim];
        for (int i = 0; i < ba.length; i++) {
            for (int j = 0; j < ba[i].length; j++) {
                ba[i][j] = scanner.nextInt();
            }
        }
        Board board = new Board(ba);
        System.out.println(board.manhattan);
        System.out.println(board.twin().toString());
    }
}
