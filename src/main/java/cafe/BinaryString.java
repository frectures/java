package cafe;

import java.nio.charset.StandardCharsets;

public class BinaryString {

    public static String from(long value) {
        return from(value, 64 + 7, 0x0101010101010100L);
    }

    public static String from(int value) {
        return from(value, 32 + 3, 0x01010100);
    }

    public static String from(short value) {
        return from(value, 16 + 1, 0x0100);
    }

    public static String from(byte value) {
        return from(value, 8, 0);
    }

    public static String from(char value) {
        return from(value, 16 + 1, 0x0100);
    }

    public static String from(double value) {
        return from(Double.doubleToRawLongBits(value), 64 + 2, 0x8010000000000000L);
    }

    public static String from(float value) {
        return from(Float.floatToRawIntBits(value), 32 + 2, 0x80800000L);
    }

    private static String from(long value, int width, long spaces) {
        byte[] latin1 = new byte[width];
        int i = width;
        while (i > 0) {
            if ((spaces & 1) != 0) latin1[--i] = (byte) ' ';
            spaces >>>= 1;

            latin1[--i] = (byte) ((value & 1) != 0 ? '1' : '·');
            value >>>= 1;
        }
        return new String(latin1, StandardCharsets.ISO_8859_1);
    }

    public static String powersOfTwo(long value, int width, String minus) {
        value <<= 64 - width;
        var builder = new StringBuilder();
        if (value < 0) {
            builder.append(minus);
        }
        String plus = "";
        for (int i = width - 1; i >= 0; --i) {
            if (value < 0) {
                builder.append(plus).append(POWERS_OF_TWO[i]);
                plus = " + ";
            }
            value <<= 1;
        }
        return builder.isEmpty() ? "0" : builder.toString();
    }

    private static final String[] POWERS_OF_TWO = {
            "1",
            "2",
            "4",
            "8",
            "16",
            "32",
            "64",
            "128",
            "256",
            "512",
            "1024",
            "2048",
            "4096",
            "8192",
            "16384",
            "32768",
            "65536",
            "131072",
            "262144",
            "524288",
            "1048576",
            "2097152",
            "4194304",
            "8388608",
            "16777216",
            "33554432",
            "67108864",
            "134217728",
            "268435456",
            "536870912",
            "1073741824",
            "2147483648",
            "4294967296",
            "8589934592",
            "17179869184",
            "34359738368",
            "68719476736",
            "137438953472",
            "274877906944",
            "549755813888",
            "1099511627776",
            "2199023255552",
            "4398046511104",
            "8796093022208",
            "17592186044416",
            "35184372088832",
            "70368744177664",
            "140737488355328",
            "281474976710656",
            "562949953421312",
            "1125899906842624",
            "2251799813685248",
            "4503599627370496",
            "9007199254740992",
            "18014398509481984",
            "36028797018963968",
            "72057594037927936",
            "144115188075855872",
            "288230376151711744",
            "576460752303423488",
            "1152921504606846976",
            "2305843009213693952",
            "4611686018427387904",
            "9223372036854775808",
    };
}
