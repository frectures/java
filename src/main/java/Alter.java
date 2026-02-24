void main() {
    // Liest einen String von der Konsole
    String name = IO.readln("Wie heißt du? ");

    // Liest eine Ganzzahl von der Konsole
    int alter = Integer.parseInt(IO.readln("Wie alt bist du? "));

    if (alter < 0 || alter > 123) {
        IO.println("Das glaube ich nicht...");
    } else {
        IO.println("In einem Jahr bist du " + (alter + 1) + ", " + name + "!");
    }
}
