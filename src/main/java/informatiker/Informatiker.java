package informatiker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Random;

public class Informatiker {
    private static final float SCALE_FONT_SIZE = 1.5f;

    public static void main(String[] args) {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(Informatiker::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // TODO weitere Vergleicher hinzufügen
        Vergleicher[] alleVergleicher = {
                new Unsortiert(),
                new PerNachname(),
                new PerAlter(),
                new Zweistufig(new PerAlter(), new PerNachname()),
        };

        Person[] personen = {
                new Person(10, "Ada", "Lovelace", 1815, false),
                new Person(11, "Charles", "Babbage", 1791, true),
                new Person(12, "Grace", "Hopper", 1906, false),
                new Person(13, "Konrad", "Zuse", 1910, true),

                new Person(20, "Alan", "Kay", 1940, true),
                new Person(21, "Alonzo", "Church", 1903, true),
                new Person(22, "Brian", "Kernighan", 1942, true),
                new Person(23, "Dennis", "Ritchie", 1941, true),
                new Person(24, "John", "Baccus", 1924, true),
                new Person(25, "Kristen", "Nygaard", 1926, true),
                new Person(26, "Niklaus", "Wirth", 1934, true),
                new Person(27, "Ole-Johan", "Dahl", 1931, true),

                new Person(30, "Ken", "Thompson", 1943, true),
                new Person(31, "Linus", "Torvalds", 1969, true),
                new Person(32, "Richard", "Stallman", 1953, true),

                new Person(40, "Alan", "Turing", 1912, true),
                new Person(41, "Claude", "Shannon", 1916, true),
                new Person(42, "George", "Boole", 1815, true),
                new Person(43, "Harry", "Nyquist", 1889, true),
                new Person(44, "John", "Neumann von", 1903, true),
                new Person(45, "Kurt", "Goedel", 1906, true),
                new Person(46, "Richard", "Hamming", 1915, true),

                new Person(50, "Christiane", "Floyd", 1943, false),
                new Person(51, "Donald", "Knuth", 1938, true),
                new Person(52, "Edsger", "Dijkstra", 1930, true),
                new Person(53, "Herman", "Hollerith", 1860, true),
                new Person(54, "Tony", "Hoare", 1934, true),
        };

        JTable table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);

        Font font = table.getFont();
        font = font.deriveFont(font.getSize() * SCALE_FONT_SIZE);
        table.setFont(font);
        table.setRowHeight(table.getFontMetrics(font).getHeight());

        Runnable updateTable = () -> {
            Object[][] data = new Object[personen.length][];
            for (int i = 0; i < personen.length; ++i) {
                data[i] = personen[i].toArray();
            }
            String[] columns = {"Vorname", "Nachname", "Geburtsjahr", "Geschlecht"};
            table.setModel(new DefaultTableModel(data, columns));
        };

        updateTable.run();

        JPanel buttons = new JPanel();
        for (Vergleicher vergleicher : alleVergleicher) {
            JButton button = new JButton(vergleicher.toString());
            button.addActionListener(event -> {
                quicksort(personen, 0, personen.length - 1, vergleicher);
                updateTable.run();
            });
            buttons.add(button);
        }

        JFrame frame = new JFrame("Bekannte Informatiker");
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Programming Pearls
    // 11.3 Better Quicksorts
    public static void quicksort(Person[] personen, int l, int u, Vergleicher vergleicher) {
        if (l < u) {
            swap(personen, l, random.nextInt(l, u + 1));
            Person pivot = personen[l];
            int i = l;
            int j = u + 1;
            while (true) {
                do ++i; while (i <= u && vergleicher.vergleiche(personen[i], pivot) < 0);
                do --j; while (/*     */ vergleicher.vergleiche(personen[j], pivot) > 0);
                if (i > j) break;
                swap(personen, i, j);
            }
            swap(personen, l, j);
            quicksort(personen, /**/l, j - 1, vergleicher);
            quicksort(personen, j + 1, u/**/, vergleicher);
        }
    }

    private static void swap(Person[] personen, int i, int j) {
        Person temp = personen[i];
        personen[i] = personen[j];
        personen[j] = temp;
    }

    private static final Random random = new Random();
}
