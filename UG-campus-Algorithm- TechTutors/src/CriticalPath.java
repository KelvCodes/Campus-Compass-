import java.util.*;

/**
 * CriticalPath.java
 * -----------------
 * Implements a basic Critical Path Method (CPM) for task scheduling.
 * Computes the longest path (critical path) through a set of tasks with dependencies.
 * 
 * Each task has a cost (duration) and may depend on other tasks.
 * The program calculates the critical cost (longest cumulative duration) for each task
 * and prints the ordered critical path.
 */
public class CriticalPath {

    public static void main(String[] args) {

        // Create a set to hold all tasks
        HashSet<Task> allTasks = new HashSet<Task>();

        // Define tasks and their dependencies
        Task end = new Task("End", 0);                       // End task, no cost
        Task F = new Task("F", 2, end);                     // Task F depends on End
        Task A = new Task("A", 3, end);                     // Task A depends on End
        Task X = new Task("X", 4, F, A);                    // Task X depends on F and A
        Task Q = new Task("Q", 2, A, X);                    // Task Q depends on A and X
        Task start = new Task("Start", 0, Q);              // Start task depends on Q

        // Add tasks to the set
        allTasks.add(end);
        allTasks.add(F);
        allTasks.add(A);
        allTasks.add(X);
        allTasks.add(Q);
        allTasks.add(start);

        // Calculate and print the critical path
        System.out.println("Critical Path: " + Arrays.toString(criticalPath(allTasks)));
    }

    /**
     * Represents a single task in the project with optional dependencies.
     */
    public static class Task {
        public int cost;                  // Duration or cost of the task
        public int criticalCost;          // Computed critical cost (longest cumulative duration to this task)
        public String name;               // Task name
        public HashSet<Task> dependencies = new HashSet<Task>(); // Tasks that must be completed before this one

        /**
         * Constructor for Task.
         * @param name Name of the task
         * @param cost Duration or cost of the task
         * @param dependencies Optional tasks that this task depends on
         */
        public Task(String name, int cost, Task... dependencies) {
            this.name = name;
            this.cost = cost;
            for (Task t : dependencies) {
                this.dependencies.add(t);
            }
        }

        @Override
        public String toString() {
            return name + ": " + criticalCost;
        }

        /**
         * Checks recursively if this task depends on another task.
         * @param t Task to check
         * @return true if this task depends on t, directly or indirectly
         */
        public boolean isDependent(Task t) {
            if (dependencies.contains(t)) {
                return true;
            }
            for (Task dep : dependencies) {
                if (dep.isDependent(t)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Computes the critical path for a set of tasks.
     * @param tasks Set of tasks with dependencies
     * @return Array of tasks sorted by critical cost in descending order
     */
    public static Task[] criticalPath(Set<Task> tasks) {
        HashSet<Task> completed = new HashSet<Task>();   // Tasks whose critical cost has been calculated
        HashSet<Task> remaining = new HashSet<Task>(tasks); // Tasks pending computation

        // Process tasks until all are completed
        while (!remaining.isEmpty()) {
            boolean progress = false;

            // Iterate over remaining tasks
            for (Iterator<Task> it = remaining.iterator(); it.hasNext(); ) {
                Task task = it.next();

                // Check if all dependencies are completed
                if (completed.containsAll(task.dependencies)) {
                    // Determine the maximum critical cost among dependencies
                    int critical = 0;
                    for (Task t : task.dependencies) {
                        if (t.criticalCost > critical) {
                            critical = t.criticalCost;
                        }
                    }
                    // Set this task's critical cost
                    task.criticalCost = critical + task.cost;

                    // Move task to completed set
                    completed.add(task);
                    it.remove();
                    progress = true;
                }
            }

            // If no tasks were processed, there is a cyclic dependency
            if (!progress) throw new RuntimeException("Cyclic dependency, algorithm stopped!");
        }

        // Convert completed tasks to an array
        Task[] ret = completed.toArray(new Task[0]);

        // Sort tasks by critical cost descending, and maintain dependency order
        Arrays.sort(ret, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                int diff = o2.criticalCost - o1.criticalCost; // Descending order
                if (diff != 0) return diff;

                // If costs are equal, ensure dependencies come before dependents
                if (o1.isDependent(o2)) return -1;
                if (o2.isDependent(o1)) return 1;
                return 0;
            }
        });

        return ret;
    }
}

