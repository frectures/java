public enum Month0 /* extends java.lang.Enum<Month0> */ {
    JAN,    // public static final Month0 JAN = new Month0("JAN",  0);
    FEB,    // public static final Month0 FEB = new Month0("FEB",  1);
    MAR,    // public static final Month0 MAR = new Month0("MAR",  2);
    APR,    // public static final Month0 APR = new Month0("APR",  3);
    MAY,    // public static final Month0 MAY = new Month0("MAY",  4);
    JUN,    // public static final Month0 JUN = new Month0("JUN",  5);
    JUL,    // public static final Month0 JUL = new Month0("JUL",  6);
    AUG,    // public static final Month0 AUG = new Month0("AUG",  7);
    SEP,    // public static final Month0 SEP = new Month0("SEP",  8);
    OCT,    // public static final Month0 OCT = new Month0("OCT",  9);
    NOV,    // public static final Month0 NOV = new Month0("NOV", 10);
    DEC;    // public static final Month0 DEC = new Month0("DEC", 11);
/*
    private Month0(String name, int ordinal) {
        super(name, ordinal);
    }
*/
    public double days() {
        return switch (this) {
            case FEB                -> 28.2425;
            case APR, JUN, SEP, NOV -> 30;
            default                 -> 31;
        };
    }

    public Month0 plus(int months) {
        return Month0.values()[Math.floorMod(super.ordinal() + months, 12)];
    }


    // Object.equals
    // Object.hashCode

    // Enum.toString
    // Enum.compareTo
}
