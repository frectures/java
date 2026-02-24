package informatiker;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Random;

public class Informatiker {
    private static final float SCALE_FONT_SIZE = 1.5f;

    static void main() {
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

        Person[] personen = {
                new Person(" Ada", " Lovelace", 1815, false, " Analytical Engine"),
                new Person(" Alan", " Turing", 1912, true, " Enigma"),
                new Person(" Charles", " Babbage", 1791, true, " Analytical Engine"),
                new Person(" Joan", " Clarke", 1917, false, " Enigma"),
                new Person(" Konrad", " Zuse", 1910, true, " erster moderner Computer"),

                new Person(" Alonzo", " Church", 1903, true, " Lambda-Kalkül"),
                new Person(" Claude", " Shannon", 1916, true, " Abtast-Theorem"),
                new Person(" George", " Boole", 1815, true, " boolean"),
                new Person(" Harry", " Nyquist", 1889, true, " Abtast-Theorem"),
                new Person(" John", " Neumann von", 1903, true, " Von-Neumann-Architektur"),
                new Person(" Kurt", " Goedel", 1906, true, " Entscheidungsproblem"),
                new Person(" Richard", " Hamming", 1915, true, " Hamming-Abstand"),

                new Person(" Adele", " Goldberg", 1945, false, " Smalltalk"),
                new Person(" Alan", " Kay", 1940, true, " Smalltalk"),
                new Person(" John", " Backus", 1924, true, " Fortran"),
                new Person(" John", " McCarthy", 1927, true, " LISP"),
                new Person(" Kristen", " Nygaard", 1926, true, " Simula"),
                new Person(" Ole-Johan", " Dahl", 1931, true, " Simula"),

                new Person(" Barbara", " Liskov", 1939, false, " das L in SOLID"),
                new Person(" Christiane", " Floyd", 1943, false, " STEPS"),
                new Person(" Edsger", " Dijkstra", 1930, true, " Strukturierte Programmierung"),
                new Person(" Frances", " Allan", 1932, false, " Compiler-Optimierungen"),
                new Person(" Grace", " Hopper", 1906, false, " der erste Compiler"),
                new Person(" Kateryna", " Yushchenko", 1919, false, " Zeiger"),
                new Person(" Margaret", " Hamilton", 1936, false, " Software Engineering"),

                new Person(" Donald", " Knuth", 1938, true, " TeX"),
                new Person(" Herman", " Hollerith", 1860, true, " Lochkarten"),
                new Person(" Radia", " Perlman", 1951, false, " Internet Routing"),
                new Person(" Tim", " Berners-Lee", 1955, true, " World Wide Web"),
                new Person(" Tony", " Hoare", 1934, true, " Quicksort"),
        };

        JTable table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);

        Font font = table.getFont();
        font = font.deriveFont(font.getSize() * SCALE_FONT_SIZE);
        table.setFont(font);
        table.setRowHeight(table.getFontMetrics(font).getHeight());

        AbstractTableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return personen.length;
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int column) {
                return switch (column) {
                    case 0 -> "Nachname";
                    case 1 -> "Vorname";
                    case 2 -> "Geburtsjahr";
                    case 3 -> "Geschlecht";
                    case 4 -> "Einfluss";
                    default -> throw new IllegalArgumentException("" + column);
                };
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Person person = personen[rowIndex];
                return switch (columnIndex) {
                    case 0 -> person.nachname();
                    case 1 -> person.vorname();
                    case 2 -> formatiertesJahr(person.geburtsjahr());
                    case 3 -> person.maennlich() ? "   ♂" : "♀   ";
                    case 4 -> person.einfluss();
                    default -> throw new IllegalArgumentException("" + columnIndex);
                };
            }
        };
        table.setModel(tableModel);

        JPanel buttons = new JPanel();
        for (Vergleicher vergleicher : alleVergleicher) {
            JButton button = new JButton(vergleicher.toString());
            button.addActionListener(event -> {
                quicksort(personen, 0, personen.length - 1, vergleicher);
                tableModel.fireTableDataChanged();
            });
            buttons.add(button);
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);
        table.getColumnModel().getColumn(3).setCellRenderer(renderer);

        JFrame frame = new JFrame("Prägende Informatiker");
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static String formatiertesJahr(int jahr) {
        return switch (jahr) {
            case 1791 -> "1791                          ";
            case 1815 -> " 1815                         ";
            case 1860 -> "  1860                        ";
            case 1889 -> "   1889                       ";
            case 1903 -> "    1903                      ";
            case 1906 -> "     1906                     ";
            case 1910 -> "      1910                    ";
            case 1912 -> "       1912                   ";
            case 1915 -> "        1915                  ";
            case 1916 -> "         1916                 ";
            case 1917 -> "          1917                ";
            case 1919 -> "           1919               ";
            case 1924 -> "            1924              ";
            case 1926 -> "             1926             ";
            case 1927 -> "              1927            ";
            case 1930 -> "               1930           ";
            case 1931 -> "                1931          ";
            case 1932 -> "                 1932         ";
            case 1934 -> "                  1934        ";
            case 1936 -> "                   1936       ";
            case 1938 -> "                    1938      ";
            case 1939 -> "                     1939     ";
            case 1940 -> "                      1940    ";
            case 1943 -> "                       1943   ";
            case 1945 -> "                        1945  ";
            case 1951 -> "                         1951 ";
            case 1955 -> "                          1955";
            default -> throw new IllegalArgumentException("" + jahr);
        };
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
