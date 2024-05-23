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
        int TEST_SIZE = 1000000;
        for (int i = 0; i < TEST_SIZE; i += 1) {
            actual.addLast(i);
        }
        assertEquals(TEST_SIZE, actual.size());

        int millionth = actual.get(TEST_SIZE - 1);
        assertEquals("The millionth should equal to the size minus 1", TEST_SIZE - 1, millionth);
        for (int i = 0; i < TEST_SIZE - 1; i += 1) {
            int removed = actual.removeLast();
            assertEquals(TEST_SIZE - 1 - i, removed);
        }
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

    @Test
    public void equalsADequeAndLLDequeTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        assertTrue(ad.equals(lld));

        ad.addFirst(1);
        lld.addFirst(1);
        assertTrue(ad.equals(lld));
    }

    @Test
    public void randomAddRemoveTest() {
        int TEST_SIZE = 10000;
        ArrayDeque<Integer> actual = new ArrayDeque<>();
        for (int i = 0; i < TEST_SIZE; i += 1) {
            double n = Math.random() * 2 + 1;
            int randomCase = (int) (Math.floor(n));
            switch (randomCase) {
                case 1:
                    actual.addFirst(i);
                    break;
                case 2:
                    actual.addLast(i);
                    break;
                default:
                    throw new IllegalArgumentException("unexpected input value: " + i);
            }
        }
        assertEquals(TEST_SIZE, actual.size());
        for (int i = 0; i < TEST_SIZE; i += 1) {
            double n = Math.random() * 2 + 1;
            int randomCase = (int) (Math.floor(n));
            switch (randomCase) {
                case 1:
                    int expectedFirst = actual.get(0);
                    int removedFirst = actual.removeFirst();
                    assertEquals(expectedFirst, removedFirst);
                    assertNotNull(expectedFirst);
                    assertNotNull(removedFirst);
                    break;
                case 2:
                    int expectedLast = actual.get(actual.size() - 1);
                    int removedLast = actual.removeLast();
                    assertEquals(expectedLast, removedLast);
                    assertNotNull(expectedLast);
                    assertNotNull(removedLast);
                    break;
                default:
                    throw new IllegalArgumentException("unexpected input value: " + i);
            }
        }
    }
}
