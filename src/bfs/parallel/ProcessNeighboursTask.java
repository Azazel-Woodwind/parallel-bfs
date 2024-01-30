package bfs.parallel;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import bfs.BreadthFirstSearch;
import bfs.parallel.bag.Bag;
import graph.GraphNode;

public class ProcessNeighboursTask<T> extends RecursiveTask<Bag<T>> {
    private static final int THRESHOLD = 16;
    private int start, end;
    private final List<GraphNode<T>> list;

    public ProcessNeighboursTask(List<GraphNode<T>> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Bag<T> compute() {
        if (end - start <= THRESHOLD) {
            // Process the chunk
            Bag<T> outputBag = new Bag<>();
            try {
                for (int i = start; i < end; i++) {

                    Thread.sleep(BreadthFirstSearch.EXPAND_NODE_DELAY);

                    GraphNode<T> neighbour = list.get(i);
                    if (!neighbour.setVisited()) {
                        outputBag.insert(neighbour.getValue());
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return outputBag;

        } else {
            int mid = start + (end - start) / 2;
            ProcessNeighboursTask<T> left = new ProcessNeighboursTask<>(list, start, mid);
            ProcessNeighboursTask<T> right = new ProcessNeighboursTask<>(list, mid, end);

            left.fork();
            Bag<T> out1 = right.compute();
            Bag<T> out2 = left.join();

            out1.union(out2);
            return out1;
        }
    }
}
