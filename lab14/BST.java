import java.util.*;

public class BST<T> {

    BSTNode<T> root;

    public BST(LinkedList<T> list) {
        root = sortedIterToTree(list.iterator(), list.size());
    }

    /* Returns the root node of a BST (Binary Search Tree) built from the given
       iterator ITER  of N items. ITER will output the items in sorted order,
       and ITER will contain objects that will be the item of each BSTNode. */
    private BSTNode sortedIterToTree(Iterator<T> iter, int N) {
        if (N == 0) {
            return null;
        }

        List list = sortList(iter, N);
        return sortedListToBST(list, 0, N - 1);
    }

    private BSTNode<T> sortedListToBST(List<T> list, int low, int high) {
        if (low > high) {
            return null;
        }

        int mid = low + ((high - low) / 2);

        BSTNode node = new BSTNode(list.get(mid));
        node.left = sortedListToBST(list, low, mid - 1);
        node.right = sortedListToBST(list, mid + 1, high);

        return node;
    }

    private List sortList(Iterator<T> iter, int N) {
        List list = new ArrayList<T>();

        while (iter.hasNext()) {
            list.add(iter.next());
        }

        Collections.sort(list);

        return list;
    }

    /* Prints the tree represented by ROOT. */
    private void print() {
        print(root, 0);
    }

    private void print(BSTNode<T> node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    class BSTNode<T> {
        T item;
        BSTNode<T> left;
        BSTNode<T> right;

        BSTNode(T item) {
            this.item = item;
        }
    }
}
