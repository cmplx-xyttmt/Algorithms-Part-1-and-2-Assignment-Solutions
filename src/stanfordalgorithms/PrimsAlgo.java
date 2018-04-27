package stanfordalgorithms;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import edu.princeton.cs.algs4.*;

public class PrimsAlgo {

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(new File("edges.txt"));
        int V = scanner.nextInt();
        int E = scanner.nextInt();
        EdgeWeightedGraph graph = new EdgeWeightedGraph(V);

        for (int i = 0; i < E; i++) graph.addEdge(new Edge(scanner.nextInt() - 1, scanner.nextInt() - 1, (double)scanner.nextInt()));
        PrimMST prim = new PrimMST(graph);
        System.out.println(prim.weight());
    }
}
