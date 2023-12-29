import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {

    private final SAP sap;
    private final HashMap<String, Set<Integer>> nounsMap;
    private final Set<String> nouns;
    private final ArrayList<String> synsetList;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        nounsMap = new HashMap<>();
        nouns = new HashSet<>();
        synsetList = new ArrayList<>();
        int lineCount = 0;

        In synsetsInput = new In(synsets);
        String line;
        while (synsetsInput.hasNextLine()) {
            line = synsetsInput.readLine();
            // separate the synset then separate all words from synset to an array (words)
            String[] items = line.split(",");
            synsetList.add(items[1]);
            String[] words = items[1].split(" ");

            // put all words as keys and the corresponding lines as values , if the key exists add lineCount to its set
            for(String word : words) {
                nouns.add(word);
                Set<Integer> set = new HashSet<>();
                if(nounsMap.containsKey(word)) {
                    set.addAll(nounsMap.get(word));
                }
                set.add(lineCount);
                nounsMap.put(word, set);
            }

            lineCount++;
        }

        Digraph digraph = new Digraph(lineCount);
        boolean isRooted = false;

        // construct the graph from file
        In hypernymsInput = new In(hypernyms);
        while (hypernymsInput.hasNextLine()) {
            line = hypernymsInput.readLine();

            String[] items = line.split(",");
            for(int i = 1 ; i < items.length; i++) {
                digraph.addEdge(Integer.parseInt(items[0]) , Integer.parseInt(items[i]));
            }
            if(items.length == 1) isRooted = true;
        }
        DirectedCycle cycleProbe = new DirectedCycle(digraph);
        boolean isDAG = !cycleProbe.hasCycle();
        if (!isDAG || !isRooted) throw new IllegalArgumentException();
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounsMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Set<Integer> aSet = nounsMap.get(nounA);
        Set<Integer> bSet = nounsMap.get(nounB);

        return sap.length(aSet, bSet);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Set<Integer> aSet = nounsMap.get(nounA);
        Set<Integer> bSet = nounsMap.get(nounB);
        int lineNum = sap.ancestor(aSet, bSet);
        return synsetList.get(lineNum);
    }

    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wordNet = new WordNet(args[0], args[1]);

        for (String s : wordNet.nouns()) {
            System.out.println(s);
        }

        System.out.println(wordNet.isNoun("ALGOL"));
        System.out.println(wordNet.isNoun("favor"));
        System.out.println(wordNet.isNoun("random_word"));

        System.out.println(wordNet.sap("range","Circus"));
        System.out.println(wordNet.distance("range","Circus"));

    }

}
