
    
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
