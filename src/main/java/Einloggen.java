void main() {
    // Variablen deklarieren und initialisieren
    String passwort = IO.readln("Passwort? ");
    int versuche = 1;

    // Solange das Passwort falsch ist...
    while (passwort.hashCode() != -1249690433) {
        IO.println("falsches Passwort");
        if (versuche == 5) return; // springt aus der aktuellen Methode raus

        // Variablen neu zuweisen
        passwort = IO.readln("Passwort? ");
        versuche = versuche + 1;
    }// springt zurück zum while

    IO.println("Willkommen im System!");
}
