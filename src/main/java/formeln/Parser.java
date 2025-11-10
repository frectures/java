package formeln;

public class Parser {
    private final char[] text;
    private int index;

    private Parser(String text) {
        this.text = text.replace(" ", "").concat(" ").toCharArray();
    }

    public static Formel parse(String text) {
        return new Parser(text).strichrechnung();
    }

    private Formel strichrechnung() {
        Formel formel = punktrechnung();
        while (true) {
            switch (text[index]) {
                case '+':
                    ++index;
                    formel = new Plus(formel, punktrechnung());
                    break; // ohne break würde es in Zeile 25 weitergehen

                case '-':
                    ++index;
                    formel = new Minus(formel, punktrechnung());
                    break; // ohne break würde es in Zeile 30 weitergehen

                default:
                    return formel;
            }
        }
    }

    private Formel punktrechnung() {
        Formel formel = primaer();
        while (true) {
            switch (text[index]) {
                case '*':
                    ++index;
                    formel = new Mal(formel, primaer());
                    break; // ohne break würde es in Zeile 45 weitergehen

                case '/':
                    ++index;
                    formel = new Durch(formel, primaer());
                    break; // ohne break würde es in Zeile 50 weitergehen

                default:
                    return formel;
            }
        }
    }

    public Formel primaer() {
        switch (text[index]) {
            case 'x':
            case 'X':
                ++index;
                return new X();

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                double wert = text[index++] - '0';
                while ('0' <= text[index] && text[index] <= '9') {
                    wert = wert * 10 + (text[index++] - '0');
                }
                return new Konstante(wert);

            case '(':
                ++index; // (
                Formel formel = strichrechnung();
                ++index; // )
                return formel;

            default:
                throw new IllegalArgumentException(new String(text, 0, index) + " @ " + new String(text, index, text.length - index));
        }
    }
}
