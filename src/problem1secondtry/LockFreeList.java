package problem1secondtry;

import java.util.concurrent.atomic.AtomicMarkableReference;
import problem1secondtry.Node;

public class LockFreeList<T> {

    //we need to define a head pointer because add/contains both refer to a "head" that isn't defined:
    public Node head;
    //tail based on page 214 figure 9.22
    //note to self, remove if not needed.
    Node tail;

    public LockFreeList(Node head, Node tail) {
        this.head = head;
        this.tail = tail;
        //connect head to tail.
        boolean compareAndSet = head.next.compareAndSet(null, tail, false, false);
        if(!compareAndSet){
            System.out.println("Error connecting head to tail, ending.");
            System.exit(0);
        }
    }

    //figure 9.25 Add method, from page 217 of book.
    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Window window = find(head, key);
            Node pred = window.pred, curr = window.curr;
            if (curr.key == key) {
                return false;
            } else {
                Node node = new Node(item, item.hashCode(), new AtomicMarkableReference<Node>(null, false));
                node.next = new AtomicMarkableReference(curr, false);
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    //figure 9.26 Remove Method, from page 217 of book.
    //casting getreference to Node to remove errors.
    public boolean remove(T item) {
        int key = item.hashCode();
        boolean snip;
        while (true) {
            Window window = find(head, key);
            Node pred = window.pred, curr = window.curr;
            if (curr.key != key) {
                return false;
            }
            else {
                Node succ = (Node)curr.next.getReference();
                snip = curr.next.compareAndSet(succ, succ, false, true);
                if (!snip)
                    continue;
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }

    //contains method based on page 218, figure 9.27
    //modified: since we're able to use AMR's in our node class instead of
    //basic node/next references, we don't need to use a marked[] array, we use our amrs.
    //there's also no need to re-write code.. not sure why the book doesn't use find here,
    //also,
    public boolean contains(T item) {
        int key = item.hashCode();
        Window containsWindow = find(head, key);
        //if the current key from our find call is equal to our items key, item is present.
        if(containsWindow.curr.key == key) return true;
        else return false;
    }


    //find method from page 216 figure 9.24
    //referenced by the add and remove methods, therefore adding it.
    //this method does NOT go in the window class because it's referenced directly by add/remove.
    //casting calls to get/getReference to Node to remove errors.
    public Window find(Node head, int key) {
        Node pred = null, curr = null, succ = null;
        boolean[] marked = {false};
        boolean snip;
        retry: while (true) {
            pred = head;
            curr = (Node) pred.next.getReference();
            while (true) {
                succ = (Node) curr.next.get(marked);
                while (marked[0]) {
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) continue retry;
                        curr = succ;
                        succ = (Node) curr.next.get(marked);
                    }
                    if (curr.key >= key)
                        return new Window(pred, curr);
                pred = curr;
                curr = succ;
            }
        }
    }



}