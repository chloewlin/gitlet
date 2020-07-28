import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HashMap<K, V> implements Map61BL<K, V> {
    /* TODO: Instance variables here */
    private List<HashMap.Entry<K, V>>[] map = new List[26];
    private List<HashMap.Entry<K, V>>[] resizeMap = new List[map.length * 2];
    private Integer count;

//    public int hash(String key) {
//        return key.charAt(0) - 'A';
//    }

    public HashMap() {
        this.count = 0;
        for (int i = 0; i < this.map.length; i++){
            this.map[i] = new LinkedList<>();
        }
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return this.count;
    }

    public int getIndex(K key) {
        return Math.floorMod(key.hashCode(), this.map.length);
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        int index = getIndex(key);
        List<HashMap.Entry<K, V>> linkedList = this.map[index];

        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        int index = getIndex(key);
        List<HashMap.Entry<K, V>> linkedList = this.map[index];

        for (HashMap.Entry<K, V> entry : linkedList) {
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
    public void put(K key, V value) {
        if (count / this.map.length < this.map.length) {
            int index = getIndex(key);
            List<HashMap.Entry<K, V>> linkedList = this.map[index];
            linkedList.add(new HashMap.Entry<K, V>(key, value));
        } else {
            resize();
            int index = getIndex(key);
            List<HashMap.Entry<K, V>> linkedList = this.map[index];
            linkedList.add(new HashMap.Entry<K, V>(key, value));
        }
        count++;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    @Override
    public V remove(K key) {
        int index = getIndex(key);
        List<HashMap.Entry<K, V>> linkedList = this.map[index];

        V removedValue = null;

        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                removedValue = entry.value;
                linkedList.remove(entry);
            }
        }

        this.count--;
        return removedValue;
    }

    @Override
    public boolean remove(K key, V value) {
        int index = getIndex(key);
        List<HashMap.Entry<K, V>> linkedList = this.map[index];


        for (HashMap.Entry<K, V> entry : linkedList) {
            if (entry.key.equals(key)) {
                this.count--;
                linkedList.remove(entry);
                return true;
            }
        }

        return false;
    }

    public void clear() {

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
