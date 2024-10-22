import java.util.ArrayList;

public class NamenSortieren {
    public static void main(String[] args) {

        ArrayList<String> namen = new ArrayList<String>();
        System.out.println("Bitte beliebig viele Namen eingeben,");
        System.out.println("ein leerer Name beendet die Eingabe:");

        String name = Konsole.readString("> ");
        while (!name.isBlank()) {
            namen.add(name);
            name = Konsole.readString("> ");
        }

        java.util.Collections.sort(namen);
        System.out.println("\nAufsteigend sortiert:");

        for (int i = 0; i < namen.size(); i++) {
            System.out.println(namen.get(i));
        }
    }
}
