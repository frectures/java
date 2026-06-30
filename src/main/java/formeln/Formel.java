package formeln;

public abstract interface Formel {
    public abstract double ausrechnen(double x);
}

class X implements Formel {
    public double ausrechnen(double x) {
        return x;
    }
}

class Konstante implements Formel {
    private final double wert;

    Konstante(double wert) {
        this.wert = wert;
    }

    public double ausrechnen(double x) {
        return wert;
    }
}

class Plus implements Formel {
    private final Formel links, rechts;

    Plus(Formel links, Formel rechts) {
        this.links = links;
        this.rechts = rechts;
    }

    public double ausrechnen(double x) {
        return links.ausrechnen(x) + rechts.ausrechnen(x);
    }
}

class Minus implements Formel {
    private final Formel links, rechts;

    Minus(Formel links, Formel rechts) {
        this.links = links;
        this.rechts = rechts;
    }

    public double ausrechnen(double x) {
        return links.ausrechnen(x) - rechts.ausrechnen(x);
    }
}


class Mal implements Formel {
    private final Formel links, rechts;

    Mal(Formel links, Formel rechts) {
        this.links = links;
        this.rechts = rechts;
    }

    public double ausrechnen(double x) {
        return links.ausrechnen(x) * rechts.ausrechnen(x);
    }
}

class Durch implements Formel {
    private final Formel links, rechts;

    Durch(Formel links, Formel rechts) {
        this.links = links;
        this.rechts = rechts;
    }

    public double ausrechnen(double x) {
        return links.ausrechnen(x) / rechts.ausrechnen(x);
    }
}
