t).name);
  
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
