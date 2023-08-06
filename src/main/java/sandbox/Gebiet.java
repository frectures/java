package sandbox;

public class Gebiet {
    private final int plz;
    private final String ort;

    public Gebiet(int plz, String ort) {
        // TODO validate constructor parameters
        this.plz = plz;
        this.ort = ort;
    }

    public int plz() {
        return plz;
    }

    public String ort() {
        return ort;
    }
}
