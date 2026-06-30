void geradeZiffern() {
    // Was ist der ERSTE Wert von i im Schleifenrumpf?
    // Was ist der LETZTE Wert von i im Schleifenrumpf?
    // WIE OFT wird der Schleifenrumpf betreten?
    // Welcher Wert bewirkt den ABBRUCH der Schleife?
    for (int i = 0; i < 10; i += 2) {
        IO.println(i + " ist eine gerade Ziffer");
    }
}

void ungeradeZiffern() {
    // Was ist der ERSTE Wert von i im Schleifenrumpf?
    // Was ist der LETZTE Wert von i im Schleifenrumpf?
    // WIE OFT wird der Schleifenrumpf betreten?
    // Welcher Wert bewirkt den ABBRUCH der Schleife?
    for (int i = 1; i < 10; i += 2) {
        IO.println(i + " ist eine ungerade Ziffer");
    }
}

void dreierZiffern() {
    // Was ist der ERSTE Wert von i im Schleifenrumpf?
    // Was ist der LETZTE Wert von i im Schleifenrumpf?
    // WIE OFT wird der Schleifenrumpf betreten?
    // Welcher Wert bewirkt den ABBRUCH der Schleife?
    for (int i = 9; i >= 0; i -= 3) {
        IO.println(i + " ist durch 3 teilbar");
    }
}

void main() {
    geradeZiffern();
    ungeradeZiffern();
    dreierZiffern();
}
