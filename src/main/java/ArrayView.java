public class ArrayView {
    // TODO implement Iterable interface

    private final Object[] array;

    public ArrayView(Object[] array) {
        this.array = array;
    }

    static void main() {
        ArrayView sorten = new ArrayView(new String[]{"Vanille", "Erdbeer", "Schoko"});

        // TODO loop over sorten

        // TODO call next() too often
    }
}
