package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyListGraph<T> implements Graph<T> {
    private Map<GraphNode<T>, List<GraphNode<T>>> adjacencyList;
    private Map<T, GraphNode<T>> vertexValueToNode;
    private List<GraphNode<T>> orderedVertices;

    public AdjacencyListGraph() {
        this.adjacencyList = new HashMap<>();
        this.vertexValueToNode = new HashMap<>();
        this.orderedVertices = new ArrayList<>();
    }

    @Override
    public List<GraphNode<T>> getOrderedVertices() {
        return this.orderedVertices;
    }

    @Override
    public void addVertex(T vertex) {
        if (!this.adjacencyList.containsKey(vertex)) {
            GraphNode<T> node = new GraphNode<>(vertex);
            vertexValueToNode.put(vertex, node);
            this.orderedVertices.add(node);
            this.adjacencyList.put(node, new ArrayList<>());
        }
    }

    @Override
    public void addEdge(T source, T destination) {
        if (!this.vertexValueToNode.containsKey(source)) {
            this.addVertex(source);
        }
        if (!this.vertexValueToNode.containsKey(destination)) {
            this.addVertex(destination);
        }

        GraphNode<T> sourceNode = vertexValueToNode.get(source);
        GraphNode<T> destinationNode = vertexValueToNode.get(destination);
        this.adjacencyList.get(sourceNode).add(destinationNode);
    }

    public void setVisited(T vertex) {
        if (this.vertexValueToNode.containsKey(vertex)) {
            this.vertexValueToNode.get(vertex).setVisited();
        }
    }

    @Override
    public int size() {
        return this.adjacencyList.size();
    }

    @Override
    public List<GraphNode<T>> getNeighbours(T vertex) {
        if (this.vertexValueToNode.containsKey(vertex)) {
            return this.adjacencyList.get(this.vertexValueToNode.get(vertex));
        }
        return new ArrayList<>();
    }

    @Override
    public void printGraph() {
        for (GraphNode<T> key : this.adjacencyList.keySet()) {
            System.out.print(key.getValue() + " -> ");
            for (GraphNode<T> neighbour : this.adjacencyList.get(key)) {
                System.out.print(neighbour.getValue() + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void clearVisited() {
        for (GraphNode<T> key : this.adjacencyList.keySet()) {
            key.setUnvisited();
        }
    }

    public static void main(String[] args) {
        Graph<Integer> graph = new AdjacencyListGraph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 1);
        graph.printGraph();
    }
}