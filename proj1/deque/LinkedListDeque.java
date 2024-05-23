package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private static class Node<N> {
        private final N item;
        private Node<N> prev;
        private Node<N> next;
        private Node(N i, Node<N> n) {
            item = i;
            next = n;
        }
    }
    private final Node<T> sentinel = new Node<>(null, null);

    private int size;
    public LinkedListDeque() {
        size = 0;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        Node<T> first = sentinel.next;
        first.prev = new Node<>(item, first);
        sentinel.next = first.prev;
        first.prev.prev = sentinel;
        size = size + 1;
    }

    public void addLast(T item) {
        Node<T> last = sentinel.prev;
        last.next = new Node<>(item, sentinel);
        sentinel.prev = last.next;
        last.next.prev = last;
        size = size + 1;
    }

    public T removeFirst() {
        Node<T> first = sentinel.next;
        T removed = first.item;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        size -= removed != null ? 1 : 0;
        return removed;
    }

    public T removeLast() {
        Node<T> last = sentinel.prev;
        T removed = last.item;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        size -= removed != null ? 1 : 0;
        return removed;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        String[] items = new String[size];
        Node<T> node = sentinel.next;
        if (node == sentinel) {
            return;
        }
        for (int i = 0; i < size; i += 1) {
            items[i] = node.item.toString();
            node = node.next;
        }
        System.out.println(String.join(" ", items));
    }

    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        Node<T> current = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            if (i == index) {
                return current.item;
            }
            current = current.next;
        }
        return null;
    }

    private T getRecursiveHelper(int index, Node<T> current) {
        if (index == 0) {
            return current.item;
        }
        return getRecursiveHelper(index - 1, current.next);
    }
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque || o instanceof  ArrayDeque)) {
            return false;
        }
        Deque<?> lld = (Deque<?>) o;
        if (lld.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            if (lld.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    private class LListDequeIterator implements Iterator<T> {
        private Node<T> node;

        LListDequeIterator() {
            node = sentinel.next;
        }

        public boolean hasNext() {
            return node != sentinel;
        }

        public T next() {
            T item = node.item;
            node = node.next;
            return item;
        }
    }

    public Iterator<T> iterator() {
        return new LListDequeIterator();
    }
}
