import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        Edge edge = new Edge(v1, v2, weight);
        this.adjLists[v1].add(edge);
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        Edge edge1 = new Edge(v1, v2, weight);
        this.adjLists[v1].add(edge1);
        Edge edge2 = new Edge(v2, v1, weight);
        this.adjLists[v2].add(edge2);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        LinkedList<Edge> list = this.adjLists[from];
        for (Edge edge : list) {
            if (edge.to == to) {
                return true;
            }
        }

        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> neighbors = new ArrayList<>();
        LinkedList<Edge> list = this.adjLists[v];
        for (Edge edge : list) {
            neighbors.add(edge.to);
        }

        return neighbors;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int inDegree = 0;
        for (LinkedList<Edge> list : this.adjLists) {
            for (Edge edge : list) {
                if (edge.to == v) {
                    inDegree++;
                }
            }
        }
        return inDegree;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true if there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        List<Integer> visited = dfs(start);

        if (start == stop) {
            return true;
        }

        if (visited.contains(stop)) {
            return true;
        }

        return false;
    }


    /** Returns the path from START to STOP. If no path exists, returns an empty
     * List. If START == STOP, returns a List with START.

       Hint: Base your method on dfs, with the following differences. First,
       add code to stop calling next when you encounter the finish vertex.
       Then, trace back from the finish vertex to the start, by first
       finding a visited vertex u for which (u, finish) is an edge,
       then a vertex v visited earlier than u for which (v, u) is an
       edge, and so on, finally finding a vertex w for which (start, w)
       is an edge (isAdjacent may be useful here!). Collecting all these
       vertices in the correct sequence produces the desired path.
       We recommend that you try this by hand with a graph or two
       to see that it works.
     */
    public List<Integer> path(int start, int stop) {
        List<Integer> path = new ArrayList<>();

        if (!pathExists(start, stop)) {
            return path;
        }

        if (start == stop) {
            path.add(start);
            return path;
        }

        DFSIterator iter = new DFSIterator(start);

        while (iter.hasNext()) {
            Integer next = iter.next();
            if (next == stop) {
                break;
            }
        }

        Stack<Integer> pathStack = new Stack<>();
        HashSet<Integer> visitedPath = new HashSet<>();
        visitedPath.add(stop);
        Integer currentNode = stop;
        pathStack.push(stop);

        while (currentNode != start) {
            boolean foundAdjacency = false;

            for (int v : iter.visited) {
                if (isAdjacent(v, currentNode) && !visitedPath.contains(v)) {
                    pathStack.push(v);
                    visitedPath.add(v);
                    currentNode = v;
                    foundAdjacency = true;
                    break;
                }
            }

            if (foundAdjacency == false) {
                pathStack.pop();
                currentNode = pathStack.peek();
            }
        }

        while (!pathStack.isEmpty()) {
            path.add(pathStack.pop());
        }

        return path;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private int[] currentInDegree;
        private HashSet<Integer> visited;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            this.currentInDegree = new int[vertexCount];
            this.visited = new HashSet<>();
            for (int i = 0; i < this.currentInDegree.length; i++) {
                if (inDegree(i) == 0) {
                    fringe.push(i);
                }
                this.currentInDegree[i] = inDegree(i);
            }
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            int removed = fringe.pop();
            visited.add(removed);

            for (int i : neighbors(removed)) {
                this.currentInDegree[i]--;
            }

            for (int i = 0; i < this.currentInDegree.length; i++) {
                if (this.currentInDegree[i] == 0) {
                    if (!visited.contains(i)) {
                        fringe.push(i);
                        visited.add(i);
                    }
                }
            }

            return removed;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}