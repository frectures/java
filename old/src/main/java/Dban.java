import java.math.BigInteger;

public final class Dban implements Comparable<Dban> {
    private final String kompaktform;

    public Dban(String kompaktform) {
        if (!isValid(kompaktform)) throw new IllegalArgumentException(kompaktform);

        this.kompaktform = kompaktform;
    }

    public String kompaktform() {
        return kompaktform;
    }

    public static boolean isValid(String kompaktform) {
        return kompaktform.length() == 22
            && kompaktform.startsWith("DE")
            // https://de.wikipedia.org/wiki/Internationale_Bankkontonummer#Pr%C3%BCfsumme
            && new BigInteger(kompaktform.substring(4, 22) + "1314" + kompaktform.substring(2, 4))
                   .mod(BigInteger.valueOf(97))
                   .equals(BigInteger.ONE);
    }

    public String bankleitzahl() {
        return kompaktform.substring( 4, 12);
    }

    public String kontonummer() {
        return kompaktform.substring(12, 22);
    }

    public String papierform() {
        return kompaktform.substring( 0,  4) + ' '
             + kompaktform.substring( 4,  8) + ' '
             + kompaktform.substring( 8, 12) + ' '
             + kompaktform.substring(12, 16) + ' '
             + kompaktform.substring(16, 20) + ' '
             + kompaktform.substring(20, 22);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Dban that
            && this.kompaktform.equals(that.kompaktform);
    }

    @Override
    public int hashCode() {
        return kompaktform.hashCode();
    }

    @Override
    public String toString() {
        return "Dban[kompaktform=" + kompaktform + ']';
    }

    @Override
    public int compareTo(Dban that) {
        return this.kompaktform.compareTo(that.kompaktform);
    }
}
