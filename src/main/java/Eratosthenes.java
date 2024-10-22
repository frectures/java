public class Eratosthenes {
    public static int[] berechnePrimzahlen(int grenze) {

        boolean[] prim = new boolean[grenze];
        java.util.Arrays.fill(prim, true);

        IntArrayBuilder primzahlen = new IntArrayBuilder(10);

        for (int i = 2; i < prim.length; ++i) {
            if (prim[i]) {
                primzahlen.add(i);

                for (int k = 2 * i; k < prim.length; k += i) {
                    prim[k] = false;
                }
            }
        }
        return primzahlen.toArray();
    }

    public static void main(String[] args) {
        int grenze = Konsole.readInt("Primzahlen bis zu welcher Grenze? ");
        int[] primzahlen = berechnePrimzahlen(grenze);
        System.out.println(java.util.Arrays.toString(primzahlen));
    }
}
