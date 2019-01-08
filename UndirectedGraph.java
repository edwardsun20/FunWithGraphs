package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author esun
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        if (!contains(v)) {
            return 0;
        }
        int a = _in.get(v).size();
        return a;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        if (v < _in.size() && _in.get(v) != null) {
            return Iteration.iteration(_in.get(v));
        }
        return Iteration.iteration(new ArrayList<>());
    }

}
