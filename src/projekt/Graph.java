package projekt;

//class
public class Graph {
	
	//LinkedList [] list = new LinkedList [8];
	
	// Attributes
	private int[] doubleArray; //instead of int the elements should be linked lists
		
	// Constructor
	public Graph(int n) {
		this.doubleArray = new int[n];
		}
		
	// Public methods
	//public boolean is_empty() {
	//		return this.doubleArray.length == 0; 
	//		}
	
	// Private methods
	
	public static void main(String args[]) {
		int n = 8;
		Graph G = new Graph(n);
		
		System.out.print("Hello World! This is Graph.");
		//try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
		//    stream.forEach(System.out::println);
		//}
	}

}

