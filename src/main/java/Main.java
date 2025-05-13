public class Main {
    public static void main(String[] args) {

        // Liest einen String von der Konsole
        String name = Konsole.readString("Wie heißt du? ");

        // Liest eine Ganzzahl von der Konsole
        int alter = Konsole.readInt("Wie alt bist du? ");

        if (alter < 0 || alter > 123) {
            System.out.println("Das glaube ich nicht...");
        } else {
            System.out.println("In einem Jahr bist du " + (alter + 1) + ", " + name + "!");
        }
    }
}
