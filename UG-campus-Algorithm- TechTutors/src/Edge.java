s, %f", source.name, destination.name, weight);
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

