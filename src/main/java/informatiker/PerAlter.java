package informatiker;

public class PerAlter implements Vergleicher {

    public int vergleiche(Person a, Person b) {
        return Integer.compare(a.geburtsjahr(), b.geburtsjahr());
    }

    public String toString() {
        return "Alter";
    }
}
