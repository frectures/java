import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Konsole {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readString(String prompt) {
        System.out.print(prompt);
        try {
            String line = reader.readLine();
            if (line == null) throw new EOFException();
            return line;
        } catch (IOException ex) {
            throw sneaky(ex);
        }
    }

    public static char readChar(String prompt) {
        while (true) {
            String line = readString(prompt);
            if (line.length() == 1) {
                return line.charAt(0);
            }
            line = line.trim();
            if (line.length() == 1) {
                return line.charAt(0);
            }
            System.out.println("*** invalid char ***");
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            switch (line.toLowerCase()) {
                case "false":
                    return false;

                case "true":
                    return true;

                default:
                    System.out.println("*** invalid boolean ***");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid double ***");
            }
        }
    }

    public static float readFloat(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Float.parseFloat(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid float ***");
            }
        }
    }

    public static long readLong(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid long ***");
            }
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid int ***");
            }
        }
    }

    public static short readShort(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Short.parseShort(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid short ***");
            }
        }
    }

    public static byte readByte(String prompt) {
        while (true) {
            String line = readString(prompt).trim();
            try {
                return Byte.parseByte(line);
            } catch (NumberFormatException ex) {
                System.out.println("*** invalid byte ***");
            }
        }
    }

    // Any sufficiently advanced technology is indistinguishable from MAGIC
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneaky(Throwable ex) throws T {
        throw (T) ex;
    }
}
