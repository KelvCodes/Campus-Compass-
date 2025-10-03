import java.util.*;


/**
 * WeatherIntegration integrates weather conditions into route planning.
 * It adjusts travel time, provides recommendations, and generates reports
 * based on real-time weather data for different campus locations.
 */
public class WeatherIntegration {
    
    /**
     * Represents the weather condition at a specific location.
     */
    public static class WeatherCondition {
        public String condition;     // General description (Sunny, Rainy, Cloudy, etc.)
        public double temperature;   // Temperature in °C
        public double humidity;      // Humidity in %
        public double windSpeed;     // Wind speed in km/h
        public boolean isRaining;    // Whether it’s raining
        public double visibility;    // Visibility in km
        
        public WeatherCondition(String condition, double temperature, double humidity, 
            double windSpeed, boolean isRaining, double visibility) {
            this.condition = condition;
            this.temperature = temperature;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.isRaining = isRaining;
            this.visibility = visibility;
        }
    }
    
    /**
     * Represents a route that has been adjusted for weather conditions.
     */
    public static class WeatherAdjustedRoute {
        public List<String> path;             // Locations along the route
        public double distance;               // Distance in km
        public double baseTime;               // Base travel time without weather effects
        public double weatherAdjustedTime;    // Adjusted time considering weather
        public String weatherImpact;          // Description of weather effect
        public List<String> recommendations;  // Weather-related travel tips
        
        public WeatherAdjustedRoute(List<String> path, double distance, double baseTime) {
            this.path = path;
            this.distance = distance;
            this.baseTime = baseTime;
            this.weatherAdjustedTime = baseTime; // Default until adjusted
            this.recommendations = new ArrayList<>();
        }
    }
    
    // Stores weather data for each campus location
    private static Map<String, WeatherCondition> campusWeather = new HashMap<>();
    
    // Initialize weather data once (mock data for campus locations)
    static {
        initializeWeatherData();
    }
    
    /**
     * Sets up predefined weather conditions for major campus landmarks.
     */
    private static void initializeWeatherData() {
        campusWeather.put("Main Gate", new WeatherCondition("Sunny", 28.0, 65.0, 5.0, false, 10.0));
        campusWeather.put("Balme Library", new WeatherCondition("Partly Cloudy", 26.0, 70.0, 3.0, false, 8.0));
        campusWeather.put("Engineering School", new WeatherCondition("Sunny", 29.0, 60.0, 4.0, false, 10.0));
        campusWeather.put("CS Department", new WeatherCondition("Sunny", 27.0, 62.0, 2.0, false, 9.0));
        campusWeather.put("Bush Canteen", new WeatherCondition("Cloudy", 25.0, 75.0, 6.0, true, 6.0));
        campusWeather.put("Night Market", new WeatherCondition("Rainy", 24.0, 80.0, 8.0, true, 4.0));
        campusWeather.put("Sarbah Park", new WeatherCondition("Partly Cloudy", 26.0, 68.0, 4.0, false, 7.0));
        campusWeather.put("Banking Square", new WeatherCondition("Sunny", 28.0, 63.0, 3.0, false, 9.0));
    }
    
    /**
     * Calculates a weather factor multiplier for a location.
     * A factor > 1 means slower travel due to weather conditions.
     */
    public static double calculateWeatherFactor(String location) {
        WeatherCondition weather = campusWeather.get(location);
        if (weather == null) {
            return 1.0; // No data means neutral factor
        }
        
        double factor = 1.0;
        
        if (weather.isRaining) {
            factor *= 1.3; // Rain slows movement
        }
        if (weather.visibility < 5.0) {
            factor *= 1.2; // Low visibility
        }
        if (weather.windSpeed > 10.0) {
            factor *= 1.1; // Strong winds
        }
        if (weather.temperature > 35.0 || weather.temperature < 15.0) {
            factor *= 1.15; // Extreme temperatures
        }
        if (weather.humidity > 80.0) {
            factor *= 1.05; // High humidity slightly affects comfort
        }
        
        return factor;
    }
    
