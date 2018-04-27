package Graphs;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.Stack;

public class ShortestPaths {

    public static void main(String[] args) {

    }
}

/**
 * This implementation of Dijkstra's algorithm grows the Shortest Paths Tree by adding an edge at a time,
 * always choosing the edge from a tree vertex to a non-tree vertex whose destination w is closest to s.
 * */
class DijkstraSP {
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<>(G.V());

        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq.insert(s, 0.0);
        while (!pq.isEmpty()) relax(G, pq.delMin());
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e: G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.changeKey(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) path.push(e);
        return path;
    }
}

/**
 * All pairs shortest paths.
 * */
class DijkstraAllPairsSP {
    private DijkstraSP[] all;

    DijkstraAllPairsSP(EdgeWeightedDigraph G) {
        all = new DijkstraSP[G.V()];
        for (int v = 0; v < G.V(); v++) all[v] = new DijkstraSP(G, v);
    }

    Iterable<DirectedEdge> path(int s, int t) {
        return all[s].pathTo(t);
    }

    double dist(int s, int t) {
        return all[s].distTo(t);
    }
}

/**
 * This shortest-paths algorithm for edge-weighted DAGs uses a topological sort to enable it to relax the vertices in topological order
 * which is all that is needed to compute shortest paths.
 * */
class AcyclicSP {
    private edu.princeton.cs.algs4.DirectedEdge[] edgeTo;
    private double[] distTo;

    public AcyclicSP(edu.princeton.cs.algs4.EdgeWeightedDigraph G, int s) {
        edgeTo = new edu.princeton.cs.algs4.DirectedEdge[G.V()];
        distTo = new double[G.V()];

        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        Topological top = new Topological(G);

        for (int v: top.order()) relax(G, v);
    }

    private void relax(edu.princeton.cs.algs4.EdgeWeightedDigraph G, int v) {
        for (edu.princeton.cs.algs4.DirectedEdge e: G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<edu.princeton.cs.algs4.DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<edu.princeton.cs.algs4.DirectedEdge> path = new Stack<>();
        for (edu.princeton.cs.algs4.DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) path.push(e);
        return path;
    }

}

class EdgeWeightedDigraph {
    private final int V;
    private int E;
    private ArrayList<DirectedEdge>[] adj;

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = E;
        adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) adj[v] = new ArrayList<>();
    }

    public int V() { return V; }
    public int E(){ return E; }

    public void addEdge(DirectedEdge e){
        adj[e.from()].add(e);
        E++;
    }

    public Iterable<DirectedEdge> adj(int v){
        return adj[v];
    }

    public Iterable<DirectedEdge> edges(){
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        for (int v = 0; v < V; v++) edges.addAll(adj[v]);
        return edges;
    }
}


class DirectedEdge {
    private final int v;
    private final int w;
    private final double weight;

    public DirectedEdge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int from(){
        return v;
    }

    public int to(){
        return w;
    }

    @Override
    public String toString() {
        return String.format("%d->%d %.2f", v, w, weight);
    }
}
