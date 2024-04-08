package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> expect = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();
        expect.addLast(4);
        actual.addLast(4);
        expect.addLast(5);
        actual.addLast(5);
        expect.addLast(6);
        actual.addLast(6);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.removeLast(), actual.removeLast());
        assertEquals(expect.removeLast(), actual.removeLast());
        assertEquals(expect.removeLast(), actual.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(correct.size(), broken.size());
            } else if (operationNumber == 2 && correct.size() > 0) {
                int correctLast = correct.getLast();
                int brokenLast = broken.getLast();
                assertEquals(correctLast, brokenLast);
            } else if (operationNumber == 3 && correct.size() > 0) {
                int correctLast = correct.removeLast();
                int brokenLast = broken.removeLast();
                assertEquals(correctLast, brokenLast);
            }
        }
    }
}
