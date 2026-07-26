package cafe;

public class Zufall {
    private static long seed = System.nanoTime();

    private static long next64() {
        return seed = seed * 6364136223846793005L + 1;
    }

    private static long next32() {
        return next64() >>> 32;
    }

    /**
     * @return false/true
     */
    public static boolean nextBoolean() {
        return next64() < 0;
    }

    /**
     * @return 0.0 <= result < 1.0
     */
    public static float nextFloat() {
        return (next64() >>> 40) * 0x1.0p-24f;
    }

    /**
     * @return -2147483648 <= result <= 2147483647
     */
    public static int nextInt() {
        return (int) next32();
    }

    /**
     * @return 0 <= result < bound
     */
    public static int nextInt(int bound) {
        return (int) (bound * next32() >>> 32);
    }

    /**
     * @return 0.0 <= result < 1.0
     */
    public static double nextDouble() {
        return (next64() >>> 11) * 0x1.0p-53;
    }

    /**
     * @return -9223372036854775808 <= result <= 9223372036854775807
     */
    public static long nextLong() {
        return next64() ^ next32();
    }
}
