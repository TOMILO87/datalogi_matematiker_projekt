package projekt;

public class Graph {
	// Attributes
	private LinkedList<Integer>[] vertices ; // The array will represents all vertices (contings) and the linked lists the edges  
		
	// Constructor
	public Graph(int n) {
		vertices = new LinkedList[n]; // n is number of contings
		for (int i=0; i<n; i++) {
			vertices[i] = new LinkedList<Integer>();
		}
	}
		
	// Public methods
	/*
	 * Add an edge to the graph
	 * Input: start - start vertex of the edge, end - end vertex of the edge  
	 */
	public void add_edge(int start, int end) { // verticies in edge are contings
		vertices[start-1].add(end-1); // want vertex with id 1 to be added to position 0 in the array
		vertices[end-1].add(start-1); // if vertex i is a neighbor of vertex j then vertex j is a neighbor of i
	}
	
	/*
	 * Get number of edges for an vertex
	 * Input: id - vertex identifier  
	 */
	public int get_size(int id) {
		return vertices[id-1].get_size(); // vertex with id 1 is found at position 0 in the array
	}
	
	/*
	 * Get all edges for an vertex
	 * Input: id - vertex identifier  
	 */
	//public int get_edges(int id) {
	//	vertex[start].get_head().get_data()
	//	return vertices[id].get_size(); // vertex with id 1 is found at position 0 in the array
	//}
	
	// Private methods
	
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
		System.out.print(g.get_size(3));
		
		//System.out.print("Hello World! This is Graph.");
		//try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
		//    stream.forEach(System.out::println);
		//}
	}

}

