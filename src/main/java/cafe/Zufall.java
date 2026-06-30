package cafe;

public class Zufall {
    private static long seed = System.nanoTime();

    private static long next(int bits) {
        return (seed = seed * 6364136223846793005L + 1) >>> (64 - bits);
    }

    /**
     * @return 0 <= result < 1
     */
    public static float nextFloat() {
        return next(24) * 0x1.0p-24f;
    }

    /**
     * @return 0 <= result <= 2147483647
     */
    public static int nextInt() {
        return (int) next(31);
    }

    /**
     * @return 0 <= result < 1
     */
    public static double nextDouble() {
        return next(53) * 0x1.0p-53;
    }

    /**
     * @return 0 <= result <= 9223372036854775807
     */
    public static long nextLong() {
        return next(63) ^ next(32);
    }
}
