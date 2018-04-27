package Graphs;

import edu.princeton.cs.algs4.*;

import java.io.IOException;
import java.util.Stack;

public class Digraphs {

    public static void main(String[] args) throws IOException{
        Reader.init(System.in);

        int v = Reader.nextInt();
        int e = Reader.nextInt();

        Digraph G = new Digraph(v);
        for (int i = 0; i < e; i++) G.addEdge(Reader.nextInt(), Reader.nextInt());

        DirectedDFS recheable = new DirectedDFS(G, 9);
        for (int i = 0; i < v; i++) if (recheable.marked(i)) System.out.print(i + " ");

    }
}

class Digraph{
    private final int V;
    private int E;
    private Bag<Integer>[] adj;

    public Digraph(int V){
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new Bag<>();
    }

    public int V(){ return V;}
    public int E(){ return E;}

    public void addEdge(int v, int w){
        adj[v].add(w);
        E++;
    }

    public Iterable<Integer> adj(int v) {return adj[v];}
    public Digraph reverse(){
        Digraph R = new Digraph(V);

        for (int v = 0; v < V; v++)
            for (int w: adj(v))
                R.addEdge(w, v);

        return R;
    }

}

class DirectedDFS{
    private boolean[] marked;

    public DirectedDFS(Digraph G, int s){
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources){
        for (int w: sources) if (!marked[w])dfs(G, w);
    }

    public void dfs(Digraph G,  int s){
        marked[s] = true;
        for (int w: G.adj(s)) if (!marked[w])dfs(G, w);     
    }

    public boolean marked(int v){ return marked[v];}
}

/**
 * This returns a topological order for a DAG.
 * */
class Topological{
    private Iterable<Integer> order;
    Topological(Digraph G){
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (!cycleFinder.hasCycle()){
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.postorder();
        }
    }

    Iterable<Integer> getOrder(){return order;}

    boolean isDAG(){return order != null;}
}


/**
 * This class enables clients to iterate through the vertices in various orders defined by depth-first search.
 * */
class DepthFirstOrder{
    private boolean[] marked;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;

    DepthFirstOrder(Digraph G){
        pre = new Queue<>();
        post = new Queue<>();
        reversePost = new Stack<>();
        marked = new boolean[G.V()];

        for (int i = 0; i < G.V(); i++) if (!marked[i]) dfs(G, i);
    }

    private void dfs(Digraph G, int v){
        pre.enqueue(v);
        marked[v] = true;
        for (int w: G.adj(v)){
            if (!marked[w]) dfs(G, w);
        }
        post.enqueue(v);
        reversePost.push(v);
    }

    public Iterable<Integer> preorder(){return pre;}

    public Iterable<Integer> postorder(){return post;}

    public Iterable<Integer> reversepost(){return reversePost;}
}

/**
 * Detects cycles in directed graphs.
 * */
class DirectedCycle{
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;
    private boolean[] onStack;

    DirectedCycle(Digraph G){
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        for (int i = 0; i < G.V(); i++) if (!marked[i]) dfs(G, i);
    }

    private void dfs(Digraph G, int v){
        onStack[v] = true;
        marked[v] = true;
        for (int w: G.adj(v)) {
            if (this.hasCycle()) return;
            else if (!marked[w]){
                edgeTo[w] = v;
                dfs(G, v);
            }
            else if (onStack[w]){
                cycle = new Stack<>();
                for (int i = v; i != w; i = edgeTo[i]) cycle.push(i);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    boolean hasCycle(){return cycle != null;}

    Iterable<Integer> cycle(){return cycle;}
}

/**
 * Kosaraju's Algorithm for computing strong components
 * */
class KosarajuSCC{
    private boolean[] marked;
    private int[] id;
    private int count;

    KosarajuSCC(Digraph G){
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder order = new DepthFirstOrder(G.reverse());
        for (int s: order.reversepost()){
            if (!marked[s]){
                dfs(G, s);
                count++;
            }
        }
    }

    private void dfs(Digraph G, int v){
        marked[v] = true;
        id[v] = count;
        for (int w: G.adj(v)){
            if (!marked[w]) dfs(G, w);
        }
    }

    boolean stronglyConnected(int v, int w){
        return id[v] == id[w];
    }

    int id(int v){
        return id[v];
    }

    int count(){
        return count;
    }
}

/**
 * The transitive closure of a digraph G is another digraph with the same set of vertices, but with an edge from v to w in the transitive
 * closure iff w is reachable from v in G.
 * */
class TransitiveClosure{
    private DirectedDFS[] all;
    TransitiveClosure(Digraph G){
        all = new DirectedDFS[G.V()];
        for (int i = 0; i < G.V(); i++) all[i] = new DirectedDFS(G, i);
    }

    boolean reachable(int v, int w){
        return all[v].marked(w);
    }
}