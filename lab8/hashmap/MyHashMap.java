package hashmap;

import jdk.jfr.Description;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size; // Number of key-value pairs
    private int capacity; // Current capacity of the array
    private double loadFactor;
    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.capacity = initialSize; // Set the initial capacity
        this.loadFactor = maxLoad; // Set the load factor threshold
        this.size = 0; // Initially, the map is empty
        this.buckets = (Collection<Node>[]) new Collection[capacity]; // Create the array of collection

        // Initialize each bucket using the createBucket factory method
        for (int i = 0; i < capacity; i+= 1) {
            buckets[i] = createBucket(); // User the factory method to create each bucket
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value); // Create and return a new Node
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>(); // Using ArrayList for the bucket
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }

    private void resize() {
        int newCapacity = capacity * 2; // Double the capacity
        Collection<Node>[] newBuckets = (Collection<Node>[]) new Collection[newCapacity];

        // Initialize the new buckets using the factory method
        for (int i = 0; i < newCapacity; i+= 1) {
            newBuckets[i] = createBucket();
        }
        // Rehash all the keys and redistribute them into the new buckets
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int newHash = (node.key.hashCode() & 0x7ffffff) % newCapacity;
                newBuckets[newHash].add(node); // Move the node to the new bucket
            }
        }

        // Update the reference to the new buckets and capacity
        this.buckets = newBuckets;
        this.capacity = newCapacity;
    }

    private int hash(K key) {
        // Step 1: Get the hash code from the key object
        int hashCode = key.hashCode();
        // Step 2: Ensure that the hash code is non-negative
        int positiveHashCode = hashCode & 0x7ffffff;
        // Step 3: Map the hash code to a valid bucket index
        return positiveHashCode % buckets.length;
    }
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        int index = hash(key); // Get the hash for the key
        Collection<Node> bucket = buckets[index]; // Get the correct bucket

        // Check if the key already exists, and update its value
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return; // Key already exists, so we just update the value
            }
        }

        // If the key doesn't exist, add a new node to the bucket
        Node newNode = createNode(key, value);
        bucket.add(newNode);
        // Increment the size as a new key-value pair is added
        size += 1;

        // Resize the map if the load factor exceeds the threshold
        if ((double) size / capacity > loadFactor) {
            resize();
        }
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }

        int hash = hash(key);
        Collection<Node> bucket = buckets[hash];

        // Iterate through the bucket to find the key
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value; // Return the value if the matches
            }
        }

        return null; // Key not found
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }

        int hash = hash(key);
        Collection<Node> bucket = buckets[hash];

        // Iterate through the bucket to find the key
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true; // Key exists
            }
        }

        return false; // Key not found
    }


    @Description("Return the number of key-value pairs")
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        // Reinitialize the buckets with empty collections
        for (int i = 0; i < capacity; i+= 1) {
            buckets[i] = createBucket();
        }
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int hash = hash(key);
        Collection<Node> bucket = buckets[hash];

        // Find and remove the node
        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key)) {
                V value = node.value;
                iterator.remove(); // Remove the node
                size -= 1;
                return value; // Return the value of the removed node
            }
        }

        return null; // Key not found
    }

    @Override
    public V remove(K key, V value) {
        if (key == null || value == null) {
            return null;
        }

        int hash = hash(key);
        Collection<Node> bucket = buckets[hash];

        // Find and remove the node if both key and value match
        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key) && node.value.equals(value)) {
                V removedValue = node.value;
                iterator.remove(); // Remove the node
                size -= 1;
                return removedValue; // Return the removed value
            }
        }

        return null; // Key-value pair not found
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>(); // Create a new HashSet to store the keys

        // Traverse all buckets
        for (Collection<Node> bucket : buckets) {
            // Traverse each node in the bucket and add the key to the set
            for (Node node : bucket) {
                keys.add(node.key);
            }
        }

        return keys; // Return the set of keys
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private int bucketIndex = 0; // Index to track the current bucket
            private Iterator<Node> currentBucketIterator = buckets[0].iterator();
            @Override
            public boolean hasNext() {
                // If the current bucket iterator has no more elements, move to the next non-empty bucket
                while (!currentBucketIterator.hasNext() && bucketIndex < capacity - 1) {
                    bucketIndex += 1;
                    currentBucketIterator = buckets[bucketIndex].iterator();
                }
                return currentBucketIterator.hasNext();
            }

            @Override
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return currentBucketIterator.next().key; // Return the key of the next node
            }
        };
    }
}
