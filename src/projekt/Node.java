package projekt;

// A node consists of the two fields: An information field and an address field. The information field contains the data to be saved
// The address field contains the address to the next node in the linked list or a special value, null, if there are no additional nodes

class Node<T> {
	
	// Attributes
	private T data; // Information field
	public Node<T> next; // Address field
    
    // Constructor
    public Node(T data) {
    	this.data = data;
    	next = null;
    }
    
    // Method
    public T get_data() {
    	return data;
    }
}