public final class Gebiet implements Comparable<Gebiet> {
    private final int plz;
    private final String ort;

    public Gebiet(int plz, String ort) {
        Contract.checkBetween(0, plz, 99_999, "plz");

        this.plz = plz;
        this.ort = ort;
    }

    public int plz() {
        return plz;
    }

    public String ort() {
        return ort;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Gebiet that
            && this.plz == that.plz
            && this.ort.equals(that.ort);
    }

    @Override
    public int hashCode() {
        return plz * 31 + ort.hashCode();
    }

    @Override
    public String toString() {
        return "Gebiet[plz=" + plz + ", ort=" + ort + ']';
    }

    @Override
    public int compareTo(Gebiet that) {
        int comparison = Integer.compare(this.plz, that.plz);
        if (comparison == 0) {
            comparison = this.ort.compareTo(that.ort);
        }
        return comparison;
    }
}
