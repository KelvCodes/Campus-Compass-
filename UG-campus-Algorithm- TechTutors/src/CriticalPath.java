

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
