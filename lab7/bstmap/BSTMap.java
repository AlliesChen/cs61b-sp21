package bstmap;

import java.lang.UnsupportedOperationException;
import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V> {

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private BSTNode root; // The root of the BSTMap
    private int size; // To keep track of the number of key-value pairs
    public void clear() {
        root = null;
        size = 0;
    }
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }
    @Override
    public V get(K key) {
        return get(root, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>(); // Create an empty HashSet to store keys
        collectKeys(root, keySet); // Perform in-order traversal to collect keys
        return keySet; // Return the set of keys
    }

    @Override
    public V remove(K key) {
        V value = get(key); // Get the value before removal
        if (value != null) {
            root = remove(root, key); // Update root after removal
        }
        return value;
    }
    @Override
    public V remove(K key, V value) {
        V currentValue = get(key);
        if (currentValue != null && currentValue.equals(value)) {
            root = remove(root, key);
            return currentValue;
        }
        return null;
    }
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true; // key found
        }
    }
    private V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value; // key found
        }
    }
    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            size++;
            return new BSTNode(key, value); // New node created
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value; // Key exists, update value
        }
        return node;
    }
    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null; // Key not found
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key); // Recur on the left subtree
        } else if (cmp > 0) {
            node.right = remove(node.right, key); // Recur on the right subtree
        } else {
            size--;
            // Key found, handle deletion
            if (node.left == null) {
                return node.right; // Node left child
            }
            if (node.right == null) {
                return node.left; // No right child
            }
            // Node has two children
            BSTNode successor = findMin(node.right); // Find the minimum node in the right subtree
            node.key = successor.key; // Replace node's key and value with the successor's
            node.value = successor.value;
            node.right = remove(node.right, successor.key); // Remove the successor
        }
        return node;
    }
    private BSTNode findMin(BSTNode node) {
        if (node.left == null) {
            return node; // Smallest node found
        } else {
            return findMin(node.left);
        }
    }
    private class BSTMapIterator implements Iterator<K> {
        private Stack<BSTNode> stack;
        public BSTMapIterator() {
            stack = new Stack<>();
            pushLeft(root); // Start with the leftmost node
        }
        // Push all the left children of the node onto the stack
        private void pushLeft(BSTNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty(); // If the stack is not empty, there are more nodes to visit
        }
        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            // Pop the node from the stack and process it
            BSTNode node = stack.pop();
            K key = node.key;
            // If the node has a right subtree, push all its left children to the stack
            if (node.right != null) {
                pushLeft(node.right);
            }

            return key; // Return the key of the visited node
        }
    }
    /**
     * Helper method to perform an in-order traversal and collect all keys in the set.
     * @param node the current node being visited
     * @param keySet the set where keys are being collected
     */
    private void collectKeys(BSTNode node, Set<K> keySet) {
        if (node == null) {
            return; // Base case: if the node is null, return (end of a branch)
        }
        collectKeys(node.left, keySet); // Recursively visit the left subtree
        keySet.add(node.key); // Add the current node's key to the set
        collectKeys(node.right, keySet); // Recursively visit the right subtree
    }
}
