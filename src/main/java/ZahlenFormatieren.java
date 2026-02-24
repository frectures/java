String hexadezimal(int zahl) {
    char[] ziffern = new char[8]; // 4 Bit pro Hexadezimalziffer
    int ende = ziffern.length;
    int anfang = ende;
    while (zahl != 0) {
        ziffern[--anfang] = "0123456789abcdef".charAt(zahl % 16); // Divisionsrest
        zahl /= 16; // ganzzahlige Division ohne Restbildung (Runden zur 0 hin)
    }
    return new String(ziffern, anfang, ende - anfang);
}

void main() {
    // Interaktion
    int zahl = Integer.parseInt(IO.readln("    Dezimal? "));
    while (zahl != 0) {
        IO.println("Hexadezimal: " + hexadezimal(zahl));
        zahl = Integer.parseInt(IO.readln("    Dezimal? "));
    }
    // Randfälle
    IO.println("hexadezimal(MAX) -> " + hexadezimal(Integer.MAX_VALUE));
    IO.println("hexadezimal(0) -> " + hexadezimal(0));
    IO.println("hexadezimal(-1) -> " + hexadezimal(-1));
    IO.println("hexadezimal(MIN+1) -> " + hexadezimal(Integer.MIN_VALUE + 1));
    IO.println("hexadezimal(MIN) -> " + hexadezimal(Integer.MIN_VALUE));
}
