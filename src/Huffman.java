import java.util.HashMap;
import java.util.Map;

/**
 * Implements construction, encoding, and decoding logic of the Huffman coding algorithm. Characters
 * not in the given seed or alphabet should not be compressible, and attempts to use those
 * characters should result in the throwing of an {@link IllegalArgumentException} if used in {@link
 * #compress(String)}.
 */
public class Huffman {
    
    
    BinaryMinHeapImpl<Integer, Huffman.Node<Character>> priorityQueue;
    
    
    public class Node<V> {
        private V val;
        private Node<V> left;
        private Node<V> right;
        
        public Node(V v, Node<V> left, Node<V> right) {
            this.val = v;
            this.left = left;
            this.right = right;
        }
        
        public Node(V v) {
            this.val = v;
            this.left = null;
            this.right = null;
        }
        
        V getVal() {
            return this.val;
        }
        
        Node<V> getLeft() {
            return this.left;
        }
        
        Node<V> getRight() {
            return this.right;
        }
    }

    boolean hasCompressed;
    int strLength;
    int preCompressLength;
    
    int oldStrLength;
    int oldPreCompressLength;
    
    HashMap<Character, String> map = new HashMap<Character, String>();
    HashMap<String, Character> revMap = new HashMap<String, Character>();
    Map<Character, Integer> freqMap = new HashMap<Character, Integer>();
    
    /**
     * Constructs a {@code Huffman} instance from a seed string, from which to deduce the alphabet
     * and corresponding frequencies.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param seed the String from which to build the encoding
     * @throws IllegalArgumentException seed is null, seed is empty, or resulting alphabet only has
     *                                  1 character
     */
    public Huffman(String seed) {
        if (seed == null) {
            throw new IllegalArgumentException("null seed");
        }
        if (seed.isEmpty()) {
            throw new IllegalArgumentException("empty seed");
        }
        if (seed.length() == 1) {
            throw new IllegalArgumentException("seed of length 1");
        }
        this.priorityQueue = new BinaryMinHeapImpl<Integer, Huffman.Node<Character>>();
        this.hasCompressed = false;
        this.strLength = 0;
        
        HashMap<Character, Integer> alphabet = new HashMap<Character, Integer>();
        for (int i = 0; i < seed.length(); i++) {
            char c = seed.charAt(i);
            if (alphabet.containsKey(c)) {
                Integer val = alphabet.get(c);
                alphabet.replace(c, val + 1);
            } else {
                alphabet.put(c, 1);
            }
        }
        Huffman huffman = new Huffman(alphabet);
        this.priorityQueue = huffman.priorityQueue;
        this.map = huffman.map;
        this.revMap = huffman.revMap;
        this.freqMap = huffman.freqMap;
    }

    /**
     * Constructs a {@code Huffman} instance from a frequency map of the input alphabet.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param alphabet a frequency map for characters in the alphabet
     * @throws IllegalArgumentException if the alphabet is null, empty, has fewer than 2 characters,
     *                                  or has any non-positive frequencies
     */
    public Huffman(Map<Character, Integer> alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("null alphabet");
        }
        if (alphabet.isEmpty()) {
            throw new IllegalArgumentException("empty alphabet");
        }
        if (alphabet.size() < 2) {
            throw new IllegalArgumentException("alphabet fewer than 2 characters");
        }
        this.priorityQueue = new BinaryMinHeapImpl<Integer, Huffman.Node<Character>>();
        this.hasCompressed = false;
        this.strLength = 0;
        this.freqMap = alphabet;
        
        for (Map.Entry<Character, Integer> asd : alphabet.entrySet()) {
            if (asd.getValue() <= 0) {
                throw new IllegalArgumentException("alphabet has non positive frequencies");
            }
            this.priorityQueue.add(asd.getValue(), new Huffman.Node<Character>(asd.getKey()));
        }
        
