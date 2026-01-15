package informatiker;

public class Unsortiert implements Vergleicher {

    public int vergleiche(Person a, Person b) {
        return Integer.compare(a.id(), b.id());
    }

    public String toString() {
        return "unsortiert";
    }
}
