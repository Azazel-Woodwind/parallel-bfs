package graph;

import java.util.List;

public interface Graph<T> {
    public void addVertex(T vertex);

    public void addEdge(T source, T destination);

    public void setVisited(T vertex);

    public int size();

    public void clearVisited();

    List<GraphNode<T>> getNeighbours(T vertex);

    List<GraphNode<T>> getOrderedVertices();

    public void printGraph();
}
