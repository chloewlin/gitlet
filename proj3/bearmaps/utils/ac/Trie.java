package bearmaps.utils.ac;

import bearmaps.AugmentedStreetMapGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    private Node root;

    private class Node {
        private boolean isKey;
        private String name;
        private HashMap<Character, Node> map;

        public Node (boolean a) {
            this.isKey = a;
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
//        System.out.println("inside contains..." + key);
//
//        key = key.toLowerCase();

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

    public void add(String key, String name) {
        if (key == null || key.length() < 1) {
            return; }
//
//        key = key.toLowerCase();

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

        keysWithPrefixHelper(list, curr, prefix);
        return list;
    }

    public void keysWithPrefixHelper(List<String> list, Node curr, String word) {
        if (curr.isKey) {
            list.add(curr.name);
        }

        curr.map.forEach((character, node) -> {
            keysWithPrefixHelper(list, node, word + character);
        });
    }
}
