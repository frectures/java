package json;

public interface Wert {
    void serialize(StringBuilder sb);

    default String stringify() {
        StringBuilder sb = new StringBuilder();
        serialize(sb);
        return sb.toString();
    }
}
