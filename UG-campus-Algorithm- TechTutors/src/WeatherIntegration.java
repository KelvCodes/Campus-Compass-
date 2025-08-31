import java.util.*;

public class WeatherIntegration {
    
    public static class WeatherCondition {
        public String condition;
        public double temperature;
        public double humidity;
        public double windSpeed;
        public boolean isRaining;
        public double visibility;
        
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
    
    public static class WeatherAdjustedRoute {
        public List<String> path;
        public double distance;
        public double baseTime;
        public double weatherAdjustedTime;
        public String weatherImpact;
        public List<String> recommendations;
        
        public WeatherAdjustedRoute(List<String> path, double distance, double baseTime) {
            this.path = path;
            this.distance = distance;
            this.baseTime = baseTime;
            this.weatherAdjustedTime = baseTime;
            this.recommendations = new ArrayList<>();
        }
    }
    
    private static Map<String, WeatherCondition> campusWeather = new HashMap<>();
    
    static {
        initializeWeatherData();
    }
    
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
    
    public static double calculateWeatherFactor(String location) {
        WeatherCondition weather = campusWeather.get(location);
        if (weather == null) {
            return 1.0;
        }
        
        double factor = 1.0;
        
        if (weather.isRaining) {
            factor *= 1.3;
        }
        
        if (weather.visibility < 5.0) {
            factor *= 1.2;
        }
        
        if (weather.windSpeed > 10.0) {
            factor *= 1.1;
        }
        
        if (weather.temperature > 35.0 || weather.temperature < 15.0) {
            factor *= 1.15;
        }
        
        if (weather.humidity > 80.0) {
            factor *= 1.05;
        }
        
        return factor;
    }
    
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
        
        totalWeatherFactor /= locationCount;
        route.weatherAdjustedTime = baseTime * totalWeatherFactor;
        
        if (totalWeatherFactor > 1.2) {
            route.weatherImpact = "Weather conditions may slow your journey";
        } else if (totalWeatherFactor > 1.1) {
            route.weatherImpact = "Slight weather impact expected";
        } else {
            route.weatherImpact = "Good weather conditions";
        }
        
        return route;
    }
    
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