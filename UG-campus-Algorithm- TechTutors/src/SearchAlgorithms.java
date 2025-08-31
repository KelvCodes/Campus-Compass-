import java.util.*;

public class SearchAlgorithms {
    
    public static class Landmark {
        public String name;
        public String category;
        public double x, y;
        public List<String> nearbyLocations;
        
        public Landmark(String name, String category, double x, double y) {
            this.name = name;
            this.category = category;
            this.x = x;
            this.y = y;
            this.nearbyLocations = new ArrayList<>();
        }
        
        public void addNearbyLocation(String location) {
            nearbyLocations.add(location);
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - Near: %s", name, category, String.join(", ", nearbyLocations));
        }
    }
    
    public static int linearSearch(List<Landmark> landmarks, String target) {
        for (int i = 0; i < landmarks.size(); i++) {
            if (landmarks.get(i).name.equalsIgnoreCase(target) || 
                landmarks.get(i).name.toLowerCase().contains(target.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    
    public static int binarySearch(List<Landmark> landmarks, String target) {
        int left = 0;
        int right = landmarks.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midName = landmarks.get(mid).name.toLowerCase();
            String targetLower = target.toLowerCase();
            
            int comparison = midName.compareTo(targetLower);
            
            if (comparison == 0 || midName.contains(targetLower)) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    public static List<Landmark> searchByCategory(List<Landmark> landmarks, String category) {
        List<Landmark> results = new ArrayList<>();
        for (Landmark landmark : landmarks) {
            if (landmark.category.equalsIgnoreCase(category)) {
                results.add(landmark);
            }
        }
        return results;
    }
    
    public static List<Landmark> searchByNearbyLocation(List<Landmark> landmarks, String location) {
        List<Landmark> results = new ArrayList<>();
        for (Landmark landmark : landmarks) {
            for (String nearby : landmark.nearbyLocations) {
                if (nearby.toLowerCase().contains(location.toLowerCase())) {
                    results.add(landmark);
                    break;
                }
            }
        }
        return results;
    }
    
    public static List<String> findLandmarksInPath(List<String> path, List<Landmark> landmarks) {
        List<String> foundLandmarks = new ArrayList<>();
        for (String location : path) {
            for (Landmark landmark : landmarks) {
                if (landmark.nearbyLocations.contains(location)) {
                    foundLandmarks.add(landmark.name);
                }
            }
        }
        return foundLandmarks;
    }
    
    public static Map<String, List<Landmark>> groupLandmarksByCategory(List<Landmark> landmarks) {
        Map<String, List<Landmark>> grouped = new HashMap<>();
        
        for (Landmark landmark : landmarks) {
            grouped.computeIfAbsent(landmark.category, k -> new ArrayList<>()).add(landmark);
        }
        
        return grouped;
    }
    
    public static List<Landmark> initializeCampusLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();
        
        Landmark bank = new Landmark("Banking Square", "Financial", 0, 0);
        bank.addNearbyLocation("Banking Square");
        bank.addNearbyLocation("Night Market");
        landmarks.add(bank);
        
        Landmark library = new Landmark("Balme Library", "Academic", 0, 0);
        library.addNearbyLocation("Balme Library");
        library.addNearbyLocation("UGCS");
        library.addNearbyLocation("Akuafo Hall");
        landmarks.add(library);
        
        Landmark canteen = new Landmark("Bush Canteen", "Food", 0, 0);
        canteen.addNearbyLocation("Bush Canteen");
        canteen.addNearbyLocation("Fire Station");
        landmarks.add(canteen);
        
        Landmark park = new Landmark("Sarbah Park", "Recreation", 0, 0);
        park.addNearbyLocation("Sarbah Park");
        park.addNearbyLocation("Legon Hall");
        park.addNearbyLocation("Akuafo Hall");
        landmarks.add(park);
        
        Landmark market = new Landmark("Night Market", "Shopping", 0, 0);
        market.addNearbyLocation("Night Market");
        market.addNearbyLocation("Banking Square");
        market.addNearbyLocation("Diaspora Halls");
        landmarks.add(market);
        
        Landmark fireStation = new Landmark("Fire Station", "Emergency", 0, 0);
        fireStation.addNearbyLocation("Fire Station");
        fireStation.addNearbyLocation("Bush Canteen");
        fireStation.addNearbyLocation("Banking Square");
        landmarks.add(fireStation);
        
        return landmarks;
    }
    
    public static List<String> suggestLandmarks(String partialName, List<Landmark> landmarks) {
        List<String> suggestions = new ArrayList<>();
        String partial = partialName.toLowerCase();
        
        for (Landmark landmark : landmarks) {
            if (landmark.name.toLowerCase().contains(partial) || 
                landmark.category.toLowerCase().contains(partial)) {
                suggestions.add(landmark.name);
            }
        }
        
        return suggestions;
    }
    
    public static double calculateDistanceToLandmark(double x1, double y1, Landmark landmark) {
        return Math.sqrt(Math.pow(x1 - landmark.x, 2) + Math.pow(y1 - landmark.y, 2));
    }
    
    public static List<Landmark> findNearestLandmarks(double x, double y, List<Landmark> landmarks, int count) {
        List<Landmark> sorted = new ArrayList<>(landmarks);
        sorted.sort((l1, l2) -> Double.compare(
            calculateDistanceToLandmark(x, y, l1),
            calculateDistanceToLandmark(x, y, l2)
        ));
        
        return sorted.subList(0, Math.min(count, sorted.size()));
    }
} 