package projekt;

import java.util.NoSuchElementException;
import java.util.Iterator;

class LinkedList<E> implements Iterable<E> { // E - Element c.f. T which is any type
    // Attribute
    private Node<E> head; // Type Node is defined in Node.java
    
    // Constructor
    public LinkedList() {
    	head = null; // Initially no elements in linked list so no head Node
    }
    
    // Methods
    // Add node to linked list
    public void add(E s) {
    	// s is the data contained in the new Node (its data field)
    	Node<E> node = new Node<E>(s);
    	
    	// Next is an attribute in the Node class which is of type Node - it is where the new node points (its address field)
    	// The new Node will point to the Node currently "most to the left" in the linked list
    	node.next = head;
    	
    	// Then the new Node is set to be the Node currently "most the the left" in the linked list
    	head = node;
    }
    
    // Gives the node "most to the left" in the linked list
    public Node<E> get_head() {
    	return head;
    }
    
    // Gives number of nodes in the linked list
    public int get_size() {
    	int counter = 0;
    	Node<E> current = head;
    	while (current != null) {
    	    counter++;
    	    current = current.next;
    	}
    	return counter;
    }
    
    // Gives data for the nodes in the linked list in an array
    public Node<E>[] get_nodes() {
    	int n = this.get_size();
    	Node<E>[] arr = new Node[n];
    	Node<E> current = head;
    	
    	for (int i=0; i<n; i++) {
    		arr[i] = current;
    	    current = current.next;
    	}
    	return arr;
    }
    
    // Required this method for class iterable 
    public Iterator<E> iterator() {
    	return new ListIterator();
    }
    
    // Lister iterator subclass
    private class ListIterator implements Iterator<E> {
    	// Attribute inside subclass
    	private Node<E> current; 
	
		// Constructor inside subclass
		public ListIterator() {
		    this.current = head; // Start with Node "most to the left"
		}
		
		// Subclass methods
		public boolean hasNext() {
		    return this.current != null;
		}
	
		public E next() {
		    if (this.hasNext()) {
		    	E res = this.current.get_data();
		    	this.current = this.current.next;
		    	return res;
		    } else {
		    	throw new NoSuchElementException();
		    }
		}
		
		// Has not been defined so throw exception instead
		public void remove() {
	            throw new UnsupportedOperationException();
	        }
	    }

    public static void main(String[] args) {
		LinkedList<String> lst = new LinkedList<String>(); // Here we choose string as type of of element and create an instance of the object
		lst.add("DA2004");
		lst.add("DA3018");
		
		// Here we use the iterator we defined
		for (String s: lst) {
		    System.out.println(s);
		}
	}
}
