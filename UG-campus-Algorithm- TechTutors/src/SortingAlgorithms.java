
        int mid = routes.size() / 2;
        List<Route> left = new ArrayList<>(routes.subList(0, mid));
        List<Route> right = new ArrayList<>(routes.subList(mid, routes.size()));
        
        mergeSort(left);
        mergeSort(right);
        merge(routes, left, right);
    }
    
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
    
    public static void sortByDistance(List<Route> routes) {
        quickSort(routes, 0, routes.size() - 1);
    }
    
    public static void sortByTime(List<Route> routes) {
        routes.sort((r1, r2) -> Double.compare(r1.time, r2.time));
    }
    
    public static void sortByAlgorithm(List<Route> routes) {
        routes.sort((r1, r2) -> r1.algorithm.compareTo(r2.algorithm));
    }
    
    public static List<Route> getTopRoutes(List<Route> routes, int count) {
        List<Route> sortedRoutes = new ArrayList<>(routes);
        sortByDistance(sortedRoutes);
        return sortedRoutes.subList(0, Math.min(count, sortedRoutes.size()));
    }
    
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
    
    public static Map<String, List<Route>> groupRoutesByAlgorithm(List<Route> routes) {
        Map<String, List<Route>> grouped = new HashMap<>();
        
        for (Route route : routes) {
            grouped.computeIfAbsent(route.algorithm, k -> new ArrayList<>()).add(route);
        }
        
        return grouped;
    }
    
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
