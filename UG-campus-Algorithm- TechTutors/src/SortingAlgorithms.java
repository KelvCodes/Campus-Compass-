rk(List<Route> route();
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
