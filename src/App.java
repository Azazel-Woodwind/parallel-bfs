import utils.Pair;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import bfs.BreadthFirstSearch;
import bfs.SequentialBFS;
import bfs.parallel.ParallelBFS;
import graph.AdjacencyListGraph;
import graph.Graph;

public class App {

    /*
     * Loads a graph from a text file. The text file should contain a list of edges
     * in the form:
     * vertex1 vertex2
     * vertex1 vertex3
     * vertex2 vertex3
     * ...
     */
    public static void loadGraph(Graph<Integer> graph, String graphName) {
        try {
            String data = new String(Files.readAllBytes(Paths.get("resources/sampleGraphs/" + graphName + ".txt")));
            String[] edges = data.split("\n");
            for (String edge : edges) {
                edge = edge.strip();
                String[] vertices = edge.split("\\s+"); // split on whitespace
                graph.addEdge(Integer.parseInt(vertices[0]), Integer.parseInt(vertices[1]));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Pair<List<List<Set<Integer>>>, Integer> benchmarkBFS(BreadthFirstSearch<Integer> bfs) {
        long startTime = System.nanoTime();
        List<List<Set<Integer>>> levels = bfs.fullSearch();
        long endTime = System.nanoTime();
        return new Pair<>(levels, (int) ((endTime - startTime) / 1000000));
    }

    public static void main(String[] args) throws Exception {

        String[] graphNames = { "small", "congress_network", "facebook_combined" };
        for (String graphName : graphNames) {
            Graph<Integer> graph = new AdjacencyListGraph<>();
            System.out.println("Loading graph: " + graphName);
            loadGraph(graph, graphName);

            System.out.println("Running Sequential BFS on " + graphName);
            Pair<List<List<Set<Integer>>>, Integer> sequentialBFSResult = benchmarkBFS(new SequentialBFS<>(graph));
            graph.clearVisited();
            System.out.println("Running Parallel BFS on " + graphName);
            Pair<List<List<Set<Integer>>>, Integer> parallelBFSResult = benchmarkBFS(new ParallelBFS<>(graph));

            if (sequentialBFSResult.getFirst().equals(parallelBFSResult.getFirst())) {
                System.out.println("Results are equal");
            } else {
                System.out.println("Results are not equal");
            }
            System.out.println("Sequential BFS: " + sequentialBFSResult.getSecond() +
                    "ms");
            System.out.println("Parallel BFS: " + parallelBFSResult.getSecond() + "ms");
            System.out.println("Speedup: " + (double) sequentialBFSResult.getSecond() /
                    parallelBFSResult.getSecond());
            System.out.println();
        }
    }
}