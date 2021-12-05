import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class HuffmanTest {

    private Huffman huffman;
    private HashMap<Character, Integer> asd;
    
    @Before
    public void setUp() {
        asd = new HashMap<>();
    }
    
    @Test
    public void constructorTest() {
        huffman = new Huffman("asaasd");
        assertEquals('\u0000', huffman.priorityQueue.arrList.get(0).value.getVal().charValue());
        assertEquals('a', huffman.priorityQueue.arrList.get(0).value.getLeft().getVal().
                charValue());
        assertEquals('\u0000', huffman.priorityQueue.arrList.get(0).value.getRight().getVal().
                charValue());
        assertEquals('d', huffman.priorityQueue.arrList.get(0).value.getRight().getLeft().getVal().
                charValue());
        assertEquals('s', huffman.priorityQueue.arrList.get(0).value.getRight().getRight().getVal().
                charValue());
    }

    @Test (expected = IllegalArgumentException.class)
    public void nullStringConstructorTest() {
        huffman = new Huffman((String) null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void emptyStringConstructorTest() {
        huffman = new Huffman("");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void singletonStringConstructorTest() {
        huffman = new Huffman("a");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullMapConstructorTest() {
        huffman = new Huffman((HashMap<Character, Integer>) null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void emptyMapConstructorTest() {
        huffman = new Huffman(asd);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void singletonMapConstructorTest() {
        asd.put('s', 1);
        huffman = new Huffman(asd);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nonPositiveMapConstructorTest() {
        asd.put('s', 0);
        huffman = new Huffman(asd);
    }
    
    @Test
    public void buildHashMapsTest() {
        huffman = new Huffman("asaasd");
//        assertEquals(0, huffman.map.size());
        assertEquals("0" ,huffman.map.get('a'));
        assertEquals("11" ,huffman.map.get('s'));
        assertEquals("10" ,huffman.map.get('d'));
    }
    
    @Test
    public void compressTest() {
        huffman = new Huffman("asaasd");
        assertEquals("011100", huffman.compress("asda"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullCompressTest() {
        huffman = new Huffman("asaasd");
        huffman.compress(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidCompressTest() {
        huffman = new Huffman("asaasd");
        huffman.compress("asd" + '\u0000');
    }
    
    @Test
    public void decompressTest() {
        huffman = new Huffman("asaasd");
        assertEquals("asda", huffman.decompress("011100"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullDecompressTest() {
        huffman = new Huffman("asaasd");
        huffman.decompress(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void invalidDecompressTest() {
        huffman = new Huffman("asaasd");
        huffman.decompress("111");
    }
    
    @Test
    public void compressionRatioTest() {
        huffman = new Huffman("asaasd");
        huffman.compress("asd");
        assertEquals(0.10416666, huffman.compressionRatio(), 0.001);
    }
    
    @Test
    public void multipleCompressionRatioTest() {
        huffman = new Huffman("asaasd");
        huffman.compress("asd");
        huffman.compress("aa");
        assertEquals(0.0875, huffman.compressionRatio(), 0.001);
        
    }
    
    @Test (expected = IllegalStateException.class)
    public void notCompressedCompressionRatioTest() {
        huffman = new Huffman("asaasd");
        huffman.compressionRatio();
    }
    
    @Test
    public void expectedEncodingLengthTest() {
        huffman = new Huffman("asaasd");
        assertEquals(1.5, huffman.expectedEncodingLength(), 0.01);
    }
}
