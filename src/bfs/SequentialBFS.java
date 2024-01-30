package bfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import graph.Graph;
import graph.GraphNode;

/**
 * SequentialBFS
 */
public class SequentialBFS<T> extends BreadthFirstSearch<T> {
    private static class Node<T> {
        private T vertex;
        private int level;

        public Node(T vertex, int level) {
            this.vertex = vertex;
            this.level = level;
        }

        public T getVertex() {
            return vertex;
        }

        public int getLevel() {
            return level;
        }
    }

    private boolean debug = false;

    public SequentialBFS(Graph<T> graph) {
        super(graph);
    }

    public SequentialBFS(Graph<T> graph, boolean debug) {
        super(graph, debug);
    }

    @Override
    public List<Set<T>> search(T start) {
        Queue<Node<T>> queue = new LinkedList<>();
        List<Set<T>> levels = new ArrayList<>();

        queue.add(new Node<T>(start, 0));

        int count = 0;
        if (debug) {
            System.out.println("BFS traversal starting from vertex: " + start);
        }
        graph.setVisited(start);
        while (!queue.isEmpty()) {
            count++;
            Node<T> node = queue.poll();
            T vertex = node.getVertex();
            int level = node.getLevel();
            if (level >= levels.size()) {
                levels.add(new HashSet<>());
            }
            levels.get(level).add(vertex);

            if (debug) {
                System.out.print(vertex + " ");
            }
            try {
                for (GraphNode<T> neighbor : graph.getNeighbours(vertex)) {

                    Thread.sleep(EXPAND_NODE_DELAY);

                    if (!neighbor.setVisited()) {
                        queue.add(new Node<T>(neighbor.getValue(), level + 1));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (debug) {
            System.out.println();
            System.out.println("Total vertices visited: " + count);
        }

        return levels;
    }
}
