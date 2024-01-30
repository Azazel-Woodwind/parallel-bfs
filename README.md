# Introduction

The Parallel Breadth-First Search (PBFS) Algorithm implemented in this repository is based on the algorithm described in this research paper:
<https://www.csd.uwo.ca/~mmorenom/CS433-CS9624/Resources/Leiserson-Schardl-2010.pdf>
Although it sounds easy, it is rather non-trivial to write an optimal concurrent BFS algorithm.

# Benchmark

I benchmarked my PBFS implementation against the typical Sequential BFS using graphs taken from the [Stanford Large Network Dataset Collection](https://snap.stanford.edu/data/): a publicly available repository of graphs of all shapes, sizes and types. Two of these ([`congress_network.txt`](https://snap.stanford.edu/data/congress-twitter.html), [`facebook_combined.txt`](https://snap.stanford.edu/data/ego-Facebook.html)) lie in `resources/sampleGraphs`. `small.txt` is a small custom graph I made.

It is extremely worth noting that in these tests, I added a **1ms** delay to neighbour expansion. More on this in the conclusion.

## How to run

To run the benchmark tests with the provided sample graphs (if you add your own, you'll need to add their file names to the array on line 47 of `App.java`), simply compile the entirety of the `src` directory and run `App.main`.

## Machine Specifications

All benchmark testing on my end was done on an AMD EPYC 7282 with **6 cores**.

## Results

All times are rounded down

### small.txt

-   Nodes: 9
-   Edges: 16
-   **Sequential BFS total runtime**: ~20ms
-   **Parallel BFS total runtime**: ~27ms
-   Speedup: ~0.74

### congress_network.txt

-   Nodes: 475
-   Edges: 13,289
-   **Sequential BFS total runtime**: ~14.894s
-   **Parallel BFS total runtime**: ~4.190s
-   Speedup: ~3.55
-   [Link](https://snap.stanford.edu/data/congress-twitter.html)

### facebook_combined.txt

-   Nodes: 4,039
-   Edges: 88,234
-   **Sequential BFS total runtime**: ~99.430s
-   **Parallel BFS total runtime**: ~21.225s
-   Speedup: ~4.68
-   [Link](https://snap.stanford.edu/data/ego-Facebook.html)

### soc-Slashdot0902.txt (not provided)

-   Nodes: 82,168
-   Edges: 948,464
-   **Sequential BFS total runtime**: ~1062.480s
-   **Parallel BFS total runtime**: ~178.471s
-   Speedup: ~5.95
-   [Link](https://snap.stanford.edu/data/soc-Slashdot0902.html)

# Conclusion

As shown in the results, the Parallel BFS heavily outperforms the Sequential BFS when the graph size is sufficient. Of course, this is when the overhead of parallelisation becomes less apparent.

These results are similar to those achieved by Charles E. Leiserson, Tao B. Schardl in the cited research paper, with speedups of 5-7x on extremely large graphs.

I added a 1ms node expansion delay (`BreadthFirstSearch.EXPAND_NODE_DELAY`) as without it, the algorithm performs negligible work on each node. In this case, the PBFS algorithm performs nearly identically to the Sequential BFS algorithm as there is essentially no work to parallelise, leaving instantiation and tear down of thread pools to take up runtime. The 1ms delay simulates work done, which is more representative of its potential use cases, such as heuristic calculations, or temporal edge traversals. Of course, changing this value will change the speed up to a certain extent.

It is also worth noting that the extent of parallelisation in the PBFS algorithm is controlled by `ParallelBFS.GRAIN_SIZE` (128 by default), and `ProcessNeighboursTask.THRESHOLD` (16 by default). I leave it up to you to figure out what these do, but ideally, these values would be tailored towards the input. In my benchmark testing, I left them at their default values, so they are not optimised.

Naturally, results heavily depend on number of logical processors. In this case, a rather underwhelming 6 were used, but even in this case, it led to substantial speed ups.
