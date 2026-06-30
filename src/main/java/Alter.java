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

    switch (alter) {
        case 2, 3, 5, 7, 11 -> IO.println("Du bist ein Primzahl-Kind!");

        case 13, 17, 19 -> IO.println("Du bist ein Primzahl-Teenager!");
    }
}
