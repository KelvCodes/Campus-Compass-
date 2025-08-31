import java.util.*;

public class CriticalPath {

    public static void main(String[] args) {

        HashSet<Task> allTasks = new HashSet<Task>();
        Task end = new Task("End", 0);
        Task F = new Task("F", 2, end);
        Task A = new Task("A", 3, end);
        Task X = new Task("X", 4, F, A);
        Task Q = new Task("Q", 2, A, X);
        Task start = new Task("Start", 0, Q);
        allTasks.add(end);
        allTasks.add(F);
        allTasks.add(A);
        allTasks.add(X);
        allTasks.add(Q);
        allTasks.add(start);
        System.out.println("Critical Path: "+Arrays.toString(criticalPath(allTasks)));
    }

    public static class Task{
        public int cost;
        public int criticalCost;
        public String name;
        public HashSet<Task> dependencies = new HashSet<Task>();
        public Task(String name, int cost, Task... dependencies) {
            this.name = name;
            this.cost = cost;
            for(Task t : dependencies){
                this.dependencies.add(t);
            }
        }
        @Override
        public String toString() {
            return name+": "+criticalCost;
        }
        public boolean isDependent(Task t){
            if(dependencies.contains(t)){
                return true;
            }
            for(Task dep : dependencies){
                if(dep.isDependent(t)){
                    return true;
                }
            }
            return false;
        }
    }

    public static Task[] criticalPath(Set<Task> tasks){
        HashSet<Task> completed = new HashSet<Task>();
        HashSet<Task> remaining = new HashSet<Task>(tasks);

        while(!remaining.isEmpty()){
            boolean progress = false;

            for(Iterator<Task> it = remaining.iterator();it.hasNext();){
                Task task = it.next();
                if(completed.containsAll(task.dependencies)){
                    int critical = 0;
                    for(Task t : task.dependencies){
                        if(t.criticalCost > critical){
                            critical = t.criticalCost;
                        }
                    }
                    task.criticalCost = critical+task.cost;
                    completed.add(task);
                    it.remove();
                    progress = true;
                }
            }
            if(!progress) throw new RuntimeException("Cyclic dependency, algorithm stopped!");
        }

        Task[] ret = completed.toArray(new Task[0]);
        Arrays.sort(ret, new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                int i= o2.criticalCost-o1.criticalCost;
                if(i != 0)return i;

                if(o1.isDependent(o2))return -1;
                if(o2.isDependent(o1))return 1;
                return 0;
            }
        });

        return ret;
    }
}