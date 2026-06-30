package json;

public enum Symbolisch implements Wert {
    NULL, FALSE, TRUE;

    @Override
    public void serialize(StringBuilder sb) {
        sb.append(switch (this) {
            case NULL -> "null";
            case FALSE -> "false";
            case TRUE -> "true";
        });
    }
}
