import java.util.*;

public class AStarSearch {
    
    public static class AStarNode implements Comparable<AStarNode> {
        Nodes node;
        double gCost;
        double hCost;
        double fCost;
        AStarNode parent;
        
        public AStarNode(Nodes node, double gCost, double hCost, AStarNode parent) {
            this.node = node;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }
        
        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.fCost, other.fCost);
        }
        
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
    
    public static class AStarResult {
        public List<String> path;
        public double totalDistance;
        public int nodesExplored;
        
        public AStarResult(List<String> path, double totalDistance, int nodesExplored) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.nodesExplored = nodesExplored;
        }
    }
    
    public static AStarResult aStarSearch(Graph graph, Nodes start, Nodes end) {
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();
        Set<Nodes> closedSet = new HashSet<>();
        Map<Nodes, AStarNode> allNodes = new HashMap<>();
        
        AStarNode startNode = new AStarNode(start, 0, heuristic(start, end), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);
        
        int nodesExplored = 0;
        
        while (!openSet.isEmpty()) {
            AStarNode currentNode = openSet.poll();
            nodesExplored++;
            
            if (currentNode.node.equals(end)) {
                return new AStarResult(reconstructPath(currentNode), currentNode.gCost, nodesExplored);
            }
            
            closedSet.add(currentNode.node);
            
            for (Edge edge : currentNode.node.edges) {
                Nodes neighbor = edge.destination;
                
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                double tentativeGCost = currentNode.gCost + edge.weight;
                AStarNode neighborNode = allNodes.get(neighbor);
                
                if (neighborNode == null) {
                    neighborNode = new AStarNode(neighbor, Double.POSITIVE_INFINITY, heuristic(neighbor, end), null);
                    allNodes.put(neighbor, neighborNode);
                }
                
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
        
        return new AStarResult(new ArrayList<>(), Double.POSITIVE_INFINITY, nodesExplored);
    }
    
    private static double heuristic(Nodes current, Nodes goal) {
        return Math.abs(current.n - goal.n) * 100;
    }
    
    private static List<String> reconstructPath(AStarNode endNode) {
        List<String> path = new ArrayList<>();
        AStarNode current = endNode;
        
        while (current != null) {
            path.add(0, current.node.name);
            current = current.parent;
        }
        
        return path;
    }
    
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
    
    public static AStarResult findPathWithLandmarks(Graph graph, Nodes start, Nodes end, List<String> landmarks) {
        if (landmarks.isEmpty()) {
            return aStarSearch(graph, start, end);
        }
        
        List<Nodes> landmarkNodes = new ArrayList<>();
        for (String landmark : landmarks) {
            for (Nodes node : graph.getNodes()) {
                if (node.name.toLowerCase().contains(landmark.toLowerCase())) {
                    landmarkNodes.add(node);
                    break;
                }
            }
        }
        
        if (landmarkNodes.isEmpty()) {
            return aStarSearch(graph, start, end);
        }
        
        List<String> bestPath = new ArrayList<>();
        double bestDistance = Double.POSITIVE_INFINITY;
        int bestNodesExplored = 0;
        
        for (Nodes landmark : landmarkNodes) {
            AStarResult toLandmark = aStarSearch(graph, start, landmark);
            AStarResult fromLandmark = aStarSearch(graph, landmark, end);
            
            if (!toLandmark.path.isEmpty() && !fromLandmark.path.isEmpty()) {
                double totalDistance = toLandmark.totalDistance + fromLandmark.totalDistance;
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    bestNodesExplored = toLandmark.nodesExplored + fromLandmark.nodesExplored;
                    
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