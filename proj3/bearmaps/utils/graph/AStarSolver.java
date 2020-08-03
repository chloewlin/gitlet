package bearmaps.utils.graph;

import bearmaps.utils.pq.MinHeapPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private int numPolls = 0;
    private double currTotalWeight = 0;
    private MinHeapPQ<Vertex> pq;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private AStarGraph<Vertex> aStarGraph;
    private SolverOutcome outcome = SolverOutcome.UNSOLVABLE;
    private ArrayList<Vertex> path;

    private double aStarPriority(Vertex v, Vertex goal) {
        return distTo.get(v) + aStarGraph.estimatedDistanceToGoal(v, goal);
    }
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        aStarGraph = input;
        pq = new MinHeapPQ<Vertex>();
        distTo.put(start, 0.0);
        edgeTo.put(start, null);
        pq.insert(start, aStarPriority(start, end));
        path = new ArrayList<Vertex>();

        while(pq.size() > 0) {
            Vertex curr = pq.poll();
            if (curr.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                Stack<Vertex> stack = new Stack<Vertex>();
                do {
                    stack.push(curr);
                    curr = edgeTo.get(curr);
                } while (curr != null);
                while (!stack.isEmpty()) {
                    path.add(stack.pop());
                }
                break;
            }
            for (WeightedEdge<Vertex> edge : aStarGraph.neighbors(curr)) {
                if (!pq.contains(edge.to())) {
                    distTo.put(edge.to(), distTo.get(curr) + edge.weight());
                    edgeTo.put(edge.to(), curr);
                    pq.insert(edge.to(), aStarPriority(edge.to(), end));
                } else if (distTo.get(curr) + edge.weight() < distTo.get(edge.to())) {
                    distTo.put(edge.to(), distTo.get(curr) + edge.weight());
                    edgeTo.put(edge.to(), curr);
                    pq.changePriority(edge.to(), aStarPriority(edge.to(), end));
                }
            }
        }
    }
    public SolverOutcome outcome() {
        return outcome;
    }
    public List<Vertex> solution() {
        return path;
    }
    public double solutionWeight() {
        return currTotalWeight;
    }
    public int numStatesExplored() {
        return numPolls;
    }
    public double explorationTime() {
        return 0;
    }
}
