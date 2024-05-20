package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int cap) {
        T[] dest = (T[]) new Object[cap];
        System.arraycopy(items, 0, dest, 0, items.length);
        nextFirst = dest.length - 1;
        nextLast = size + 1;
        items = dest;
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        size += 1;
        if (nextFirst - 1 < 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }
        if (nextFirst == nextLast) {
            resize(size * 2);
        }
    }
    public void addLast(T item) {
        items[nextLast] = item;
        size += 1;
        if (nextLast + 1 == items.length) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }
        if (nextLast == nextFirst) {
            resize(size * 2);
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (nextFirst + 1 == items.length) {
            nextFirst = 0;
        } else {
            nextFirst += 1;
        }
        T item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        return item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (nextLast - 1 < 0) {
            nextLast = items.length;
        } else {
            nextLast -= 1;
        }
        T item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return item;
    }

    private int getFirst() {
        if (nextFirst + 1 > items.length) {
            return 0;
        }
        return nextFirst + 1;
    }

    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int itemIndex = getFirst() + index % items.length;
        return items[itemIndex];
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
}
