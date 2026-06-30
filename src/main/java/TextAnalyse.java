boolean enthaeltLeertaste(String text) {
    for (int i = 0; i < text.length(); ++i) {
        if (text.charAt(i) == ' ') return true; // erste Leertaste gefunden
    }
    return false; // keine Leertaste gefunden
}

int anzahlLeertasten(String text) {
    int leertasten = 0;
    for (int i = 0; i < text.length(); ++i) {
        if (text.charAt(i) == ' ') {
            ++leertasten; // weitere Leertaste gefunden
        }
    }
    return leertasten;
}

// Jedes Vorkommen eines Vokals zählt
int anzahlVokale(String text) {
    return 0;
}

// Mehrfache Vorkommen desselben Vokals zählen nur einmal
int vokalVielfalt(String text) {
    return 0;
}

// Ein Palindrom liest sich in beide Richtungen gleich,
// z.B. Anna, Bob oder Regal-Lager
boolean istPalindrom(String text) {
    return false;
}

void main() {
    String zeile = IO.readln("Text? ");

    IO.println("enthaeltLeertaste: " + enthaeltLeertaste(zeile));
    IO.println("anzahlLeertasten: " + anzahlLeertasten(zeile));
    IO.println("anzahlVokale: " + anzahlVokale(zeile));
    IO.println("vokalVielfalt: " + vokalVielfalt(zeile));
    IO.println("istPalindrom: " + istPalindrom(zeile));
}
