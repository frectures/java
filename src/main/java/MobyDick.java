void main() throws IOException {

    Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

    String inhalt = Files.readString(pfad);

    IO.println("Anzahl Zeichen: " + inhalt.length());
}
