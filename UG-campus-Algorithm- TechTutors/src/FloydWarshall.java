import java.util.*;

/**
 * Floyd-Warshall Algorithm Implementation for CampusCompass 🌍
 * -------------------------------------------------------------
 * This class implements the Floyd-Warshall algorithm, which finds the shortest
 * paths between all pairs of nodes in a weighted graph.
 *
 * Features:
 * - Compute single shortest path between a given start and end node.
 * - Compute all shortest paths between all pairs of nodes.
 */
public class FloydWarshall {
    // Represent infinity (unreachable path distance)
    private static final double INF = Double.POSITIVE_INFINITY;
    
    /**
     * Stores the result of the Floyd-Warshall algorithm.
     */
    public static class ShortestPathResult {
        public double[][] distances;   // Distance matrix (shortest distances between all pairs)
        public int[][] next;           // "Next step" matrix for path reconstruction
        public List<String> path;      // Reconstructed path from start → end
        public double totalDistance;   // Distance of that specific path

        public ShortestPathResult(double[][] distances, int[][] next, List<String> path, double totalDistance) {
            this.distances = distances;
            this.next = next;
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }
    
    /**
     * Runs Floyd-Warshall algorithm to find the shortest path
     * between a given start node and end node.
     */
    public static ShortestPathResult floydWarshall(Graph graph, Nodes start, Nodes end) {
        List<Nodes> nodesList = new ArrayList<>(graph.getNodes());
        int n = nodesList.size();
        
        // Initialize distance and next matrices
        double[][] distances = new double[n][n];
        int[][] next = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            Arrays.fill(distances[i], INF);
            Arrays.fill(next[i], -1);
            distances[i][i] = 0; // Distance to itself is zero
        }
        
        // Fill with edge weights
        for (int i = 0; i < n; i++) {
            Nodes node = nodesList.get(i);
            for (Edge edge : node.edges) {
                int j = nodesList.indexOf(edge.destination);
                distances[i][j] = edge.weight;
                next[i][j] = j;
            }
        }
        
        // Core Floyd-Warshall triple loop
        for (int k = 0; k < n; k++) {         // Intermediate nodes
            for (int i = 0; i < n; i++) {     // Start node
                for (int j = 0; j < n; j++) { // End node
                    if (distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                        next[i][j] = next[i][k]; // Update path
                    }
                }
            }
        }
        
        // Reconstruct shortest path from start → end
        int startIndex = nodesList.indexOf(start);
        int endIndex = nodesList.indexOf(end);
        
        List<String> path = reconstructPath(next, nodesList, startIndex, endIndex);
        double totalDistance = distances[startIndex][endIndex];
        
        return new ShortestPathResult(distances, next, path, totalDistance);
    }
    
    /**
     * Reconstructs the path from start → end using the "next" matrix.
     */
    private static List<String> reconstructPath(int[][] next, List<Nodes> nodesList, int start, int end) {
        List<String> path = new ArrayList<>();
        
        if (next[start][end] == -1) {
            return path; // No path found
        }
        
        path.add(nodesList.get(start).name);
        while (start != end) {
            start = next[start][end];
            path.add(nodesList.get(start).name);
        }
        
        return path;
    }
    
    /**
     * Returns all shortest paths between every pair of nodes in the graph.
     */
    public static List<List<String>> getAllShortestPaths(Graph graph) {
        List<Nodes> nodesList = new ArrayList<>(graph.getNodes());
        int n = nodesList.size();
        
        // Initialize distance and next matrices
        double[][] distances = new double[n][n];
        int[][] next = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            Arrays.fill(distances[i], INF);
            Arrays.fill(next[i], -1);
            distances[i][i] = 0;
        }
        
        // Fill with edge weights
        for (int i = 0; i < n; i++) {
            Nodes node = nodesList.get(i);
            for (Edge edge : node.edges) {
                int j = nodesList.indexOf(edge.destination);
                distances[i][j] = edge.weight;
                next[i][j] = j;
            }
        }
        
        // Floyd-Warshall algorithm (all pairs shortest paths)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        
        // Collect all shortest paths
        List<List<String>> allPaths = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && distances[i][j] != INF) {
                    List<String> path = reconstructPath(next, nodesList, i, j);
                    allPaths.add(path);
                }
            }
        }
        
        return allPaths;
    }
}

