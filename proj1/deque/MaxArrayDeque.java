package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max(Comparator<T> cmp) {
        if (isEmpty()) {
            return null;
        }
        if (size() == 1) {
            return get(0);
        }
        int maxIndex = 0;
        for (int i = 1; i < size(); i += 1) {
            if (cmp.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }
    public T max() {
        return max(comparator);
    }
}
