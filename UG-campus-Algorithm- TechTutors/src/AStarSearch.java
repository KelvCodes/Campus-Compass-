import java.util.*;

/**
 * A* Search Algorithm Implementation for CampusCompass 🚀
 * -------------------------------------------------------
 * This class provides an implementation of the A* (A-star) pathfinding algorithm,
 * which is used to find the shortest path between two nodes in a weighted graph.
 *
 * Features:
 * - Standard A* search (optimal shortest path).
 * - Multiple path discovery (up to 3 variations).
 * - Landmark-based pathfinding (forces algorithm to pass through specific nodes).
 */
public class AStarSearch {
    
    /**
     * Internal class to represent a node in A* search.
     * Stores gCost (distance from start), hCost (heuristic),
     * fCost (g + h), and parent (for path reconstruction).
     */
    public static class AStarNode implements Comparable<AStarNode> {
        Nodes node;          // Current graph node
        double gCost;        // Cost from start to this node
        double hCost;        // Estimated cost (heuristic) to goal
        double fCost;        // Total cost = gCost + hCost
        AStarNode parent;    // Parent node (for backtracking path)

        public AStarNode(Nodes node, double gCost, double hCost, AStarNode parent) {
            this.node = node;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }

        // Compare nodes by their fCost (priority queue ordering)
        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.fCost, other.fCost);
        }

        // Equality based on the graph node, not the path
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            AStarNode aStarNode = (AStarNode) obj;
            return Objects.equals(node, aStarNode.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node);
        }
    }
    
    /**
     * Stores the result of an A* search.
     * Includes the final path, total distance, and nodes explored.
     */
    public static class AStarResult {
        public List<String> path;     // List of node names in order
        public double totalDistance;  // Total path distance
        public int nodesExplored;     // Number of nodes explored

        public AStarResult(List<String> path, double totalDistance, int nodesExplored) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.nodesExplored = nodesExplored;
        }
    }
    
    /**
     * Core A* search algorithm.
     * Finds the shortest path between start and end nodes in a weighted graph.
     */
    public static AStarResult aStarSearch(Graph graph, Nodes start, Nodes end) {
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();  // Nodes to explore
        Set<Nodes> closedSet = new HashSet<>();                    // Already explored
        Map<Nodes, AStarNode> allNodes = new HashMap<>();          // Store visited nodes

        // Initialize start node
        AStarNode startNode = new AStarNode(start, 0, heuristic(start, end), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);

        int nodesExplored = 0;

        while (!openSet.isEmpty()) {
            // Pick the node with lowest fCost
            AStarNode currentNode = openSet.poll();
            nodesExplored++;

            // If goal is reached -> reconstruct and return path
            if (currentNode.node.equals(end)) {
                return new AStarResult(
                    reconstructPath(currentNode),
                    currentNode.gCost,
                    nodesExplored
                );
            }

            closedSet.add(currentNode.node);

            // Explore neighbors
            for (Edge edge : currentNode.node.edges) {
                Nodes neighbor = edge.destination;

                // Skip if already processed
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGCost = currentNode.gCost + edge.weight;
                AStarNode neighborNode = allNodes.get(neighbor);

                // If neighbor not visited yet, create a new entry
                if (neighborNode == null) {
                    neighborNode = new AStarNode(
                        neighbor,
                        Double.POSITIVE_INFINITY,
                        heuristic(neighbor, end),
                        null
                    );
                    allNodes.put(neighbor, neighborNode);
                }

                // If better path found -> update node
                if (tentativeGCost < neighborNode.gCost) {
                    neighborNode.parent = currentNode;
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.fCost = tentativeGCost + neighborNode.hCost;

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                }
            }
        }

        // No path found
        return new AStarResult(new ArrayList<>(), Double.POSITIVE_INFINITY, nodesExplored);
    }
    
    /**
     * Simple heuristic function for A*.
     * Here: based on node index difference (scaled).
     * Replace with Manhattan or Euclidean distance for spatial graphs.
     */
    private static double heuristic(Nodes current, Nodes goal) {
        return Math.abs(current.n - goal.n) * 100;
    }
    
    /**
     * Reconstructs path by backtracking parent nodes.
     */
    private static List<String> reconstructPath(AStarNode endNode) {
        List<String> path = new ArrayList<>();
        AStarNode current = endNode;

        while (current != null) {
            path.add(0, current.node.name);  // Insert at start
            current = current.parent;
        }
        return path;
    }
    
    /**
     * Finds multiple unique paths (up to 3 variations).
     * Useful for suggesting alternatives.
     */
    public static List<AStarResult> findMultiplePaths(Graph graph, Nodes start, Nodes end, int numPaths) {
        List<AStarResult> paths = new ArrayList<>();
        Set<String> usedPaths = new HashSet<>();

        for (int i = 0; i < numPaths && i < 3; i++) {
            AStarResult result = aStarSearch(graph, start, end);

            if (result.path.isEmpty() || result.totalDistance == Double.POSITIVE_INFINITY) {
                break;
            }

            String pathString = String.join(" -> ", result.path);

            if (!usedPaths.contains(pathString)) {
                paths.add(result);
                usedPaths.add(pathString);
            }
        }
        return paths;
    }
    
    /**
     * Finds a path that passes through specific landmarks (if any).
     * Example: Path from Hostel -> Library -> Lecture Hall.
     */
    public static AStarResult findPathWithLandmarks(Graph graph, Nodes start, Nodes end, List<String> landmarks) {
        // If no landmarks, run normal A*
        if (landmarks.isEmpty()) {
            return aStarSearch(graph, start, end);
        }

        // Convert landmark names into Nodes
        List<Nodes> landmarkNodes = new ArrayList<>();
        for (String landmark : landmarks) {
            for (Nodes node : graph.getNodes()) {
                if (node.name.toLowerCase().contains(landmark.toLowerCase())) {
                    landmarkNodes.add(node);
                    break;
                }
            }
        }

        // If no valid landmarks found, fallback to normal A*
        if (landmarkNodes.isEmpty()) {
            return aStarSearch(graph, start, end);
        }

        List<String> bestPath = new ArrayList<>();
        double bestDistance = Double.POSITIVE_INFINITY;
        int bestNodesExplored = 0;

        // Try routing through each landmark and pick the shortest total distance
        for (Nodes landmark : landmarkNodes) {
            AStarResult toLandmark = aStarSearch(graph, start, landmark);
            AStarResult fromLandmark = aStarSearch(graph, landmark, end);

            if (!toLandmark.path.isEmpty() && !fromLandmark.path.isEmpty()) {
                double totalDistance = toLandmark.totalDistance + fromLandmark.totalDistance;

                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestNodesExplored = toLandmark.nodesExplored + fromLandmark.nodesExplored;

                    // Merge paths (avoid duplicating landmark node)
                    List<String> combinedPath = new ArrayList<>(toLandmark.path);
                    for (int i = 1; i < fromLandmark.path.size(); i++) {
                        combinedPath.add(fromLandmark.path.get(i));
                    }
                    bestPath = combinedPath;
                }
            }
        }

        return new AStarResult(bestPath, bestDistance, bestNodesExplored);
    }
}

