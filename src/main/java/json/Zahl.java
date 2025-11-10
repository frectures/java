package json;

public class Zahl implements Wert {
    private final double number;

    public Zahl(double number) {
        this.number = number;
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append(number);
    }
}
