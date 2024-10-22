package informatiker;

public record Person(String vorname, String nachname, int geburtsjahr, boolean maennlich) {
    public Object[] toArray() {
        return new Object[]{vorname, nachname, geburtsjahr, maennlich ? "♂" : "♀"};
    }
}
