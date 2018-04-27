package Graphs;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class MinimumSpanningTrees {

    public static void main(String[] args) {

    }
}


/**
 * This implementation of Prim's algorithm uses a priority queue to hold crossing edges.
 * It uses a vertex-indexed array to mark vertices, and a queue to hold MST edges.
 * This implementation is a lazy approach where we leave ineligible edges in the priority queue.
 * */
class LazyPrimMST{
    private boolean[] marked;
    private Queue<Edge> mst;
    private MinPQ<Edge> pq;

    public LazyPrimMST(EdgeWeightedGraph G){
        pq = new MinPQ<>();
        marked = new boolean[G.V()];
        mst = new Queue<>();

        visit(G, 0);
        while (!pq.isEmpty()){
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            if (marked[v] && marked[w]) continue;
            if (!marked[v]) visit(G, w);
            if (!marked[w]) visit(G, w);
        }
    }

    private void visit(EdgeWeightedGraph G, int v){
        //Mark v and to pq all edges with unmarked vertices.
        marked[v] = true;
        for (Edge e: G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e);
    }

    public Iterable<Edge> edges(){
        return mst;
    }

//    public double weight() TODO:Implement this. See exercise 4.3.31.
}

/**
 * Representation of a weighted edge.
 * */
class Edge implements Comparable<Edge>{
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight){
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight(){
        return weight;
    }

    public int either(){
        return v;
    }

    public int other(int vertex){
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Inconsistent edge");
    }

    @Override
    public int compareTo(Edge that) {
        if (this.weight > that.weight) return 1;
        else if (this.weight < that.weight) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d - %d %.2f", v, w, weight);
    }
}

class EdgeWeightedGraph{
    private final int V;
    private int E;
    private ArrayList<Edge>[] adj;

    public EdgeWeightedGraph(int V){
        this.V = V;
        this.E = 0;
        adj = (ArrayList<Edge>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    public void addEdge(Edge e){
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public int V(){
        return V;
    }

    public int E(){
        return E;
    }

    public Iterable<Edge> adj(int v){
        return adj[v];
    }

    public Iterable<Edge> edges(){
        ArrayList<Edge> b = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            for (Edge e: adj[v])
                if (e.other(v) > v) b.add(e);
        }
        return b;
    }
}
