package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addIsEmptySizeTest() {
        ArrayDeque<String> actual = new ArrayDeque<>();
        assertTrue("A newly initialized ADeque should be empty", actual.isEmpty());

        actual.addFirst("prev");
        assertEquals(1, actual.size());
        assertFalse("ADeque should contain 1 item", actual.isEmpty());

        actual.addLast("next");
        assertEquals(2, actual.size());

        actual.addLast("next.next");
        assertEquals(3, actual.size());

        System.out.println("Printing out deque: ");
        actual.printDeque();
    }

    @Test
    public void resizeTest() {
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            actual.addFirst(i);
        }
        for (int j = 0; j < 3; j++) {
            actual.addLast(10 + j);
        }
        assertEquals(13, actual.size());

        int thirteenth = actual.get(12);
        assertEquals(12, thirteenth);

        System.out.println("Printing out deque: ");
        actual.printDeque();
    }

    @Test
    public void addRemoveTest() {
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        assertTrue("actual should be empty in initialization", actual.isEmpty());

        actual.addFirst(10);
        assertFalse("actual should contain 1 item", actual.isEmpty());

        actual.removeFirst();
        assertTrue("actual should be empty after removal", actual.isEmpty());
        assertNull(actual.get(0));
    }

    @Test
    public void removeEmptyTest() {
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        actual.addFirst(1);
        actual.removeLast();
        assertNull(actual.get(0));
        // should do nothing
        actual.removeFirst();
        actual.removeLast();
        actual.removeFirst();
        assertEquals(0, actual.size());
        assertNull(actual.get(0));
    }

    @Test
    public void millionInsertionAndRemoveLastTest() {
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i ++) {
            actual.addLast(i);
        }
        assertEquals(1000000, actual.size());
        int oneToMillionth = actual.get(999999);
        assertEquals(999999, oneToMillionth);
        int removedLast = actual.removeLast();
        assertEquals(999999, removedLast);
    }

    @Test
    public void repeatedAddRemoveTest() {
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        for (int i = 0; i < 3; i ++) {
            for (int a = 0; a < 100; a++) {
                actual.addLast(a);
            }
            for (int r = 0; r < 99; r++) {
                actual.removeLast();
            }
        }
        assertEquals(3, actual.size());
        // Check if any garbage data remain.
        assertEquals(0, (int) actual.get(0));
        assertEquals(0, (int) actual.get(1));
        assertEquals(0, (int) actual.get(2));
        actual.printDeque();
    }

    @Test
    public void equalsADequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        assertTrue(ad1.equals(ad2));
        assertTrue(ad2.equals(ad1));

        ad1.addFirst(1);
        ad2.addFirst(1);
        assertTrue(ad1.equals(ad2));
        assertTrue(ad2.equals(ad1));
    }
}
