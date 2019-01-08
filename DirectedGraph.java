package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author esun
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
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
