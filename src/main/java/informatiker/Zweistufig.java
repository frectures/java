package informatiker;

/**
 * Vergleicht zwei Personen anhand eines primären Vergleichers.
 * Wenn die beiden Personen laut diesem primären Vergleicher gleich sind,
 * dann werden die Personen anhand eines sekundären Vergleichers verglichen.
 */
public class Zweistufig implements Vergleicher {
    private final Vergleicher primaer;
    private final Vergleicher sekundaer;

    public Zweistufig(Vergleicher primaer, Vergleicher sekundaer) {
        this.primaer = primaer;
        this.sekundaer = sekundaer;
    }

    public int vergleiche(Person a, Person b) {
        int ergebnis = primaer.vergleiche(a, b);
        if (ergebnis == 0) {
            ergebnis = sekundaer.vergleiche(a, b);
        }
        return ergebnis;
    }

    public String toString() {
        return "(" + primaer + " dann " + sekundaer + ")";
    }
}
