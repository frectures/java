package formeln;

public class Negiert implements Formel {
    private final Formel formel;

    public Negiert(Formel formel) {
        this.formel = formel;
    }

    @Override
    public double ausrechnen(double x) {
        return -formel.ausrechnen(x);
    }
}
