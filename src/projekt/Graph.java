package projekt;

public class Graph {
	// Attributes
	private LinkedList<Integer>[] vertices ; // The array will represents all vertices (contings) and the linked lists the edges 
	private int num_vertices; // Number of vertices in the graph
	private boolean[] visited; // Visited nodes, used for finding components in the graph 
		
	// Constructor
	/*
	 * Input: n - number of vertices in the graph   
	 */
	public Graph(int n) {
		vertices = new LinkedList[n];
		for (int i=0; i<n; i++) {
			vertices[i] = new LinkedList<Integer>();
		}
		num_vertices = n;
		visited = new boolean[n];
	}
		
	// Public methods
	/*
	 * Add an edge to the graph
	 * Input: start - start vertex of the edge, end - end vertex of the edge  
	 */
	public void add_edge(int start, int end) { // verticies in edge are contings
		vertices[start-1].add(end); // want vertex with id 1 to be added to position 0 in the array
		vertices[end-1].add(start); // if vertex i is a neighbor of vertex j then vertex j is a neighbor of i
	}
	
	/*
	 * Get number of edges for an vertex (degree)
	 * Input: id - vertex identifier
	 * Return: an integer equal to number of edges for id   
	 */
	public int get_size(int id) {
		return vertices[id-1].get_size(); // vertex with id 1 is found at position 0 in the array
	}
	
	
	/*
	 * Get neighbors for an vertex
	 * Input: id - vertex identifier
	 * Output: arr - an array containing neighbors (values) for id 
	 */
	public int[] get_neighbors(int id) {
		int n = vertices[id-1].get_size(); // vertex with id 1 is found at position 0 in the array
		int[] arr = new int[n];
		int count = 0; 
		
		for (Node<Integer> node: vertices[id-1].get_nodes()) {
		    arr[count] = node.get_data();
		    count++;
		}
		return arr;
	}
	
	/*
	 * Get component for an vertex using depth first search (DFS)
	 * Input: id - vertex identifier
	 * Output: arr - an array containing nodes (values) in same component as id 
	 */
	public int[] get_component(int id) {
		int[] arr = new int[100]; // conjecture that component size will be maximum 100 nodes
		int arr_size = 0; // initially no nodes in the array
		
		for (int v=0; v < num_vertices; v++) {
			visited[v] = false;
		}
		
		return arr;
	}
	
	// Private methods
	/*
	 * Helper function to get_component() 
	 */
	//private int int[] get_component2(int id, int[] arr, int arr_size, HashSet<Integer> visited) {
	//	visited.add(id);
	//	arr[arr_size] = id;
	//	arr_size++;
	//	for (int i: get_neighbors(id)) {
	//		if (visited.contains(i)==false) {
	//			
	//		}
	//	}
		//return arr;
	//}
	
	/*
	 * Get slice of array 
	 */
	//private static int[] get_slice(int[] array, int start, int end) {
	//	int[] slicedArr = new int[end - start];
	//	for (int i = 0; i < slicedArr.length; i++) {
	//		slicedArr[i] = array[start + i];   
	//	}
	//return slicedArr;
	//}
	
	public static void main(String args[]) {
		//int n = 11393435; // number of contings in the dataset
		int n = 8; // to run some initial tests
		Graph g = new Graph(n);
		
		// test data
		g.add_edge(1,3);
		g.add_edge(1,2);
		g.add_edge(3,2);
		g.add_edge(3,4);
		g.add_edge(2,5);
		g.add_edge(5,4);
		g.add_edge(6,7);
		g.add_edge(7,8);
		g.add_edge(8,6);
		g.add_edge(2,4);
		
		// check
		System.out.print(g.get_size(1) + "\n\n");
		
		for (int i: g.get_neighbors(8)) {
			System.out.print(i + "\n");
		}
		
		
		//System.out.print("Hello World! This is Graph.");
		//try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
		//    stream.forEach(System.out::println);
		//}
	}

}

