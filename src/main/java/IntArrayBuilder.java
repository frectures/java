//           Klasse
public class IntArrayBuilder {

    //      (Zustands-)Felder
    private int[] array;
    private int used;

    //     Konstruktor
    public IntArrayBuilder(int capacity) {
        // Initialisierung der Felder
        array = new int[capacity];
        used = 0;
    }

    //     Methode
    public void add(int entry) {
        if (used == array.length) {
            array = java.util.Arrays.copyOf(array, used * 2);
        }
        array[used++] = entry;
    }

    //     Methode
    public int[] toArray() {
        return java.util.Arrays.copyOf(array, used);
    }
}
