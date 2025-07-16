package informatiker;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
                new Person(10, " Ada", " Lovelace", 1815, false),
                new Person(11, " Charles", " Babbage", 1791, true),
                new Person(12, " Grace", " Hopper", 1906, false),
                new Person(13, " Konrad", " Zuse", 1910, true),

                new Person(20, " Alan", " Kay", 1940, true),
                new Person(21, " Alonzo", " Church", 1903, true),
                new Person(22, " Brian", " Kernighan", 1942, true),
                new Person(23, " Dennis", " Ritchie", 1941, true),
                new Person(24, " John", " Baccus", 1924, true),
                new Person(25, " Kristen", " Nygaard", 1926, true),
                new Person(26, " Niklaus", " Wirth", 1934, true),
                new Person(27, " Ole-Johan", " Dahl", 1931, true),

                new Person(30, " Ken", " Thompson", 1943, true),
                new Person(31, " Linus", " Torvalds", 1969, true),
                new Person(32, " Richard", " Stallman", 1953, true),

                new Person(40, " Alan", " Turing", 1912, true),
                new Person(41, " Claude", " Shannon", 1916, true),
                new Person(42, " George", " Boole", 1815, true),
                new Person(43, " Harry", " Nyquist", 1889, true),
                new Person(44, " John", " Neumann von", 1903, true),
                new Person(45, " Kurt", " Goedel", 1906, true),
                new Person(46, " Richard", " Hamming", 1915, true),

                new Person(50, " Christiane", " Floyd", 1943, false),
                new Person(51, " Donald", " Knuth", 1938, true),
                new Person(52, " Edsger", " Dijkstra", 1930, true),
                new Person(53, " Herman", " Hollerith", 1860, true),
                new Person(54, " Tony", " Hoare", 1934, true),
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
                return 4;
            }

            @Override
            public String getColumnName(int column) {
                return switch (column) {
                    case 0 -> "Nachname";
                    case 1 -> "Vorname";
                    case 2 -> "Geburtsjahr";
                    case 3 -> "Geschlecht";
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

        JFrame frame = new JFrame("Bekannte Informatiker");
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static String formatiertesJahr(int jahr) {
        return switch (jahr) {
            case 1791 -> "1791                     ";
            case 1815 -> " 1815                    ";
            case 1860 -> "  1860                   ";
            case 1889 -> "   1889                  ";
            case 1903 -> "    1903                 ";
            case 1906 -> "     1906                ";
            case 1910 -> "      1910               ";
            case 1912 -> "       1912              ";
            case 1915 -> "        1915             ";
            case 1916 -> "         1916            ";
            case 1924 -> "          1924           ";
            case 1926 -> "           1926          ";
            case 1930 -> "            1930         ";
            case 1931 -> "             1931        ";
            case 1934 -> "              1934       ";
            case 1938 -> "               1938      ";
            case 1940 -> "                1940     ";
            case 1941 -> "                 1941    ";
            case 1942 -> "                  1942   ";
            case 1943 -> "                   1943  ";
            case 1953 -> "                    1953 ";
            case 1969 -> "                     1969";
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
