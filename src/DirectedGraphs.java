import java.util.LinkedList;
import java.util.Stack;

public class DirectedGraphs {

    public static void main(String[] args) {
        
    }
}

class Digraph{
    private final int V;
    private int E;
    private LinkedList<Integer>[] adj;

    public Digraph(int V){
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Integer>[]) new LinkedList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<>();
        }
    }

    public int V(){
        return V;
    }

    public int E(){
        return E;
    }

    public void addEdege(int v, int w){
        adj[v].add(w);
        E++;
    }

    public Iterable<Integer> adj(int v){
        return adj[v];
    }

    public Digraph reverse(){
        Digraph R = new Digraph(V);
        for (int i = 0; i < V; i++) {
            for (int w: adj(i))R.addEdege(w, i);
        }

        return R;
    }
}

/**
 * This implementation of DFS provides clients the ability to test which vertices are reachable from a given vertex or a given set of vertices.
 * */
class DirectedDFS{
    private boolean[] marked;

    public DirectedDFS(Digraph G, int s){
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources){
        marked = new boolean[G.V()];
        for (int v: sources) {
            if (!marked[v])dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v){
        marked[v] = true;
        for(int w: G.adj(v)){
            if (!marked[w])dfs(G, w);
        }
    }

    public boolean marked(int v){
        return marked[v];
    }
}

/**
 * Finding a directed cycle.
 * This class uses the boolean array onStack[] to keep track of the vertices for which the recursive call has not completed.
 * When it finds an edge v -> w to a vertex w that is on the stack, it has discovered a directed cycle, which it can recover
 * by following edgeTo[] links.
 * */
class DirectedCycle{
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;
    private boolean[] onStack;

    public DirectedCycle(Digraph G){
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v){
        onStack[v] = true;
        marked[v] = true;
        for (int w: G.adj(v)) {
            if (this.hasCycle()) return;
            else if (!marked[w]){
                edgeTo[w] = v;
                dfs(G, w);
            }
            else if(onStack[w]){
                cycle = new Stack<>();
                for (int i = v; i != w; i = edgeTo[i]) cycle.push(i);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle(){
        return cycle != null;
    }

    public Iterable<Integer> cycle(){
        return cycle;
    }
}