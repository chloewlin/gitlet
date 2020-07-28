import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SimpleNameMap {

    /* TODO: Instance variables here */
    private List<Entry>[] map = new List[26];
    private List<Entry>[] resizeMap = new List[map.length * 2];
    private Integer count;

//    public int hash(String key) {
//        return key.charAt(0) - 'A';
//    }

    public SimpleNameMap() {
        this.count = 0;
        for (int i = 0; i < this.map.length; i++){
            this.map[i] = new LinkedList<>();
        }
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return this.count;
    }

    public int getIndex(String key) {
        return Math.floorMod(key.hashCode(), this.map.length);
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        int index = getIndex(key);
        List<Entry> linkedList = this.map[index];

        for (Entry entry : linkedList) {
            if (entry.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        int index = getIndex(key);
        List<Entry> linkedList = this.map[index];

        for (Entry entry : linkedList) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public void resize() {
        for (int i = 0; i < this.resizeMap.length; i++){
            this.resizeMap[i] = new LinkedList<>();
        }
        for (int i = 0; i < this.map.length; i++){
            this.resizeMap[i] = this.map[i];
        }
        this.map = this.resizeMap;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        if (containsKey(key)) {
            int index = getIndex(key);
            List<Entry> linkedList = this.map[index];

            for (Entry entry : linkedList) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
            }
        }

        if (count / this.map.length > this.map.length) {
            resize();
        }

        int index = getIndex(key);
        List<Entry> linkedList = this.map[index];
        linkedList.add(new Entry(key, value));
        count++;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        int index = getIndex(key);
        List<Entry> linkedList = this.map[index];

        String value = null;

        for (Entry entry : linkedList) {
            if (entry.key.equals(key)) {
                value = entry.value;
                linkedList.remove(entry);
            }
        }

        this.count--;
        return value;
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }


    public static void main(String[] args) {
        Entry Christal = new Entry("Christal", "Huang");
        Entry Christal2 = new Entry("Christal", "Huang");
        SimpleNameMap map = new SimpleNameMap();

        map.put("Christal", "Huang");
        map.put("Chloe", "Forrester");
        map.put("Chloe", "Lin");
        map.put("Parth", "Gupta");
        System.out.println(map.containsKey("Christal")); // true
        System.out.println(map.containsKey("Chloe")); // true
        System.out.println(map.containsKey("Cat")); // false
        System.out.println(map.size()); // 3

        System.out.println(map.remove("Chloe")); // Lin
        System.out.println(map.containsKey("Chloe")); // false
        System.out.println(map.size()); // 2
        System.out.println(map.get("Christal")); // Huang
        System.out.println(map.containsKey("Parth")); // true

    }
}
