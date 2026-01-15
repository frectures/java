package informatiker;

public abstract interface Vergleicher {
    /**
     * NEGATIV: a gehört VOR b
     * POSITIV: a gehört NACH b
     * NULL: Reihenfolge EGAL
     */
    public abstract int vergleiche(Person a, Person b);

    public abstract String toString();
}
