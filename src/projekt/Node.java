package projekt;

// En nod består av två fält; ett informationsfält och ett adressfält. Informationsfältet är det data som ska sparas.
// Adressfältet innehåller adressen till nästa nod i listan eller ett speciellt värde, null, om det inte finns fler noder.

class Node<T> {
	
	// 'Attributes
    private T data; // 'The data type is generic // Informationsfältet
    public Node<T> next; // 'Adressfältet
    
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
