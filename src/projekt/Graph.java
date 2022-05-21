package projekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Graph {
	// Attributes
	private LinkedList<Integer>[] vertices ; // Each index in the array represents a vertex (conting) and the linked lists the edges 
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
		vertices[start].add(end);
		vertices[end].add(start); // if vertex i is a neighbor of vertex j then vertex j is a neighbor of i
	}
	
	/*
	 * Get the degree of a vertex corresponding to number of neighbors of the vertex
	 * Input: id - vertex identifier
	 * Return: an integer equal to the degree of id
	 */
	public int get_degree(int id) {
		return vertices[id].get_size();
	}
	
	/*
	 * Get the vertices in the graph
	 * Return: arr - array with the numbers representing the vertices
	 */
	public int[] get_vertices() {
		int[] arr = new int[num_vertices];
		for (int v=0; v < num_vertices; v++) {
			arr[v] = v;
		}
		return arr;
	}
	
	/*
	 * Get neighbors for a vertex
	 * Input: id - vertex identifier
	 * Output: arr - an array containing neighbors of id 
	 */
	public int[] get_neighbors(int id) {
		int n = vertices[id].get_size();
		int[] arr = new int[n];
		int count = 0; 
		
		for (Node<Integer> node: vertices[id].get_nodes()) {
		    arr[count] = node.get_data();
		    count++;
		}
		return arr;
	}
	
	/*
	 * Get all components in the graph using depth first search (DFS)
	 * Output: arrLst - an ArrayList containing Hashsets representing components of the graph 
	 */
	public ArrayList<HashSet<Integer>> get_components() {
		ArrayList<HashSet<Integer>> arrLst = new ArrayList<HashSet<Integer>>();
		
		// Initially no nodes visited
		for (int v=0; v < num_vertices; v++) {
			visited[v] = false;
		}
		
		// Add components to the array recursively
		HashSet<Integer> S = new HashSet<Integer>();
		for (int v=0; v < num_vertices; v++) {
			if (visited[v] == false) {
				S = get_components_visited(v);
				arrLst.add(S);
			}
		}
		return arrLst;
	}
	
	// Private methods
	/*
	 * Helper function to get_components(v) 
	 */
	private HashSet<Integer> get_components_visited(int v) {
		visited[v] = true;
		
		// Add nodes to component recursively 
		HashSet<Integer> S = new HashSet<Integer>();
		HashSet<Integer> R = new HashSet<Integer>();
		S.add(v);
		for (int u: get_neighbors(v)) {
			if (visited[u] == false) {
				R = get_components_visited(u);
				S.addAll(R); // union of S and R
			}
		}
		return S;
	}
	
	public static void main(String args[]) {
		int n = 11393435; // number of contings in the dataset
		Graph g = new Graph(n);
		
		// Read from edges_processed.olp.m4 and add edges to the graph
		Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] split_line = line.split(" ");
            int edge_from = Integer.parseInt(split_line[0]) - 1; // identifier starts with 1 and want to place at position 0 in array
            int edge_to = Integer.parseInt(split_line[1]) - 1;
            g.add_edge(edge_from, edge_to);
        }
        sc.close();
        System.out.print("Scanning complete and edges added to graph\n\n");
        
		// Find node degree distribution
		Map<Integer, Integer> freq_count = new HashMap<Integer, Integer>();
		for (int v: g.get_vertices()) {
			if (freq_count.get(g.get_degree(v)) == null) {
				freq_count.put(g.get_degree(v), 1);
			} else {
				freq_count.put(g.get_degree(v), freq_count.get(g.get_degree(v)) + 1);
			} 
		}
		
		// Assign degree frequencies to intervals
		int num_intervals = 10;
		int max_freq = (Collections.max(freq_count.keySet()));
		float length_interval = (float) max_freq / num_intervals;
		Map<Float, Integer> interval_count = new HashMap<Float, Integer>();
		
		for (float i=length_interval; i < (max_freq - length_interval); i+=length_interval) {
			interval_count.put(i, 0);
		}
		interval_count.put((float) max_freq, 0);
		
		for (int i: freq_count.keySet()) { // assign degrees to intervals
			float j = length_interval;
			while (i > j) {
				j += length_interval;
				j = Math.min(j,  max_freq);
			}
			if (interval_count.get(j) == null) {
				interval_count.put(j, freq_count.get(i));
			} else {
				interval_count.put(j, interval_count.get(j) + freq_count.get(i));
			}
		}
		
		// Print frequencies per interval
		ArrayList<Float> interval_keys = new ArrayList<Float>(interval_count.keySet());
		Collections.sort(interval_keys);
		float prev = (float) 0; // for nicer print
		for (float key : interval_keys) {
			float freq = interval_count.get(key);
			if (prev == 0) {
				System.out.printf("[%.2f" + ";" + "%.2f" + "]: " + "%.0f" + "\n", prev, key, freq);
			} else {
				System.out.printf("(%.2f" + ";" + "%.2f" + "]: " + "%.0f" + "\n", prev, key, freq);
			}
			prev = key;
		}
		
		// Find number of components and cliques with at least three vertices
		int component_count = 0;
		int clique_count = 0;
		
		for (HashSet c: g.get_components()) {
			int m=c.size();
			
			// Count components with at least three vertices
			if (m >= 3) {
				// Create a iterator of type integer to iterate HashSet
		        Iterator<Integer> it = c.iterator();
				
		        // Check for clicks
				boolean clique = true;
		        while (it.hasNext()) {
		            if (g.get_degree(it.next()) != (m-1)) {
		            	clique = false;
		            	break;
		            }
		        }
		        if (clique == true) {
		        	clique_count++;
		        }
		        component_count++;
		    }
		}
		
		// Print component count and clique count
		System.out.print("\nComponent count (at least three verticies): " + component_count);
		System.out.print("\nClique count (at least three verticies): " + clique_count);
		
		// Print share of components which are cliques
		if (component_count > 0) {
			float clique_share =  (float) clique_count / component_count;
			System.out.print("\nShare cliques: " + clique_share + "\n");
		}
	}
}

