
ew i][j] = edge.weight;
              distances[k][j];
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
