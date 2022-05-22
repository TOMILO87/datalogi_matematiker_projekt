package projekt;

public class Stack {
	
	// Attributes
	private int[] intArr;
	private int sizeArr; 
	
	// Constructor
	public Stack() {
		intArr = new int[70000000];
		sizeArr = 0;
	}
	
	// Public methods
	
	// Check if stack is empty
	public boolean is_empty() {
		return sizeArr == 0; 
	}
	
	// Return size of the stack
	public int size() {
		return sizeArr; 
	}
	
	// Add element to the stack
	public void push(int x) throws Exception {
		if (sizeArr < 70000000) {
			intArr[sizeArr] = x;
			sizeArr++;
		} else {
			System.out.println(intArr[sizeArr-1]);
			throw new Exception("Stack is full!");
		}
	}
	
	// Return the top element and remove it from the stack
	public int pop() throws Exception {
		if (is_empty() == false) {
			int x = intArr[sizeArr-1];
			sizeArr--;
			return x;
		} else {
			throw new Exception("Stack is empty!");
		}
	}
	
	// Return the top element and don't remove it from the stack
	public int peek() throws Exception {
		if (is_empty() == false) {
			int x = intArr[sizeArr-1];
			return x;
		} else {
			throw new Exception("Stack is empty!");
		}
	}
	
	public static void main(String args[]) throws Exception {
		Stack stack = new Stack();
		
		// Test
		stack.push(3);
		stack.push(7);
		System.out.println(stack.size());
		System.out.println(stack.peek());
		System.out.println(stack.pop());
		System.out.println(stack.peek());
		stack.push(8);
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		// System.out.println(stack.peek()); // Raises Exception
	}
}