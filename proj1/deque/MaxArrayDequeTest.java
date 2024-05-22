package deque;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    private static class IntComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return i1 - i2;
        }
    }
    @Test
    public void getLargestNumberTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntComparator());
        for (int i = 0; i < 5; i++) {
            mad.addLast(i);
        }

        assertEquals((Integer) 4, mad.max());
    }
    private static class StrComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            int lenS1 = s1.length();
            int lenS2 = s2.length();

            for (int i = 0; i < Math.min(lenS1, lenS2); i += 1) {
                int s1Char = s1.charAt(i);
                int s2Char = s2.charAt(i);

                if (s1Char != s2Char) {
                    return s1Char - s2Char;
                }
            }
            if (lenS1 != lenS2) {
                return lenS1 - lenS2;
            }
            return 0;
        }
    }

    private static class StrLengthComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }
    @Test
    public void getLongestStringTest() {
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(new StrComparator());
        mad.addLast("hello");
        mad.addLast("World!");

        assertEquals("hello", mad.max());
        assertEquals("World!", mad.max(new StrLengthComparator()));
    }
}
