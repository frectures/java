//           Klasse
public class StringArrayBuilder {

    //               Zustandsfelder
    private String[] array;
    private int used;

    //     Konstruktor
    public StringArrayBuilder(int capacity) {
        // Initialisierung der Zustandsfelder
        array = new String[capacity];
        used = 0;
    }

    //          Methode
    public void add(String entry) {
        if (used == array.length) {
            array = java.util.Arrays.copyOf(array, used * 2);
        }
        array[used++] = entry;
    }

    //              Methode
    public String[] toArray() {
        return java.util.Arrays.copyOf(array, used);
    }
}
