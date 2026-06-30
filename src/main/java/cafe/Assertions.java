package cafe;

public class Assertions {

    public static final String EXPECTED = "     expected: ";
    public static final String ACTUAL = "\n       actual: ";

    public static int passed;

    public static void assertEquals(long expected, long actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(short expected, short actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(byte expected, byte actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(char expected, char actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(boolean expected, boolean actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertFalse(boolean b) {
        assertEquals(false, b);
    }

    public static void assertTrue(boolean b) {
        assertEquals(true, b);
    }

    public static void assertEquals(double expected, double actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(float expected, float actual) {
        if (expected != actual) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }

    public static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) throw new AssertionFailed(EXPECTED + expected + ACTUAL + actual);
        ++passed;
    }
}

class AssertionFailed extends RuntimeException {
    public AssertionFailed(String message) {
        super(message);
    }
}
