import java.util.*;

/**
 * The Nodes class represents a vertex (node) in a graph.
 * Each node has:
 * - An identifier (n)
 * - A name (location name)
 * - A visited flag (used for graph traversal algorithms like DFS, BFS, etc.)
 * - A list of edges connected to it
 */
public class Nodes {
    // Unique identifier for the node (can represent index or ID)
    int n;

    // Human-readable name of the location (e.g., "Library", "Hostel")
    String name;

    // Boolean flag to check if the node has been visited (useful for traversals)
    private boolean visited;

    // List of edges (connections) that originate from this node
    LinkedList<Edge> edges;

    /**
     * Constructor to initialize a node with an ID and location name.
     *
     * @param n               Unique integer identifier for the node
     * @param nameOfLocation  Descriptive name of the location
     */
    public Nodes(int n, String nameOfLocation) {
        this.n = n;
        this.name = nameOfLocation;
        visited = false; // Initially, node is not visited
        edges = new LinkedList<>(); // Initialize empty edge list
    }

    /**
     * Checks whether this node has been visited.
     *
     * @return true if visited, false otherwise
     */
    boolean isVisited() {
        return visited;
    }

    /**
     * Marks this node as visited.
     * Useful when performing graph traversal to avoid cycles.
     */
    void visit() {
        visited = true;
    }

    /**
     * Marks this node as unvisited.
     * Resets traversal state if needed for multiple graph traversals.
     */
    void unvisit() {
        visited = false;
    }
}

