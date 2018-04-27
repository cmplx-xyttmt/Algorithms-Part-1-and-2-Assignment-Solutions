import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
    private Digraph digraph;
    private int[] distV;
    private int[] distW;
    private int length;

    public SAP(Digraph G){
        digraph = G;
    }

    public int length(int v, int w){
        if (v < 0 || w < 0 || v >= digraph.V() || w >= digraph.V()) throw new IllegalArgumentException();
        if (ancestor(v, w) == -1) return -1;
        else return length;
    }

    public int ancestor(int v, int w){
        if (v < 0 || w < 0 || v >= digraph.V() || w >= digraph.V()) throw new IllegalArgumentException();
        distV = new int[digraph.V()];
        distW = new int[digraph.V()];
        bfs(v, true); bfs(w, false);
        int ancestor = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < distV.length; i++) {
            int dV = distV[i], dW = distW[i];
            if (i != v && dV == 0) continue;
            if (i != w && dW == 0) continue;
            if (dV + dW < minDistance){
                minDistance = dV + dW;
                ancestor = i;
            }
        }
        length = minDistance;
        return ancestor;
    }

    private void bfs(int v, boolean which){
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(v);

        while (!queue.isEmpty()){
            int nextV = queue.dequeue();

            for (int u: digraph.adj(nextV)){
                if (which && distV[u] == 0 && u != v) {
                    distV[u] = distV[nextV] + 1;
                    queue.enqueue(u);
                }
                else if (!which && distW[u] == 0 && u != v){
                    distW[u] = distW[nextV] + 1;
                    queue.enqueue(u);
                }
            }
        }
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null) throw new IllegalArgumentException();
        int minDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int a: v){
            for (int b: w){
                int anc = ancestor(a, b);
                int dist = length;
                if (dist < minDistance){
                    minDistance = dist;
                    ancestor = anc;
                }
            }
        }

        if (ancestor == -1) length = -1;
        else length = minDistance;
        return ancestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null) throw new IllegalArgumentException();
        ancestor(v, w);
        return length;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
