package problem1secondtry;

import java.util.concurrent.atomic.AtomicMarkableReference;

//modified node from Page 197 Figure 9.2
public class Node<T> {
    T item;
    int key;

    //in this case, we use an AtomicMarkableReference because:
    /*
    A. Our Remove method is looking for a "reference" "Node succ = curr.next.getReference()", so we use an AMR.
    B. Page 215 of the book states:
            As described in detail in Pragma 9.8.1, an AtomicMarkableReference<T>
        object encapsulates both a reference to an object of type T and a Boolean mark.
        These fields can be atomically updated, either together or individually.
        Meaning we can use an AMR to simplify code & remove boolean mark arrays.
     */
    public AtomicMarkableReference<Node> next;

    public Node(T item, int key, AtomicMarkableReference<Node> next) {
        this.item = item;
        this.key = key;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "item=" + item +
                ", key=" + key +
                '}';
    }
}
