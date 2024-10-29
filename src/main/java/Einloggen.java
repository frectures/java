public class Einloggen {
    public static void main(String[] args) {
        // Variablen deklarieren und initialisieren
        String passwort = Konsole.readString("Passwort? ");
        int versuche = 1;

        // Solange das Passwort falsch ist...
        while (passwort.hashCode() != -1249690433) {
            System.out.println("falsches Passwort");
            if (versuche == 5) return; // springt aus der aktuellen Methode raus

            // Variablen neu zuweisen
            passwort = Konsole.readString("Passwort? ");
            versuche = versuche + 1;
        }// springt zurück zum while

        System.out.println("Willkommen im System!");
    }
}
