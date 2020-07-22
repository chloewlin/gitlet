public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }

        if (r.getItemCount() == 1) {
            RBTreeNode tree = new RBTreeNode(true, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                tree.left = buildRedBlackTree(r.getChildAt(0));
                tree.right = buildRedBlackTree(r.getChildAt(1));
            }
            return tree;
        } else if (r.getItemCount() == 2) {
            RBTreeNode tree = new RBTreeNode(true, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                tree.left = buildRedBlackTree(r.getChildAt(0));
                tree.right = new RBTreeNode(false, r.getItemAt(1),
                        buildRedBlackTree(r.getChildAt(1)),
                        buildRedBlackTree(r.getChildAt(2)));
            }
            return tree;
        } else {
            RBTreeNode tree = new RBTreeNode(true, r.getItemAt(1));
            if (r.getChildrenCount() != 0) {
                tree.left = new RBTreeNode(false, r.getItemAt(0),
                    buildRedBlackTree(r.getChildAt(0)),
                    buildRedBlackTree(r.getChildAt(1)));
                tree.right = new RBTreeNode(false, r.getItemAt(2),
                    buildRedBlackTree(r.getChildAt(2)),
                    buildRedBlackTree(r.getChildAt(3)));
            }
            return tree;
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> rightTree =
                new RBTreeNode(false, node.item, node.left.right, node.right);
        return new RBTreeNode<T>(node.isBlack, node.left.item, node.left.left, rightTree);
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> leftTree =
                new RBTreeNode(false, node.item, node.left, node.right.left);
        return new RBTreeNode<T>(node.isBlack, node.right.item, leftTree, node.right.right);
    }

    public void insert(T item) {   
        root = insert(root, item);  
        root.isBlack = true;    
    }

    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        /* If no node, insert a new child node */
        if (node == null) {
            node = new RBTreeNode<>(false, item);
        }
        /* Insert into BST */
        if (item.compareTo(node.item) == 0) {
            return node;
        } else if (item.compareTo(node.item) > 1) {
            node.right = insert(node.right, item);
        } else {
            node.left = insert(node.left, item);
        }

        /* Edge case: right leaning tree */
        if (!isRed(node.left) && isRed(node.right)) {
            node = rotateLeft(node);
        }

        /* Edge case: overflow */
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        /* Edge case: a black node has a red left node which also has red left node */
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }

        return node;
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
