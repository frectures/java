void main() throws IOException {
    Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

    String inhalt = Files.readString(pfad);
    IO.println("Anzahl Zeichen: " + inhalt.length());

    List<String> woerter = new ArrayList<>();

    Matcher matcher = Pattern.compile("\\p{IsAlphabetic}+").matcher(inhalt);
    while (matcher.find()) {
        String wort = matcher.group();
        woerter.add(wort.toLowerCase());
    }
    IO.println("Anzahl Wörter hintereinander: " + woerter.size());

    StringSet wortschatz = new StringSet();

    long before = System.currentTimeMillis();
    for (String wort : woerter) {
        wortschatz.add(wort);
    }
    long after = System.currentTimeMillis();
    IO.println(after - before + " ms");

    IO.println("Anzahl unterschiedlicher Wörter: " + wortschatz.size());
    IO.println("Die ersten 10 Wörter im Wortschatz:");
    for (String wort : wortschatz.first10()) {
        IO.println(wort);
    }
}
