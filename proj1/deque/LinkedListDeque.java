package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private static class Node<N> {
        private final N item;
        private Node<N> prev;
        private Node<N> next;
        private Node(N i, Node<N> n) {
            item = i;
            next = n;
        }
    };
    private final Node<T> sentinel = new Node<>(null, null);

    private int size;
    public LinkedListDeque() {
        size = 0;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }
    public static void main(String[] args) {
        System.out.print("linked list deque");
    };

    @Override
    public void addFirst(T item) {
        Node first = sentinel.next;
        first.prev = new Node(item, first);
        sentinel.next = first.prev;
        first.prev.prev = sentinel;
        size = size + 1;
    }

    @Override
    public void addLast(T item) {
        Node last = sentinel.prev;
        last.next = new Node(item, sentinel);
        sentinel.prev = last.next;
        last.next.next = sentinel;
        size = size + 1;
    }

    @Override
    public T removeFirst() {
        Node<T> first = sentinel.next;
        T removed = first.item;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        size -= removed != null ? 1 : 0;
        return removed;
    }

    @Override
    public T removeLast() {
        Node<T> last = sentinel.prev;
        T removed = last.item;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        size -= removed != null ? 1 : 0;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {

    }

    @Override
    public T get(int index) {
        return null;
    }
};