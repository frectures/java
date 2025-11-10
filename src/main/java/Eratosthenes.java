public class Eratosthenes {
    public static int[] berechnePrimzahlen(int grenze) {

        boolean[] prim = new boolean[grenze];
        java.util.Arrays.fill(prim, true); // jede Zahl zunächst prim, bis Gegenteil bewiesen wird

        int[] primzahlen = new int[0]; // bisher wurde noch keine Primzahl gefunden

        for (int i = 2; i < prim.length; ++i) {
            if (prim[i]) {
                // Kopiere die bisherigen Primzahlen in ein um 1 größeres Array um
                primzahlen = java.util.Arrays.copyOf(primzahlen, primzahlen.length + 1);

                // Kopiere die soeben gefundene Primzahl auf die letzte Position
                primzahlen[primzahlen.length - 1] = i;

                // Markiere alle Vielfachen von i als zusammengesetzt
                for (int k = 2 * i; k < prim.length; k += i) {
                    prim[k] = false;
                }
            }
        }
        return primzahlen;
    }

    public static void main(String[] args) {
        int grenze = Konsole.readInt("Primzahlen bis zu welcher Grenze? ");
        int[] primzahlen = berechnePrimzahlen(grenze);
        System.out.println(java.util.Arrays.toString(primzahlen));
    }
}
