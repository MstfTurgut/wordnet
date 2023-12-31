import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.Map;


public final class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.digraph = G;
    }

    private boolean isValid(int v, int w) {
        return (v > 0 && v < digraph.V() && w > 0 && w < digraph.V());
    }

    private boolean isValid(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null) return false;
        for (Integer i : v) if (i == null || i < 0 || i >= digraph.V()) return false;
        for (Integer i : w) if (i == null || i < 0 || i >= digraph.V()) return false;
        return true;
    }

    private HashMap<Integer, Integer> possibleVertices(int s) {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(s,0);
        Queue<Integer> q = new Queue<>();
        q.enqueue(s); // put it on the queue.
        while (!q.isEmpty()) {
            int v = q.dequeue(); // Remove next vertex from the queue.
            for (int w : digraph.adj(v))
                if (!map.containsKey(w)) // For every unmarked adjacent vertex,
                {
                    q.enqueue(w); // add it to the queue.
                    map.put(w,map.get(v) + 1);
                }
        }
        return map;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        if(!isValid(v, w)) throw new IllegalArgumentException();

        HashMap<Integer, Integer> verticesV = possibleVertices(v);
        HashMap<Integer, Integer> verticesW = possibleVertices(w);

        int minLen = Integer.MAX_VALUE;
        int championAncestor = -1;

        for(Map.Entry<Integer, Integer> xAndToX : verticesW.entrySet()) {
            if(verticesV.containsKey(xAndToX.getKey())) {
                int len = verticesV.get(xAndToX.getKey()) + xAndToX.getValue();
                if(len < minLen) {
                    minLen = len;
                    championAncestor = xAndToX.getKey();
                }
            }
        }

        return championAncestor == -1 ? -1 : minLen;
    }

    // a common ancestor of v and w that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        if(!isValid(v, w)) throw new IllegalArgumentException();

        HashMap<Integer, Integer> verticesV = possibleVertices(v);
        HashMap<Integer, Integer> verticesW = possibleVertices(w);

        int minLen = Integer.MAX_VALUE;
        int championAncestor = -1;

        for(Map.Entry<Integer, Integer> xAndToX : verticesW.entrySet()) {
            if(verticesV.containsKey(xAndToX.getKey())) {
                int len = verticesV.get(xAndToX.getKey()) + xAndToX.getValue();
                if(len < minLen) {
                    minLen = len;
                    championAncestor = xAndToX.getKey();
                }
            }
        }

        return championAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if(!isValid(v, w)) throw new IllegalArgumentException();

        int minLen = Integer.MAX_VALUE;
        for (int i : v) {
            for (int j : w) {
                int len = length(i,j);
                if (len != -1 && len < minLen) minLen = len;
            }
        }
        return (minLen == Integer.MAX_VALUE) ? -1 : minLen;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        if(!isValid(v, w)) throw new IllegalArgumentException();

        int minLen = Integer.MAX_VALUE;
        int iMin = -1;
        int jMin = -1;
        for (int i : v) {
            for(int j : w) {
                int len = length(i,j);
                if(len != -1 && len < minLen) {
                    minLen = len;
                    iMin = i;
                    jMin = j;
                }
            }
        }
        return (minLen == Integer.MAX_VALUE) ? -1 : ancestor(iMin, jMin);
    }

    // unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}
