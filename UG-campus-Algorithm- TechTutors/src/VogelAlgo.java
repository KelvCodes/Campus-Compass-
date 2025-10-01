import static java.util.Arrays.stream;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Vogel's Approximation Method (VAM) for solving Transportation Problems.
 * 
 * The goal is to minimize transportation cost by optimally allocating supply 
 * from sources (rows) to demands (columns) while respecting constraints.
 */
public class VogelAlgo {

    // Demand for each destination (columns)
    final static int[] demand = {30, 20, 70, 30, 60};

    // Supply available at each source (rows)
    final static int[] supply = {50, 60, 50, 50};

    // Cost matrix: cost of transporting 1 unit from supply[i] to demand[j]
    final static int[][] costs = {
        {16, 16, 13, 22, 17},
        {14, 14, 13, 19, 15},
        {19, 19, 20, 23, 50},
        {50, 12, 50, 15, 11}
    };

    // Number of rows and columns
    final static int nRows = supply.length;
    final static int nCols = demand.length;

    // Flags to track if a row/column is completely allocated
    static boolean[] rowDone = new boolean[nRows];
    static boolean[] colDone = new boolean[nCols];

    // Result allocation matrix (how much is transported from each source to each destination)
    static int[][] result = new int[nRows][nCols];

    // Thread pool for parallel computation of penalties
    static ExecutorService es = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        int supplyLeft = stream(supply).sum(); // total supply left to allocate
        int totalCost = 0; // final transportation cost

        // Keep allocating until all supply is used
        while (supplyLeft > 0) {
            // Find next best cell to allocate based on Vogel’s penalty
            int[] cell = nextCell();
            int r = cell[0]; // row index (supply source)
            int c = cell[1]; // col index (demand destination)

            // Allocate as much as possible (min of supply and demand)
            int quantity = Math.min(demand[c], supply[r]);
            demand[c] -= quantity;
            if (demand[c] == 0) colDone[c] = true; // mark column as done if demand fulfilled

            supply[r] -= quantity;
            if (supply[r] == 0) rowDone[r] = true; // mark row as done if supply exhausted

            result[r][c] = quantity; // record allocation
            supplyLeft -= quantity;

            // Add cost contribution
            totalCost += quantity * costs[r][c];
        }

        // Print final allocation table
        stream(result).forEach(a -> System.out.println(Arrays.toString(a)));

        // Print minimized transportation cost
        System.out.println("Total cost: " + totalCost);

        // Shut down executor
        es.shutdown();
    }

    /**
     * Finds the next cell (row, col) to allocate supply to demand 
     * based on Vogel’s Approximation penalties.
     */
    static int[] nextCell() throws Exception {
        // Run penalty calculations in parallel for rows and columns
        Future<int[]> f1 = es.submit(() -> maxPenalty(nRows, nCols, true));
        Future<int[]> f2 = es.submit(() -> maxPenalty(nCols, nRows, false));

        int[] res1 = f1.get(); // row-based penalty
        int[] res2 = f2.get(); // col-based penalty

        // Choose the cell with higher penalty (greater cost difference)
        if (res1[3] == res2[3]) 
            return res1[2] < res2[2] ? res1 : res2; // tie-breaker using min cost

        return (res1[3] > res2[3]) ? res2 : res1;
    }

    /**
     * Computes cost differences (penalties) for a row or column.
     * Penalty = difference between two lowest costs in that row/column.
     * 
     * @param j    Row/col index
     * @param len  Number of elements (cols if row, rows if col)
     * @param isRow Whether we are processing a row (true) or column (false)
     * @return {penalty, minCost, minPos}
     */
    static int[] diff(int j, int len, boolean isRow) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        int minP = -1; // position of minimum cost

        for (int i = 0; i < len; i++) {
            if (isRow ? colDone[i] : rowDone[i]) continue; // skip completed rows/cols
            int c = isRow ? costs[j][i] : costs[i][j];
            if (c < min1) {
                min2 = min1;
                min1 = c;
                minP = i;
            } else if (c < min2) {
                min2 = c;
            }
        }

        // penalty = difference between smallest and second smallest cost
        return new int[]{min2 - min1, min1, minP};
    }

    /**
     * Finds the maximum penalty for a row or column using Vogel’s method.
     * 
     * @param len1 Number of rows/columns
     * @param len2 Opposite dimension
     * @param isRow True if checking rows, false if checking columns
     * @return {rowIndex, colIndex, minCost, penalty}
     */
    static int[] maxPenalty(int len1, int len2, boolean isRow) {
        int md = Integer.MIN_VALUE; // max penalty difference
        int pc = -1, pm = -1, mc = -1;

        for (int i = 0; i < len1; i++) {
            if (isRow ? rowDone[i] : colDone[i]) continue; // skip if row/col done
            int[] res = diff(i, len2, isRow);
            if (res[0] > md) {
                md = res[0];  // max penalty (diff between two min costs)
                pm = i;       // position (row/col)
                mc = res[1];  // min cost in this row/col
                pc = res[2];  // column/row index of min cost
            }
        }

        // Return [row, col, minCost, penalty]
        return isRow ? new int[]{pm, pc, mc, md} : new int[]{pc, pm, mc, md};
    }
}

