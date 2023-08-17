public record Gebiet16(int plz, String ort) {
    public Gebiet16 {
        Contract.checkBetween(0, plz, 99_999, "plz");
    }
}
