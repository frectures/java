public final class Contract {
    private Contract() {
        // prevent instantiation
    }

    public static void checkBetween(int lower, int value, int upper, String variable) {
        if (value < lower) {
            throw new IllegalArgumentException(variable + " " + value + " < " + lower);
        }
        if (value > upper) {
            throw new IllegalArgumentException(variable + " " + value + " > " + upper);
        }
    }
}
