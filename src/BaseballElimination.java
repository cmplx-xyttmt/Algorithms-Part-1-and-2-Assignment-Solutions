import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private final int[] w;
    private final int[] losses;
    private final int[] r;
    private final int[][] g;
    private final HashMap<String, Integer> teamIDs;
    private final ArrayList<String> teams;
    private ArrayList<String> eliminationSet;
    private int max;
    private int maxIndex;

    public BaseballElimination(String filename) {
        In input = new In(filename);
        int n = input.readInt();
        w = new int[n];
        losses = new int[n];
        r = new int[n];
        g = new int[n][n];
        teamIDs = new HashMap<>();
        teams = new ArrayList<>();
        max = 0;
        maxIndex = 0;

        for (int i = 0; i < n; i++) {
            String team = input.readString();
            teamIDs.put(team, i);
            teams.add(team);
            w[i] = input.readInt();
            losses[i] = input.readInt();
            r[i] = input.readInt();
            if (w[i] >= max) {
                max = w[i];
                maxIndex = i;
            }

            for (int j = 0; j < n; j++)
                g[i][j] = input.readInt();
        }
    }

    public int numberOfTeams() {
        return w.length;
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        if (!teamIDs.containsKey(team)) throw new IllegalArgumentException();
        return w[teamIDs.get(team)];
    }

    public int losses(String team) {
        if (!teamIDs.containsKey(team)) throw new IllegalArgumentException();
        return losses[teamIDs.get(team)];
        
    }

    public int remaining(String team) {
        if (!teamIDs.containsKey(team)) throw new IllegalArgumentException();
        return r[teamIDs.get(team)];
    }

    public int against(String team1, String team2) {
        if (!teamIDs.containsKey(team1) || !teamIDs.containsKey(team2)) throw new IllegalArgumentException();
        return g[teamIDs.get(team1)][teamIDs.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teamIDs.containsKey(team)) throw new IllegalArgumentException();
        int x = teamIDs.get(team);
        eliminationSet = new ArrayList<>();
        if (max > w[x] + r[x]) {
            eliminationSet.add(teams.get(maxIndex));
            return true;
        }
        
        int m = (w.length - 1)*(w.length - 2)/2;
        int v = m + w.length + 1;
        FlowNetwork flowNetwork = new FlowNetwork(v);
        int vertex = 1;
        int t = m;
        for (int i = 0; i < w.length; i++) {
            if (i == x) continue;
            t++;
            int other = t;
            for (int j = i + 1; j < w.length; j++) {
                if (j == x || i == j) continue;
                other++;
                flowNetwork.addEdge(new FlowEdge(0, vertex, g[i][j]));
                flowNetwork.addEdge(new FlowEdge(vertex, t, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(vertex, other, Double.POSITIVE_INFINITY));
                vertex++;
            }
            flowNetwork.addEdge(new FlowEdge(t, v - 1, w[x] + r[x] - w[i]));
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        boolean elim = false;
        int vv = 0;
        for (FlowEdge e: flowNetwork.adj(0)) {
            if (e.capacity() != e.flow()) {
                elim = true;
                break;
            }
        }

        if (elim) {
            for (int i = 0; i < w.length; i++) {
                if (i != x) {
                    vv++;
                    if (fordFulkerson.inCut(m + vv)) eliminationSet.add(teams.get(i));
                }
            }
        }

        return elim;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIDs.containsKey(team)) throw new IllegalArgumentException();
        if (isEliminated(team)) {
            return eliminationSet;
        }
        else return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }

        }
    }
}
