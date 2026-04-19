package cafe;

import java.math.BigDecimal;

public class FractionString {

    public static String from(double x) {

        long bits = Double.doubleToRawLongBits(x);
        int exponent = (int) (bits << 1 >>> 53) - 1023;
        if (exponent > 52) return null;

        long implicit = Long.MIN_VALUE;
        if (exponent == -1023) {
            // subnormal
            implicit = 0;
            exponent = -1022;
        }
        long mantissa = (bits << 11 | implicit) >>> 11;
        exponent = 52 - exponent;

        return from(mantissa, exponent);
    }

    private static String from(long mantissa, int exponent) {

        String numerator = "" + mantissa;
        String denominator = BigDecimal.TWO.pow(exponent).toPlainString();

        int n = numerator.length();
        int d = denominator.length();

        return "\n" + " ".repeat(Math.max(0, d - n)) + numerator + "\n"
                + "-".repeat(Math.max(n, d)) + "\n"
                + " ".repeat(Math.max(0, n - d)) + denominator + " (2^" + exponent + ")";
    }

    public static String from(float x) {

        int bits = Float.floatToRawIntBits(x);
        int exponent = (bits << 1 >>> 24) - 127;
        if (exponent > 23) return null;

        int implicit = Integer.MIN_VALUE;
        if (exponent == -127) {
            // subnormal
            implicit = 0;
            exponent = -126;
        }
        int mantissa = (bits << 8 | implicit) >>> 8;
        exponent = 23 - exponent;

        return from(mantissa, exponent);
    }
}
