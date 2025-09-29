import java.util.*;

public class SortingAlgorithms {
    
    /**
     * Route class to represent a path with details such as:
     * - List of locations (path)
     * - Distance (in meters)
     * - Time taken (in seconds)
     * - Algorithm used (for grouping and analysis)
     */
    public static class Route {
        public List<String> path;
        public double distance;
        public double time;
        public String algorithm;
        
        public Route(List<String> path, double distance, double time, String algorithm) {
            this.path = path;
            this.distance = distance;
            this.time = time;
            this.algorithm = algorithm;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%.2fm, %.1fs)", String.join(" -> ", path), distance, time);
        }
    }
    
    /**
     * QuickSort algorithm to sort routes based on distance.
     * 
     * @param routes List of routes
     * @param low Starting index
     * @param high Ending index
     */
    public static void quickSort(List<Route> routes, int low, int high) {
        if (low < high) {
            int pi = partition(routes, low, high);
            quickSort(routes, low, pi - 1);   // Sort left partition
            quickSort(routes, pi + 1, high); // Sort right partition
        }
    }
    
    /**
     * Partition function for QuickSort.
     * Uses the last element as pivot and rearranges elements accordingly.
     */
    private static int partition(List<Route> routes, int low, int high) {
        double pivot = routes.get(high).distance; // Pivot distance
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (routes.get(j).distance <= pivot) {
                i++;
                Collections.swap(routes, i, j);
            }
        }
        
        Collections.swap(routes, i + 1, high);
        return i + 1;
    }
    
    /**
     * MergeSort algorithm to sort routes by distance.
     * Recursive divide-and-conquer sorting method.
     */
    public static void mergeSort(List<Route> routes) {
        if (routes.size() <= 1) {
            return;
        }
        
        int mid = routes.size() / 2;
        List<Route> left = new ArrayList<>(routes.subList(0, mid));
        List<Route> right = new ArrayList<>(routes.subList(mid, routes.size()));
        
        mergeSort(left);
        mergeSort(right);
        merge(routes, left, right);
    }
    
    /**
     * Merge step for MergeSort.
     * Combines two sorted lists into one sorted list.
     */
    private static void merge(List<Route> routes, List<Route> left, List<Route> right) {
        int i = 0, j = 0, k = 0;
        
        while (i < left.size() && j < right.size()) {
            if (left.get(i).distance <= right.get(j).distance) {
                routes.set(k++, left.get(i++));
            } else {
                routes.set(k++, right.get(j++));
            }
        }
        
        while (i < left.size()) {
            routes.set(k++, left.get(i++));
        }
        
        while (j < right.size()) {
            routes.set(k++, right.get(j++));
        }
    }
    
    /**
     * Sorts routes by distance using QuickSort.
     */
    public static void sortByDistance(List<Route> routes) {
        quickSort(routes, 0, routes.size() - 1);
    }
    
    /**
     * Sorts routes by time (ascending order).
     */
    public static void sortByTime(List<Route> routes) {
        routes.sort((r1, r2) -> Double.compare(r1.time, r2.time));
    }
    
    /**
     * Sorts routes alphabetically by the algorithm used.
     */
    public static void sortByAlgorithm(List<Route> routes) {
        routes.sort((r1, r2) -> r1.algorithm.compareTo(r2.algorithm));
    }
    
    /**
     * Returns the top N shortest routes based on distance.
     */
    public static List<Route> getTopRoutes(List<Route> routes, int count) {
        List<Route> sortedRoutes = new ArrayList<>(routes);
        sortByDistance(sortedRoutes);
        return sortedRoutes.subList(0, Math.min(count, sortedRoutes.size()));
    }
    
    /**
     * Filters routes that pass through a given landmark.
     * 
     * @param routes List of all routes
     * @param landmark Substring of the location name
     * @return List of routes containing the landmark
     */
    public static List<Route> filterRoutesByLandmark(List<Route> routes, String landmark) {
        List<Route> filtered = new ArrayList<>();
        for (Route route : routes) {
            for (String location : route.path) {
                if (location.toLowerCase().contains(landmark.toLowerCase())) {
                    filtered.add(route);
                    break;
                }
            }
        }
        return filtered;
    }
    
    /**
     * Groups routes by the algorithm used to generate them.
     * 
     * @param routes List of routes
     * @return Map of algorithm name -> List of routes
     */
    public static Map<String, List<Route>> groupRoutesByAlgorithm(List<Route> routes) {
        Map<String, List<Route>> grouped = new HashMap<>();
        
        for (Route route : routes) {
            grouped.computeIfAbsent(route.algorithm, k -> new ArrayList<>()).add(route);
        }
        
        return grouped;
    }
    
    /**
     * Finds the most "optimal" route based on a weighted score.
     * The score is computed as: (0.7 * distance + 0.3 * time).
     * Lower scores mean better routes.
     */
    public static Route findOptimalRoute(List<Route> routes) {
        if (routes.isEmpty()) {
            return null;
        }
        
        Route optimal = routes.get(0);
        double bestScore = optimal.distance * 0.7 + optimal.time * 0.3;
        
        for (Route route : routes) {
            double score = route.distance * 0.7 + route.time * 0.3;
            if (score < bestScore) {
                bestScore = score;
                optimal = route;
            }
        }
        
        return optimal;
    }
}
