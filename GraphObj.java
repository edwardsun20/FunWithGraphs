package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author esun
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        _in = new ArrayList<ArrayList<Integer>>();
        _in.add(null);
        _out = new ArrayList<ArrayList<Integer>>();
        _out.add(null);
        _edgeID = new ArrayList<ArrayList<Integer>>();
        _edgeID.add(null);
        _edgeList = new ArrayList<int []>();
        _edgeList.add(null);
    }

    @Override
    public int vertexSize() {
        return _vertexSize;
    }

    @Override
    public int maxVertex() {
        return _maxVertex;
    }

    @Override
    public int edgeSize() {
        return _edgeList.size() - _freeEdges - 1;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        if (!contains(v)) {
            return 0;
        }
        if (_out.get(v) == null) {
            return 0;
        }
        int size = _out.get(v).size();
        return size;

    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        if ((u >= _out.size() || _out.get(u) == null)
                && (u >= _in.size() || _in.get(u) == null)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean contains(int u, int v) {
        boolean a = indexOf(u, v) != -1;
        return a;
    }

    /**
     * @param u int 1
     * @param v int 2
     * @return int index
     */
    private int indexOf(int u, int v) {
        if (!contains(u) || !contains(v)) {
            return -1;
        }
        if (_out.get(u) == null) {
            return -1;
        }
        return _out.get(u).indexOf(v);
    }

    /** Void expand.
     * @param adj ArrayList of ArrayLists
     * @param index int
     * */
    private void expand(ArrayList<ArrayList<Integer>> adj, int index) {
        if (adj.size() <= index) {
            for (int i = adj.size(); i <= index; i++) {
                adj.add(i, null);
            }
        }
    }

    @Override
    public int add() {
        int index = 0;
        for (int i = 1; i < _maxVertex; i++) {
            if (!contains(i)) {
                index = i;
                expand(_out, index);
                _out.set(index, new ArrayList<Integer>());
                break;
            }
        }
        if (index == 0) {
            index = _maxVertex + 1;
            expand(_out, index);
            _out.set(index, new ArrayList<Integer>());
        }
        _vertexSize++;
        _maxVertex = Math.max(index, _maxVertex);
        return index;
    }

    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);

        int index = indexOf(u, v);
        if (index != -1) {
            return index;
        }
        if (!contains(u)) {
            _vertexSize++;
        }
        if (!contains(v)) {
            _vertexSize++;
        }
        int edgeID = getNextAvailableEdge(u, v);

        addOne(_edgeID, u, edgeID);
        addOne(_out, u, v);
        addOne(_in, v, u);
        if (!isDirected() && u != v) {
            addOne(_edgeID, v, edgeID);
            addOne(_out, v, u);
            addOne(_in, u, v);
        }
        return edgeID;
    }

    /**
     * @param u int 1
     * @param v int 2
     * @return index
     */
    private int getNextAvailableEdge(int u, int v) {
        int[] edge = new int[2];
        if (isDirected()) {
            edge[0] = u;
            edge[1] = v;
        } else {
            edge[0] = Math.min(u, v);
            edge[1] = Math.max(u, v);
        }
        _edgeList.add(edge);
        return _edgeList.size() - 1;
    }


    /**
     * @param adj arraylist of in/out
     * @param u int 1
     * @param v int 2
     */
    private void addOne(ArrayList<ArrayList<Integer>> adj, int u, int v) {
        while (adj.size() <= u) {
            adj.add(null);
        }
        if (adj.get(u) == null) {
            adj.set(u, new ArrayList<Integer>());
            _maxVertex = Math.max(u, _maxVertex);
        }
        adj.get(u).add(v);
    }

    @Override
    public void remove(int v) {
        if (!contains(v)) {
            return;
        }
        if (v == maxVertex()) {
            for (int i = v - 1; i >= 0; i--) {
                if (_out.get(i) != null || _in.get(i) != null) {
                    _maxVertex = i;
                    break;
                }
            }
            if (v == maxVertex()) {
                _maxVertex = 0;
            }
        }
        _vertexSize--;

        if (v < _out.size() && _out.get(v) != null) {
            for (int i : _out.get(v)) {
                removeOnePair(_in, i, v);
                if (!isDirected() && i != v) {
                    removeOnePair(_in, v, i);
                }
            }
        }
        if (v < _in.size() && _in.get(v) != null) {
            for (int i : _in.get(v)) {
                removeOut(i, v);
            }
        }

        if (v < _out.size() && _out.get(v) != null) {
            for (int i = _out.get(v).size() - 1; i >= 0; i--) {
                removeOut(v, _out.get(v).get(i));
            }
            if (v == _out.size() - 1) {
                _out.remove(v);
            } else {
                _out.set(v, null);
            }


            expand(_edgeID, v);
            if (v == _edgeID.size() - 1) {
                _edgeID.remove(v);
            } else {
                _edgeID.set(v, null);
            }
        }
        if (v < _in.size() && _in.get(v) != null) {
            _in.set(v, null);
        }
    }

    @Override
    public void remove(int u, int v) {
        int index = indexOf(u, v);
        if (index == -1) {
            return;
        }
        removeOnePair(_in, v, u);

        if (!isDirected() && u != v) {
            removeOnePair(_in, u, v);
        }
        removeOut(u, v);
        int x = 0;
    }

    /**
     * @param u int 1
     * @param v int 2
     */
    private void removeOut(int u, int v) {
        int outindex = removeOnePair(_out, u, v);
        int currentEdgeID = _edgeID.get(u).get(outindex);
        _edgeID.get(u).remove(outindex);

        if (!isDirected() && u != v) {
            int rindex = removeOnePair(_out, v, u);
            int rEdgeID = _edgeID.get(v).get(rindex);
            _edgeID.get(v).remove(rindex);
        }
        _edgeList.set(currentEdgeID, null);
        _freeEdges++;
    }

    /**
     * @param adj arraylist of in/out
     * @param u int 1
     * @param v int 2
     * @return int index
     */
    private int removeOnePair(ArrayList<ArrayList<Integer>> adj, int u, int v) {
        if (adj.get(u) == null) {
            return -1;
        }
        int index = adj.get(u).indexOf(v);
        adj.get(u).remove(index);
        return index;
    }

    /** Vertex Iterator. */
    private static class VertexIteration extends Iteration<Integer> {
        /** ITER as an iteration.
         * @param list List. */
        VertexIteration(ArrayList<ArrayList<Integer>> list) {
            _list = list;
            _index = 0;
            advance();
        }

        @Override
        public boolean hasNext() {
            boolean a = _index < _list.size();
            return a;
        }

        /** Advance. */
        private void advance() {
            while (_index < _list.size() - 1) {
                _index++;
                if (_list.get(_index) != null) {
                    return;
                }
            }
            if (_index == _list.size() - 1) {
                _index = _list.size();
            }
        }

        @Override
        public Integer next() {
            int indexToReturn = _index;
            advance();
            return indexToReturn;
        }
        /** The iterator with which I was constructed. */
        private ArrayList<ArrayList<Integer>> _list;
        /** Current index. */
        private int _index;
    }

    /** Edge Iterator. */
    private static class EdgeIteration extends Iteration<int[]> {
        /** ITER as an iteration.
         * @param list List. */
        EdgeIteration(ArrayList<int[]> list) {
            _list = list;
            _index = 0;
            advance();
        }

        @Override
        public boolean hasNext() {
            boolean a = _index < _list.size();
            return a;
        }

        /** Advance. */
        private void advance() {
            while (_index < _list.size() - 1) {
                _index++;
                if (_list.get(_index) != null) {
                    return;
                }
            }
            if (_index == _list.size() - 1) {
                _index = _list.size();
            }
        }

        @Override
        public int[] next() {
            int indexToReturn = _index;
            advance();
            return _list.get(indexToReturn);
        }
        /** The iterator with which I was constructed. */
        private ArrayList<int[]> _list;
        /** Index in iterator. */
        private int _index;
    }

    @Override
    public Iteration<Integer> vertices() {
        return new VertexIteration(_out);
    }

    @Override
    public Iteration<Integer> successors(int v) {
        if (v < _out.size() && _out.get(v) != null) {
            return Iteration.iteration(_out.get(v));
        }
        return Iteration.iteration(new ArrayList<>());
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        return new EdgeIteration(_edgeList);
    }

    @Override
    protected void checkMyVertex(int v) {
        super.checkMyVertex(v);
    }

    @Override
    protected int edgeId(int u, int v) {
        int index = indexOf(u, v);
        if (index == -1) {
            return 0;
        }
        return _edgeID.get(u).get(index);
    }

    /**
     * @param _in Arraylist of Arraylist representing in connections
     */
    protected ArrayList<ArrayList<Integer>> _in;
    /**
     * @param _out Arraylist of Arraylist representing out connections
     */
    protected ArrayList<ArrayList<Integer>> _out;
    /**
     * @param _edgeID Arraylist of Arraylist representing edge IDs
     */
    private ArrayList<ArrayList<Integer>> _edgeID;
    /**
     * @param _edgeList edgelist of Arraylist of Arraylist
     */
    private ArrayList<int []> _edgeList;
    /**
     * @param _maxVertex vertex with largest value
     */
    private int _maxVertex;
    /**
     * @param _vertexSize number of vertices
     */
    private int _vertexSize;
    /**
     * @param _freeEdges edges skipped in adj implementation
     */
    private int _freeEdges;
}
