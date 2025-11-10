public class Formatierung {
    public static String hexadezimal(int zahl) {
        char[] ziffern = new char[8]; // 4 Bit pro Hexadezimalziffer
        int ende = ziffern.length;
        int anfang = ende;
        while (zahl != 0) {
            ziffern[--anfang] = "0123456789abcdef".charAt(zahl % 16); // Divisionsrest
            zahl /= 16; // ganzzahlige Division ohne Restbildung (Runden zur 0 hin)
        }
        return new String(ziffern, anfang, ende - anfang);
    }

    public static void main(String[] args) {
        // Interaktion
        int zahl = Konsole.readInt("    Dezimal? ");
        while (zahl != 0) {
            System.out.println("Hexadezimal: " + hexadezimal(zahl));
            zahl = Konsole.readInt("    Dezimal? ");
        }
        // Randfälle
        System.out.println("hexadezimal(MAX) -> " + hexadezimal(Integer.MAX_VALUE));
        System.out.println("hexadezimal(0) -> " + hexadezimal(0));
        System.out.println("hexadezimal(-1) -> " + hexadezimal(-1));
        System.out.println("hexadezimal(MIN+1) -> " + hexadezimal(Integer.MIN_VALUE + 1));
        System.out.println("hexadezimal(MIN) -> " + hexadezimal(Integer.MIN_VALUE));
    }
}
