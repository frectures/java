package json;

public class Freddy {
    static void main() {
        Objekt fred = new Objekt(
                "name", new Zeichenkette("Fred"),
                "hungrig", Symbolisch.TRUE,
                "haustiere", new Zahl(0)
        );
        IO.println(fred.stringify());
    }
}
