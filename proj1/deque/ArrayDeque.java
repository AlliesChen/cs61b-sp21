package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final int INITIAL_CAPACITY = 8;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_CAPACITY];
        nextFirst = -1;
        nextLast = -1;
        size = 0;
    }

    private boolean isFull() {
        return size == items.length;
    }

    private void resize(int newCapacity) {
        T[] newItems = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(nextFirst + i) % items.length];
        }
        nextFirst = 0;
        nextLast = size - 1;
        items = newItems;
    }

    public void addFirst(T item) {
        if (isFull()) {
            resize(items.length * 2);
        }
        if (isEmpty()) {
            nextFirst = 0;
            nextLast = 0;
        } else {
            nextFirst = (nextFirst - 1 + items.length) % items.length;
        }
        items[nextFirst] = item;
        size += 1;
    }
    public void addLast(T item) {
        if (isFull()) {
            resize(items.length * 2);
        }
        if (isEmpty()) {
            nextFirst = 0;
            nextLast = 0;
        } else {
            nextLast = (nextLast + 1) % items.length;
        }
        items[nextLast] = item;
        size += 1;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T removedItem = items[nextFirst];
        items[nextFirst] = null;
        if (nextFirst == nextLast) {
            nextFirst = -1;
            nextLast = -1;
        } else {
            nextFirst = (nextFirst + 1) % items.length;
        }
        if (size > 0 && size == (items.length / 4) && items.length > INITIAL_CAPACITY) {
            resize(Math.max(items.length / 2, INITIAL_CAPACITY));
        }
        size -= 1;
        return removedItem;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T removedItem = items[nextLast];
        items[nextLast] = null;
        if (nextFirst == nextLast) {
            nextFirst = -1;
            nextLast = -1;
        } else {
            nextLast = (nextLast - 1 + items.length) % items.length;
        }
        size -= 1;
        if (size > 0 && size == (items.length / 4) && items.length > INITIAL_CAPACITY) {
            resize(Math.max(items.length / 2, INITIAL_CAPACITY));
        }
        return removedItem;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(nextFirst + index) % items.length];
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (T i : items) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<?> ad = (ArrayDeque<?>) o;
        if (ad.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i+= 1) {
            if (ad.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    private class ADequeIterator implements Iterator<T> {
        private int index;
        ADequeIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }
    }
    public Iterator<T> iterator() {
        return new ADequeIterator();
    }
}
