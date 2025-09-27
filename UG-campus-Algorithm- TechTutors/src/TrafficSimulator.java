
import java.time.LocalTime;
import java.util.*;

/**
 * TrafficSimulator.java
 * ---------------------
 * A simple traffic simulation and route optimization program.
 *
 * Features:
 * ✅ Models traffic conditions based on location and time.
 * ✅ Adjusts travel times dynamically according to congestion factors.
 * ✅ Suggests better departure times when traffic is high.
 * ✅ Finds top 3 optimal departure times for a given route.
 * ✅ Generates human-readable traffic reports with severity levels.
 *
 * This could be adapted for real-world navigation/route-planning systems.
 */
public class TrafficSimulator {
    
    /**
     * Represents traffic conditions for a specific location and time.
     */
    public static class TrafficCondition {
        public double congestionFactor;   // Multiplier for congestion (e.g., 1.5x = 50% delay)
        public LocalTime time;            // Time of observation
        public String location;           // Location name
        public String weather;            // Weather conditions (could affect traffic)
        
        public TrafficCondition(double congestionFactor, LocalTime time, String location, String weather) {
            this.congestionFactor = congestionFactor;
            this.time = time;
            this.location = location;
            this.weather = weather;
        }
    }
    
    /**
     * Represents a travel route adjusted for time and traffic conditions.
     */
    public static class TimeBasedRoute {
        public List<String> path;         // Ordered list of locations in the route
        public double distance;           // Total distance (km, miles, etc.)
        public double time;               // Base travel time (without traffic)
        public double trafficAdjustedTime;// Travel time adjusted for congestion
        public String recommendedTime;    // Advice on better departure time
        
        public TimeBasedRoute(List<String> path, double distance, double time) {
            this.path = path;
            this.distance = distance;
            this.time = time;
            this.trafficAdjustedTime = time; // Default until recalculated
        }
    }
    
    // Predefined traffic factors for specific locations on campus/area
    private static Map<String, Double> locationTrafficFactors = new HashMap<>();
    
    // Predefined traffic factors based on time of day (hourly)
    private static Map<LocalTime, Double> timeTrafficFactors = new HashMap<>();
    
    // Static block ensures traffic data is loaded once at startup
    static {
        initializeTrafficData();
    }
    
    /**
     * Initializes traffic factors for different locations and times.
     * Higher values = worse congestion.
     */
    private static void initializeTrafficData() {
        // Location-specific congestion multipliers
        locationTrafficFactors.put("Main Gate", 1.5);
        locationTrafficFactors.put("Balme Library", 1.3);
        locationTrafficFactors.put("Bush Canteen", 1.4);
        locationTrafficFactors.put("Night Market", 1.6);
        locationTrafficFactors.put("Banking Square", 1.2);
        locationTrafficFactors.put("Engineering School", 1.1);
        locationTrafficFactors.put("CS Department", 1.0);
        locationTrafficFactors.put("Math Department", 1.0);
        
        // Time-specific congestion multipliers (peak hours worse)
        timeTrafficFactors.put(LocalTime.of(8, 0), 1.8);
        timeTrafficFactors.put(LocalTime.of(9, 0), 1.6);
        timeTrafficFactors.put(LocalTime.of(10, 0), 1.4);
        timeTrafficFactors.put(LocalTime.of(11, 0), 1.3);
        timeTrafficFactors.put(LocalTime.of(12, 0), 1.7);
        timeTrafficFactors.put(LocalTime.of(13, 0), 1.5);
        timeTrafficFactors.put(LocalTime.of(14, 0), 1.2);
        timeTrafficFactors.put(LocalTime.of(15, 0), 1.3);
        timeTrafficFactors.put(LocalTime.of(16, 0), 1.6);
        timeTrafficFactors.put(LocalTime.of(17, 0), 1.9);
        timeTrafficFactors.put(LocalTime.of(18, 0), 1.7);
    }
    
    /**
     * Calculates combined traffic factor for a location at a given time.
     * @param location The location name
     * @param time The time of departure
     * @return The effective traffic multiplier
     */
    public static double calculateTrafficFactor(String location, LocalTime time) {
        double locationFactor = locationTrafficFactors.getOrDefault(location, 1.0); // Default = no extra traffic
        double timeFactor = getTimeFactor(time);
        return locationFactor * timeFactor;
    }
    
