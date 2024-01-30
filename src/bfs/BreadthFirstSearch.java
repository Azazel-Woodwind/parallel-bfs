package bfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graph.Graph;
import graph.GraphNode;

public abstract class BreadthFirstSearch<T> {
    protected Graph<T> graph;
    protected boolean debug = false;
    public static final int EXPAND_NODE_DELAY = 1;

    public BreadthFirstSearch(Graph<T> graph) {
        this.graph = graph;
    }

    public BreadthFirstSearch(Graph<T> graph, boolean debug) {
        this.graph = graph;
        this.debug = debug;
    }

    public List<List<Set<T>>> fullSearch() {
        return fullSearch(graph.getOrderedVertices().get(0).getValue());
    }

    public List<List<Set<T>>> fullSearch(T source) {
        List<List<Set<T>>> allLevels = new ArrayList<>();
        List<Set<T>> levels = this.search(source);
        allLevels.add(levels);
        List<GraphNode<T>> orderedVertices = graph.getOrderedVertices();
        for (GraphNode<T> vertex : orderedVertices) {
            if (!vertex.setVisited()) {
                levels = this.search(vertex.getValue());
                allLevels.add(levels);
            }
        }

        return allLevels;
    }

    public abstract List<Set<T>> search(T start);
}
