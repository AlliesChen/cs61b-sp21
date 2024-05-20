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
        ArrayDeque<String> actual = new ArrayDeque<>();
        for (int i = 0; i < 13; i++) {
            actual.addLast(Integer.toString(i));
        }
        assertEquals(13, actual.size());
        System.out.println("Printing out deque: ");
        actual.printDeque();
    }
}
