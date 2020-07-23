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
            RBTreeNode tree = new RBTreeNode<T>(true, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                tree.left = buildRedBlackTree(r.getChildAt(0));
                tree.right = buildRedBlackTree(r.getChildAt(1));
            }
            return tree;
        } else if (r.getItemCount() == 2) {
            RBTreeNode tree = new RBTreeNode<T>(true, r.getItemAt(1));
            tree.left =  new RBTreeNode<T>(false, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                tree.left.left = buildRedBlackTree(r.getChildAt(0));
                tree.left.right = buildRedBlackTree(r.getChildAt(1));
                tree.right = buildRedBlackTree(r.getChildAt(2));
            }
            return tree;
        } else {
            RBTreeNode tree = new RBTreeNode<T>(true, r.getItemAt(1));
            tree.left = new RBTreeNode<T>(false, r.getItemAt(0));
            tree.right =  new RBTreeNode<T>(false, r.getItemAt(2));
            if (r.getChildrenCount() != 0) {
                tree.left.left = buildRedBlackTree(r.getChildAt(0));
                tree.left.right = buildRedBlackTree(r.getChildAt(1));
                tree.right.left = buildRedBlackTree(r.getChildAt(2));
                tree.right.right = buildRedBlackTree(r.getChildAt(3));
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
                new RBTreeNode<T>(false, node.item, node.left.right, node.right);
        return new RBTreeNode<T>(node.isBlack, node.left.item, node.left.left, rightTree);
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> leftTree =
                new RBTreeNode<T>(false, node.item, node.left, node.right.left);
        return new RBTreeNode<T>(node.isBlack, node.right.item, leftTree, node.right.right);
    }

    public void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        /* If no node, insert a new child node */
        if (node == null) {
            node = new RBTreeNode<T>(false, item, null, null);
        }
        /* Insert into BST */
        if (item.compareTo(node.item) == 0) {
            return node;
        } else if (item.compareTo(node.item) < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }

        /* Case 1 */
        if (node.left == null && isRed(node.right)) {
            node = rotateLeft(node);
        } else if (isRed(node.left) && isRed(node.right)) {
            /* Cast 2.a */
            flipColors(node);
        } else if (isRed(node.left) && isRed(node.left.left)) {
            /* Cast 2.b */
            node = rotateRight(node);
            flipColors(node);
        } else if (isRed(node.left) && isRed(node.left.right)) {
            /* Cast 2.c */
            node.left = rotateLeft(node.left);
            node = rotateRight(node);
            flipColors(node);
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

    private static String isBlackNode(boolean isBlack) {
        if (isBlack) return "black";
        else return "red";
    }

    private void print() {
        print(root, 0);
    }

    public static void main(String[] args) {
//       ========== test1
//                                    WRONG             RIGHT (left leaning)
//               (5, 9)                5                 9
//            /    |   \      --->   /   \              / \
//            4    6    10          4     9*          5*   10
//                                       / \         / \
//                                      6  10       4   6
//        BTree<Integer> bt1 = new BTree<>();
//        BTree.TwoThreeFourNode<Integer> t = new BTree.TwoThreeFourNode<>(5, 9);
//        BTree.TwoThreeFourNode<Integer> t1 = new BTree.TwoThreeFourNode<>(4);
//        BTree.TwoThreeFourNode<Integer> t2 = new BTree.TwoThreeFourNode<>(6);
//        BTree.TwoThreeFourNode<Integer> t3 = new BTree.TwoThreeFourNode<>(10);
//
//        bt1.root = t;
//        t.setChildAt(0, t1);
//        t.setChildAt(1, t2);
//        t.setChildAt(2, t3);
//        RedBlackTree<Integer> rbt1 = new RedBlackTree<Integer>(bt1);
//        rbt1.print();

//        ========== test2
//                                             WRONG               RIGHT (left leaning)
//             (3, 5, 8)                        5                    5
//          /    |  |   \           --->       /  \                 /  \
//        (1,2)  4  6    (10, 16)             3*    8*            3*    8*
//                                           /\      /\          / \    / \
//                                          1  4    6  10       2   4  6   16
//                                           \          \       /          /
//                                            2*        16*    1*         10
//
//        BTree<Integer> bt2 = new BTree<>();
//        BTree.TwoThreeFourNode<Integer> t4 = new BTree.TwoThreeFourNode<>(3, 5, 8);
//        BTree.TwoThreeFourNode<Integer> t5 = new BTree.TwoThreeFourNode<>(1, 2);
//        BTree.TwoThreeFourNode<Integer> t6 = new BTree.TwoThreeFourNode<>(4);
//        BTree.TwoThreeFourNode<Integer> t7 = new BTree.TwoThreeFourNode<>(6);
//        BTree.TwoThreeFourNode<Integer> t8 = new BTree.TwoThreeFourNode<>(10, 16);
//
//        bt2.root = t4;
//        t4.setChildAt(0, t5);
//        t4.setChildAt(1, t6);
//        t4.setChildAt(2, t7);
//        t4.setChildAt(3, t8);
//        RedBlackTree<Integer> rbt2 = new RedBlackTree<Integer>(bt2);
//        rbt2.print();


//        ========== test2
//
//             (      12      )                                         12
//          /                 \                          --->         /      \
//       (4,8)             (16, 24, 28)                              8         24
//      /   |  \       /   |         \   \                          /\        /   \
//   (0,2)  6  10   14  (18,20,22)   26   (30,32)                  2  4*     16*    28*
//                                                               /    /\     /\    /   \
//                                                              0*   6 10  14 20  26   32
//                                                                            /\        /
//                                                                          18* 22*     30*
//        BTree<Integer> bt = new BTree<>();
//        BTree.TwoThreeFourNode<Integer> ttf = new BTree.TwoThreeFourNode<>(12);
//        BTree.TwoThreeFourNode<Integer> ttf1 = new BTree.TwoThreeFourNode<>(4, 8);
//        BTree.TwoThreeFourNode<Integer> ttf2 = new BTree.TwoThreeFourNode<>(16, 24, 28);
//        BTree.TwoThreeFourNode<Integer> ttf3 = new BTree.TwoThreeFourNode<>(0, 2);
//        BTree.TwoThreeFourNode<Integer> ttf4 = new BTree.TwoThreeFourNode<>(6);
//        BTree.TwoThreeFourNode<Integer> ttf5 = new BTree.TwoThreeFourNode<>(10);
//        BTree.TwoThreeFourNode<Integer> ttf6 = new BTree.TwoThreeFourNode<>(14);
//        BTree.TwoThreeFourNode<Integer> ttf7 = new BTree.TwoThreeFourNode<>(18, 20, 22);
//        BTree.TwoThreeFourNode<Integer> ttf8 = new BTree.TwoThreeFourNode<>(26);
//        BTree.TwoThreeFourNode<Integer> ttf9 = new BTree.TwoThreeFourNode<>(30, 32);
//        ttf.setChildAt(0, ttf1);
//        ttf.setChildAt(1, ttf2);
//
//        ttf1.setChildAt(2, ttf5);
//        ttf1.setChildAt(0, ttf3);
//        ttf1.setChildAt(1, ttf4);
//
//        ttf2.setChildAt(0, ttf6);
//        ttf2.setChildAt(1, ttf7);
//        ttf2.setChildAt(2, ttf8);
//        ttf2.setChildAt(3, ttf9);
//        bt.root = ttf;
//        RedBlackTree<Integer> rbt = new RedBlackTree<>(bt);
//        rbt.print();

//        BTree Btree = new BTree();
//        BTree.TwoThreeFourNode n1 = new BTree.TwoThreeFourNode(3, 5, 8);
//        BTree.TwoThreeFourNode n2 = new BTree.TwoThreeFourNode(1, 2);
//        BTree.TwoThreeFourNode n3 = new BTree.TwoThreeFourNode(4);
//        BTree.TwoThreeFourNode n4 = new BTree.TwoThreeFourNode(6);
//        BTree.TwoThreeFourNode n5 = new BTree.TwoThreeFourNode(10, 16);
//
//        Btree.root = n1;
//        n1.setChildAt(0, n2);
//        n1.setChildAt(1, n3);
//        n1.setChildAt(2, n4);
//        n1.setChildAt(3, n5);
//
//        RedBlackTree RBTree = new RedBlackTree(Btree);
//        print(RBTree.root, 0);
//
//        System.out.println("======");
//        BTree Btree2 = new BTree();
//        Btree2.root = new BTree.TwoThreeFourNode(3);
//
//        RedBlackTree RB1 = new RedBlackTree(Btree2);
//        RB1.insert(RB1.root,5);
//        print(RB1.root,0);
//
//        System.out.println("======");
//        RB1.insert(RB1.root,10);
//        print(RB1.root,0);

        BTree Btree = new BTree();
        BTree.TwoThreeFourNode n1 = new BTree.TwoThreeFourNode(3, 5, 8);
        BTree.TwoThreeFourNode n2 = new BTree.TwoThreeFourNode(1, 2);
        BTree.TwoThreeFourNode n3 = new BTree.TwoThreeFourNode(4);
        BTree.TwoThreeFourNode n4 = new BTree.TwoThreeFourNode(6);
        BTree.TwoThreeFourNode n5 = new BTree.TwoThreeFourNode(10, 16);

        Btree.root = n1;
        n1.setChildAt(0, n2);
        n1.setChildAt(1, n3);
        n1.setChildAt(2, n4);
        n1.setChildAt(3, n5);

        RedBlackTree RBTree = new RedBlackTree(Btree);
        RBTree.insert(0);
        RBTree.insert(30);
        RBTree.insert(7);
        RBTree.insert(9);
        RBTree.insert(13);
        RBTree.print();
    }

    private void print(RBTreeNode<T> node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("   ");
        }
        if (isRed(node)) {
            System.out.println(node.item + " red"); //* means red
        } else {
            System.out.println(node.item + " ");
        }
        print(node.left, d + 1);
        print(node.right, d + 1);
    }
}
