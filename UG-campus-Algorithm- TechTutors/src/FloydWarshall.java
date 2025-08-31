import java.util.*;

public class FloydWarshall {
    private static final double INF = Double.POSITIVE_INFINITY;
    
    public static class ShortestPathResult {
        public double[][] distances;
        public int[][] next;
        public List<String> path;
        public double totalDistance;
        
        public ShortestPathResult(double[][] distances, int[][] next, List<String> path, double totalDistance) {
            this.distances = distances;
            this.next = next;
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }
    
    public static ShortestPathResult floydWarshall(Graph graph, Nodes start, Nodes end) {
        List<Nodes> nodesList = new ArrayList<>(graph.getNodes());
        int n = nodesList.size();
        
        double[][] distances = new double[n][n];
        int[][] next = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            Arrays.fill(distances[i], INF);
            Arrays.fill(next[i], -1);
            distances[i][i] = 0;
        }
        
        for (int i = 0; i < n; i++) {
            Nodes node = nodesList.get(i);
            for (Edge edge : node.edges) {
                int j = nodesList.indexOf(edge.destination);
                distances[i][j] = edge.weight;
                next[i][j] = j;
            }
        }
        
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
        
        int startIndex = nodesList.indexOf(start);
        int endIndex = nodesList.indexOf(end);
        
        List<String> path = reconstructPath(next, nodesList, startIndex, endIndex);
        double totalDistance = distances[startIndex][endIndex];
        
        return new ShortestPathResult(distances, next, path, totalDistance);
    }
    
    private static List<String> reconstructPath(int[][] next, List<Nodes> nodesList, int start, int end) {
        List<String> path = new ArrayList<>();
        
        if (next[start][end] == -1) {
            return path;
        }
        
        path.add(nodesList.get(start).name);
        while (start != end) {
            start = next[start][end];
            path.add(nodesList.get(start).name);
        }
        
        return path;
    }
    
    public static List<List<String>> getAllShortestPaths(Graph graph) {
        List<Nodes> nodesList = new ArrayList<>(graph.getNodes());
        int n = nodesList.size();
        
        double[][] distances = new double[n][n];
        int[][] next = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            Arrays.fill(distances[i], INF);
            Arrays.fill(next[i], -1);
            distances[i][i] = 0;
        }
        
        for (int i = 0; i < n; i++) {
            Nodes node = nodesList.get(i);
            for (Edge edge : node.edges) {
                int j = nodesList.indexOf(edge.destination);
                distances[i][j] = edge.weight;
                next[i][j] = j;
            }
        }
        
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