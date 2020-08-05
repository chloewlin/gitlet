import jdk.nashorn.api.tree.Tree;

import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.IOException;

/* A mutable and finite Graph object. Edge labels are stored via a HashMap
   where labels are mapped to a key calculated by the following. The graph is
   undirected (whenever an Edge is added, the dual Edge is also added). Vertices
   are numbered starting from 0. */
public class Graph {

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
    /* Maps vertices to a list of its connected edges. */
    private HashMap<Integer, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();

    /* Returns the vertices that neighbor V. */
    public TreeSet<Integer> getNeighbors(int v) {
        return new TreeSet<Integer>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    public TreeSet<Edge> getEdges(int v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    public TreeSet<Integer> getAllVertices() {
        return new TreeSet<Integer>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(Integer v) {
        if (neighbors.get(v) == null) {
            neighbors.put(v, new HashSet<Integer>());
            edges.put(v, new HashSet<Edge>());
        }
    }

    /* Adds Edge E to the graph. */
    public void addEdge(Edge e) {
        addEdgeHelper(e.getSource(), e.getDest(), e.getWeight());
    }

    /* Creates an Edge between V1 and V2 with no weight. */
    public void addEdge(int v1, int v2) {
        addEdgeHelper(v1, v2, 0);
    }

    /* Creates an Edge between V1 and V2 with weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        addEdgeHelper(v1, v2, weight);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(int v1, int v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(int v) {
        return neighbors.get(v) != null;
    }

    /* Returns true if the graph contains the edge E. */
    public boolean containsEdge(Edge e) {
        return allEdges.contains(e);
    }

    /* Returns if this graph spans G. */
    public boolean spans(Graph g) {
        TreeSet<Integer> all = getAllVertices();
        if (all.size() != g.getAllVertices().size()) {
            return false;
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> vertices = new ArrayDeque<>();
        Integer curr;

        vertices.add(all.first());
        while ((curr = vertices.poll()) != null) {
            if (!visited.contains(curr)) {
                visited.add(curr);
                for (int n : getNeighbors(curr)) {
                    vertices.add(n);
                }
            }
        }
        return visited.size() == g.getAllVertices().size();
    }

    /* Overrides objects equals method. */
    public boolean equals(Object o) {
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        return neighbors.equals(other.neighbors) && edges.equals(other.edges);
    }

    /* A helper function that adds a new edge from V1 to V2 with WEIGHT as the
       label. */
    private void addEdgeHelper(int v1, int v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2, weight);
        Edge e2 = new Edge(v2, v1, weight);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }

    public Graph prims(int start) {
        HashSet<Integer> visited = new HashSet<>();
        int[] distTo = new int[this.neighbors.size()];
        HashMap<Integer, Integer> edgeTo = new HashMap<Integer, Integer>();
        distTo[start] = 0;

        Graph result = new Graph();
        int maxEdges = this.neighbors.size() - 1;
        int resultEdges = 0;

        PriorityQueue<Integer> fringe =
                new PriorityQueue<Integer>((a, b) -> distTo[a] - distTo[b]);
        fringe.offer(start);
        while (resultEdges < maxEdges) {
            int curr = fringe.poll();
            visited.add(curr);
            result.addVertex(curr);
            if (curr != start) {
                result.addEdge(edgeTo.get(curr), curr, distTo[curr]); // TODO: Check order
                resultEdges++;
            }
            for (Edge e : getEdges(curr)) {
                if (!visited.contains(e.getDest())) {
                    if (!edgeTo.containsKey(e.getDest())) {
                        distTo[e.getDest()] = e.getWeight();
                        edgeTo.put(e.getDest(), curr);
                        fringe.offer(e.getDest());
                    } else if (e.getWeight() < distTo[e.getDest()]) {
                        fringe.remove(e.getDest());
                        distTo[e.getDest()] = e.getWeight();
                        edgeTo.put(e.getDest(), curr);
                        fringe.offer(e.getDest());
                    }
                }
            }
        }

        return result;
    }

    public Graph kruskals() {
        Graph MST = new Graph();
        UnionFind djs = new UnionFind(this.neighbors.size());

        for (int v : getAllVertices()) {
            MST.addVertex(v);
        }

        for (Edge e : getAllEdges()) {
            if (!djs.connected(e.getSource(), e.getDest())) {
                MST.addEdge(e.getSource(), e.getDest(), e.getWeight());
                djs.union(e.getSource(), e.getDest());
            }
        }

        return MST;
    }

    /* Returns a randomly generated graph with VERTICES number of vertices and
       EDGES number of edges with max weight WEIGHT. */
    public static Graph randomGraph(int vertices, int edges, int weight) {
        Graph g = new Graph();
        Random rng = new Random();
        for (int i = 0; i < vertices; i += 1) {
            g.addVertex(i);
        }
        for (int i = 0; i < edges; i += 1) {
            Edge e = new Edge(rng.nextInt(vertices), rng.nextInt(vertices), rng.nextInt(weight));
            g.addEdge(e);
        }
        return g;
    }

    /* Returns a Graph object with integer edge weights as parsed from
       FILENAME. Talk about the setup of this file. */
    public static Graph loadFromText(String filename) {
        Charset cs = Charset.forName("US-ASCII");
        try (BufferedReader r = Files.newBufferedReader(Paths.get(filename), cs)) {
            Graph g = new Graph();
            String line;
            while ((line = r.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length == 3) {
                    int from = Integer.parseInt(fields[0]);
                    int to = Integer.parseInt(fields[1]);
                    int weight = Integer.parseInt(fields[2]);
                    g.addEdge(from, to, weight);
                } else if (fields.length == 1) {
                    g.addVertex(Integer.parseInt(fields[0]));
                } else {
                    throw new IllegalArgumentException("Bad input file!");
                }
            }
            return g;
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        for (int i = 0; i < 6; i++) {
            g.addVertex(i);
        }
        g.addEdge(0, 2, 1);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 2);
        g.addEdge(1, 3, 1);
        g.addEdge(3, 5, 4);
        g.addEdge(3, 4, 3);
        g.addEdge(2, 4, 3);
        g.addEdge(4, 5, 1);

        Graph gPrim = g.prims(0);
        System.out.println(gPrim.spans(g)); // true

        TreeSet<Edge> gPrimEdges = gPrim.getAllEdges();

        gPrimEdges.forEach(e -> System.out.println(e));
//        {2, 0} -> 1
//        {3, 1} -> 1
//        {5, 4} -> 1
//        {1, 0} -> 2
//        {4, 2} -> 3

        System.out.println("============");

        Graph gKruskals = g.kruskals();
        System.out.println(gKruskals.spans(g)); // true

        TreeSet<Edge> gKruskalsEdges = gKruskals.getAllEdges();

        gKruskalsEdges.forEach(e -> System.out.println(e));
//        {0, 2} -> 1
//        {1, 3} -> 1
//        {4, 5} -> 1
//        {0, 1} -> 2
//        {3, 4} -> 3

    }
}
