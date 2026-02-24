public class ArrayView<E> {
    private final E[] array;

    public ArrayView(E[] array) {
        this.array = array;
    }

    static void main() {
        ArrayView<String> sorten = new ArrayView<>(new String[]{"Vanille", "Erdbeer", "Schoko"});
    }
}
