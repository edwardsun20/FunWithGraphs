package graph;

import org.junit.Test;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author esun
 */
public class GraphTest {

    private DirectedGraph buildDirectedGraph() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 1; i <= 9; i++) {
            g.add();
        }
        g.remove(2);
        g.remove(5);

        g.add(9, 1);
        g.add(1, 4);
        g.add(4, 3);
        g.add(4, 7);
        g.add(7, 8);
        g.add(3, 6);
        assertEquals("Initial graph has vertices", 7, g.vertexSize());
        assertEquals("Initial graph has edges", 6, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        assertEquals(true, g.isDirected());
        assertEquals(1, g.inDegree(7));
        assertEquals(1, g.outDegree(7));
        assertEquals(true, g.contains(4, 7));
        assertEquals(false, g.contains(4, 9));
        assertEquals(true, g.contains(6));
        assertEquals(false, g.contains(2));

        return g;
    }

    @Test
    public void testSelfEdge() {
        Graph g = buildDirectedGraph();
        g.add(7, 7);
        g.add(7, 7);
        g.add(7, 8);
        assertEquals("Initial graph has vertices", 7, g.vertexSize());
        assertEquals("Initial graph has edges", 7, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        g.remove(7, 7);
        assertEquals("Initial graph has vertices", 7, g.vertexSize());
        assertEquals("Max vertex is", 9, g.maxVertex());
    }

    @Test
    public void testRemove1() {

        Graph g = buildDirectedGraph();
        int exp = g.add();
        assertEquals(2, exp);

        g.remove(3, 4);
        assertEquals("Initial graph has vertices", 8, g.vertexSize());
        assertEquals("Initial graph has edges", 6, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        g.remove(4, 3);
        assertEquals("Initial graph has vertices", 8, g.vertexSize());
        assertEquals("Initial graph has edges", 5, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        g.add(4, 3);
        assertEquals("Initial graph has vertices", 8, g.vertexSize());
        assertEquals("Initial graph has edges", 6, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        g.remove(4, 7);
        g.remove(7, 8);
        assertEquals("Initial graph has vertices", 8, g.vertexSize());
        assertEquals("Initial graph has edges", 4, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

    }

    @Test
    public void testRemove2() {

        Graph g = buildDirectedGraph();
        g.add(4, 4);
        g.remove(4);

        assertEquals("Initial graph has vertices", 6, g.vertexSize());
        assertEquals("Initial graph has edges", 3, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        g.remove(9);
        assertEquals("Initial graph has vertices", 5, g.vertexSize());
        assertEquals("Initial graph has edges", 2, g.edgeSize());
        assertEquals("Max vertex is", 8, g.maxVertex());

        g.remove(1);
        assertEquals("Initial graph has vertices", 4, g.vertexSize());
        assertEquals("Initial graph has edges", 2, g.edgeSize());
        assertEquals("Max vertex is", 8, g.maxVertex());
        g.remove(7);
        assertEquals("Initial graph has vertices", 3, g.vertexSize());
        assertEquals("Initial graph has edges", 1, g.edgeSize());
        assertEquals("Max vertex is", 8, g.maxVertex());
        g.remove(3);
        assertEquals("Initial graph has vertices", 2, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
        assertEquals("Max vertex is", 8, g.maxVertex());
        g.remove(6);
        assertEquals("Initial graph has vertices", 1, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
        assertEquals("Max vertex is", 8, g.maxVertex());
        g.remove(8);
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
        assertEquals("Max vertex is", 0, g.maxVertex());
    }

    @Test
    public void testSuccessors() {
        Graph g = buildDirectedGraph();

        g.add(4, 4);

        ArrayList<Integer> lst = new ArrayList<>();
        for (int v : g.successors(4)) {
            lst.add(v);
        }
        assertEquals("Successors: ", Arrays.asList(3, 7, 4), lst);

        g.remove(7);
        ArrayList<Integer> lst2 = new ArrayList<>();
        for (int v : g.successors(4)) {
            lst2.add(v);
        }
        assertEquals("Successors: ", Arrays.asList(3, 4), lst2);
    }

    @Test
    public void testSuccessors2() {
        Graph g = buildDirectedGraph();
        ArrayList<Integer> lst = new ArrayList<>();
        for (int v : g.successors(6)) {
            lst.add(v);
        }
        assertEquals("Successors: ", Arrays.asList(), lst);
    }

    @Test
    public void testVertices1() {
        Graph g = buildDirectedGraph();
        g.remove(9);

        ArrayList<Integer> lst = new ArrayList<>();
        for (Integer i : g.vertices()) {
            lst.add(i);
        }
        assertEquals("Vertices: ", Arrays.asList(1, 3, 4, 6, 7, 8), lst);
    }

    @Test
    public void testVertices2() {
        Graph g = buildDirectedGraph();

        ArrayList<Integer> lst = new ArrayList<>();
        for (Integer i : g.vertices()) {
            lst.add(i);
        }
        assertEquals("Vertices: ", Arrays.asList(1, 3, 4, 6, 7, 8, 9), lst);
    }

    private class CompareEdge implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            int a = Integer.compare(o1[0], o2[0]);
            if (a == 0) {
                return Integer.compare(o1[1], o2[1]);
            }
            return a;
        }
    }

    @Test
    public void testEdges() {
        Graph g = buildDirectedGraph();
        List<int[]> expected = new ArrayList<int[]>();
        expected.add(new int[]{9, 1});
        expected.add(new int[]{1, 4});
        expected.add(new int[]{4, 3});
        expected.add(new int[]{4, 7});
        expected.add(new int[]{7, 8});
        expected.add(new int[]{3, 6});
        expected.sort(new CompareEdge());
        List<int[]> actual = new ArrayList<int[]>();
        for (int[] e : g.edges()) {
            actual.add(e.clone());
        }
        actual.sort(new CompareEdge());
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertArrayEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testedgeID() {
        Graph g = buildDirectedGraph();
        g.add(4, 4);

        assertEquals(7, g.edgeId(4, 4));
        assertEquals(3, g.edgeId(4, 3));
        assertEquals(1, g.edgeId(9, 1));
        assertEquals(2, g.edgeId(1, 4));
        assertEquals(4, g.edgeId(4, 7));
        assertEquals(5, g.edgeId(7, 8));
        assertEquals(6, g.edgeId(3, 6));
        assertEquals(0, g.edgeId(1, 2));
        assertEquals(0, g.edgeId(2, 1));
        assertEquals(0, g.edgeId(3, 4));

        g.remove(3);
        assertEquals(0, g.edgeId(4, 3));
        assertEquals(4, g.edgeId(4, 7));

        g.remove(4, 7);
        g.add(4, 7);

        assertEquals(8, g.edgeId(4, 7));

    }

    @Test
    public void testOutDegree() {
        Graph g = buildDirectedGraph();
        assertEquals(2, g.outDegree(4));
        assertEquals(1, g.outDegree(9));
        assertEquals(1, g.outDegree(1));
        assertEquals(1, g.outDegree(3));
        assertEquals(1, g.outDegree(7));
        assertEquals(0, g.outDegree(6));
        assertEquals(0, g.outDegree(8));
        g.add(6, 8);
        g.add(8, 6);
        assertEquals(1, g.outDegree(6));
        assertEquals(1, g.outDegree(8));
        g.remove(8);
        assertEquals(0, g.outDegree(6));
        assertEquals(0, g.outDegree(7));
        assertEquals(0, g.outDegree(8));
    }

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    private class DFSPrint extends DepthFirstTraversal {
        DFSPrint(Graph g, boolean shouldPostVisit) {
            super(g);
            _shouldPostVisit = shouldPostVisit;
        }

        @Override
        protected boolean visit(int v) {
            System.out.printf("%d visted%n", v);
            _ret += Integer.toString(v) + " ";
            return true;
        }

        @Override
        protected boolean postVisit(int v) {
            System.out.printf("\t%d post-visted%n", v);
            _ret += "(" + Integer.toString(v) + " post) ";
            return true;
        }

        @Override
        protected boolean shouldPostVisit(int v) {
            return _shouldPostVisit;
        }

        public String toString() {
            return _ret;
        }

        boolean _shouldPostVisit;
        String _ret = new String();
    }

    @Test
    public void testDFS1() {
        Graph g = buildDirectedGraph();
        Traversal m = new DFSPrint(g, true);
        m.traverse(9);
        assertEquals("9 1 4 3 6 (6 post) (3 post)"
                + " 7 8 (8 post) (7 post) (4 post) (1 post)"
                + " (9 post) ", m.toString());
    }

    private class TopologicalOrder extends DepthFirstTraversal {

        TopologicalOrder(Graph g, boolean shouldPostVisit) {
            super(g);
            _shouldPostVisit = shouldPostVisit;
            _onStack = new boolean[g.maxVertex() + 1];
            _ret = new String();
        }

        @Override
        protected boolean visit(int v) {
            _onStack[v] = true;

            System.out.printf("%d visted%n", v);
            _ret += Integer.toString(v) + " ";
            return true;
        }

        @Override
        protected boolean processSuccessor(int u, int v) {
            if (_onStack[v] && marked(v)) {
                cycleFound = true;
                return false;
            }
            return !marked(v);
        }

        @Override
        protected boolean postVisit(int v) {
            System.out.printf("\t%d post-visted%n", v);
            _ret += "(" + Integer.toString(v) + " post) ";
            _onStack[v] = false;
            return true;
        }

        @Override
        protected boolean shouldPostVisit(int v) {
            return _shouldPostVisit;
        }

        public String toString() {
            return _ret;
        }

        private boolean[] _onStack;
        private boolean cycleFound;
        boolean _shouldPostVisit;
        String _ret;
    }

    @Test
    public void testCycles1() {
        Graph g = buildDirectedGraph();

        g.add(3, 9);

        TopologicalOrder m = new TopologicalOrder(g, true);
        m.traverse(9);
        assertEquals("9 1 4 3 6 (6 post) (3 post) 7 8 (8 post)"
                + " (7 post) (4 post) (1 post) (9 post) ", m.toString());
        assertEquals(true, m.cycleFound);
    }

    @Test
    public void testCycles2() {
        Graph g = buildDirectedGraph();
        g.add();
        g.add();

        g.add(1, 2);
        g.add(1, 3);
        g.add(4, 1);
        g.add(2, 5);
        TopologicalOrder m = new TopologicalOrder(g, true);
        m.traverse(4);
        assertEquals("4 3 6 (6 post) (3 post) 7 8 (8 post) (7 post)"
                + " 1 2 5 (5 post) (2 post) (1 post) (4 post) ", m.toString());
        assertEquals(true, m.cycleFound);
    }

    @Test
    public void testLabeledGraph() {
        Graph g0 = buildDirectedGraph();
        g0.add();
        g0.add();

        g0.add(1, 2);
        LabeledGraph<String, String> g = new LabeledGraph<String, String>(g0);
        g.add(1, 2, "foo.o");
        g.add(1, 3);
        g.add(4, 1, "foo");
        g.add(2, 5, "foo.c");
        g.setLabel(3, "foo.h");
        g.setLabel(5, "foo.y");
        assertEquals("foo.h", g.getLabel(3));
        assertEquals("foo.y", g.getLabel(5));

        TopologicalOrder m = new TopologicalOrder(g, true);
        m.traverse(4);
        assertEquals("4 3 6 (6 post) (3 post) 7 8 (8 post) (7 post)"
                + " 1 2 5 (5 post) (2 post) (1 post) (4 post) ", m.toString());
        assertEquals(true, m.cycleFound);
    }

    private LabeledGraph<String, Integer> buildLabeledGraph() {
        LabeledGraph<String, Integer> g =
                new LabeledGraph<>(new DirectedGraph());

        for (int i = 1; i <= 9; i++) {
            g.add();
        }
        g.remove(8);

        g.add(9, 2); g.setLabel(9, 2, 5);
        g.add(9, 1); g.setLabel(9, 1, 2);
        g.add(9, 3); g.setLabel(9, 3, 3);
        g.add(9, 6); g.setLabel(9, 6, 7);
        g.add(1, 2); g.setLabel(1, 2, 4);
        g.add(1, 3); g.setLabel(1, 3, 5);
        g.add(3, 6); g.setLabel(3, 6, 3);
        g.add(3, 7); g.setLabel(3, 7, 6);
        g.add(2, 4); g.setLabel(2, 4, 2);
        g.add(2, 5); g.setLabel(2, 5, 2);
        g.add(4, 5); g.setLabel(4, 5, 1);
        g.add(6, 7); g.setLabel(6, 7, 1);
        g.add(7, 4); g.setLabel(7, 4, 2);

        for (int i = 1; i <= g.maxVertex(); i++) {
            if (g.contains(i)) {
                g.setLabel(i, "Node " + i);
            }
        }

        assertEquals("Initial graph has vertices", 8, g.vertexSize());
        assertEquals("Initial graph has edges", 13, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        return g;
    }

    private class BFSPrint extends BreadthFirstTraversal {
        BFSPrint(Graph g) {
            super(g);
        }
        @Override
        protected boolean visit(int v) {
            System.out.printf("%d visted%n", v);
            _ret += Integer.toString(v) + " ";
            return true;
        }
        String _ret = new String();
        public String toString() {
            return _ret;
        }
    }

    @Test
    public void testBFS1() {
        LabeledGraph<String, Integer> g = buildLabeledGraph();
        Traversal m = new BFSPrint(g);
        m.traverse(9);
        assertEquals("9 2 1 3 6 4 5 7 ", m.toString());
    }

    @Test
    public void testLabelGraph1() {
        LabeledGraph<String, Integer> g = buildLabeledGraph();

        int nv = g.add();
        assertEquals(8, nv);

        assertEquals((Integer) 3, g.getLabel(9, 3));
        g.remove(9, 3);
        g.add(9, 3);
        assertEquals(null, g.getLabel(9, 3));
        assertEquals("Initial graph has vertices", 9, g.vertexSize());
        assertEquals("Initial graph has edges", 13, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

        assertEquals("Node 9", g.getLabel(9));
        g.remove(9);
        int newv = g.add();
        assertEquals(9, newv);
        g.setLabel(newv, "Node new");
        assertEquals("Node new", g.getLabel(newv));

        assertEquals("Initial graph has vertices", 9, g.vertexSize());
        assertEquals("Initial graph has edges", 9, g.edgeSize());
        assertEquals("Max vertex is", 9, g.maxVertex());

    }

    @Test
    public void randomTest1() {
        DirectedGraph g = new DirectedGraph();

        Random rand = new Random();
        rand.setSeed(13);
        int count = 0;

        for (int i = 0; i < 1000; i++) {
            int op = rand.nextInt(6);
            int u = rand.nextInt(10);
            int v = rand.nextInt(10);
            try {
                switch (op) {
                case 0:
                    g.add();
                    break;
                case 1:
                    g.add(u, v);
                    break;
                case 2:
                    g.remove(u);
                    break;
                case 3:
                    g.remove(u, v);
                    break;
                case 4:
                    g.add(u, u);
                    break;
                case 5:
                    g.remove(u, u);
                    break;
                default:
                    break;
                }
                count++;
            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                continue;
            }
        }
        System.out.println("Counts: " + count);
    }

    @Test
    public void testNotMyVertex() {
        DirectedGraph g = new DirectedGraph();
        try {
            g.add(9, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception: "
                    + e + ", good behavior");
            return;
        }
        assertTrue("Vertices 9,1 are not my vertex", false);
    }
}
