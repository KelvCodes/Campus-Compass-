import java.util.*;

public class RouteOptimizer {
    
    public static class RouteAnalysis {
        public List<SortingAlgorithms.Route> routes;
        public SortingAlgorithms.Route optimalRoute;
        public Map<String, Double> algorithmPerformance;
        public List<String> landmarks;
        public double trafficFactor;
        
        public RouteAnalysis() {
            this.routes = new ArrayList<>();
            this.algorithmPerformance = new HashMap<>();
            this.landmarks = new ArrayList<>();
            this.trafficFactor = 1.0;
        }
    }
    
    public static RouteAnalysis findOptimalRoutes(Graph graph, Nodes start, Nodes end, List<String> landmarks) {
        RouteAnalysis analysis = new RouteAnalysis();
        
        long startTime = System.currentTimeMillis();
        
        String dijkstraPath = graph.shortestPath(start, end);
        long dijkstraTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        FloydWarshall.ShortestPathResult floydResult = FloydWarshall.floydWarshall(graph, start, end);
        long floydTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        AStarSearch.AStarResult aStarResult = AStarSearch.aStarSearch(graph, start, end);
        long aStarTime = System.currentTimeMillis() - startTime;
        
        analysis.algorithmPerformance.put("Dijkstra", (double) dijkstraTime);
        analysis.algorithmPerformance.put("Floyd-Warshall", (double) floydTime);
        analysis.algorithmPerformance.put("A*", (double) aStarTime);
        
        if (!dijkstraPath.contains("There isn't a path")) {
            String[] dijkstraParts = dijkstraPath.split("----");
            List<String> dijkstraPathList = new ArrayList<>();
            for (String part : dijkstraParts) {
                dijkstraPathList.add(part.trim());
            }
            double dijkstraDistance = extractDistance(dijkstraPath);
            analysis.routes.add(new SortingAlgorithms.Route(dijkstraPathList, dijkstraDistance, dijkstraDistance/2, "Dijkstra"));
        }
        
        if (!floydResult.path.isEmpty()) {
            double floydDistance = floydResult.totalDistance;
            analysis.routes.add(new SortingAlgorithms.Route(floydResult.path, floydDistance, floydDistance/2, "Floyd-Warshall"));
        }
        
        if (!aStarResult.path.isEmpty()) {
            analysis.routes.add(new SortingAlgorithms.Route(aStarResult.path, aStarResult.totalDistance, aStarResult.totalDistance/2, "A*"));
        }
        
        if (!landmarks.isEmpty()) {
            AStarSearch.AStarResult landmarkResult = AStarSearch.findPathWithLandmarks(graph, start, end, landmarks);
            if (!landmarkResult.path.isEmpty()) {
                analysis.routes.add(new SortingAlgorithms.Route(landmarkResult.path, landmarkResult.totalDistance, landmarkResult.totalDistance/2, "Landmark-A*"));
            }
        }
        
        analysis.optimalRoute = SortingAlgorithms.findOptimalRoute(analysis.routes);
        analysis.landmarks = landmarks;
        
        return analysis;
    }
    
