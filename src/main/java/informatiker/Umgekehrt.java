package informatiker;

/**
 * Vergleicht zwei Personen anhand eines primären Vergleichers.
 * Wenn die beiden Personen laut diesem primären Vergleicher gleich sind,
 * dann werden die Personen anhand eines sekundären Vergleichers verglichen.
 */
public class Umgekehrt implements Vergleicher {
    private final Vergleicher primaer;

    public Umgekehrt(Vergleicher primaer) {
        this.primaer = primaer;
    }

    public int vergleiche(Person a, Person b) {
        return primaer.vergleiche(b, a);
    }

    public String toString() {
        return "(umgekehrt " + primaer + ")";
    }
}
