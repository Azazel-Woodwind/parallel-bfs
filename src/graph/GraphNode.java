package graph;

public class GraphNode<T> {
    private T value;
    private boolean visited;

    public GraphNode(T value) {
        this.value = value;
        this.visited = false;
    }

    public T getValue() {
        return this.value;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public boolean setVisited() {
        boolean temp = this.visited;
        this.visited = true;
        return temp;
    }

    public void setUnvisited() {
        this.visited = false;
    }
}
