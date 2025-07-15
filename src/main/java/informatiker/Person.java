package informatiker;

public record Person(
        int id,
        String vorname,
        String nachname,
        int geburtsjahr,
        boolean maennlich) {
}
