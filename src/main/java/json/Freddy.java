package json;

public class Freddy {
    public static void main(String[] args) {
        Objekt fred = new Objekt(
                "name", new Zeichenkette("Fred"),
                "hungrig", Symbolisch.TRUE,
                "haustiere", new Zahl(0)
        );
        System.out.println(fred.stringify());
    }
}
