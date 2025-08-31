import java.util.*;

public class Graph {
    private Set<Nodes> nodes;
    private boolean directed;
    Graph(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    public void addNode(Nodes... n) {
        nodes.addAll(Arrays.asList(n));
    }

    public void addEdge(Nodes source, Nodes destination, double weight) {
        nodes.add(source);
        nodes.add(destination);

        addEgdeHelper(source, destination, weight);

        if (!directed && source != destination) {
            addEgdeHelper(destination, source, weight);
        }

    }

    public void addEgdeHelper(Nodes a, Nodes b, double weight) {
        for (Edge edge : a.edges) {
            if (edge.source == a && edge.destination == b) {
                edge.weight = weight;
                return;
            }
        }

        a.edges.add(new Edge(a, b, weight));
    }

    public void printEdges() {
        for (Nodes node : nodes) {
            LinkedList<Edge> edges = node.edges;

            if (edges.isEmpty()) {
                System.out.println(node.name + " has no edges");
                continue;
            }
            System.out.println("Node " + node.name + " has edges to: ");

            for (Edge edge : edges) {
                System.out.println("\t" + edge.destination.name + " with weight " + edge.weight);
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
