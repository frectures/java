public enum Month /* extends java.lang.Enum<Month> */ {
    JAN(31),      // public static final Month JAN = new Month("JAN",  0, 31);
    FEB(28.2425), // public static final Month FEB = new Month("FEB",  1, 28.2425);
    MAR(31),      // public static final Month MAR = new Month("MAR",  2, 31);
    APR(30),      // public static final Month APR = new Month("APR",  3, 30);
    MAY(31),      // public static final Month MAY = new Month("MAY",  4, 31);
    JUN(30),      // public static final Month JUN = new Month("JUN",  5, 30);
    JUL(31),      // public static final Month JUL = new Month("JUL",  6, 31);
    AUG(31),      // public static final Month AUG = new Month("AUG",  7, 31);
    SEP(30),      // public static final Month SEP = new Month("SEP",  8, 30);
    OCT(31),      // public static final Month OCT = new Month("OCT",  9, 31);
    NOV(30),      // public static final Month NOV = new Month("NOV", 10, 30);
    DEC(31);      // public static final Month DEC = new Month("DEC", 11, 31);

    /*private*/ Month(/* String name, int ordinal, */ double days) {
        /* super(name, ordinal); */
        this.days = days;
    }

    private final double days;

    public double days() {
        return days;
    }

    public Month plus(int months) {
        return Month.values()[Math.floorMod(super.ordinal() + months, 12)];
    }
}
