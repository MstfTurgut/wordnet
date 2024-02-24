import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)  {
        if (wordnet == null) throw new IllegalArgumentException();
        this.wordNet = wordnet;
    }

    // Given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();
        int maxDist = Integer.MIN_VALUE;
        String outcastNoun = "";
        int distSum;
        for (String i : nouns) {
            distSum = 0;
            for (String j : nouns) {
                distSum += wordNet.distance(i, j);
            }
            if (distSum > maxDist) {
                maxDist = distSum;
                outcastNoun = i;
            }
        }
        return outcastNoun;
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


