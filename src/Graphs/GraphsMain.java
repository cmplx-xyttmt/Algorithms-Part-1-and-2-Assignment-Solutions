package Graphs;

import java.io.*;
import java.util.LinkedList;
import edu.princeton.cs.algs4.*;
import java.util.Stack;

public class GraphsMain {

    public static void main(String[] args) throws IOException {
//        File file = new File("C:\\Users\\Isaac o\\Desktop\\MY STUFF\\Programs\\SedgewickAlgorithms\\algs4-data\\tinyG.txt");
//        Reader.init(new FileInputStream(file));
//        Graphs.Graph graph = new Graphs.Graph();
//        System.out.println(graph.toString());
        Reader.init(System.in);
        Graph graph = new Graph();
        Cycle testCycle = new Cycle(graph);
        System.out.println(testCycle.hasCycle());
    }

    /**
     * Some client code for the Graphs.Graph interface
     * */

    public static int degree(Graph G, int v){
        int degree = 0;
        for (int w: G.adj(v)) degree++;
        return degree;
    }

    public static int maxDegree(Graph G){
        int max = 0;
        for (int v = 0; v < G.V(); v++)
            if (degree(G, v) > max) max = degree(G, v);

        return max;
    }

    public static int avgDegree(Graph G){
        return 2 * G.E() / G.V();
    }

    public static int numberOfSelfLoops(Graph G){
        int count = 0;
        for (int v = 0; v < G.V(); v++) {
            for (int w: G.adj(v)) if (v == w) count++;
        }

        return count/2; //Each edge is counted twice
    }
}

/**
 * Symbol graph data type.
 * This graph client allows clients to define graphs with String vertex names instead of integer indices.
 *
 * (See the section about "Degrees of Separation" for an application of this.)
 * */
class SymbolGraph{
    private ST<String, Integer> st;
    private String[] keys;
    private Graph G;

    public SymbolGraph(String stream, String sp){
        st = new ST<>();
        In in = new In(stream);
        while (in.hasNextLine()){
            String[] a = in.readLine().split(sp);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }                                   
        }

        keys = new String[st.size()];
        for (String name: st.keys())
            keys[st.get(name)] = name;

        G = new Graph(st.size());
        in = new In(stream);
        while (in.hasNextLine()){
            String[] a = in.readLine().split(sp);
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                G.addEdge(v, st.get(a[i]));
            }
        }
    }

    public boolean contains(String s){
        return st.contains(s);
    }

    public int index(String s){
        return st.get(s);
    }

    public String name(int v){
        return keys[v];
    }

    public Graph G(){
        return G;
    }
}

/**
 * Determines whether a graph is bipartite i.e it's 2-colorable
 * (can be colored with 2 colors such that no 2 vertices with an edge between them have the same color).
 * */
class TwoColor{
    private boolean[] marked;
    private boolean[] color;
    private boolean isTwoColorable = true;

    public TwoColor(Graph G){
        marked = new boolean[G.V()];
        color = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s])
                dfs(G, s);
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w: G.adj(v)){
            if (!marked[w]){
                color[w] = !color[v];
                dfs(G, w);
            }
            else if (color[w] == color[v]) isTwoColorable = false;
        }
    }

    public boolean isBipartite(){
        return isTwoColorable;
    }
}

/**
 * Determines whether a graph G is acyclic.
 * */
class Cycle{
    private boolean[] marked;
    private boolean hasCycle;

    public Cycle(Graph G){
        marked = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s])
                dfs(G, s, s);
        }
    }

    private void dfs(Graph G, int v, int u) {
        marked[v] = true;
        for (int w: G.adj(v)){
//            System.out.println(v + " is connected to " + w + " and " + w + " " + (marked[w]?"has been seen":"hasn't been seen yet"));
            if (!marked[w])
                dfs(G, w, v);
            else if (w != u) hasCycle = true;
        }
    }

    public boolean hasCycle(){
        return hasCycle;
    }
}

/**
 * Depth-first search to find connected components in a graph.
 * */
class ConnectedComponents{
    private boolean[] marked;
    private int[] id;
    private int count; //Number of connected components.

    public ConnectedComponents(Graph G){
        marked = new boolean[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]){
                dfs(G, s);
                count++;
            }
        }
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;

        for(int w: G.adj(v))
            if (!marked[w]) dfs(G, w);
    }

    public boolean connected(int v, int w){
        return id[v] == id[w];
    }

    public int id(int v) {
        return id[v];
    }

    public int count(){return count;}
}

/**
 * Depth First Search.
 * */
class DepthFirstSearch{
    private boolean[] marked;
    private int count;

    public DepthFirstSearch(Graph G, int s){
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;

        for(int w: G.adj(v))
            if (!marked[w]) dfs(G, w);
    }

    public boolean marked(int v){return marked[v];}

    public int count(){return count;}
}

/**
 * Depth-first search to find paths in a graph.
 * This uses breadth-first search to find paths in a graph with the fewest number of edges from the source s given in the constructor.
 * */
class BreadthFirstPaths{
    private boolean[] marked;
    private final int s;
    private int[] edgeTo;

    public BreadthFirstPaths(Graph G, int s){
        marked = new boolean[G.V()];
        this.s = s;
        edgeTo = new int[G.V()];
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        marked[s] = true;
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.enqueue(w);
                }
            }
        }
    }
    
    public boolean hasPathTo(int v){return marked[v];}

    public Iterable<Integer> pathTo(int v){
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }

        path.push(s);
        return path;
    }
}

class DepthFirstPaths{
    private boolean[] marked;
    private final int s;
    private int[] edgeTo;

    public DepthFirstPaths(Graph G, int s){
        marked = new boolean[G.V()];
        this.s = s;
        edgeTo = new int[G.V()];
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {

        marked[v] = true;

        for(int w: G.adj(v))
            if (!marked[w]){
                edgeTo[w] = v;
                dfs(G, w);
            }
    }

    public boolean hasPathTo(int v){return marked[v];}

    public Iterable<Integer> pathTo(int v){
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }

        path.push(s);
        return path;
    }

}

class Graph implements GraphInterface{
    private final int V; // number of vertices
    private int E;
    private LinkedList<Integer>[] adj;


    /**
     * Create a V-vertex graph with no edges.
     * */
    public Graph(int V){
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Integer>[])new LinkedList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<>();
        }
    }

    /**
     * Read a graph from input stream.
     * Assumes the input is in the format where the first line has number of vertices, V and number of edges, E.
     * The next E lines consist of 2 numbers denoting an edge in the graph.
     * */
    public Graph() throws IOException{
        this(Reader.nextInt());
        int E = Reader.nextInt();
        for (int i = 0; i < E; i++) {
            int v = Reader.nextInt();
            int w = Reader.nextInt();
            addEdge(v, w);
        }
    }

    @Override
    public int V() {
        return V;
    }

    @Override
    public int E() {
        return E;
    }

    @Override
    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    @Override
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(V + " vertices, " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (int w: this.adj(v))
                s.append(w).append(" ");
            s.append("\n");
        }

        return s.toString();
    }
}

/**
 * This interface defines an Undirected Graphs.Graph data type.
 * */
interface GraphInterface{
    /**
     * Number of vertices
     * */
    int V();

    /**
     * Number of edges.
     * */
    int E();

    /**
     * Add edge v-w to this graph.
     * */
    void addEdge(int v, int w);

    /**
     * Vertices adjacent to v.
     * */
    Iterable<Integer> adj(int v);
}