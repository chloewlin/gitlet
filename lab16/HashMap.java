import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {
    /* TODO: Instance variables here */
    private LinkedList<Entry<K, V>>[] map;
    private Integer count;
    private double loadFactor;

    /* Creates a new hash map with a default array of size 16 and a maximum load factor of 0.75. */
    public HashMap() {
        this(16, 0.75);
    }

    /* Creates a new hash map with an array of size INITIALCAPACITY and a maximum load factor of 0.75. */
    public HashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /* Creates a new hash map with INITIALCAPACITY and LOADFACTOR. */
    public HashMap(int initialCapacity, double loadFactor) {
        this.count = 0;
        this.map = new LinkedList[initialCapacity];
        for (int i = 0; i < this.map.length; i++){
            this.map[i] = new LinkedList<Entry<K, V>>();
        }
        this.loadFactor = loadFactor;
    }

    /* Returns the length of this HashMap's internal array. */
    public int capacity() {
        return this.map.length;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return this.count;
    }

    public int getIndex(K key, int arrayLength) {
        return Math.floorMod(key.hashCode(), arrayLength);
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        int index = getIndex(key, capacity());
        LinkedList<Entry<K, V>> linkedList = this.map[index];

        for (Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        int index = getIndex(key, capacity());
        LinkedList<HashMap.Entry<K, V>> linkedList = this.map[index];

        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public void resize() {
        int newSize = this.map.length * 2;
        LinkedList<Entry<K, V>>[] resizedMap = new LinkedList[newSize];

        for (int i = 0; i < resizedMap.length; i++){
            resizedMap[i] = new LinkedList<Entry<K, V>>();
        }

        for (int i = 0; i < this.map.length; i++){
            for (HashMap.Entry<K, V> entry : this.map[i]) {
                 int newKey = getIndex(entry.key, newSize);
                 LinkedList<Entry<K, V>> linkedList = resizedMap[newKey];
                 linkedList.add(entry);
            }
        }

        this.map = resizedMap;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {

        if (containsKey(key)) {
            int index = getIndex(key, this.map.length);
            LinkedList<Entry<K, V>> linkedList = this.map[index];
            for (HashMap.Entry<K, V> entry : linkedList) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
            }
        }

        count++;
        double currLoadFactor = (double)count / this.map.length;
        if (currLoadFactor > this.loadFactor) {
            resize();
        }

        int index = getIndex(key, capacity());
        LinkedList<Entry<K, V>> linkedList = this.map[index];
        linkedList.add(new HashMap.Entry<K, V>(key, value));
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    @Override
    public V remove(K key) {
        count--;
        int index = getIndex(key, capacity());
        LinkedList<Entry<K, V>> linkedList = this.map[index];
        V removedValue = null;

        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                removedValue = entry.value;
                linkedList.remove(entry);
            }
        }

        return removedValue;
    }

    @Override
    public boolean remove(K key, V value) {
        count--;
        int index = getIndex(key, capacity());
        LinkedList<Entry<K, V>> linkedList = this.map[index];

        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                linkedList.remove(entry);
                return true;
            }
        }

        return false;
    }

    public void clear() {
        this.map = new LinkedList[this.capacity()];
        for (int i = 0; i < this.map.length; i++){
            this.map[i] = new LinkedList<Entry<K, V>>();
        }
        this.count = 0;
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(HashMap.Entry<K, V> other) {
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
}
