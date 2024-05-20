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

        int thirteenth = actual.get(13);
        assertEquals(7, thirteenth);

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
        int oneMillionAndSecond = actual.get(99999);
        assertEquals(99984, oneMillionAndSecond);
        int removedLast = actual.removeLast();
        assertEquals(999999, removedLast);
    }
}
