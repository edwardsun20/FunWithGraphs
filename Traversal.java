package graph;

/* See restrictions in Graph.java. */

import java.util.Queue;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Arrays;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular collection of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.
 *
 *  Generally, the client will extend Traversal.  By overriding the visit
 *  method, the client can determine what happens when a node is visited.
 *  By supplying an appropriate type of Queue object to the constructor,
 *  the client can control the behavior of the fringe. By overriding the
 *  shouldPostVisit and postVisit methods, the client can arrange for
 *  post-visits of a node (as in depth-first search).  By overriding
 *  the reverseSuccessors and processSuccessor methods, the client can control
 *  the addition of neighbor vertices to the fringe when a vertex is visited.
 *
 *  Traversals may be interrupted or restarted, remembering the previously
 *  marked vertices.
 *  @author esun
 */
public abstract class Traversal {

    /** A Traversal of G, using FRINGE as the fringe. */
    protected Traversal(Graph G, Queue<Integer> fringe) {
        _G = G;
        _fringe = fringe;
    }

    /** Unmark all vertices in the graph. */
    public void clear() {
        _marked = new boolean[_G.maxVertex() + 1];
        _prec = new int[_G.maxVertex() + 1];
        _blocked = new int[_G.maxVertex() + 1];
        _posted = new boolean[_G.maxVertex() + 1];
        _fringe.clear();
    }

    /** Initialize the fringe to V0 and perform a traversal. */
    public void traverse(Collection<Integer> V0) {
        clear();
        for (int i : V0) {
            _fringe.add(i);
        }

        LinkedList<Integer> reverse = new LinkedList<Integer>();

        while (!_fringe.isEmpty()) {
            int u = _fringe.remove();
            int eofVisit = 0;

            if (!marked(u)) {
                mark(u);
                if (!visit(u)) {
                    return;
                }
                reverse.clear();
                for (int w : _G.successors(u)) {
                    if (reverseSuccessors(u)) {
                        reverse.addFirst(w);
                    } else {
                        reverse.add(w);
                    }
                }
                for (int w : reverse) {
                    if (processSuccessor(u, w)) {
                        _blocked[u]++;
                        _prec[w] = u;
                        eofVisit++;
                        _fringe.add(w);
                    }
                }
            }
            if (eofVisit == 0) {
                while (_blocked[u] == 0) {
                    if (!_posted[u] && shouldPostVisit(u)) {
                        _posted[u] = true;
                        if (!postVisit(u)) {
                            return;
                        }
                    }
                    if (_prec[u] == 0) {
                        break;
                    }
                    u = (_prec[u] > 0) ? _prec[u] : u;
                    if (_blocked[u] > 0) {
                        _blocked[u]--;
                    }
                }
            }
        }
    }

    /** Initialize the fringe to { V0 } and perform a traversal. */
    public void traverse(int v0) {
        traverse(Arrays.<Integer>asList(v0));
    }

    /** Returns true iff V has been marked. */
    protected boolean marked(int v) {
        return _marked[v];
    }

    /** Mark vertex V. */
    protected void mark(int v) {
        _marked[v] = true;
    }

    /** Perform a visit on vertex V.  Returns false iff the traversal is to
     *  terminate immediately. */
    protected boolean visit(int v) {
        return true;
    }

    /** Return true if we should postVisit V after traversing its
     *  successors.  (Post-visiting generally is useful only for depth-first
     *  traversals, although we define it for all traversals.) */
    protected boolean shouldPostVisit(int v) {
        return false;
    }

    /** Revisit vertex V after traversing its successors.  Returns false iff
     *  the traversal is to terminate immediately. */
    protected boolean postVisit(int v) {
        return true;
    }

    /** Return true if we should schedule successors of V in reverse order. */
    protected boolean reverseSuccessors(int v) {
        return false;
    }

    /** Process the successors of vertex U.  Assumes U has been visited.  This
     *  default implementation simply processes each successor using
     *  processSuccessor. */
    protected void processSuccessors(int u) {
        for (int v : _G.successors(u)) {
            if (processSuccessor(u, v)) {
                _fringe.add(v);
            }
        }
    }

    /** Process successor V to U.  Returns true iff V is then to
     *  be added to the fringe.  By default, returns true iff V is unmarked. */
    protected boolean processSuccessor(int u, int v) {
        return !marked(v);
    }

    /** The graph being traversed. */
    private final Graph _G;
    /** The fringe. */
    protected final Queue<Integer> _fringe;

    /**
     * @param _marked Array of marked nodes.
     */
    private boolean[] _marked;
    /**
     * @param _blocked Array of blocked nodes.
     */
    private int[] _blocked;
    /**
     * @param _prec Array of predecesors.
     */
    private int[] _prec;

    /**
     * @param _posted Array of posted nodes.
     */
    private boolean[] _posted;
}
