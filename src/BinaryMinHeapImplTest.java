import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class BinaryMinHeapImplTest {
    
    BinaryMinHeapImpl<Integer, Integer> empty;
    
    @Before
    public void setUp() {
        empty = new BinaryMinHeapImpl<>();
    }

    @Test
    public void sizeTest() {
        assertEquals(0, empty.size());
        empty.add(0, 1);
        assertEquals(1, empty.size());
    }
    
    @Test
    public void isEmptyTest() {
        assertTrue(empty.isEmpty());
        empty.add(0, 1);
        assertFalse(empty.isEmpty());
    }
    
    @Test
    public void containsValueTest() {
        assertFalse(empty.containsValue(1));
        empty.add(0, 1);
        assertFalse(empty.containsValue(0));
        assertTrue(empty.containsValue(1));
        empty.add(0, 3);
        empty.add(1, 2);
        assertTrue(empty.containsValue(3));
        assertTrue(empty.containsValue(2));
    }
    
    @Test
    public void addToEmptyTest() {
        empty.add(0, 1);
        assertEquals(1, empty.arrList.size());
        assertTrue(empty.containsValue(1));
        assertTrue(empty.revHashMap.containsKey(1));
        assertTrue(empty.revHashMap.containsValue(0));
        assertTrue(empty.valIndexHashMap.containsKey(1));
        assertTrue(empty.valIndexHashMap.containsValue(0));
    }
    
    @Test
    public void addToNotEmptyTest() {
        empty.add(30, 0);
        empty.add(5, 1);
        empty.add(2, 2);
        empty.add(1, 3);
        empty.add(2, 4);
        
        assertEquals(Integer.valueOf(1), empty.arrList.get(0).key);
        assertEquals(Integer.valueOf(2), empty.arrList.get(1).key);
        assertEquals(Integer.valueOf(5), empty.arrList.get(2).key);
        assertEquals(Integer.valueOf(30), empty.arrList.get(3).key);
        assertEquals(Integer.valueOf(2), empty.arrList.get(4).key);
        
        assertEquals(Integer.valueOf(30), empty.revHashMap.get(0));
        assertEquals(Integer.valueOf(5), empty.revHashMap.get(1));
        assertEquals(Integer.valueOf(2), empty.revHashMap.get(2));
        assertEquals(Integer.valueOf(1), empty.revHashMap.get(3));
        assertEquals(Integer.valueOf(2), empty.revHashMap.get(4));
        
        assertEquals(Integer.valueOf(3), empty.valIndexHashMap.get(0));
        assertEquals(Integer.valueOf(2), empty.valIndexHashMap.get(1));
        assertEquals(Integer.valueOf(1), empty.valIndexHashMap.get(2));
        assertEquals(Integer.valueOf(0), empty.valIndexHashMap.get(3));
        assertEquals(Integer.valueOf(4), empty.valIndexHashMap.get(4));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void addNullKeyTest() {
        empty.add(null, 0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void addDupeValueTest() {
        empty.add(1, 5);
        empty.add(2, 5);
    }
    
    @Test
    public void decreaseKeySingleTest() {
        empty.add(100, 1);
        empty.decreaseKey(1, 2);
        assertEquals(Integer.valueOf(2), empty.arrList.get(0).key);
    }
    
    @Test
    public void decreaseKeyMultiTest() {
        empty.add(30, 0);
        empty.add(5, 1);
        empty.add(2, 2);
        empty.add(1, 3);
        empty.add(2, 4);
        
        empty.decreaseKey(0, 0);
        
        assertEquals(Integer.valueOf(0), empty.arrList.get(0).key);
        assertEquals(Integer.valueOf(1), empty.arrList.get(1).key);
        assertEquals(Integer.valueOf(5), empty.arrList.get(2).key);
        assertEquals(Integer.valueOf(2), empty.arrList.get(3).key);
        assertEquals(Integer.valueOf(2), empty.arrList.get(4).key);
        
        assertEquals(Integer.valueOf(0), empty.revHashMap.get(0));
        assertEquals(Integer.valueOf(5), empty.revHashMap.get(1));
        assertEquals(Integer.valueOf(2), empty.revHashMap.get(2));
        assertEquals(Integer.valueOf(1), empty.revHashMap.get(3));
        assertEquals(Integer.valueOf(2), empty.revHashMap.get(4));
        
        assertEquals(Integer.valueOf(0), empty.valIndexHashMap.get(0));
        assertEquals(Integer.valueOf(2), empty.valIndexHashMap.get(1));
        assertEquals(Integer.valueOf(3), empty.valIndexHashMap.get(2));
        assertEquals(Integer.valueOf(1), empty.valIndexHashMap.get(3));
        assertEquals(Integer.valueOf(4), empty.valIndexHashMap.get(4));
    }
    
    @Test (expected = NoSuchElementException.class)
    public void invalidKeyDecreaseKeyTest() {
        empty.add(0, 1);
        empty.decreaseKey(0, 2);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullKeyDecreaseKeyTest() {
        empty.add(0, 1);
        empty.decreaseKey(1, null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void largerKeyDecreaseKeyTest() {
        empty.add(0, 1);
        empty.decreaseKey(1, 2);
    }
    
    @Test
    public void peekTest() {
        empty.add(30, 0);
        empty.add(5, 1);
        empty.add(2, 2);
        empty.add(1, 3);
        empty.add(2, 4);
        
        assertEquals(Integer.valueOf(1), empty.peek().key);
        assertEquals(Integer.valueOf(3), empty.peek().value);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void emptyPeekTest() {
        empty.peek();
    }
    
    @Test 
    public void extractMinSingleTest() {
        empty.add(30, 0);
        BinaryMinHeap.Entry<Integer, Integer> asd = empty.extractMin();
        assertEquals(Integer.valueOf(0), asd.value);
        assertEquals(Integer.valueOf(30), asd.key);
        assertEquals(0, empty.size());
    }
    
    @Test
    public void extractMinMultiTest() {
        empty.add(30, 0);
        empty.add(5, 1);
        empty.add(2, 2);
        empty.add(1, 3);
        empty.add(2, 4);
        
        empty.extractMin();
        
        assertEquals(Integer.valueOf(2), empty.peek().key);
        assertEquals(Integer.valueOf(4), empty.peek().value);
        
        empty.extractMin();
        
        assertEquals(Integer.valueOf(2), empty.peek().key);
        assertEquals(Integer.valueOf(2), empty.peek().value);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void extractMinEmptyTest() {
        empty.extractMin();
    }
    
    @Test
    public void valuesTest() {
        empty.add(30, 0);
        empty.add(5, 1);
        empty.add(2, 2);
        empty.add(1, 3);
        empty.add(2, 4);
        
        assertTrue(empty.values().contains(0));
        assertTrue(empty.values().contains(1));
        assertTrue(empty.values().contains(2));
        assertTrue(empty.values().contains(3));
        assertTrue(empty.values().contains(4));
        assertFalse(empty.values().contains(30));
    }
}