    /**
     * Returns traffic factor based on time of day.
     * Peak hours (8 AM - 6 PM) use predefined factors,
     * while off-peak hours have lighter traffic (0.8x).
     */
    private static double getTimeFactor(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 8 && hour <= 18) {
            return timeTrafficFactors.getOrDefault(LocalTime.of(hour, 0), 1.0);
        }
        return 0.8; // Off-peak factor
    }
    
    /**
     * Optimizes a route for a given departure time by applying traffic adjustments.
     * @param path List of locations in the route
     * @param distance Total distance
     * @param baseTime Estimated travel time without traffic
     * @param departureTime Time of departure
     * @return A TimeBasedRoute object with adjusted travel time and recommendations
     */
    public static TimeBasedRoute optimizeForTime(List<String> path, double distance, double baseTime, LocalTime departureTime) {
        TimeBasedRoute route = new TimeBasedRoute(path, distance, baseTime);
        
        // Calculate average congestion factor across all locations
        double totalTrafficFactor = 1.0;
        for (String location : path) {
            totalTrafficFactor += calculateTrafficFactor(location, departureTime);
        }
        totalTrafficFactor /= path.size();
        
        // Apply traffic adjustment
        route.trafficAdjustedTime = baseTime * totalTrafficFactor;
        
        // Provide departure recommendation
        route.recommendedTime = getRecommendedTime(departureTime, totalTrafficFactor);
        
        return route;
    }
    
    /**
     * Suggests a better departure time if traffic is too heavy.
     * @param currentTime Current departure time
     * @param trafficFactor Calculated traffic multiplier
     * @return Recommendation message
     */
    private static String getRecommendedTime(LocalTime currentTime, double trafficFactor) {
        if (trafficFactor > 1.5) {
            LocalTime recommended = currentTime.plusMinutes(30);
            return "🚨 Heavy traffic! Consider leaving at " + recommended.toString();
        } else if (trafficFactor > 1.2) {
            LocalTime recommended = currentTime.plusMinutes(15);
            return "⚠️ Moderate traffic. Leaving at " + recommended.toString() + " may be better.";
        } else {
            return "✅ Current time is optimal";
        }
    }
    
    /**
     * Finds the top 3 best departure times (between 8 AM - 6 PM).
     * @param path The travel route
     * @param distance Total distance
     * @param baseTime Base travel time without traffic
     * @return List of 3 routes with least congestion
     */
    public static List<TimeBasedRoute> findOptimalDepartureTimes(List<String> path, double distance, double baseTime) {
        List<TimeBasedRoute> routes = new ArrayList<>();
        
        // Check every hour between 8 AM and 6 PM
        for (int hour = 8; hour <= 18; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            TimeBasedRoute route = optimizeForTime(path, distance, baseTime, time);
            routes.add(route);
        }
        
        // Sort by traffic-adjusted travel time (ascending)
        routes.sort((r1, r2) -> Double.compare(r1.trafficAdjustedTime, r2.trafficAdjustedTime));
        
        // Return the best 3 options
        return routes.subList(0, Math.min(3, routes.size()));
    }
    
    /**
     * Generates a detailed traffic report for a route at a specific time.
     * @param path Route path (list of locations)
     * @param time Departure time
     * @return A human-readable traffic report with severity levels
     */
    public static String generateTrafficReport(List<String> path, LocalTime time) {
        StringBuilder report = new StringBuilder();
        report.append("🚦 TRAFFIC ANALYSIS REPORT\n");
        report.append("🕒 Departure Time: ").append(time.toString()).append("\n\n");
        
        report.append("📍 Location Traffic Factors:\n");
        for (String location : path) {
            double factor = calculateTrafficFactor(location, time);
            String status = factor > 1.5 ? "🔴 HIGH" : factor > 1.2 ? "🟡 MODERATE" : "🟢 LOW";
            report.append("• ").append(location).append(": ").append(status)
                  .append(" (").append(String.format("%.1f", factor)).append("x)\n");
        }
        
        return report.toString();
    }
}
