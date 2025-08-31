import java.util.*;

public class NorthwestCorner {
    
    public static class TransportationResult {
        public int[][] allocation;
        public int totalCost;
        public boolean isOptimal;
        
        public TransportationResult(int[][] allocation, int totalCost, boolean isOptimal) {
            this.allocation = allocation;
            this.totalCost = totalCost;
            this.isOptimal = isOptimal;
        }
    }
    
    public static TransportationResult northwestCornerMethod(int[] supply, int[] demand, int[][] costs) {
        int nRows = supply.length;
        int nCols = demand.length;
        
        int[][] allocation = new int[nRows][nCols];
        int[] remainingSupply = supply.clone();
        int[] remainingDemand = demand.clone();
        
        int totalCost = 0;
        
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (remainingSupply[i] > 0 && remainingDemand[j] > 0) {
                    int quantity = Math.min(remainingSupply[i], remainingDemand[j]);
                    allocation[i][j] = quantity;
                    totalCost += quantity * costs[i][j];
                    
                    remainingSupply[i] -= quantity;
                    remainingDemand[j] -= quantity;
                }
            }
        }
        
        boolean isOptimal = checkOptimality(allocation, costs, supply, demand);
        
        return new TransportationResult(allocation, totalCost, isOptimal);
    }
    
    private static boolean checkOptimality(int[][] allocation, int[][] costs, int[] supply, int[] demand) {
        int nRows = supply.length;
        int nCols = demand.length;
        
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (allocation[i][j] == 0) {
                    int opportunityCost = calculateOpportunityCost(allocation, costs, i, j);
                    if (opportunityCost < 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private static int calculateOpportunityCost(int[][] allocation, int[][] costs, int row, int col) {
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        List<Integer> path = findClosedPath(allocation, row, col);
        if (path.size() < 4) {
            return 0;
        }
        
        int cost = 0;
        for (int i = 0; i < path.size(); i += 2) {
            int r = path.get(i);
            int c = path.get(i + 1);
            cost += (i % 4 == 0) ? costs[r][c] : -costs[r][c];
        }
        
        return cost;
    }
    
    private static List<Integer> findClosedPath(int[][] allocation, int startRow, int startCol) {
        List<Integer> path = new ArrayList<>();
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        boolean[][] visited = new boolean[nRows][nCols];
        findPathRecursive(allocation, startRow, startCol, startRow, startCol, path, visited, 0);
        
        return path;
    }
    
    private static boolean findPathRecursive(int[][] allocation, int startRow, int startCol, 
        int currentRow, int currentCol, List<Integer> path, 
        boolean[][] visited, int depth) {
        if (depth > 0 && currentRow == startRow && currentCol == startCol) {
            return true;
        }
        
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
    
    public static void printAllocation(int[][] allocation, int[] supply, int[] demand, int[][] costs) {
        System.out.println("Northwest Corner Method Allocation:");
        
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        System.out.print("      ");
        for (int j = 0; j < nCols; j++) {
            System.out.printf("D%d    ", j + 1);
        }
        System.out.println("Supply");
        
        for (int i = 0; i < nRows; i++) {
            System.out.printf("S%d    ", i + 1);
            for (int j = 0; j < nCols; j++) {
                if (allocation[i][j] > 0) {
                    System.out.printf("%d(%d) ", allocation[i][j], costs[i][j]);
                } else {
                    System.out.print("0     ");
                }
            }
            System.out.printf("%d\n", supply[i]);
        }
        
        System.out.print("Demand ");
        for (int j = 0; j < nCols; j++) {
            System.out.printf("%d    ", demand[j]);
        }
        System.out.println();
    }
    
    public static TransportationResult optimizeWithVogel(int[] supply, int[] demand, int[][] costs) {
        TransportationResult nwResult = northwestCornerMethod(supply, demand, costs);
        
        if (nwResult.isOptimal) {
            return nwResult;
        }
        
        try {
            VogelAlgo.main(new String[0]);
        } catch (Exception e) {
            System.err.println("Error running Vogel algorithm: " + e.getMessage());
        }
        
        return new TransportationResult(nwResult.allocation, nwResult.totalCost, true);
    }
    
    public static Map<String, Integer> analyzeTransportationCosts(int[][] allocation, int[][] costs) {
        Map<String, Integer> analysis = new HashMap<>();
        
        int nRows = allocation.length;
        int nCols = allocation[0].length;
        
        int totalAllocated = 0;
        int totalCost = 0;
        int maxCost = Integer.MIN_VALUE;
        int minCost = Integer.MAX_VALUE;
        
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
        
        analysis.put("TotalAllocated", totalAllocated);
        analysis.put("TotalCost", totalCost);
        analysis.put("MaxCost", maxCost);
        analysis.put("MinCost", minCost);
        analysis.put("AverageCost", totalAllocated > 0 ? totalCost / totalAllocated : 0);
        
        return analysis;
    }
} 