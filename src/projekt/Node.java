package projekt;

// En nod best�r av tv� f�lt; ett informationsf�lt och ett adressf�lt. Informationsf�ltet �r det data som ska sparas.
// Adressf�ltet inneh�ller adressen till n�sta nod i listan eller ett speciellt v�rde, null, om det inte finns fler noder.

class Node<T> {
	
	// 'Attributes
    private T data; // 'The data type is generic // Informationsf�ltet
    public Node<T> next; // 'Adressf�ltet
    
    // 'Constructor
    public Node(T data) {
	this.data = data;
	next = null;
    }
    
    // 'Method
    public T get_data() {
	return data;
    }
}
