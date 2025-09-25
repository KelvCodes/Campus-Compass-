
        for (String location : path) {
            AccessibilityInfo info = locationAccessibility.get(location);
            i
                
                addAccessibilityFeatures(route, info, location);
            }
        }
        
        if (locationCount > 0) {
            route.accessibilityScore = totalScore / locationCount;
        }
        
        route.time = adjustTimeForAccessibility(route.time, accessibilityType, route.accessibilityScore);
        
        return route;
    }
    
    private static double calculateAccessibilityScore(AccessibilityInfo info, String accessibilityType) {
        double score = 1.0;
        
        switch (accessibilityType.toLowerCase()) {
            case "wheelchair":
                if (info.hasRamp) score += 0.3;
                if (info.hasElevator) score += 0.3;
                if (info.hasWidePath) score += 0.2;
                if (info.hasSmoothSurface) score += 0.2;
                if (info.stepCount > 0) score -= 0.5;
                break;
                
            case "elderly":
                if (info.hasRamp) score += 0.2;
                if (info.hasElevator) score += 0.3;
                if (info.hasRestArea) score += 0.3;
                if (info.hasShade) score += 0.2;
                if (info.stepCount > 10) score -= 0.3;
                break;
                
            case "visually_impaired":
                if (info.hasSmoothSurface) score += 0.4;
                if (info.hasWidePath) score += 0.3;
                if (info.stepCount == 0) score += 0.3;
                break;
                
            case "mobility_impaired":
                if (info.hasRamp) score += 0.3;
                if (info.hasElevator) score += 0.3;
                if (info.hasRestArea) score += 0.2;
                if (info.hasSmoothSurface) score += 0.2;
                break;
        }
        
        return Math.max(0.1, score);
    }
    
    private static void addAccessibilityFeatures(AccessibilityRoute route, AccessibilityInfo info, String location) {
        if (info.hasRamp) {
            route.features.add("♿ Ramp available at " + location);
        }
        if (info.hasElevator) {
            route.features.add("🛗 Elevator available at " + location);
        }
        if (info.hasRestArea) {
            route.features.add("🪑 Rest area available at " + location);
        }
        if (info.hasShade) {
            route.features.add("🌳 Shaded area at " + location);
        }
        if (info.stepCount > 0) {
            route.features.add("⚠️ " + info.stepCount + " steps at " + location);
        }
        if (info.pathWidth < 2.0) {
            route.features.add("⚠️ Narrow path (" + info.pathWidth + "m) at " + location);
        }
    }
    
    private static double adjustTimeForAccessibility(double baseTime, String accessibilityType, double accessibilityScore) {
        double adjustment = 1.0;
        
        switch (accessibilityType.toLowerCase()) {
            case "wheelchair":
                adjustment = 1.5;
                break;
            case "elderly":
                adjustment = 1.3;
                break;
            case "visually_impaired":
                adjustment = 1.4;
                break;
            case "mobility_impaired":
                adjustment = 1.6;
                break;
        }
        
        if (accessibilityScore < 0.5) {
            adjustment *= 1.5;
        } else if (accessibilityScore < 0.8) {
            adjustment *= 1.2;
        }
        
        return baseTime * adjustment;
    }
    
    public static String generateAccessibilityReport(List<String> path, String accessibilityType) {
        StringBuilder report = new StringBuilder();
        report.append("♿ ACCESSIBILITY ANALYSIS REPORT\n");
        report.append("===============================\n");
        report.append("Accessibility Type: ").append(accessibilityType.replace("_", " ").toUpperCase()).append("\n\n");
        
        int totalSteps = 0;
        int accessibleLocations = 0;
        int totalLocations = 0;
        
        for (String location : path) {
            AccessibilityInfo info = locationAccessibility.get(location);
            if (info != null) {
                totalLocations++;
                totalSteps += info.stepCount;
                
                report.append("📍 ").append(location).append(":\n");
                report.append("   Ramp: ").append(info.hasRamp ? "✅" : "❌").append("\n");
                report.append("   Elevator: ").append(info.hasElevator ? "✅" : "❌").append("\n");
                report.append("   Wide Path: ").append(info.hasWidePath ? "✅" : "❌").append("\n");
                report.append("   Smooth Surface: ").append(info.hasSmoothSurface ? "✅" : "❌").append("\n");
                report.append("   Rest Area: ").append(info.hasRestArea ? "✅" : "❌").append("\n");
                report.append("   Shade: ").append(info.hasShade ? "✅" : "❌").append("\n");
                report.append("   Steps: ").append(info.stepCount).append("\n");
                report.append("   Path Width: ").append(String.format("%.1fm", info.pathWidth)).append("\n\n");
                
                double score = calculateAccessibilityScore(info, accessibilityType);
                if (score >= 0.8) {
                    accessibleLocations++;
                }
            }
        }
        
        report.append("📊 SUMMARY:\n");
        report.append("===========\n");
        report.append("Total Steps: ").append(totalSteps).append("\n");
        report.append("Accessible Locations: ").append(accessibleLocations).append("/").append(totalLocations).append("\n");
        report.append("Accessibility Rate: ").append(String.format("%.1f%%", (double)accessibleLocations/totalLocations*100)).append("\n");
        
        if (totalSteps > 20) {
            report.append("⚠️ High step count - consider alternative route\n");
        }
        if (accessibleLocations < totalLocations * 0.7) {
            report.append("⚠️ Limited accessibility - may need assistance\n");
        }
        
        return report.toString();
    }
    
    public static List<String> getAccessibilityRecommendations(List<String> path, String accessibilityType) {
        List<String> recommendations = new ArrayList<>();
        
        int totalSteps = 0;
        boolean hasInaccessibleAreas = false;
        
        for (String location : path) {
            AccessibilityInfo info = locationAccessibility.get(location);
            if (info != null) {
                totalSteps += info.stepCount;
                
                if (accessibilityType.equalsIgnoreCase("wheelchair") && info.stepCount > 0) {
                    hasInaccessibleAreas = true;
                }
            }
        }
        
        if (totalSteps > 15) {
            recommendations.add("♿ Consider requesting assistance for step-heavy areas");
        }
        if (hasInaccessibleAreas) {
            recommendations.add("⚠️ Some areas may not be fully wheelchair accessible");
        }
        if (accessibilityType.equalsIgnoreCase("elderly")) {
            recommendations.add("👴 Take breaks at rest areas along the route");
        }
        if (accessibilityType.equalsIgnoreCase("visually_impaired")) {
            recommendations.add("👁️ Consider using a guide or assistance device");
        }
        
        return recommendations;
    }
} 
