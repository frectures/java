package informatiker;

public class PerNachname implements Vergleicher {

    public int vergleiche(Person a, Person b) {
        return a.nachname().compareTo(b.nachname());
    }

    public String toString() {
        return "Nachname";
    }
}
