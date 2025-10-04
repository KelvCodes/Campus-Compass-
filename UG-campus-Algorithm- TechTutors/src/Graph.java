import java.util.*;

public class Graph {
    private Set<Nodes> nodes;     // Stores all nodes in the graph
    private boolean directed;     // Determines if graph is directed (true) or undirected (false)

    // Constructor: initializes the graph as directed/undirected
    Graph(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    // Add one or more nodes to the graph
    public void addNode(Nodes... n) {
        nodes.addAll(Arrays.asList(n));
    }

    // Add an edge between two nodes with a given weight
    public void addEdge(Nodes source, Nodes destination, double weight) {
        nodes.add(source);
        nodes.add(destination);

        // Add edge from source → destination
        addEgdeHelper(source, destination, weight);

        // If graph is undirected, add edge in both directions
        if (!directed && source != destination) {
            addEgdeHelper(destination, source, weight);
        }
    }

    // Helper method to add or update an edge between two nodes
    public void addEgdeHelper(Nodes a, Nodes b, double weight) {
        for (Edge edge : a.edges) {
            if (edge.source == a && edge.destination == b) {
                // If edge already exists, update weight
                edge.weight = weight;
                return;
            }
        }
        // If edge doesn’t exist, create new one
        a.edges.add(new Edge(a, b, weight));
    }

    // Print all edges for each node in the graph
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

    // Check if there is an edge between two nodes
    public boolean hasEdge(Nodes source, Nodes destination) {
        LinkedList<Edge> edges = source.edges;
        for (Edge edge : edges) {
            if (edge.destination == destination) {
                return true;
            }
        }
        return false;
    }

    // Reset visited status of all nodes (useful for multiple traversals/algorithms)
    public void resetNodesVisited() {
        for (Nodes node : nodes) {
            node.unvisit();
        }
    }

    // Get all nodes in the graph
    public Set<Nodes> getNodes() {
        return new HashSet<>(nodes);
    }

    /**
     * Dijkstra-like shortest path algorithm
     * Finds and returns the shortest path between two nodes (start → end).
     * Tracks both path and total distance.
     */
    public String shortestPath(Nodes start, Nodes end) {
        HashMap<Nodes, Nodes> changedAt = new HashMap<>(); // Keeps track of parent nodes for path reconstruction
        changedAt.put(start, null);

        HashMap<Nodes, Double> shortestPath = new HashMap<>(); // Stores shortest distance to each node

        // Initialize all distances: 0 for start, infinity for others
        for (Nodes node : nodes) {
            if (node == start) {
                shortestPath.put(node, 0.0);
            } else {
                shortestPath.put(node, Double.POSITIVE_INFINITY);
            }
        }

        // Initialize distances for neighbors of start node
        for (Edge edge : start.edges) {
            shortestPath.put(edge.destination, edge.weight);
            changedAt.put(edge.destination, start);
        }

        start.visit();

        // Core loop: select closest unvisited node and relax its neighbors
        while (true) {
            Nodes currentNode = closestReachableUnvisited(shortestPath);

            // If no more reachable nodes → no path exists
            if (currentNode == null) {
                System.out.println("There isn't a path between " + start.name + " and " + end.name + " (or they are the same place).");
                return "There isn't a path between " + start.name + " and " + end.name + " (or they are the same place).";
            }

            // If we reached the destination → reconstruct path
            if (currentNode == end) {
                System.out.println("The path with the smallest weight between "
                        + start.name + " and " + end.name + " is:");

                Nodes child = end;
                String path = end.name;

                // Trace path backwards using changedAt map
                while (true) {
                    Nodes parent = changedAt.get(child);
                    if (parent == null) break;
                    path = parent.name + " ---- " + path;
                    child = parent;
                }

                System.out.println(path);
                System.out.println("Distance: " + shortestPath.get(end));
                System.out.println("Time taken: " + shortestPath.get(end) / 2 + " seconds");

                return path + "\nDistance: " + shortestPath.get(end) +
                        "\nTime taken: " + shortestPath.get(end) / 2 + " seconds";
            }

            // Mark node as visited
            currentNode.visit();

            // Relax neighbors (update distances if shorter path found)
            for (Edge edge : currentNode.edges) {
                if (edge.destination.isVisited())
                    continue;

                if (shortestPath.get(currentNode) + edge.weight < shortestPath.get(edge.destination)) {
                    shortestPath.put(edge.destination, shortestPath.get(currentNode) + edge.weight);
                    changedAt.put(edge.destination, currentNode);
                }
            }
        }
    }

    /**
     * Finds the closest unvisited node based on shortestPath map.
     * Equivalent to Dijkstra’s "extract-min" step.
     */
    private Nodes closestReachableUnvisited(HashMap<Nodes, Double> shortestPathMap) {
        double shortestDistance = Double.POSITIVE_INFINITY;
        Nodes closestReachableNode = null;

        for (Nodes node : nodes) {
            if (node.isVisited()) continue;

            double currentDistance = shortestPathMap.get(node);
            if (currentDistance == Double.POSITIVE_INFINITY) continue;

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }
}

