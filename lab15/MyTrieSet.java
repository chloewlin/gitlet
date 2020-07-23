import java.util.List;

public class MyTrieSet implements TrieSet61BL{
    @Override
    public void clear() {

    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override public void add(String key) {
        if (key == null || key.length() < 1) {
            return; }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        return null;
    }

    @Override
    public String longestPrefixOf(String key) throws UnsupportedOperationException {
        return null;
    }
}
