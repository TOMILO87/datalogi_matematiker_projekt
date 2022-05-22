## Course project "Datalogi matematiker"

#### Introduction
We analyse a large dataset containing information about segments of DNA (contigs) and their connections. The contigs are analysed as vertices in a graph and their connections corresponds to edges between vertices.

Each row in the given dataset "Spruce_fingerprint_2017-03-10_16.48" contains information about a potential edge. In particular each row contains two vertex identifiers and also information about their overlap (start and end position of overlap). We are given that the line is an edge if the length of the overlap is greater than 1000 for both verticis  (intuitively the overlap should be exactly the same for both verticies, but because of size differences contings they may differ slightly).

By running shell command `wc -l` we find that the number of lines (potential edges in the file) is 64 056 772, so we are dealing with a large dataset. 

#### Processing the data
The shell script "convert_data_to_edgeges_integer_format" contains the command we use such that only lines which are edges are kept and further the contig identifiers have been replaced by integers. Here is a brief summary of the steps (using shell command `awk` to a great extent):

1) we remove columns which are not useful for the project, only identifiers and overlap information are kept. This reduces the size of the file somewhat from about 7 GB to 6 GB.
2) Next we identify unique contigs by first grabbing the first column in the file (identifier of the first contig) and similarly grab the second columns (identifiers of the second contig) and place them in new files. Then we put the second columns below the first columns in a new file. After sorting and removing duplicates we are done.
3) We confirm that we found the right identifiers by using `wc -l` and indeed number of unique identifiers in the dataset is 11 393 435 as given by the project assignment. Further to get integer identifiers we add line numbers to the identifiers and save in file "unique_identifiers_id.olp.m4".
4) We take the original datafile and use "unique_identifiers_id.olp.m4" as a lookup table to replace the string contig identifiers by integers between 1 and 11 393 435.
5) We then check which lines contains edges by filtering out those where the overlaps are sufficiently large. We save the result in the file "edges_processed.olp.m4". **It turns out that number of edges (filtered lines) equals 63 962 895.**
6) We remove all columns but those which contains the contig identifiers in integer format.

We note that almost all potential edges are actual edges, 63 962 895 / 64 056 772 = 99.85%.

#### Creating a graph
There are two popular graph representations we consider, using an adjacency list or a metrix representation. As stated in the lecture videos the former is better if the graph is sparse meaning that the numbers of edges is proportional to "size" of graph (here number of contigs), whereas the latter useful if there are many edges between nodes.

Considering we have about 11 million nodes and if we had a matrix representation then the total number of elements in the matrix (consisting of zeros and ones) would be |V|^2, were |V| denotes number of vertices, which is about 10^14, which seems very unpractical. Therefore we use an adjacency list e.g. an array where each array element consists of a linked list. Specifically we use the generic linked list provided earlier in the course. The indices of the array corresponds to different contig identifiers and the linked lists contains information about the edges of the vertex. In total the number of elements (each element is a contig identifier) in the adjacency list will be limited by |E| + |V|, where |E| is the number of edges in the graph. If e.g on average each vertex have about C edges, where C is a constant which don't depend on |V|, then the number of elements in the graph will be about 2C *|V| which we conjuncture will be much less than |V|^2. And considering we know that |V|+|E| is about (1 + 6) * 10^7 our chosen graph seems justified.

Despite choosing the adjacency list it took a lot of RAM memory to create the graph in Java. First we encountred "main" java.lang.OutOfMemoryError: Java heap space: failed reallocation of scalar replaced objects". Fortunately, we could simply increase the heap size, on Virtual Machine used to run Ubuntu on Windows, from its default value: "VM settings: Max. Heap Size (Estimated): 3.10G Using VM: OpenJDK 64-Bit Server VM" to about 9.50G (a smaller size might have sufficient) using shell command  `export _JAVA_OPTIONS=-Xmx6000m`.

Out graph class is available in "Graph.java", the linked lists in "LinkeList.java" with associated "Node.java". To get an instance of the graph we must specify the number of vertices that will be used. To add edges we provide two integers representing start and end node (the graph is considered undirected), which we scan iteratively from an input file with O(|V|) complexity.

#### Running the analysis
Since we want to find the node degree distribution, number of components, with at least three nodes, and share of those which are cliques, we include in our graph a few methods.

The method "get_degree()" simple returns the number of nodes in in the linked list. To get the degree distribution we iterate over all vertices with complexity limited by O(|V| + |E|) (|E| being the maximum number of elements in the linked list we must check). But since not every vertex has |E| edges we can be confident that the actual complexity is much lower, considering one vertex having degree |E| would mean that the other vertices would not have degree greater than one. We conclude complexity will not be an issue.

In the analysis we divide the degrees into intervals to plot a the number of elements within each interval (bin). The time complexity depends on the number of bins (free parameter, we choose 10) and the number of different vertex degrees in the data. Conservative maximum time complexity is O(|V|).

To get number of components and share which are cliques we traverse the graph using depth first serach (DFS). First we tried using a recursive method shown in the lecture videos and in Coreman et al. Introduction to Algorithms (p. 604). This is known to have time complexity O(|V| + |E|). In "Graph.java" this is implemented in "get_components()" which returns all components in the graph in an array containing sets (HashSets). It works well for smaller datasets. However when analysing the whole dataset we run into "Exception in thread "main" java.lang.StackOverflowError" which, after searching online, seems to have to do with too many recursions being performed resulting in stack limitations. Adjusting the parameters for the Virtual Machine did not seem to solve it.

We instead implemented an iterative version where we use the Stack class introduced in Lab2, "Stack.java". The iterative version (based on post on GeeksforGeeks webpage but with similar logic as the recurisve verion and thus with similar complexity) is implemented in "get_component_iter()". Unlike the recursive version this only returns one component depending in the vertex used as argument when calling the function. This works better for the large file. When doing the analysis we iterate over all vertices and collect all components in one array to get similar output as "get_components()".

When we have all components in an array, we iterate over them to check which contains at least three vertices and which are cliques (meaning that all have degrees equal to number of vertices in the component minus one). This have complexity less than O(|V|) since at minimum every component contains a vertex (although we need to get number of vertices per component this will not be very time consuming).

#### Results
By running `cat data/edges_processed.olp.m4 | java -jar Graph.jar` we get the below output. Bins are of equal width and e.g. first bin is number of vertices with degrees 0 to 435 (in a subsequent analysis I would consider more bins or splitting the first bin into smaller intervals).

**Vertices per bin:**
[0.00;435.40]: 11363925
(435.40;870.80]: 26741
(870.80;1306.20]: 2255
(1306.20;1741.60]: 313
(1741.60;2177.00]: 150
(2177.00;2612.40]: 43
(2612.40;3047.80]: 3
(3047.80;3483.20]: 1
(3483.20;3918.60]: 2
(3918.60;4354.00]: 1
(4354.00;4354.00]: 1

**Component count (at least three verticies):** 273187
**Clique count (at least three verticies):** 35472
**Share cliques:** 0.12984513

Time for task Create graph: 112257 ms
Time for task Calcualte degree distribution: 5413 ms
Time for task Create histogram: 24 ms
Time for task Count components and cliques: 14873755 ms
