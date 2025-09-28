n[0].length;
        
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
