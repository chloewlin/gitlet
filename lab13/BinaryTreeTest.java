import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BinaryTreeTest {
    @Test
    public void treeFormatTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outContent));
        BinaryTree.print(x, "x");
        System.setOut(oldOut);
        assertEquals("x in preorder\nC A B E D \nx in inorder\nA B C D E \n\n".trim(),
                     outContent.toString().trim());
    }

    @Test
    public void BSTAddTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        assertTrue(x.contains("D"));
        assertEquals(false, x.contains("L"));
    }

    @Test
    public void BSTAddTest1() {
        BinarySearchTree<Integer> x = new BinarySearchTree<Integer>();
        x.add(1);
        x.add(5);
        x.add(9);
        x.add(11);
        x.add(13);
        assertTrue(x.contains(5));
        assertEquals(false, x.contains(50));
    }
}