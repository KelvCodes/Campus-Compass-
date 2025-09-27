
        doubl
        return 0.8;
    }
    
    public static TimeBasedRoute optimizeForTime(List<String> path, double distance, double baseTime, LocalTime departureTime) {
        TimeBasedRoute route = new TimeBasedRoute(path, distance, baseTime);
        
        double totalTrafficFactor = 1.0;
        for (String location : path) {
            totalTrafficFactor += calculateTrafficFactor(location, departureTime);
        }
        totalTrafficFactor /= path.size();
        
        route.trafficAdjustedTime = baseTime * totalTrafficFactor;
        route.recommendedTime = getRecommendedTime(departureTime, totalTrafficFactor);
        
        return route;
    }
    
    private static String getRecommendedTime(LocalTime currentTime, double trafficFactor) {
        if (trafficFactor > 1.5) {
            LocalTime recommended = currentTime.plusMinutes(30);
            return "Consider leaving at " + recommended.toString();
        } else if (trafficFactor > 1.2) {
            LocalTime recommended = currentTime.plusMinutes(15);
            return "Consider leaving at " + recommended.toString();
        } else {
            return "Current time is optimal";
        }
    }
    
    public static List<TimeBasedRoute> findOptimalDepartureTimes(List<String> path, double distance, double baseTime) {
        List<TimeBasedRoute> routes = new ArrayList<>();
        
        for (int hour = 8; hour <= 18; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            TimeBasedRoute route = optimizeForTime(path, distance, baseTime, time);
            routes.add(route);
        }
        
        routes.sort((r1, r2) -> Double.compare(r1.trafficAdjustedTime, r2.trafficAdjustedTime));
        return routes.subList(0, Math.min(3, routes.size()));
    }
    
    public static String generateTrafficReport(List<String> path, LocalTime time) {
        StringBuilder report = new StringBuilder();
        report.append("🚦 TRAFFIC ANALYSIS REPORT\n");
        report.append("Departure Time: ").append(time.toString()).append("\n\n");
        
        report.append("Location Traffic Factors:\n");
        for (String location : path) {
            double factor = calculateTrafficFactor(location, time);
            String status = factor > 1.5 ? "HIGH" : factor > 1.2 ? "MODERATE" : "LOW";
            report.append("• ").append(location).append(": ").append(status)
                  .append(" (").append(String.format("%.1f", factor)).append("x)\n");
        }
        
        return report.toString();
    }
} 
