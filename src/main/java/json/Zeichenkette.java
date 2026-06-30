package json;

public class Zeichenkette implements Wert {
    private final String str;

    public Zeichenkette(String str) {
        this.str = str;
    }

    @Override
    public void serialize(StringBuilder sb) {
        appendLiteral(sb, str);
    }

    // Schreibt den String genau so in den StringBuilder rein,
    // wie er auch in einem Java-Programm auftauchen würde.
    public static void appendLiteral(StringBuilder sb, String s) {
        sb.append('"');

        sb.append(s);

        sb.append('"');
    }
}
