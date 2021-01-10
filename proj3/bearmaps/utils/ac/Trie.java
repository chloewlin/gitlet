package bearmaps.utils.ac;

import bearmaps.AugmentedStreetMapGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private Node root;

    private class Node {
        private List<Map<String, Object>> locations;
        private HashMap<Character, Node> map;

        public Node (boolean a) {
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

        return !curr.locations.isEmpty();
    }

    public void add(String key, HashMap<String, Object> location) {
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);

            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(false));
            }
            curr = curr.map.get(c);
        }
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
        for (Map<String, Object> location : curr.locations) {
            list.add((String)location.get("name"));
        }

        curr.map.forEach((character, node) -> {
            keysWithPrefixHelper(list, node);
        });
    }

    public List<Map<String, Object>> exactMatches(String word) {
        Node curr = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (!curr.map.containsKey(c)) {
                return new ArrayList<>();
            }
            curr = curr.map.get(c);
        }

        return curr.locations;
    }
}
