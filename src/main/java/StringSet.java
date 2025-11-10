public class StringSet {
    // Grundidee: Viele sehr kurze Arrays anstatt einem langen Array
    private final String[][] arrays;

    public StringSet() {
        arrays = new String[32768][]; // MUSS eine Zweierpotenz sein!
        java.util.Arrays.fill(arrays, new String[0]); // anstatt null
    }

    public void add(String s) {
        // Aus jedem String kann man seine zugehörige Zeile berechnen
        int zeile = s.hashCode() & (arrays.length - 1);

        // Strings in derselben Zeile teilen sich dasselbe innere Array
        String[] array = arrays[zeile];

        // TODO Wenn das Wort noch nicht enthalten ist, füge es hinzu.
        // TODO Dazu muss das innere Array um 1 Element vergrößert werden. (+1)
    }

    public int size() {
        // TODO Entweder die Längen aller inneren Arrays aufaddieren
        // TODO oder ein neues Zustandsfeld beim Einfügen pflegen
        return 123;
    }

    public String[] first10() {
        // TODO
        return arrays[0];
    }
}
