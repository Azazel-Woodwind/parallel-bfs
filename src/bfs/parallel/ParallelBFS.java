package bfs.parallel;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import bfs.BreadthFirstSearch;
import bfs.parallel.bag.Bag;

import java.util.HashSet;
import java.util.ArrayList;

import graph.Graph;

public class ParallelBFS<T> extends BreadthFirstSearch<T> {
    public static final int GRAIN_SIZE = 128;

    public ParallelBFS(Graph<T> graph) {
        super(graph);
    }

    @Override
    public List<Set<T>> search(T start) {
        List<Set<T>> levels = new ArrayList<>();
        Bag<T> currentBag = new Bag<>();
        currentBag.insert(start);
        graph.setVisited(start);
        try (ForkJoinPool pool = new ForkJoinPool()) {
            while (!currentBag.isEmpty()) {
                Set<T> level = new HashSet<>();
                for (T vertex : currentBag) {
                    level.add(vertex);
                }
                levels.add(level);
                ProcessLayerTask<T> task = new ProcessLayerTask<T>(currentBag, level, graph, pool);
                currentBag = pool.invoke(task);
            }
        }
        return levels;
    }

}
