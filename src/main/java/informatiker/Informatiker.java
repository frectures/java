package informatiker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class Informatiker {
    public static void main(String[] args) {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(Informatiker::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // TODO weitere Vergleicher hinzufügen
        Vergleicher[] alleVergleicher = {
                new PerNachname(),
                new PerAlter(),
                new Zweistufig(new PerAlter(), new PerNachname()),
        };

        Person[] unsortiertePersonen = {
                new Person("Ada", "Lovelace", 1815, false),
                new Person("Charles", "Babbage", 1791, true),
                new Person("Grace", "Hopper", 1906, false),
                new Person("Konrad", "Zuse", 1910, true),

                new Person("Alan", "Kay", 1940, true),
                new Person("Alonzo", "Church", 1903, true),
                new Person("Brian", "Kernighan", 1942, true),
                new Person("Dennis", "Ritchie", 1941, true),
                new Person("John", "Baccus", 1924, true),
                new Person("Kristen", "Nygaard", 1926, true),
                new Person("Niklaus", "Wirth", 1934, true),
                new Person("Ole-Johan", "Dahl", 1931, true),

                new Person("Ken", "Thompson", 1943, true),
                new Person("Linus", "Torvalds", 1969, true),
                new Person("Richard", "Stallman", 1953, true),

                new Person("Alan", "Turing", 1912, true),
                new Person("Claude", "Shannon", 1916, true),
                new Person("George", "Boole", 1815, true),
                new Person("Harry", "Nyquist", 1889, true),
                new Person("John", "Neumann von", 1903, true),
                new Person("Kurt", "Goedel", 1906, true),
                new Person("Richard", "Hamming", 1915, true),

                new Person("Christiane", "Floyd", 1943, false),
                new Person("Donald", "Knuth", 1938, true),
                new Person("Edsger", "Dijkstra", 1930, true),
                new Person("Herman", "Hollerith", 1860, true),
                new Person("Tony", "Hoare", 1934, true),
        };

        JTable table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);

        Consumer<Person[]> updateTable = (personen) -> {
            Object[][] data = new Object[personen.length][];
            for (int i = 0; i < personen.length; ++i) {
                data[i] = personen[i].toArray();
            }
            String[] columns = {"Vorname", "Nachname", "Geburtsjahr", "Geschlecht"};
            table.setModel(new DefaultTableModel(data, columns));
        };

        updateTable.accept(unsortiertePersonen);

        JPanel buttons = new JPanel();
        JButton button = new JButton("unsortiert");
        button.addActionListener(event -> {
            updateTable.accept(unsortiertePersonen);
        });
        buttons.add(button);
        for (Vergleicher vergleicher : alleVergleicher) {
            button = new JButton(vergleicher.toString());
            button.addActionListener(event -> {
                Person[] personen = unsortiertePersonen.clone();
                sortiere(personen, vergleicher);
                updateTable.accept(personen);
            });
            buttons.add(button);
        }

        JFrame frame = new JFrame("Bekannte Informatiker");
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void sortiere(Person[] personen, Vergleicher vergleicher) {
        for (int n = 1; n < personen.length; ++n) {
            // Die ersten n Elemente (links von n) sind bereits sortiert;
            // nun muss Element n an die passende Stelle geschoben werden.
            int i = n;
            Person einzufuegen = personen[i];
            while ((i > 0) && vergleicher.vergleiche(einzufuegen, personen[i - 1]) < 0) {
                personen[i] = personen[i - 1];
                --i;
            }
            personen[i] = einzufuegen;
        }
    }
}
