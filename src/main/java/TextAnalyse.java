public class TextAnalyse {
    public static boolean enthaeltLeertaste(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == ' ') return true; // erste Leertaste gefunden
        }
        return false; // keine Leertaste gefunden
    }

    public static int anzahlLeertasten(String text) {
        int leertasten = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == ' ') {
                ++leertasten; // weitere Leertaste gefunden
            }
        }
        return leertasten;
    }

    // Jedes Vorkommen eines Vokals zählt
    public static int anzahlVokale(String text) {
        return 0;
    }

    // Mehrfache Vorkommen desselben Vokals zählen nur einmal
    public static int vokalVielfalt(String text) {
        return 0;
    }

    // Ein Palindrom liest sich in beide Richtungen gleich,
    // z.B. Anna, Bob oder Regal-Lager
    public static boolean istPalindrom(String text) {
        return false;
    }

    public static void main(String[] args) {
        String zeile = Konsole.readString("Text? ");

        System.out.println("enthaeltLeertaste: " + enthaeltLeertaste(zeile));
        System.out.println("anzahlLeertasten: " + anzahlLeertasten(zeile));
        System.out.println("anzahlVokale: " + anzahlVokale(zeile));
        System.out.println("vokalVielfalt: " + vokalVielfalt(zeile));
        System.out.println("istPalindrom: " + istPalindrom(zeile));
    }
}
