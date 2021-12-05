import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @param <V>   {@inheritDoc}
 * @param <Key> {@inheritDoc}
 */
public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    /**
     * {@inheritDoc}
     */
    
    ArrayList<Entry<Key, V>> arrList;
    HashMap<V, Integer> valIndexHashMap;
    HashMap<V, Key> revHashMap;
    
    public BinaryMinHeapImpl() {
        arrList = new ArrayList<Entry<Key, V>>();
        valIndexHashMap = new HashMap<V, Integer>();
        revHashMap = new HashMap<V, Key>();
    }
    
    @Override
    public int size() {
        return arrList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrList.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(V value) {
        return revHashMap.containsKey(value);
    }
    
    void minHeapify(int index) {
        int l = 2 * index + 1;
        int r = 2 * index + 2;
        int smallest = Integer.MAX_VALUE;
        if (l < arrList.size() && arrList.get(l).key.compareTo(arrList.get(index).key) < 0) {
            smallest = l;
        } else {
            smallest = index;
        }
        if (r < arrList.size() && arrList.get(r).key.compareTo(arrList.get(smallest).key) < 0) {
            smallest = r;
        }
        if (smallest != index) {
            Key k = arrList.get(index).key;
            Key k2 = arrList.get(smallest).key;
            V v = arrList.get(index).value;
            V v2 = arrList.get(smallest).value;
            
            arrList.set(smallest, new BinaryMinHeap.Entry<Key, V>(k, v));
            arrList.set(index, new BinaryMinHeap.Entry<Key, V>(k2, v2));
            
            valIndexHashMap.put(v, smallest);
            valIndexHashMap.put(v2, index);
            
            minHeapify(smallest);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Key key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }
        if (revHashMap.containsKey(value)) {
            throw new IllegalArgumentException("value already in heap");
        }
        revHashMap.put(value, key);
        arrList.add(new BinaryMinHeap.Entry<Key, V>(key, value));
        valIndexHashMap.put(value, arrList.size() - 1);
        addHelper(arrList.size() - 1);
    }
    
    void addHelper(int i) {
        while (i > 0) {
            if (arrList.get((i - 1) / 2).key.compareTo(arrList.get(i).key) > 0) {
                Key k = arrList.get(i).key;
                Key k2 = arrList.get((i - 1) / 2).key;
                V v = arrList.get(i).value;
                V v2 = arrList.get((i - 1) / 2).value;
                
                arrList.set((i - 1) / 2, new BinaryMinHeap.Entry<Key, V>(k, v));
                arrList.set(i, new BinaryMinHeap.Entry<Key, V>(k2, v2));
                
                valIndexHashMap.put(v, (i - 1) / 2);
                valIndexHashMap.put(v2, i);
                
                i = (i - 1) / 2;
            } else {
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseKey(V value, Key newKey) {
        if (!revHashMap.containsKey(value)) {
            throw new NoSuchElementException("value does not exist");
        }
        if (newKey == null) {
            throw new IllegalArgumentException("null key");
        }
        Key oldKey = revHashMap.get(value);
        if (newKey.compareTo(oldKey) > 0) {
            throw new IllegalArgumentException("larger new key");
        }
        revHashMap.replace(value, newKey);
        int index = valIndexHashMap.get(value);
        arrList.set(index, new BinaryMinHeap.Entry<Key, V>(newKey, value));
        addHelper(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> peek() {
        if (arrList.isEmpty()) {
            throw new NoSuchElementException("empty heap");
        }
        return arrList.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> extractMin() {
        if (arrList.isEmpty()) {
            throw new NoSuchElementException("empty heap");
        }
           
        Key k = arrList.get(0).key;
        Key k2 = arrList.get(arrList.size() - 1).key;
        V v = arrList.get(0).value;
        V v2 = arrList.get(arrList.size() - 1).value;
        
        arrList.set(arrList.size() - 1, new BinaryMinHeap.Entry<Key, V>(k, v));
        arrList.set(0, new BinaryMinHeap.Entry<Key, V>(k2, v2));
        
        valIndexHashMap.put(v, arrList.size() - 1);
        valIndexHashMap.put(v2, 0);
        
        BinaryMinHeap.Entry<Key, V> min = arrList.remove(arrList.size() - 1);
        revHashMap.remove(min.value);
        valIndexHashMap.remove(min.value);
        minHeapify(0);
        return min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> values() {
        return revHashMap.keySet();
    }
}