    private static double extractDistance(String pathString) {
        try {
            String[] parts = pathString.split("Distance:");
            if (parts.length > 1) {
                String distancePart = parts[1].split("Time")[0].trim();
                return Double.parseDouble(distancePart);
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }
    
    public static RouteAnalysis findMultipleRouteOptions(Graph graph, Nodes start, Nodes end, int numOptions) {
        RouteAnalysis analysis = new RouteAnalysis();
        
        List<AStarSearch.AStarResult> multiplePaths = AStarSearch.findMultiplePaths(graph, start, end, numOptions);
        
        for (AStarSearch.AStarResult result : multiplePaths) {
            analysis.routes.add(new SortingAlgorithms.Route(result.path, result.totalDistance, result.totalDistance/2, "A* Multiple"));
        }
        
        SortingAlgorithms.sortByDistance(analysis.routes);
        analysis.optimalRoute = analysis.routes.isEmpty() ? null : analysis.routes.get(0);
        
        return analysis;
    }
    
    public static RouteAnalysis optimizeForTraffic(Graph graph, Nodes start, Nodes end, double trafficFactor) {
        RouteAnalysis analysis = findOptimalRoutes(graph, start, end, new ArrayList<>());
        analysis.trafficFactor = trafficFactor;
        
        for (SortingAlgorithms.Route route : analysis.routes) {
            route.time *= trafficFactor;
        }
        
        SortingAlgorithms.sortByTime(analysis.routes);
        analysis.optimalRoute = SortingAlgorithms.findOptimalRoute(analysis.routes);
        
        return analysis;
    }
    
    public static List<SortingAlgorithms.Route> findRoutesWithLandmarks(Graph graph, Nodes start, Nodes end, String landmark) {
        List<SortingAlgorithms.Route> allRoutes = new ArrayList<>();
        
        RouteAnalysis analysis = findOptimalRoutes(graph, start, end, Arrays.asList(landmark));
        allRoutes.addAll(analysis.routes);
        
        return SortingAlgorithms.filterRoutesByLandmark(allRoutes, landmark);
    }
    
    public static Map<String, List<SortingAlgorithms.Route>> compareAlgorithms(Graph graph, Nodes start, Nodes end) {
        RouteAnalysis analysis = findOptimalRoutes(graph, start, end, new ArrayList<>());
        return SortingAlgorithms.groupRoutesByAlgorithm(analysis.routes);
    }
    
    public static String generateRouteReport(RouteAnalysis analysis) {
        StringBuilder report = new StringBuilder();
        report.append("=== UG Campus Route Analysis ===\n\n");
        
        if (analysis.optimalRoute != null) {
            report.append("Optimal Route: ").append(analysis.optimalRoute.toString()).append("\n");
            report.append("Algorithm: ").append(analysis.optimalRoute.algorithm).append("\n\n");
        }
        
        report.append("All Available Routes:\n");
        for (int i = 0; i < analysis.routes.size(); i++) {
            SortingAlgorithms.Route route = analysis.routes.get(i);
            report.append(i + 1).append(". ").append(route.toString()).append("\n");
        }
        
        if (!analysis.landmarks.isEmpty()) {
            report.append("\nLandmarks Considered: ").append(String.join(", ", analysis.landmarks)).append("\n");
        }
        
        if (analysis.trafficFactor != 1.0) {
            report.append("Traffic Factor: ").append(analysis.trafficFactor).append("x\n");
        }
        
        report.append("\nAlgorithm Performance (ms):\n");
        for (Map.Entry<String, Double> entry : analysis.algorithmPerformance.entrySet()) {
            report.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("ms\n");
        }
        
        return report.toString();
    }
    
    public static List<SortingAlgorithms.Route> applyDivideAndConquer(Graph graph, Nodes start, Nodes end) {
        List<SortingAlgorithms.Route> routes = new ArrayList<>();
        
        Set<Nodes> allNodes = graph.getNodes();
        List<Nodes> nodesList = new ArrayList<>(allNodes);
        int mid = nodesList.size() / 2;
        
        List<Nodes> firstHalf = nodesList.subList(0, mid);
        List<Nodes> secondHalf = nodesList.subList(mid, nodesList.size());
        
        for (Nodes intermediate : firstHalf) {
            if (!intermediate.equals(start) && !intermediate.equals(end)) {
                String path1 = graph.shortestPath(start, intermediate);
                String path2 = graph.shortestPath(intermediate, end);
                
                if (!path1.contains("There isn't a path") && !path2.contains("There isn't a path")) {
                    double distance1 = extractDistance(path1);
                    double distance2 = extractDistance(path2);
                    double totalDistance = distance1 + distance2;
                    
                    List<String> combinedPath = new ArrayList<>();
                    String[] parts1 = path1.split("----");
                    for (String part : parts1) {
                        combinedPath.add(part.trim());
                    }
                    String[] parts2 = path2.split("----");
                    for (int i = 1; i < parts2.length; i++) {
                        combinedPath.add(parts2[i].trim());
                    }
                    
                    routes.add(new SortingAlgorithms.Route(combinedPath, totalDistance, totalDistance/2, "Divide-Conquer"));
                }
            }
        }
        
        SortingAlgorithms.sortByDistance(routes);
        return routes.subList(0, Math.min(3, routes.size()));
    }
    
    public static SortingAlgorithms.Route applyGreedyAlgorithm(Graph graph, Nodes start, Nodes end) {
        List<String> path = new ArrayList<>();
        path.add(start.name);
        
        Nodes current = start;
        double totalDistance = 0;
        
        while (!current.equals(end)) {
            Edge bestEdge = null;
            double minDistance = Double.POSITIVE_INFINITY;
            
            for (Edge edge : current.edges) {
                if (!path.contains(edge.destination.name) && edge.weight < minDistance) {
                    minDistance = edge.weight;
                    bestEdge = edge;
                }
            }
            
            if (bestEdge == null) {
                break;
            }
            
            path.add(bestEdge.destination.name);
            totalDistance += bestEdge.weight;
            current = bestEdge.destination;
        }
        
        return new SortingAlgorithms.Route(path, totalDistance, totalDistance/2, "Greedy");
    }
} 