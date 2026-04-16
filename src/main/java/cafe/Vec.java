package cafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class Vec<E> implements Iterable<E> {

    private final E[] array;
    private final int length;

    private Vec(E[] array, int length) {
        this.array = array;
        this.length = length;
    }

    public static final Vec<Object> EMPTY = new Vec<>(new Object[]{new Object()}, 0);

    @SuppressWarnings("unchecked")
    public static <E> Vec<E> of() {
        return (Vec<E>) EMPTY;
    }

    @SafeVarargs
    public static <E> Vec<E> of(E... elements) {
        if (elements.length == 0) return Vec.of();

        for (Object element : elements) {
            requireNonNull(element, "Vec rejects null");
        }

        return new Vec<>(elements, elements.length);
    }

    private static final VarHandle OBJECT_ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(Object[].class);

    public Vec<E> plus(E element) {
        requireNonNull(element, "Vec rejects null");

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

        return new Vec<>(array, length + 1);
    }

    @SafeVarargs
    public final Vec<E> plus(E... elements) {
        int additional = elements.length;
        if (additional == 0) return this;

        for (Object element : elements) {
            requireNonNull(element, "Vec rejects null");
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

        return new Vec<>(array, required);
    }

    public Vec<E> plus(Vec<E> that) {
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

        return new Vec<>(array, required);
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

    public Vec<E> with(int index, E element) {
        index = fix(index);
        requireNonNull(element, "Vec rejects null");

        E[] a = copy();
        a[index] = element;
        return new Vec<>(a, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec<?> that)) return false;

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

    public <R> Vec<R> map(Function<E, R> f) {
        R[] a = newArray(length);
        for (int i = 0; i < length; ++i) {
            a[i] = Objects.requireNonNull(f.apply(array[i]), "Vec rejects null");
        }
        return new Vec<>(a, length);
    }

    public <R> Vec<R> mapIndexed(BiFunction<Integer, E, R> f) {
        R[] a = newArray(length);
        for (int i = 0; i < length; ++i) {
            a[i] = Objects.requireNonNull(f.apply(i, array[i]), "Vec rejects null");
        }
        return new Vec<>(a, length);
    }

    public <R> Vec<R> flatMap(Function<E, Vec<R>> f) {
        Vec<R> result = new Vec<>(newArray(length), 0);
        for (int i = 0; i < length; ++i) {
            result = result.plus(f.apply(array[i]));
        }
        return result;
    }

    public <R> Vec<R> flatMapIndexed(BiFunction<Integer, E, R> f) {
        Vec<R> result = new Vec<>(newArray(length), 0);
        for (int i = 0; i < length; ++i) {
            result = result.plus(f.apply(i, array[i]));
        }
        return result;
    }

    public Vec<E> filter(Predicate<E> p) {
        E[] a = newArray(length);
        int n = 0;
        for (int i = 0; i < length; ++i) {
            if (p.test(array[i])) {
                a[n++] = array[i];
            }
        }
        if (n == 0) return Vec.of();
        if (n == length) return this;

        return new Vec<>(a, n);
    }

    public Vec<E> filterIndexed(BiPredicate<Integer, E> p) {
        E[] a = newArray(length);
        int n = 0;
        for (int i = 0; i < length; ++i) {
            if (p.test(i, array[i])) {
                a[n++] = array[i];
            }
        }
        return new Vec<>(a, n);
    }

    public <R> R fold(R value, BiFunction<R, E, R> f) {
        for (int i = 0; i < length; ++i) {
            value = f.apply(value, array[i]);
        }
        return value;
    }

    public Vec<E> take(int n) {
        if (n <= 0) return Vec.of();
        if (n >= length) return this;

        return new Vec<>(array, n);
    }

    public Vec<E> drop(int n) {
        if (n <= 0) return this;
        if (n >= length) return Vec.of();

        int len = length - n;
        E[] a = newArray(len);
        System.arraycopy(array, n, a, 0, len);
        return new Vec<>(a, len);
    }

    public Vec<E> sorted() {
        E[] a = copy();
        Arrays.sort(a);
        return new Vec<>(a, length);
    }

    public Vec<E> sorted(Comparator<E> comparator) {
        E[] a = copy();
        Arrays.sort(a, comparator);
        return new Vec<>(a, length);
    }

    public Vec<E> reversed() {
        E[] a = newArray(length);
        for (int i = 0, j = length - 1; j >= 0; ++i, --j) {
            a[j] = array[i];
        }
        return new Vec<>(a, length);
    }

    public Vec<E> shuffled() {
        E[] a = copy();
        Random random = new Random();
        for (int n = length; n >= 2; --n) {
            int i = n - 1;
            int j = random.nextInt(n);
            E t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return new Vec<>(a, length);
    }

    public static <E> Vec<E> iterate(int length, E first, UnaryOperator<E> next) {
        if (length <= 0) return Vec.of();

        E[] array = newArray(length);
        array[0] = first;
        for (int i = 1; i < length; ++i) {
            array[i] = next.apply(array[i - 1]);
        }
        return new Vec<>(array, length);
    }

    public static <E> Vec<E> iterate(int length, E first, E second, BinaryOperator<E> next) {
        if (length <= 0) return Vec.of();

        E[] array = newArray(length);
        array[0] = first;
        if (length >= 2) {
            array[1] = second;
            for (int i = 2; i < length; ++i) {
                array[i] = next.apply(array[i - 2], array[i - 1]);
            }
        }
        return new Vec<>(array, length);
    }

    public Stream<E> stream() {
        return Arrays.stream(array, 0, length);
    }

    @SuppressWarnings("unchecked")
    public static <E> Collector<E, ?, Vec<E>> collector() {
        return Collector.of(
                ArrayList::new,
                ArrayList::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                list -> Vec.of((E[]) list.toArray()));
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
