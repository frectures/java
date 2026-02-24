// Die Programm-Ausführung startet in der main-Methode
void main() {
    // Fordert den Benutzer auf, seinen Namen auf der Tastatur einzutippen.
    // Die eingetippte Zeichenkette wird in der Variable 'name' gespeichert.
    String name = IO.readln("Ihr Name? ");

    // Wünscht dem Benutzer einen guten Tag.
    IO.println("Guten Tag, " + name + "!");
}
