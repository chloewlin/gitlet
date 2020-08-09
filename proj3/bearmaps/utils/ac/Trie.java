package bearmaps.utils.ac;

import bearmaps.AugmentedStreetMapGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private Node root;

    private class Node {
        private boolean isKey;
        private String name;
        private List<Map<String, Object>> locations;
        private HashMap<Character, Node> map;

        public Node (boolean a) {
            this.isKey = a;
            locations = new ArrayList<Map<String, Object>>();
            map = new HashMap<>();
        }
    }

    public Trie() {
        this.root = new Node(true);
    }


    public void clear() {
        this.root = new Node(true);
    }

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

    public void add(String key, String name, HashMap<String, Object> location) {
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
        curr.name = name;
        curr.locations.add(location);
    }

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

        keysWithPrefixHelper(list, curr);
        return list;
    }

    public void keysWithPrefixHelper(List<String> list, Node curr) {
        if (curr.isKey) {
            list.add(curr.name);
        }

        curr.map.forEach((character, node) -> {
            keysWithPrefixHelper(list, node);
        });
    }

    public List<Map<String, Object>> exactMatches(String word) {
        Node curr = root;
        List<Map<String, Object>> empty = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (!curr.map.containsKey(c)) {
                return empty;
            }
            curr = curr.map.get(c);
        }

        if (curr.isKey) {
            return curr.locations;
        }
        return empty;
    }
}
