import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet implements TrieSet61BL {
    private static final int R = 128; // ASCII
    private Node root;	// root of trie

    private class Node {
        private boolean isKey;
        private HashMap<Character, Node> map;
        public Node (boolean a) {
            this.isKey = a;
            map = new HashMap<>();
        }
    }

    public MyTrieSet() {
        this.root = new Node(true);
    }


    @Override
    public void clear() {
        this.root = new Node(true);
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);

            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }

        return curr.isKey;
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return; }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> list = new ArrayList<>();

        Node curr = root;

        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);

            if (!curr.map.containsKey(c)) {
                return new ArrayList<>();
            }
            curr = curr.map.get(c);

        }

        keysWithPrefixHelper(list, curr, prefix);
        return list;
    }

    public void keysWithPrefixHelper(List<String> list, Node curr, String word) {
        if (curr.isKey) {
            list.add(word);
        }

        curr.map.forEach((character, node) -> {
            keysWithPrefixHelper(list, node, word + character);
        });

    }

    @Override
    public String longestPrefixOf(String key) throws UnsupportedOperationException {
        return null;
    }

//    public static void main(String[] args) {
//        MyTrieSet t = new MyTrieSet();

//        t.add("hello");
//        t.add("hi");
//        t.add("help");
//        t.add("zebra");
//    }
}
