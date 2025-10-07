import java.util.*;

/**
 * The SearchAlgorithms class provides various search and filtering functionalities
 * for campus navigation and information systems.
 * 
 * It includes:
 * - Searching landmarks by name, category, or nearby location.
 * - Grouping landmarks by category.
 * - Suggesting possible landmarks based on partial names.
 * - Calculating distances and finding nearest landmarks.
 * 
 * This class supports an imaginary campus guide system — "Campus Compass".
 */
public class SearchAlgorithms {
    
    /**
     * Inner class representing a landmark or point of interest on campus.
     * Each landmark has:
     * - A name (e.g., "Balme Library")
     * - A category (e.g., "Academic", "Food")
     * - Coordinates (x, y) for location-based operations
     * - A list of nearby locations for proximity searches
     */
    public static class Landmark {
        public String name;                 // Landmark name
        public String category;             // Type or category of landmark
        public double x, y;                 // Coordinates (for spatial searches)
        public List<String> nearbyLocations; // List of nearby locations

        /**
         * Constructor to initialize a landmark.
         *
         * @param name      The name of the landmark
         * @param category  The category/type (Academic, Food, etc.)
         * @param x         X-coordinate of the landmark
         * @param y         Y-coordinate of the landmark
         */
        public Landmark(String name, String category, double x, double y) {
            this.name = name;
            this.category = category;
            this.x = x;
            this.y = y;
            this.nearbyLocations = new ArrayList<>();
        }

        /**
         * Adds a nearby location to this landmark’s proximity list.
         *
         * @param location The name of the nearby location
         */
        public void addNearbyLocation(String location) {
            nearbyLocations.add(location);
        }

        /**
         * Returns a formatted string representation of the landmark.
         *
         * @return A readable string with name, category, and nearby locations
         */
        @Override
        public String toString() {
            return String.format("%s (%s) - Near: %s", name, category, String.join(", ", nearbyLocations));
        }
    }

    /**
     * Performs a linear search to find a landmark by name.
     * 
     * @param landmarks  List of all landmarks
     * @param target     Target name or keyword
     * @return The index of the matching landmark, or -1 if not found
     */
    public static int linearSearch(List<Landmark> landmarks, String target) {
        for (int i = 0; i < landmarks.size(); i++) {
            if (landmarks.get(i).name.equalsIgnoreCase(target) ||
                landmarks.get(i).name.toLowerCase().contains(target.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Performs a binary search for a landmark by name.
     * 
     * Note: The list must be sorted alphabetically by name before calling this.
     *
     * @param landmarks  Sorted list of landmarks
     * @param target     The landmark name or keyword to search for
     * @return Index of the found landmark, or -1 if not found
     */
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

    /**
     * Filters landmarks by category.
     *
     * @param landmarks  List of all landmarks
     * @param category   The desired category (e.g., "Food", "Academic")
     * @return A list of landmarks matching the category
     */
    public static List<Landmark> searchByCategory(List<Landmark> landmarks, String category) {
        List<Landmark> results = new ArrayList<>();
        for (Landmark landmark : landmarks) {
            if (landmark.category.equalsIgnoreCase(category)) {
                results.add(landmark);
            }
        }
        return results;
    }

    /**
     * Searches for landmarks that are near a specific location.
     *
     * @param landmarks  List of landmarks
     * @param location   Target nearby location name
     * @return List of landmarks near the specified location
     */
    public static List<Landmark> searchByNearbyLocation(List<Landmark> landmarks, String location) {
        List<Landmark> results = new ArrayList<>();
        for (Landmark landmark : landmarks) {
            for (String nearby : landmark.nearbyLocations) {
                if (nearby.toLowerCase().contains(location.toLowerCase())) {
                    results.add(landmark);
                    break; // Avoid duplicate entries
                }
            }
        }
        return results;
    }

    /**
     * Finds landmarks that are located along a given path.
     * 
     * @param path        List of location names in the route
     * @param landmarks   List of all landmarks
     * @return List of landmark names found in the path
     */
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

    /**
     * Groups landmarks by their category.
     *
     * @param landmarks List of landmarks
     * @return A map where the key is the category, and the value is a list of landmarks
     */
    public static Map<String, List<Landmark>> groupLandmarksByCategory(List<Landmark> landmarks) {
        Map<String, List<Landmark>> grouped = new HashMap<>();

        for (Landmark landmark : landmarks) {
            grouped.computeIfAbsent(landmark.category, k -> new ArrayList<>()).add(landmark);
        }

        return grouped;
    }

    /**
     * Initializes a sample dataset of campus landmarks.
     * Useful for testing or demonstration purposes.
     *
     * @return A list of predefined campus landmarks
     */
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

    /**
     * Suggests landmarks based on a partial input (name or category).
     * Used for auto-complete or user assistance.
     *
     * @param partialName  Partial string typed by user
     * @param landmarks    List of available landmarks
     * @return List of suggested landmark names
     */
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

    /**
     * Calculates the Euclidean distance from a point (x1, y1) to a given landmark.
     *
     * @param x1        X-coordinate of the reference point
     * @param y1        Y-coordinate of the reference point
     * @param landmark  The target landmark
     * @return The computed distance
     */
    public static double calculateDistanceToLandmark(double x1, double y1, Landmark landmark) {
        return Math.sqrt(Math.pow(x1 - landmark.x, 2) + Math.pow(y1 - landmark.y, 2));
    }

    /**
     * Finds the nearest landmarks to a given coordinate.
     *
     * @param x         X-coordinate of current location
     * @param y         Y-coordinate of current location
     * @param landmarks List of all landmarks
     * @param count     Number of nearest landmarks to return
     * @return A list of the nearest landmarks (sorted by distance)
     */
    public static List<Landmark> findNearestLandmarks(double x, double y, List<Landmark> landmarks, int count) {
        List<Landmark> sorted = new ArrayList<>(landmarks);
        sorted.sort((l1, l2) -> Double.compare(
            calculateDistanceToLandmark(x, y, l1),
            calculateDistanceToLandmark(x, y, l2)
        ));

        return sorted.subList(0, Math.min(count, sorted.size()));
    }
}

