package cafe;

import java.util.Iterator;
import java.util.function.Consumer;

public class Ints implements Iterable<Integer> {

    private final int from;
    private final int until;
    private final int step;

    private Ints(int from, int until, int step) {
        if (from > until) throw new IllegalArgumentException("from " + from + " > " + until + " until");
        if (step < 1) throw new IllegalArgumentException("step " + step + " < 1");

        this.from = from;
        this.until = until;
        this.step = step;
    }

    public static Ints range(int until) {
        return new Ints(0, until, 1);
    }

    public static Ints range(int from, int until) {
        return new Ints(from, until, 1);
    }

    public static Ints range(int from, int until, int step) {
        return new Ints(from, until, step);
    }

    @Override
    public String toString() {
        return '{' +
                "from=" + from + ", " +
                "until=" + until + ", " +
                "step=" + step + '}';
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            private int index = from;

            @Override
            public boolean hasNext() {
                return index < until;
            }

            @Override
            public Integer next() {
                int result = index;
                index += step;
                // overflow?
                if (index < result) {
                    // stop
                    index = until;
                }
                return result;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        if (step == 1) {
            for (int i = from; i < until; i += 1) {
                action.accept(i);
                // cannot overflow
            }
        } else {
            for (int i = from; i < until; i += step) {
                action.accept(i);
                // overflow?
                if (i + step < i) {
                    // stop
                    break;
                }
            }
        }
    }
}
