package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayDeque;
import java.util.List;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Comparator;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author esun
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        for (int i = 1; i <= _G.maxVertex(); i++) {
            setWeight(i, Double.POSITIVE_INFINITY);
            setPredecessor(i, 0);
        }
        setWeight(getSource(), 0);
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {

        Traversal m = new Dijkstras(_G);
        m.traverse(getSource());

        LinkedList<Integer> shortestPath = new LinkedList<>();
        while (v != getSource()) {
            shortestPath.addFirst(v);
            v = getPredecessor(v);
        }
        shortestPath.addFirst(getSource());
        return shortestPath;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;

    /** Class PQ.*/
    private class PQ extends ArrayDeque<Integer> {

        /** PQ Constructor.*/
        PQ() {
            _treeSet = new TreeSet<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer u, Integer v) {
                    if (u == v) {
                        return 0;
                    }
                    double uDist = getWeight(u);
                    double vDist = getWeight(v);
                    int a = Double.compare(uDist, vDist);
                    if (a == 0) {
                        return Integer.compare(u, v);
                    }
                    return a;
                }
            });
        }

        @Override
        public boolean add(Integer a) {
            return _treeSet.add(a);
        }

        @Override
        public Integer remove() {
            return _treeSet.pollFirst();
        }

        @Override
        public void clear() {
            _treeSet.clear();
        }

        @Override
        public boolean isEmpty() {
            return _treeSet.isEmpty();
        }

        /** TreeSet _treeSet.*/
        private TreeSet<Integer> _treeSet;
    }

    /** Dijkstras class.*/
    private class Dijkstras extends Traversal {
        /** Dijkstras method.
         * @param g Graph g.
         * */
        Dijkstras(Graph g) {
            super(g, new PQ());
        }

        @Override
        protected boolean visit(int v) {
            _ret += Integer.toString(v) + " ";
            return true;
        }

        /** String _ret.*/
        private String _ret = new String();

        /** To String method.
         * @return String
         * */
        public String toString() {
            return _ret;
        }

        @Override
        protected boolean processSuccessor(int v, int w) {
            if (getWeight(v, w) < getWeight(w) - getWeight(v)) {
                setWeight(w, getWeight(v) + getWeight(v, w));
                setPredecessor(w, v);
                return true;
            } else {
                return false;
            }
        }
    }
}
