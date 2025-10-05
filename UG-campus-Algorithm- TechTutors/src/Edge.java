/**
 * Represents an edge in a graph.
 * Each edge connects a source node to a destination node with a given weight (cost/distance).
 * Implements Comparable to allow sorting edges based on weight (useful in algorithms like Kruskal's MST).
 */
public class Edge implements Comparable<Edge> {
    Nodes source;        // Starting node of the edge
    Nodes destination;   // Ending node of the edge
    double weight;       // Weight (or cost/distance) of traveling through this edge

    /**
     * Constructor to create an edge between two nodes with a given weight.
     * @param source       Starting node
     * @param destination  Ending node
     * @param weight       Weight (e.g., distance, cost, or time)
     */
    public Edge(Nodes source, Nodes destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Returns a string representation of the edge in the format:
     * "Source -> Destination, weight"
     */
    public String toString() {
        return String.format("%s -> %s, %f", source.name, destination.name, weight);
    }

    /**
     * Compares this edge with another based on their weights.
     * Used to sort edges in ascending order of weight.
     * 
     * @param otherEdge The edge to compare against
     * @return 1 if this edge has greater weight,
     *        -1 if this edge has smaller or equal weight
     */
    public int compareTo(Edge otherEdge) {
        if (this.weight > otherEdge.weight) {
            return 1;
        } else {
            return -1;
        }
    }
}

