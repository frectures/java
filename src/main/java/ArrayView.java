public class ArrayView<E> {
    private final E[] array;

    public ArrayView(E[] array) {
        this.array = array;
    }

    public static void main(String[] args) {
        ArrayView<String> sorten = new ArrayView<>(new String[]{"Vanille", "Erdbeer", "Schoko"});
    }
}