        while (this.priorityQueue.size() > 1) {
            Huffman.Node<Character> x = this.priorityQueue.peek().value;
            Integer xFreq = this.priorityQueue.peek().key;
            this.priorityQueue.extractMin();
           
            Huffman.Node<Character> y = this.priorityQueue.peek().value;
            Integer yFreq = this.priorityQueue.peek().key;
            this.priorityQueue.extractMin();
            
            Huffman.Node<Character> combine = new Huffman.Node<Character>('\u0000', x, y);
            this.priorityQueue.add(xFreq + yFreq, combine);
        }
        buildHashMaps("", this.priorityQueue.peek().value);
    }
    
    void buildHashMaps(String s, Huffman.Node<Character> c) {
        if (c.val.charValue() == '\u0000') {
            String l = s + "0";
            buildHashMaps(l, c.left);
            
            String r = s + "1";
            buildHashMaps(r, c.right);
        } else {
            this.map.put(c.val, s);
            this.revMap.put(s, c.val);
        }
    } 
    
    /**
     * Compresses the input string.
     *
     * @param input the string to compress, can be the empty string
     * @return a string of ones and zeroes, representing the binary encoding of the inputted String.
     * @throws IllegalArgumentException if the input is null or if the input contains characters
     *                                  that are not compressible
     */
    public String compress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("null input");
        }
        this.oldPreCompressLength = this.preCompressLength;
        this.preCompressLength = input.length();
        this.hasCompressed = true;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (!this.revMap.containsValue(input.charAt(i))) {
                throw new IllegalArgumentException("not compressible");
            }
            s.append(map.get(input.charAt(i)));
        }
        this.oldStrLength = this.strLength;
        this.strLength = s.length();
        return s.toString();
    }
    

    /**
     * Decompresses the input string.
     *
     * @param input the String of binary digits to decompress, given that it was generated by a
     *              matching instance of the same compression strategy
     * @return the decoded version of the compressed input string
     * @throws IllegalArgumentException if the input is null, or if the input contains characters
     *                                  that are NOT 0 or 1, or input contains a sequence of bits
     *                                  that is not decodable
     */
    public String decompress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("null input");
        }
        StringBuilder s = new StringBuilder();

        int x = 0;
        int i = 0;
        while (i < input.length()) {
            int j = i + x;
            if (j == input.length() + 1) {
                throw new IllegalArgumentException("not decodeable");
            }
            if (this.map.containsValue(input.substring(i, j))) {
                s.append(this.revMap.get(input.substring(i, j)));
                i = j;
                x = 0;
            } else {
                x++;
            }
        }
        return s.toString();
    }

    /**
     * Computes the compression ratio so far. This is the length of all output strings from {@link
     * #compress(String)} divided by the length of all input strings to {@link #compress(String)}.
     * Assume that each char in the input string is a 16 bit int.
     *
     * @return the ratio of the total output length to the total input length in bits
     * @throws IllegalStateException if no calls have been made to {@link #compress(String)} before
     *                               calling this method
     */
    public double compressionRatio() {
        if (!this.hasCompressed) {
            throw new IllegalStateException("has not compressed");
        }
        this.strLength += this.oldStrLength;
        this.preCompressLength += this.oldPreCompressLength;
        return (double) this.strLength / ((double) 16 * (double) this.preCompressLength);
    }

    /**
     * Computes the expected encoding length of an arbitrary character in the alphabet based on the
     * objective function of the compression.
     * <p>
     * The expected encoding length is simply the sum of the length of the encoding of each
     * character multiplied by the probability that character occurs.
     *
     * @return the expected encoding length of an arbitrary character in the alphabet
     */
    public double expectedEncodingLength() {
        int sum = 0;
        for (Integer i : this.freqMap.values()) {
            sum += i;
        }
        int sum2 = 0;
        for (Character c : this.revMap.values()) {
            sum2 += this.map.get(c).length() * this.freqMap.get(c);
        }
        return (double) sum2 / (double) sum;
    }
}
