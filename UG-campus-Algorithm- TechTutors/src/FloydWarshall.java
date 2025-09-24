
                    List<String> path = reconstructPath(next, nodesList, i, j);
                    allPaths.add(path);
                }
            }
        }
        
        return allPaths;
    }
} 
