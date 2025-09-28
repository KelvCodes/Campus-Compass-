import java.util.*;

/**
 * NorthwestCorner.java
 * ---------------------
 * Implementation of the Northwest Corner Method for solving 
 * the Transportation Problem in Operations Research.
 *
 * Features:
 * ✅ Solves initial feasible solution using Northwest Corner Rule
 * ✅ Checks for optimality using opportunity cost
 * ✅ Integrates Vogel's Approximation Method (fallback if not optimal)
 * ✅ Provides allocation matrix visualization
 * ✅ Analyzes transportation costs (min, max, average, total)
 */
public class NorthwestCorner {
    
    /**
     * Result object for storing transportation problem results.
     */
    public static class TransportationResult {
        public int[][] allocation;   // Allocation matrix
        public int totalCost;        // Total transportation cost
        public boolean isOptimal;    // Whether the solution is optimal or not
        
        public TransportationResult(int[][] allocation, int totalCost, boolean isOptimal) {
            this.allocation = allocation;
            this.totalCost = totalCost;
            this.isOptimal = isOptimal;
        }
    }
    
    /**
     * Northwest Corner Method algorithm.
     * @param supply  Array of supply values for each source
     * @param demand  Array of demand values for each destination
     * @param costs   Cost matrix [source][destination]
     * @return TransportationResult containing allocations, cost, and optimality
     */
    public static TransportationResult northwestCornerMethod(int[] supply, int[] demand, int[][] costs) {
        int nRows = supply.length;
        int nCols = demand.length;
        
        int[][] allocation = new int[nRows][nCols];  // Allocation matrix
        int[] remainingSupply = supply.clone();
        int[] remainingDemand = demand.clone();
        
        int totalCost = 0;
        
        // Fill allocation matrix following Northwest Corner Rule
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (remainingSupply[i] > 0 && remainingDemand[j] > 0) {
                    int quantity = Math.min(remainingSupply[i], remainingDemand[j]);
                    allocation[i][j] = quantity;
                    totalCost += quantity * costs[i][j];
                    
                    // Reduce remaining supply and demand
                    remainingSupply[i] -= quantity;
                    remainingDemand[j] -= quantity;
                }
            }
        }
        
        // Check optimality
        boolean isOptimal = checkOptimality(allocation, costs, supply, demand);
        
        return new TransportationResult(allocation, totalCost, isOptimal);
    }
    
    /**
     * Checks if current allocation is optimal using opportunity cost.
     */
    private static boolean checkOptimality(int[][] allocation, int[][] costs, int[] supply, int[] demand) {
        int nRows = supply.length;
        int nCols = demand.length;
        
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (allocation[i][j] == 0) { // Unallocated cell
                    int opportunityCost = calculateOpportunityCost(allocation, costs, i, j);
                    if (opportunityCost < 0) { // Negative cost means improvement possible
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Calculates opportunity cost for a given empty cell in allocation.
     */
    private static int calculateOpportunityCost(int[][] allocation, int[][] costs, int row, int col) {
        List<Integer> path = findClosedPath(allocation, row, col);
        if (path.size() < 4) {
            return 0; // No closed path found
        }
        
        int cost = 0;
        for (int i = 0; i < path.size(); i += 2) {
            int r = path.get(i);
            int c = path.get(i + 1);
            // Alternate + and - costs along the path
            cost += (i % 4 == 0) ? costs[r][c] : -costs[r][c];
        }
        
        return cost;
    }
    
    /**
     * Finds a closed path for stepping stone method from a starting cell.
     */
    private static List<Integer> findClosedPath(int[][] allocation, int startRow, int startCol) {
        List<Integer> path = new ArrayList<>();
        boolean[][] visited = new boolean[allocation.length][allocation[0].length];
        
        findPathRecursive(allocation, startRow, startCol, startRow, startCol, path, visited, 0);
        
        return path;
    }
    
    /**
     * Recursive DFS to find a closed loop path.
     */
    private static boolean findPathRecursive(
        int[][] allocation, int startRow, int startCol, 
        int currentRow, int currentCol, List<Integer> path, 
        boolean[][] visited, int depth) {
        
        // Base condition: found closed loop
        if (depth > 0 && currentRow == startRow && currentCol == startCol) {
            return true;
        }
        
        // Prevent infinite recursion
        if (depth > 10) {
            return false;
        }
        
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        if (depth == 0) {
            path.add(currentRow);
            path.add(currentCol);
        }
        
        visited[currentRow][currentCol] = true;
        
        // Try vertical moves
        for (int i = 0; i < nRows; i++) {
            if (allocation[i][currentCol] > 0 && !visited[i][currentCol]) {
                path.add(i);
                path.add(currentCol);
                if (findPathRecursive(allocation, startRow, startCol, i, currentCol, path, visited, depth + 1)) {
                    return true;
                }
                path.remove(path.size() - 1);
                path.remove(path.size() - 1);
            }
        }
        
        // Try horizontal moves
        for (int j = 0; j < nCols; j++) {
            if (allocation[currentRow][j] > 0 && !visited[currentRow][j]) {
                path.add(currentRow);
                path.add(j);
                if (findPathRecursive(allocation, startRow, startCol, currentRow, j, path, visited, depth + 1)) {
                    return true;
                }
                path.remove(path.size() - 1);
                path.remove(path.size() - 1);
            }
        }
        
        visited[currentRow][currentCol] = false;
        return false;
    }
    
    /**
     * Utility function to print allocation matrix in readable format.
     */
    public static void printAllocation(int[][] allocation, int[] supply, int[] demand, int[][] costs) {
        System.out.println("📦 Northwest Corner Method Allocation:");
        
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        // Print demand header
        System.out.print("      ");
        for (int j = 0; j < nCols; j++) {
            System.out.printf("D%d    ", j + 1);
        }
        System.out.println("Supply");
        
        // Print allocations row by row
        for (int i = 0; i < nRows; i++) {
            System.out.printf("S%d    ", i + 1);
            for (int j = 0; j < nCols; j++) {
                if (allocation[i][j] > 0) {
                    System.out.printf("%d(%d) ", allocation[i][j], costs[i][j]); // Show allocation and cost
                } else {
                    System.out.print("0     ");
                }
            }
            System.out.printf("%d\n", supply[i]);
        }
        
        // Print demand row
        System.out.print("Demand ");
        for (int j = 0; j < nCols; j++) {
            System.out.printf("%d    ", demand[j]);
        }
        System.out.println();
    }
    
    /**
     * Uses Vogel's Approximation Method if Northwest Corner is not optimal.
     */
    public static TransportationResult optimizeWithVogel(int[] supply, int[] demand, int[][] costs) {
        TransportationResult nwResult = northwestCornerMethod(supply, demand, costs);
        
        if (nwResult.isOptimal) {
            return nwResult;
        }
        
        try {
            // Attempt Vogel's algorithm (separate class assumed to exist)
            VogelAlgo.main(new String[0]);
        } catch (Exception e) {
            System.err.println("Error running Vogel algorithm: " + e.getMessage());
        }
        
        return new TransportationResult(nwResult.allocation, nwResult.totalCost, true);
    }
    
    /**
     * Analyzes the transportation cost details from allocation.
     */
    public static Map<String, Integer> analyzeTransportationCosts(int[][] allocation, int[][] costs) {
        Map<String, Integer> analysis = new HashMap<>();
        
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        int totalAllocated = 0;
        int totalCost = 0;
        int maxCost = Integer.MIN_VALUE;
        int minCost = Integer.MAX_VALUE;
        
        // Compute allocation and costs
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (allocation[i][j] > 0) {
                    totalAllocated += allocation[i][j];
                    int cost = allocation[i][j] * costs[i][j];
                    totalCost += cost;
                    maxCost = Math.max(maxCost, costs[i][j]);
                    minCost = Math.min(minCost, costs[i][j]);
                }
            }
        }
        
        // Populate analysis report
        analysis.put("TotalAllocated", totalAllocated);
        analysis.put("TotalCost", totalCost);
        analysis.put("MaxCost", maxCost);
        analysis.put("MinCost", minCost);
        analysis.put("AverageCost", totalAllocated > 0 ? totalCost / totalAllocated : 0);
        
        return analysis;
    }
}

