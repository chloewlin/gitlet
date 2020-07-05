public class LinkedListDeque<T> implements Deque<T> {

    private static class Node<T> {
        public T item;
        public Node prev;
        public Node next;

        public Node(T item,  Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinel;
    private int size;

    /** Constructor which creates an empty linked list deque */
    public LinkedListDeque() {
        this.sentinel = new Node(42, null, null);
        this.sentinel.prev = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    public LinkedListDeque(T item) {
        this.sentinel = new Node(42, null, null);
        Node newNode = new Node(item, this.sentinel, this.sentinel);
        this.sentinel.next = newNode;
        this.sentinel.prev = newNode;
        this.size++;
    }

    @Override
    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        Node first = new Node(item, this.sentinel, null);
        if (this.size == 0) {
            first.next = this.sentinel;
            this.sentinel.prev = first;
        } else {
            first.next = this.sentinel.next;
            this.sentinel.next.prev = first;
        }
        this.sentinel.next = first;
        this.size++;
    }

    @Override
    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        Node last = new Node(item, null, null);
        if (this.size == 0) {
            last.prev = this.sentinel;
            last.next = this.sentinel;
            this.sentinel.prev = last;
            this.sentinel.next = last;
        } else {
            last.next = this.sentinel;
            last.prev = this.sentinel.prev;
            this.sentinel.prev.next = last;
            this.sentinel.prev = last;
        }
        this.size++;
    }

    @Override
    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        Node node = sentinel.next;
        while (!node.equals(sentinel)) {
            System.out.print(node.item + " ");
            node = node.next;
        }
        System.out.println("");
    }

    @Override
    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     */
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        Node oldHead = this.sentinel.next;
        this.sentinel.next = oldHead.next;
        Node newHead = this.sentinel.next;
        newHead.prev = this.sentinel;
        oldHead.next = null;
        oldHead.prev = null;
        this.size--;
        return (T)oldHead.item;
    }

    @Override
    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     */
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        Node oldTail = this.sentinel.prev;
        this.sentinel.prev = oldTail.prev;
        Node newTail = this.sentinel.prev;
        newTail.next = this.sentinel;
        oldTail.next = null;
        oldTail.prev = null;
        this.size--;
        return (T)oldTail.item;
    }

    @Override
    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque
     */
    public T get(int index) {
        if (this.isEmpty() || (index > this.size - 1) || (index < 0)) {
            return null;
        }
        int count = 0;
        Node curr = this.sentinel.next;
        while (count != index) {
            curr = curr.next;
            count++;
        }
        return (T)curr.item;
    }


    /**
     * Same as get, but this method should be implemented using recursion
     */
    public T getRecursive(int index) {
        if (this.isEmpty() || (index > this.size - 1) || (index < 0)) {
            return null;
        } else {
            return getRecursiveHelper(this.sentinel.next, index);
        }
    }

    private T getRecursiveHelper(Node node, int count) {
        if (count == 0) {
            return (T)node.item;
        } else {
            return getRecursiveHelper(node.next, --count);
        }
    }
}
