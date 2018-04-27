package epuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;

public class Solver {
    private final Board initial;
    private LinkedList<Board> sol;
    private boolean isSolvable;
    private Node goal;

    public Solver(Board initial){
        if (initial == null) throw new IllegalArgumentException();
        this.initial = initial;
        sol = new LinkedList<>();

        Board twin = initial.twin();

        MinPQ<Node> nodes = new MinPQ<>();
        nodes.insert(new Node(initial, 0, null));

        MinPQ<Node> nodes1 = new MinPQ<>();
        nodes1.insert(new Node(twin, 0, null));

        while (!nodes.min().getBoard().isGoal() && !nodes1.min().getBoard().isGoal()){
            Node curr = nodes.delMin();
            for (Board board: curr.getBoard().neighbors()){
                if (curr.getPrevious() != null && !board.equals(curr.getPrevious().getBoard())){
                    nodes.insert(new Node(board, curr.getMoves() + 1, curr));
                }
                else nodes.insert(new Node(board, curr.getMoves() + 1, curr));
            }

            curr = nodes1.delMin();
            for (Board board: curr.getBoard().neighbors()){
                if (curr.getPrevious() != null && !board.equals(curr.getPrevious().getBoard())){
                    nodes1.insert(new Node(board, curr.getMoves() + 1, curr));
                }
                else nodes1.insert(new Node(board, curr.getMoves() + 1, curr));
            }
        }

        Node curr = nodes.delMin();
        if (curr.getBoard().isGoal()){
            goal = curr;
            while (curr.getPrevious() != null){
                sol.addFirst(curr.getBoard());
                curr = curr.getPrevious();
            }
            sol.addFirst(curr.getBoard());
        }
        else goal = null;

}

    public boolean isSolvable(){
        return goal != null;
    }

    public int moves(){
        return sol.size() - 1;
    }

    public Iterable<Board> solution(){
        if (sol == null) return null;
        ArrayList<Board> solution = new ArrayList<>();
        solution.addAll(sol);
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

class Node implements Comparable<Node>{
    private Board board;
    private int priority;
    private int moves;
    private Node previous;

    Node(Board board, int moves, Node previous){
        this.moves = moves;
        this.priority = moves + board.manhattan();
        this.board = board;
        this.previous = previous;
    }
    @Override
    public int compareTo(Node o) {
        return this.priority - o.priority;
    }

    public Board getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public Node getPrevious() {
        return previous;
    }
}
