package json;

public class Objekt implements Wert {
    private final String[] keys;
    private final Wert[] values;

    public Objekt(
            String key1, Wert value1
    ) {
        keys = new String[]{key1};
        values = new Wert[]{value1};
    }

    public Objekt(
            String key1, Wert value1,
            String key2, Wert value2
    ) {
        keys = new String[]{key1, key2};
        values = new Wert[]{value1, value2};
    }

    public Objekt(
            String key1, Wert value1,
            String key2, Wert value2,
            String key3, Wert value3
    ) {
        keys = new String[]{key1, key2, key3};
        values = new Wert[]{value1, value2, value3};
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append('{');
        for (int i = 0; i < keys.length; ++i) {
            Zeichenkette.appendLiteral(sb, keys[i]);
            sb.append(": ");
            values[i].serialize(sb);
            sb.append(", ");
        }
        sb.append('}');
    }
}
