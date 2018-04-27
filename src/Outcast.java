import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordNet){
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns){
        int maxDistance = -1;
        String out = null;
        for (String s: nouns){
            int distance = 0;
            for (String t: nouns){
                distance += wordNet.distance(s, t);
            }
            if (distance > maxDistance){
                maxDistance = distance;
                out = s;
            }
        }

        return out;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
