package bfs.parallel;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import bfs.parallel.bag.Bag;
import graph.Graph;
import graph.GraphNode;

public class ProcessLayerTask<T> extends RecursiveTask<Bag<T>> {
    private Bag<T> inputBag;
    private Set<T> level;
    private Graph<T> graph;
    private ForkJoinPool pool;

    public ProcessLayerTask(Bag<T> inputBag, Set<T> level, Graph<T> graph, ForkJoinPool pool) {
        this.inputBag = inputBag;
        this.level = level;
        this.graph = graph;
        this.pool = pool;
    }

    @Override
    protected Bag<T> compute() {
        if (inputBag.size() <= ParallelBFS.GRAIN_SIZE) {
            Bag<T> outputBag = new Bag<>();
            for (T vertex : inputBag) {
                List<GraphNode<T>> neighbours = graph.getNeighbours(vertex);
                ProcessNeighboursTask<T> task = new ProcessNeighboursTask<>(neighbours, 0,
                        neighbours.size());

                Bag<T> out = pool.invoke(task);
                if (outputBag == null) {
                    outputBag = out;
                } else {
                    outputBag.union(out);
                }
            }

            return outputBag;
        } else {
            // Split the bag and create new subtasks
            Bag<T> leftBag = (Bag<T>) inputBag.split();

            ProcessLayerTask<T> leftTask = new ProcessLayerTask<>(leftBag, level, graph, pool);
            ProcessLayerTask<T> rightTask = new ProcessLayerTask<>(inputBag, level, graph, pool);

            leftTask.fork();
            Bag<T> out1 = rightTask.compute();
            Bag<T> out2 = leftTask.join();

            out1.union(out2);
            return out1;
        }
    }
}