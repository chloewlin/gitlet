package bearmaps.utils.graph;

import bearmaps.utils.pq.MinHeapPQ;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private int numPolls = 0;
    private double currTotalWeight = 0;
    private MinHeapPQ<Vertex> pq;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private AStarGraph<Vertex> aStarGraph;
    private SolverOutcome outcome = SolverOutcome.UNSOLVABLE;
    private ArrayList<Vertex> path;
    private double timeSpent = 0.0;

    private double aStarPriority(Vertex v, Vertex goal) {
//        System.out.println("id: " + v + " weight: " + this.distTo.get(v) + " distToGoal: " + this.aStarGraph.estimatedDistanceToGoal(v, goal));
        return this.distTo.get(v) + this.aStarGraph.estimatedDistanceToGoal(v, goal);
    }
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.aStarGraph = input;
        this.pq = new MinHeapPQ<Vertex>();
        this.distTo = new HashMap<Vertex, Double>();
        this.edgeTo = new HashMap<Vertex, Vertex>();
        this.distTo.put(start, 0.0);
        this.edgeTo.put(start, null);
        this.pq.insert(start, aStarPriority(start, end));
        this.path = new ArrayList<Vertex>();

        Date date= new Date();
        long startTime = date.getTime();

        while (this.pq.size() > 0) {
            this.timeSpent = (date.getTime() - startTime) / 1000.0;
            if (this.timeSpent > timeout) {
                this.outcome = SolverOutcome.TIMEOUT;
                break;
            }
            Vertex curr = this.pq.poll();
            this.numPolls++;
            if (curr.equals(end)) {
                this.outcome = SolverOutcome.SOLVED;
                Stack<Vertex> stack = new Stack<Vertex>();
                do {
                    stack.push(curr);
                    curr = this.edgeTo.get(curr);
                } while (curr != null);
                while (!stack.isEmpty()) {
                    this.path.add(stack.pop());
                }
                break;
            }
            for (WeightedEdge<Vertex> edge : this.aStarGraph.neighbors(curr)) {
                if (edge.to() == start) {
                    continue;
                } else if (this.distTo.get(edge.to()) == null
                        || this.distTo.get(curr) + edge.weight() < this.distTo.get(edge.to())) {
                    this.distTo.put(edge.to(), this.distTo.get(curr) + edge.weight());
                    this.edgeTo.put(edge.to(), curr);
                    if (!this.pq.contains(edge.to())) {
                        this.pq.insert(edge.to(), aStarPriority(edge.to(), end));
                    } else {
                        this.pq.changePriority(edge.to(), aStarPriority(edge.to(), end));
                    }
                }
            }
        }
    }
    public SolverOutcome outcome() {
        return this.outcome;
    }
    public List<Vertex> solution() {
        return this.path;
    }
    public double solutionWeight() {
        return this.currTotalWeight;
    }
    public int numStatesExplored() {
        return this.numPolls;
    }
    public double explorationTime() {
        return this.timeSpent;
    }
}
