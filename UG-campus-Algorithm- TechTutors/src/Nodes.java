tion;
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
