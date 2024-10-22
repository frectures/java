public class Einloggen {
    public static boolean einloggen(int passwortHashCode, int erlaubteVersuche) {

        String passwort = Konsole.readString("Passwort? ");
        int versuche = 1;

        while (passwort.hashCode() != passwortHashCode) {
            System.out.println("falsches Passwort");
            if (versuche == erlaubteVersuche) return false;

            passwort = Konsole.readString("Passwort? ");
            versuche = versuche + 1;
        }

        return true;
    }

    public static void main(String[] args) {
        boolean erfolgreich = einloggen(-1249690433, 5);
        if (erfolgreich) {
            System.out.println("Willkommen im System!");
        } else {
            System.out.println("zu viele Fehlversuche");
        }
    }
}
