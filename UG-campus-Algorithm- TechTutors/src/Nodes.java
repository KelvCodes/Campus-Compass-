import java.util.*;

public class Nodes{
    int n;
    String name;
    private boolean visited;
    LinkedList<Edge> edges;

    public Nodes (int n, String nameOfLocation) {
        this.n = n;
        this.name = nameOfLocation;
        visited = false;
        edges = new LinkedList<>();
    }

    boolean isVisited() {
        return visited;
    }

    void visit() {

        visited = true;
    }

    void unvisit() {

        visited = false;
    }
}
