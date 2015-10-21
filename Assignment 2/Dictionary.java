/**
 * Creates a dictionary using seperate chaining and String-Int key-value pairs. The size of the dictionary
 * is initialized to the prime number higher than the size given as a parameter. For example, if the size
 * parameter is given as 8, the dictionary will initialize the size to 11.
 *
 * The hash function is computed using Horner's rule, which computes the hash of a String by summing the values
 * of the characters in the String by z^i, where z is a predefined value (in this case 47) and i is the location
 * of the character in the String.
 *
 * @author Robert Meagher   2502749364
 * @since October 6, 2015
 *
 * Implements methods from DictionaryADT
 * @see DictionaryADT
 */

public class Dictionary implements DictionaryADT {
    private Node elements[]; //An array of nodes, each which serves as the head of a Linked List
    private int numElements; //The number of elements that exist in the current Dictionary

    /**
     * Creates a new Dictionary object with the designated size.
     * The size should approximately be the number of items you
     * want to store in the dictionary.
     * @param size  The size to allocate to the Dictionary
     */
    public Dictionary(int size){
        size = nextPrime(size);
        elements = new Node[size];
        numElements = 0;
    }

    /**
     * Inserts a new DictEntry object into the dictionary, using the
     * Dictionary's hash function. Returns an int stating whether or not
     * the entry collided with an existing Dictionary entry
     * @param pair The item to insert
     * @return 0 if no collision occured, 1 otherwise
     * @throws DictionaryException if an item with the given key already exists
     *                              in the Dictionary
     */
    public int insert(DictEntry pair) throws DictionaryException {
        // get the location to insert the new DictEntry
        int hashCode = hash(pair.getKey());
        Node n = elements[hashCode];
        Node last = null;

        // iterate until the end of the list is found
        while(n != null) {
            // if a duplicate key is found, throw an exception
            if(pair.getKey().equals(n.value.getKey()))
                throw new DictionaryException("Duplicate key found while inserting");
            last = n;
            n = n.next;
        }

        numElements++;

        // if there were no elements in the list, insert at the beginning and return no collision
        if(last == null) {
            elements[hashCode] = new Node(pair);
            return 0;
        }
        // if there were elements in the list, insert at the end and return a collision
        else {
            last.next = new Node(pair);
            return 1;
        }
    }

    /**
     * @param key The key to find
     * @return The DictEntry with the matching key. Returns null if no entry
     *          with the given key exists in the Dictionary.
     */
    public DictEntry find(String key) {
        Node n = elements[hash(key)];

        // iterate through until the end of the list
        while(n != null) {
            //if the key was found, return the object that the key belongs to
            if(key.equals(n.value.getKey()))
                return n.value;
            n = n.next;
        }

        return null;
    }

    /**
     * Removes the DictEntry with the given key in the Dictionary
     * @param key The key to remove
     * @throws DictionaryException if the key is not in the Dictionary
     */
    public void remove(String key) throws DictionaryException {
        Node n = elements[hash(key)];
        Node last = null;

        // if the list is empty, throw an exception
        if(n == null) throw new DictionaryException("Key to remove not found");

        // iterate through the list until the key is found
        while(!key.equals(n.value.getKey())) {
            last = n;
            n = n.next;

            //if the key doesn't exist, throw an exception
            if(n == null) throw new DictionaryException("Key to remove not found");
        }

        if(last == null)
            elements[hash(key)] = n.next;
        else
            last.next = n.next;

        numElements--;
    }

    /**
     * Returns the number of elements in the Dictionary
     * @return The number of elements in the Dictionary
     */
    public int numElements() {
        return numElements;
    }

    /**
     * Hashes a given String and returns the result of the hash. Used
     * to find the location of an entry.
     *
     * The hash function is computed using Horner's rule, which computes the hash of a String by summing the values
     * of the characters in the String by z^i, where z is a predefined value (in this case 47) and i is the location
     * of the character in the String.
     *
     * @param key    The String to hash
     * @return       The hashed int value of the key
     */
    private int hash(String key) {
        int z = 47;
        int total = 0;

        for(int i = key.length() - 1; i >= 0; i--)
            total = (z * total + key.charAt(i)) % elements.length;

        return total;
    }

    /**
     * Finds the first number after n that is prime. If the number given as a parameter is
     * negative, returns 2.
     * @param n The minimum number to check
     * @return The fo=irst prime number after n
     */
    private int nextPrime(int n) {
        if(n < 2) return 2;

        if(n % 2 == 0) n++;
        boolean isPrime;

        while(true) {
            int s = (int) Math.sqrt(n);
            isPrime = true;

            for(int i = 3; i <= s; i+=2) {
                if(n % i == 0) {
                    isPrime = false;
                    break;
                }
            }

            if(isPrime) return n;
            n += 2;
        }
    }

    /**
     * A private class used to generate Linked Lists in the Dictionary,
     * required for seperate chaining. Stores one DictEntry object and a
     * reference to the next Node in the list.
     */
    private class Node {
        public DictEntry value;
        public Node next;

        public Node(DictEntry value) {
            next = null;
            this.value = value;
        }
    }
}