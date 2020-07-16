public class BinaryTree<T> {

    TreeNode<T> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode<T> t) {
        root = t;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /* Returns the height of the tree. */
    public int height() {
        if (root != null) {
            return root.height(root);
        }
        return 0;
    }

    /* Returns true if the tree's left and right children are the same height
       and are themselves completely balanced. */
    public boolean isCompletelyBalanced() {
        if (root != null) {
            return root.isCompletelyBalanced();
        }
        return true;
    }

    /* Returns a BinaryTree representing the Fibonacci calculation for N. */
    public static BinaryTree<Integer> fibTree(int N) {
        BinaryTree<Integer> result = new BinaryTree<Integer>();
        if (N <= 1) {
            result.root = new TreeNode<>(N);
        } else {
            TreeNode left = fibTree(N-1).root;
            TreeNode right = fibTree(N-2).root;
            result.root = new TreeNode<>((int)left.item + (int)right.item);
        }
        return result;
    }

    /* Print the values in the tree in preorder: root value first, then values
       in the left subtree (in preorder), then values in the right subtree
       (in preorder). */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /* Print the values in the tree in inorder: values in the left subtree
       first (in inorder), then the root value, then values in the first
       subtree (in inorder). */
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /* Prints out the contents of a BinaryTree with a description in both
       preorder and inorder. */
    private static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    /* Fills this BinaryTree with values a, b, and c. DO NOT MODIFY. */
    public void sampleTree1() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c"));
    }

    /* Fills this BinaryTree with values a, b, and c, d, e, f. DO NOT MODIFY. */
    public void sampleTree2() {
        root = new TreeNode("a",
                  new TreeNode("b", new TreeNode("d", new TreeNode("e"),
                  new TreeNode("f")), null), new TreeNode("c"));
    }

    /* Fills this BinaryTree with the values a, b, c, d, e, f. DO NOT MODIFY. */
    public void sampleTree3() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c",
               new TreeNode("d", new TreeNode("e"), new TreeNode("f")), null));
    }

    /* Fills this BinaryTree with the same leaf TreeNode. DO NOT MODIFY. */
    public void sampleTree4() {
        TreeNode leafNode = new TreeNode("c");
        root = new TreeNode("a", new TreeNode("b", leafNode, leafNode),
                                 new TreeNode("d", leafNode, leafNode));
    }

    /* Creates two BinaryTrees and prints them out in inorder. */
    public static void main(String[] args) {
        BinaryTree t;
        t = new BinaryTree();
        print(t, "the empty tree");
        System.out.println(t.height());
        t.sampleTree1();
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());
        print(t, "sample tree 1");
        t.sampleTree2();
        print(t, "sample tree 2");
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());
    }

    /* Note: this class is public in this lab for testing purposes. However,
       in professional settings as well as the rest of your labs and projects,
       we recommend that you keep your inner classes private. */
    static class TreeNode<T> {

        private T item;
        private TreeNode left;
        private TreeNode right;

        TreeNode(T obj) {
            item = obj;
            left = null;
            right = null;
        }

        TreeNode(T obj, TreeNode<T> left, TreeNode<T> right) {
            item = obj;
            this.left = left;
            this.right = right;
        }

        public T getItem() {
            return item;
        }

        public TreeNode<T> getLeft() {
            return left;
        }

        public TreeNode<T> getRight() {
            return right;
        }

        void setItem(T item) {
            this.item = item;
        }

        void setLeft(TreeNode<T> left) {
            this.left = left;
        }

        void setRight(TreeNode<T> right) {
            this.right = right;
        }

        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        /** height helper function */
        private int height(TreeNode node) {
            if (node == null) return 0;
            return Math.max(height(node.left) + 1, height(node.right) + 1);
        }

        //  Another implementation (Use return root.height(); on line 20)
        //  private int height() {
        //      if (left == null & right == null) {
        //          return 1;
        //      } else if (left == null) {
        //          return 1 + right.height();
        //      } else if (right == null) {
        //           return 1 + left.height();
        //      }
        //      return 1 + Math.max(left.height(), right.height());
        //  }

        /** isCompletelyBalanced helper function */
        private boolean isCompletelyBalanced() {
            if (left.height(left) == 1 && right.height(left) == 1) {
                return true;
            }
            return left.height(left) == right.height(left);
        }
    }
}
