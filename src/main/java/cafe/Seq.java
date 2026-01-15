package cafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class Seq<E> implements Iterable<E> {

    private final E[] array;
    private final int length;

    private Seq(E[] array, int length) {
        this.array = array;
        this.length = length;
    }

    public static final Seq<Object> EMPTY = new Seq<>(new Object[]{new Object()}, 0);

    @SuppressWarnings("unchecked")
    public static <E> Seq<E> seq() {
        return (Seq<E>) EMPTY;
    }

    @SafeVarargs
    public static <E> Seq<E> seq(E... elements) {
        if (elements.length == 0) return seq();

        for (Object element : elements) {
            requireNonNull(element, "Seq rejects null");
        }

        return new Seq<>(elements, elements.length);
    }

    private static final VarHandle OBJECT_ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(Object[].class);

    public Seq<E> plus(E element) {
        requireNonNull(element, "Seq rejects null");

        var array = this.array;
        int length = this.length;
        int capacity = array.length;

        if (length == capacity) {
            array = copy(capacity * 2);
            array[length] = element;
        } else if (!OBJECT_ARRAY_HANDLE.weakCompareAndSet(array, length, null, element)) {
            array = copy(capacity);
            array[length] = element;
        }

        return new Seq<>(array, length + 1);
    }

    @SafeVarargs
    public final Seq<E> plus(E... elements) {
        int additional = elements.length;
        if (additional == 0) return this;

        for (Object element : elements) {
            requireNonNull(element, "Seq rejects null");
        }

        var array = this.array;
        int length = this.length;
        int required = length + additional;

        if (required > array.length) {
            array = copy(required * 2);
        } else if (!OBJECT_ARRAY_HANDLE.weakCompareAndSet(array, length, null, elements[0])) {
            array = copy(required);
        }
        System.arraycopy(elements, 0, array, length, additional);

        return new Seq<>(array, required);
    }

    public Seq<E> plus(Seq<E> that) {
        int additional = that.length;
        if (additional == 0) return this;

        var elements = that.array;

        var array = this.array;
        int length = this.length;
        int required = length + additional;

        if (required > array.length) {
            array = copy(required * 2);
        } else if (!OBJECT_ARRAY_HANDLE.weakCompareAndSet(array, length, null, elements[0])) {
            array = copy(required);
        }
        System.arraycopy(elements, 0, array, length, additional);

        return new Seq<>(array, required);
    }

    private E[] copy(int capacity) {
        E[] a = newArray(capacity);
        System.arraycopy(array, 0, a, 0, length);
        return a;
    }

    private E[] copy() {
        return copy(length);
    }

    @SuppressWarnings("unchecked")
    private static <E> E[] newArray(int capacity) {
        return (E[]) new Object[capacity];
    }

    public int length() {
        return length;
    }

    public E at(int index) {
        return array[fix(index)];
    }

    private int fix(int index) {
        int length = this.length;
        if (index >= length) {
            throw new IndexOutOfBoundsException("invalid index: " + index + " >= " + length);
        }
        if (index < 0) {
            if (index < -length) {
                throw new IndexOutOfBoundsException("invalid index: " + index + " < " + -length);
            }
            index += length;
        }
        return index;
    }

    public Seq<E> with(int index, E element) {
        index = fix(index);
        requireNonNull(element, "Seq rejects null");

        E[] a = copy();
        a[index] = element;
        return new Seq<>(a, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Seq<?> that)) return false;

        if (this.length != that.length) return false;

        for (int i = 0; i < length; ++i) {
            if (!this.array[i].equals(that.array[i])) return false;
        }

        return true;
    }

    private int hashCode;

    @Override
    public int hashCode() {
        int hash = hashCode;
        // lazy initialization, benign data race (deterministic computation)
        if (hash == 0) {
            hash = 1;
            for (int i = 0; i < length; ++i) {
                hash = hash * 31 + array[i].hashCode();
            }
            hashCode = hash;
        }
        return hash;
    }

    @Override
    public String toString() {
        return join("[", ", ", "]");
    }

    public String join(String prefix, String separator, String suffix) {
        var array = this.array;
        int length = this.length;

        switch (length) {
            case 0:
                return prefix + suffix;
            case 1:
                return prefix + array[0] + suffix;
            case 2:
                return prefix + array[0] + separator + array[1] + suffix;
        }

        var builder = new StringBuilder(prefix);
        builder.append(array[0]);
        for (int i = 1; i < length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.append(suffix).toString();
    }

    public String join(String separator) {
        return join("", separator, "");
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < length;
            }

            @Override
            public E next() {
                return array[index++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (int i = 0; i < length; ++i) {
            action.accept(array[i]);
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(array, 0, length,
                Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE);
    }

    public <R> Seq<R> map(Function<E, R> f) {
        R[] a = newArray(length);
        for (int i = 0; i < length; ++i) {
            a[i] = Objects.requireNonNull(f.apply(array[i]), "Seq rejects null");
        }
        return new Seq<>(a, length);
    }

    public <R> Seq<R> mapIndexed(BiFunction<Integer, E, R> f) {
        R[] a = newArray(length);
        for (int i = 0; i < length; ++i) {
            a[i] = Objects.requireNonNull(f.apply(i, array[i]), "Seq rejects null");
        }
        return new Seq<>(a, length);
    }

    public <R> Seq<R> flatMap(Function<E, Seq<R>> f) {
        Seq<R> result = new Seq<>(newArray(length), 0);
        for (int i = 0; i < length; ++i) {
            result = result.plus(f.apply(array[i]));
        }
        return result;
    }

    public <R> Seq<R> flatMapIndexed(BiFunction<Integer, E, R> f) {
        Seq<R> result = new Seq<>(newArray(length), 0);
        for (int i = 0; i < length; ++i) {
            result = result.plus(f.apply(i, array[i]));
        }
        return result;
    }

    public Seq<E> filter(Predicate<E> p) {
        E[] a = newArray(length);
        int n = 0;
        for (int i = 0; i < length; ++i) {
            if (p.test(array[i])) {
                a[n++] = array[i];
            }
        }
        if (n == 0) return seq();
        if (n == length) return this;

        return new Seq<>(a, n);
    }

    public Seq<E> filterIndexed(BiPredicate<Integer, E> p) {
        E[] a = newArray(length);
        int n = 0;
        for (int i = 0; i < length; ++i) {
            if (p.test(i, array[i])) {
                a[n++] = array[i];
            }
        }
        return new Seq<>(a, n);
    }

    public <R> R fold(R value, BiFunction<R, E, R> f) {
        for (int i = 0; i < length; ++i) {
            value = f.apply(value, array[i]);
        }
        return value;
    }

    public Seq<E> take(int n) {
        if (n <= 0) return seq();
        if (n >= length) return this;

        return new Seq<>(array, n);
    }

    public Seq<E> drop(int n) {
        if (n <= 0) return this;
        if (n >= length) return seq();

        int len = length - n;
        E[] a = newArray(len);
        System.arraycopy(array, n, a, 0, len);
        return new Seq<>(a, len);
    }

    public Seq<E> sorted() {
        E[] a = copy();
        Arrays.sort(a);
        return new Seq<>(a, length);
    }

    public Seq<E> sorted(Comparator<E> comparator) {
        E[] a = copy();
        Arrays.sort(a, comparator);
        return new Seq<>(a, length);
    }

    public Seq<E> reversed() {
        E[] a = newArray(length);
        for (int i = 0, j = length - 1; j >= 0; ++i, --j) {
            a[j] = array[i];
        }
        return new Seq<>(a, length);
    }

    public Seq<E> shuffled() {
        E[] a = copy();
        Random random = new Random();
        for (int n = length; n >= 2; --n) {
            int i = n - 1;
            int j = random.nextInt(n);
            E t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return new Seq<>(a, length);
    }

    public static <E> Seq<E> iterate(int length, E first, UnaryOperator<E> next) {
        if (length <= 0) return seq();

        E[] array = newArray(length);
        array[0] = first;
        for (int i = 1; i < length; ++i) {
            array[i] = next.apply(array[i - 1]);
        }
        return new Seq<>(array, length);
    }

    public static <E> Seq<E> iterate(int length, E first, E second, BinaryOperator<E> next) {
        if (length <= 0) return seq();

        E[] array = newArray(length);
        array[0] = first;
        if (length >= 2) {
            array[1] = second;
            for (int i = 2; i < length; ++i) {
                array[i] = next.apply(array[i - 2], array[i - 1]);
            }
        }
        return new Seq<>(array, length);
    }

    public Stream<E> stream() {
        return Arrays.stream(array, 0, length);
    }

    @SuppressWarnings("unchecked")
    public static <E> Collector<E, ?, Seq<E>> collector() {
        return Collector.of(
                ArrayList::new,
                ArrayList::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                list -> Seq.seq((E[]) list.toArray()));
    }

    public Object[] toArray() {
        Object[] a = newArray(length);
        System.arraycopy(array, 0, a, 0, length);
        return a;
    }

    public E[] toArray(IntFunction<E[]> factory) {
        E[] a = factory.apply(length);
        System.arraycopy(array, 0, a, 0, length);
        return a;
    }
}