    /**
     * Adjusts travel time and provides recommendations based on weather along a route.
     */
    public static WeatherAdjustedRoute adjustRouteForWeather(List<String> path, double distance, double baseTime) {
        WeatherAdjustedRoute route = new WeatherAdjustedRoute(path, distance, baseTime);
        
        double totalWeatherFactor = 1.0;
        int locationCount = 0;
        
        for (String location : path) {
            double factor = calculateWeatherFactor(location);
            totalWeatherFactor += factor;
            locationCount++;
            
            WeatherCondition weather = campusWeather.get(location);
            if (weather != null) {
                // Generate recommendations per location
                if (weather.isRaining) {
                    route.recommendations.add(" Bring umbrella for " + location);
                }
                if (weather.temperature > 30.0) {
                    route.recommendations.add(" Stay hydrated, high temperature at " + location);
                }
                if (weather.visibility < 5.0) {
                    route.recommendations.add(" Low visibility at " + location + ", be careful");
                }
            }
        }
        
        // Compute average weather effect along the path
        totalWeatherFactor /= locationCount;
        route.weatherAdjustedTime = baseTime * totalWeatherFactor;
        
        // Add overall weather impact message
        if (totalWeatherFactor > 1.2) {
            route.weatherImpact = "Weather conditions may slow your journey";
        } else if (totalWeatherFactor > 1.1) {
            route.weatherImpact = "Slight weather impact expected";
        } else {
            route.weatherImpact = "Good weather conditions";
        }
        
        return route;
    }
    
    /**
     * Generates a detailed weather report for every location along the path.
     */
    public static String generateWeatherReport(List<String> path) {
        StringBuilder report = new StringBuilder();
        report.append(" WEATHER ANALYSIS REPORT\n");
        
        for (String location : path) {
            WeatherCondition weather = campusWeather.get(location);
            if (weather != null) {
                report.append(" ").append(location).append(":\n");
                report.append("   Condition: ").append(weather.condition).append("\n");
                report.append("   Temperature: ").append(String.format("%.1f°C", weather.temperature)).append("\n");
                report.append("   Humidity: ").append(String.format("%.1f%%", weather.humidity)).append("\n");
                report.append("   Wind: ").append(String.format("%.1f km/h", weather.windSpeed)).append("\n");
                report.append("   Visibility: ").append(String.format("%.1f km", weather.visibility)).append("\n");
                report.append("   Rain: ").append(weather.isRaining ? "Yes" : "No").append("\n\n");
            }
        }
        
        return report.toString();
    }
    
    /**
     * Provides generalized weather-based recommendations for the route.
     * Ensures no duplicate tips are added.
     */
    public static List<String> getWeatherRecommendations(List<String> path) {
        List<String> recommendations = new ArrayList<>();
        Set<String> addedRecommendations = new HashSet<>();
        
        for (String location : path) {
            WeatherCondition weather = campusWeather.get(location);
            if (weather != null) {
                if (weather.isRaining && !addedRecommendations.contains("umbrella")) {
                    recommendations.add(" Bring an umbrella");
                    addedRecommendations.add("umbrella");
                }
                if (weather.temperature > 30.0 && !addedRecommendations.contains("hydration")) {
                    recommendations.add(" Stay hydrated");
                    addedRecommendations.add("hydration");
                }
                if (weather.visibility < 5.0 && !addedRecommendations.contains("visibility")) {
                    recommendations.add(" Be extra careful due to low visibility");
                    addedRecommendations.add("visibility");
                }
                if (weather.windSpeed > 10.0 && !addedRecommendations.contains("wind")) {
                    recommendations.add(" Strong winds expected");
                    addedRecommendations.add("wind");
                }
            }
        }
        
        return recommendations;
    }
    
    /**
     * Produces an overall weather summary by averaging values along the path.
     */
    public static String getOverallWeatherSummary(List<String> path) {
        int sunnyCount = 0, rainyCount = 0, cloudyCount = 0;
        double avgTemp = 0.0, avgHumidity = 0.0;
        int locationCount = 0;
        
        for (String location : path) {
            WeatherCondition weather = campusWeather.get(location);
            if (weather != null) {
                locationCount++;
                avgTemp += weather.temperature;
                avgHumidity += weather.humidity;
                
                if (weather.isRaining) {
                    rainyCount++;
                } else if (weather.condition.toLowerCase().contains("cloudy")) {
                    cloudyCount++;
                } else {
                    sunnyCount++;
                }
            }
        }
        
        if (locationCount > 0) {
            avgTemp /= locationCount;
            avgHumidity /= locationCount;
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append(" OVERALL WEATHER SUMMARY:\n");
        summary.append("Average Temperature: ").append(String.format("%.1f°C", avgTemp)).append("\n");
        summary.append("Average Humidity: ").append(String.format("%.1f%%", avgHumidity)).append("\n");
        summary.append("Sunny Locations: ").append(sunnyCount).append("\n");
        summary.append("Cloudy Locations: ").append(cloudyCount).append("\n");
        summary.append("Rainy Locations: ").append(rainyCount).append("\n");
        
        // Final interpretation
        if (rainyCount > sunnyCount) {
            summary.append("Overall: Rainy conditions expected\n");
        } else if (cloudyCount > sunnyCount) {
            summary.append("Overall: Cloudy conditions expected\n");
        } else {
            summary.append("Overall: Mostly sunny conditions\n");
        }
        
        return summary.toString();
    }
}

