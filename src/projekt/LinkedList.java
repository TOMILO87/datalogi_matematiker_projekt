package projekt;

// Inom datavetenskap ar en lankad lista en linjar foljd av noder, aven ibland kallat for celler, som lankas ihop med pekare.
// En nod bestar av tva falt; ett informationsfalt och ett adressfalt. Informationsfaltet ar det data som ska sparas.
// Adressfaltet innehaller adressen till nasta nod i listan eller ett speciellt varde, null, om det inte finns fler noder.

import java.util.NoSuchElementException;
import java.util.Iterator;

class LinkedList<E> implements Iterable<E> {
    // 'Attribute
	// Single linked list
    private Node<E> head; // 'Type Node is defined in Node.java // sager vilken nod som ar overst i listan
    
    // 'Constructor
    public LinkedList() {
	head = null; // initially no elements in linked list // till att borja med ar ingen nod overst i listan
    }
    
    //' Methods
    public void add(E s) {
    // Skapar en ny nod
	Node<E> node = new Node<E>(s); // 'E - Element (used extensively by the Java Collections Framework) c.f. T which is any type
	// Anger att pekaren till noden finns hos nuvarande oversta noden i lankade listan
	node.next = head; // 'Head is of type Node, see above. Next is an attribute in the Node class
	// Satter den nya noden som overst i listan (nar en ny nod laggas till kommer denna ocksa att fa en pekare till nasta)
	head = node; // 'Set the value of head to our node (seems kind of circular)
    }
    
    // Sager vilken nod som ar overst i listan. Anvands nedan
    public Node<E> get_head() {
	return head;
    }
    
    // krav da klassen implementerar iterable
    // anvander sig av java.utils for att fa type Iterator
    public Iterator<E> iterator() {
	return new ListIterator();
    }
    
    // lister iterator subclass 'finns krav pa metoder da implemenets iterator anges
    private class ListIterator implements Iterator<E> {
    
    //attribute inside subclass
	private Node<E> current; //'maybe inline here would be more correct 'current is set to null by default 
	
	// 'Constructor
	public ListIterator() {
	    this.current = head; // borjar med oversata noden!
	}
	
	// 'Subclass methods
	public boolean hasNext() {
	    return this.current != null;
	}

	public E next() { // 'E is datatype 
	    if (this.hasNext()) {
		E res = this.current.get_data(); // 'definerad inom super-klassen
		this.current = this.current.next; // 'varje nod anger adressen till nasta nod i listan som satts som ny aktuell nod
		return res;
	    } else {
		throw new NoSuchElementException();
	    }
	}
	
	// Error handling // ' has not been defined so throw exception instead
	public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
	LinkedList<String> lst = new LinkedList<String>(); // here we choose string as type and create an instance of the object
	lst.add("DA2004");
	lst.add("DA3018");
	
	// Anvander sig av att vi har definerat hur iteration over datatypen gors
	for (String s: lst) {
	    System.out.println(s);
	}
    }
}
