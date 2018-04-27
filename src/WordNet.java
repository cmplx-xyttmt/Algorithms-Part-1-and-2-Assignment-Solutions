import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WordNet {
    private Digraph wordnetGraph;
    private Map<String, ArrayList<Integer>> nounToSynsetID;
    private Map<Integer, String> idToSynset;
    private SAP sap;

    public WordNet(String synsets, String hypernyms){
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        nounToSynsetID = new HashMap<>();
        idToSynset = new HashMap<>();
        In input = new In(synsets);
        int count = 0;
        while (input.hasNextLine()) {
            String[] syn = input.readLine().split(",");
            int id = Integer.parseInt(syn[0]);
            idToSynset.put(id, syn[1]);
            syn = syn[1].split(" ");
            for (String noun: syn){
                if (nounToSynsetID.containsKey(noun)) nounToSynsetID.get(noun).add(id);
                else {
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(id);
                    nounToSynsetID.put(noun, ids);
                }
            }
            count++;
        }

        wordnetGraph = new Digraph(count);
        input = new In(hypernyms);
        while (input.hasNextLine()){
            String[] edge = input.readLine().split(",");
            int id = Integer.parseInt(edge[0]);
            for (int i = 1; i < edge.length; i++) wordnetGraph.addEdge(id, Integer.parseInt(edge[i]));
        }

        DirectedCycle isDag = new DirectedCycle(wordnetGraph);
        if (isDag.hasCycle()) throw new IllegalArgumentException();
        sap = new SAP(wordnetGraph);
    }

    public Iterable<String> nouns(){
        return nounToSynsetID.keySet();
    }

    public boolean isNoun(String word){
        if (word == null) throw new IllegalArgumentException();
        return nounToSynsetID.containsKey(word);
    }

    public int distance(String nounA, String nounB){
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!nounToSynsetID.containsKey(nounA) || !nounToSynsetID.containsKey(nounB)) throw new IllegalArgumentException();
        return sap.length(nounToSynsetID.get(nounA), nounToSynsetID.get(nounB));
    }

    public String sap(String nounA, String nounB){
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!nounToSynsetID.containsKey(nounA) || !nounToSynsetID.containsKey(nounB)) throw new IllegalArgumentException();
        return idToSynset.get(sap.ancestor(nounToSynsetID.get(nounA), nounToSynsetID.get(nounB)));
    }
}
