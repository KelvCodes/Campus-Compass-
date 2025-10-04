h weight " + edge.weight);
            }

            System.out.println();
        }

    }

    public boolean hasEdge(Nodes source, Nodes destination) {
        LinkedList<Edge> edges = source.edges;
        for (Edge edge : edges) {
            if (edge.destination == destination) {
                return true;
            }
        }
        return false;

    }

    public void resetNodesVisited() {
        for (Nodes node : nodes) {
            node.unvisit();
        }
    }

    public Set<Nodes> getNodes() {
        return new HashSet<>(nodes);
    }


    public String shortestPath(Nodes start, Nodes end) {
        HashMap<Nodes, Nodes> changedAt = new HashMap<>();
        changedAt.put(start, null);

        HashMap<Nodes, Double> shortestPath = new HashMap<>();

        for (Nodes node : nodes) {
            if (node == start) {
                shortestPath.put(node, 0.0);
            } else {
                shortestPath.put(node, Double.POSITIVE_INFINITY);
            }
        }
        
        for (Edge edge : start.edges) {
            shortestPath.put(edge.destination, edge.weight);
            changedAt.put(edge.destination, start);
        }

        start.visit();

        while (true) {
            Nodes currentNode = closestReachableUnvisited(shortestPath);
            
            if (currentNode == null) {
                System.out.println("There isn't a path between " + start.name + " and " + end.name + "it is the same place.");
                return "There isn't a path between " + start.name + " and " + end.name + "it is the same place.";
            }

            if (currentNode == end) {
                System.out.println("The path with the smallest weight between "
                        + start.name + " and " + end.name + " is:");

                Nodes child = end;

                String path = end.name;
                while (true) {
                    Nodes parent = changedAt.get(child);
                    if (parent == null) {
                        break;
                    }

                    path = parent.name + " ---- " + path;
                    child = parent;
                }
                System.out.println(path);
                System.out.println("Distance: " + shortestPath.get(end));
                System.out.println(" Time taken: " + shortestPath.get(end)/2 + " seconds");
                return path + "\n   Distance: " + shortestPath.get(end) + " " + "   \nTime taken: " + shortestPath.get(end)/2 + " seconds";
            }
            currentNode.visit();

            for (Edge edge : currentNode.edges) {
                if (edge.destination.isVisited())
                    continue;

                if (shortestPath.get(currentNode)
                        + edge.weight
                        < shortestPath.get(edge.destination)) {
                    shortestPath.put(edge.destination,
                            shortestPath.get(currentNode) + edge.weight);
                    changedAt.put(edge.destination, currentNode);
                }
            }
        }
        
    }

    private Nodes closestReachableUnvisited(HashMap<Nodes, Double> shortestPathMap) {

        double shortestDistance = Double.POSITIVE_INFINITY;
        Nodes closestReachableNode = null;
        for (Nodes node : nodes) {
            if (node.isVisited())
                continue;

            double currentDistance = shortestPathMap.get(node);
            if (currentDistance == Double.POSITIVE_INFINITY)
                continue;

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }
}
