public class Edge implements Comparable<Edge> {
    Nodes source;
    Nodes destination;
    double weight;

    public Edge(Nodes source, Nodes destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String toString(){
        return String.format("%s-> %s, %f", source.name, destination.name, weight);
    }

    public int compareTo(Edge otherEdge) {
        if (this.weight > otherEdge.weight) {
            return 1;
        }
        else {
            return -1;
        }
    }
}

