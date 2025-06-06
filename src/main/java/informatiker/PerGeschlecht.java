package informatiker;

public class PerGeschlecht implements Vergleicher {

    public int vergleiche(Person a, Person b) {
        return Boolean.compare(a.maennlich(), b.maennlich());
    }

    public String toString() {
        return "Geschlecht";
    }
}
