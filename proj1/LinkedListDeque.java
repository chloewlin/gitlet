@SuppressWarnings("unchecked")

public class LinkedListDeque<T> implements Deque<T> {

    private static class Node<T> {
        private T item;
        private Node prev;
        private Node next;

        Node(T item,  Node prev, Node next) {
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

    /**
     * Adds an item of type T to the front of the deque.
     */
    @Override
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

    /**
     * Adds an item of type T to the back of the deque.
     */
    @Override
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

    /**
     * Returns the number of items in the deque.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        Node node = sentinel.next;
        String result = new String();
        while (!node.equals(sentinel)) {
            result += node.item + " ";
            node = node.next;
        }
        result = result.trim();
        System.out.print(result);
        System.out.println(" ");
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     */
    @Override
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
        return (T) oldHead.item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     */
    @Override
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
        return (T) oldTail.item;
    }

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. If no such item exists,
     * returns null. Must not alter the deque
     */
    @Override
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
        return (T) curr.item;
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
            return (T) node.item;
        } else {
            return getRecursiveHelper(node.next, --count);
        }
    }
}
