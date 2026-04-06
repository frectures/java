## Objektorientierte Programmierung

### Motivation

- Wie viele Namen konnten wir gestern sortieren?
- Das hatte zunächst der Programmierer festgelegt:

```java
String[] namen = new String[3];
IO.println("Bitte 3 Namen eingeben:");

namen[0] = IO.readln("> ");
namen[1] = IO.readln("> ");
namen[2] = IO.readln("> ");
```

- Später dann der Anwender des Programms:

```java
int n = Integer.parseInt(IO.readln("Wie viele Namen sortieren? "));
String[] namen = new String[n];
IO.println("Bitte " + n + " Namen eingeben:");

for (int i = 0; i < n; ++i) {
    namen[i] = IO.readln("> ");
}
```

- Noch schöner wäre es, wenn der Anwender die Anzahl nicht von vornherein festlegen müsste
- sondern einfach so lange Namen eingeben könnte, bis ihm keiner mehr einfällt:

```
> q
> w
> e
> r
> t
> z
> u
> i
> o
> p
>
```

- Dazu könnte man erst mal mit einem Array der Größe 0 anfangen
- und dann jedes Mal die alten Namen in ein um 1 größeres Array umkopieren: `·`

```
{}

{q}
 ·
{q, w}
 ·  ·
{q, w, e}
 ·  ·  ·
{q, w, e, r}
 ·  ·  ·  ·
{q, w, e, r, t}
 ·  ·  ·  ·  ·
{q, w, e, r, t, z}
 ·  ·  ·  ·  ·  ·
{q, w, e, r, t, z, u}
 ·  ·  ·  ·  ·  ·  ·
{q, w, e, r, t, z, u, i}
 ·  ·  ·  ·  ·  ·  ·  ·
{q, w, e, r, t, z, u, i, o}
 ·  ·  ·  ·  ·  ·  ·  ·  ·
{q, w, e, r, t, z, u, i, o, p}
```

- Wie viele Namen mussten insgesamt umkopiert werden? `·`
- 1+2+3+4+5+6+7+8+9 = 45
- Bei 1000 Namen wären das 1+2+3+...+997+998+999 = 499500 Kopien!

```java
String[] namen = new String[0];
IO.println("Bitte beliebig viele Namen eingeben:");

String name = IO.readln("> "); // erster Name
while (!name.isBlank()) {

    // Alte Namen umkopieren                  ///
    namen = Arrays.copyOf(namen, namen.length + 1);

    // Neuen Namen eintragen
    namen[namen.length - 1] = name;

    name = IO.readln("> ");  // nächster Name
}
```

- Schlauer wäre es, am Anfang ein paar Plätze zu **reservieren**
- und die Array-Größe nur noch bei Bedarf zu **verdoppeln**:

```
{_, _}
{q, _}
{q, w}
 ·  ·
{q, w, e, _}
{q, w, e, r}
 ·  ·  ·  ·
{q, w, e, r, t, _, _, _}
{q, w, e, r, t, z, _, _}
{q, w, e, r, t, z, u, _}
{q, w, e, r, t, z, u, i}
 ·  ·  ·  ·  ·  ·  ·  ·
{q, w, e, r, t, z, u, i, o, _, _, _, _, _, _, _}
{q, w, e, r, t, z, u, i, o, p, _, _, _, _, _, _}
```

- Wie viele Namen mussten insgesamt umkopiert werden? ·
- 2+4+8 = 14
- Bei 1000 Namen wären das nur noch 2+4+8+16+32+64+128+256+512 = 1022 Kopien

```java
String[] namen = new String[2]; // Platz für 2 Namen
int used = 0;                   //    bisher 0 Namen eingetragen
IO.println("Bitte beliebig viele Namen eingeben:");

String name = IO.readln("> "); // erster Name
while (!name.isBlank()) {

    // Alle Array-Einträge mit Namen belegt?
    if (used == namen.length) {
        // Dann alte Namen umkopieren     ///
        namen = Arrays.copyOf(namen, used * 2);
    }

    // Neuen Namen eintragen
    namen[used++] = name;

    name = IO.readln("> ");  // nächster Name
}
```

- Ursprünglich ging es in unserem Programm ausschließlich um das Sortieren von Namen
- Nun müssen unsere Kollegen leider auch noch das Array-Wachstum verstehen
- Wünschenswert wäre ein spezieller Typ, der dieses Array-Wachstum versteckt:

```java
StringArrayBuilder builder = new StringArrayBuilder(2);
IO.println("Bitte beliebig viele Namen eingeben:");

String name = IO.readln("> "); // erster Name
while (!name.isBlank()) {

    // Neuen Namen eintragen
    builder.add(name);

    name = IO.readln("> ");  // nächster Name
}

String[] namen = builder.toArray();
```

- Leider existiert kein solcher Typ `StringArrayBuilder`
- Wir können ihn aber selber definieren:

```java
//           Klasse
public class StringArrayBuilder {

    //               Zustandsfelder
    private String[] array;
    private int      used;

    //     Konstruktor
    public StringArrayBuilder(int capacity) {
        // Initialisierung der Zustandsfelder
        array = new String[capacity];
        used = 0;
    }

    //          Methode
    public void add(String entry) {
        if (used == array.length) {
            array = java.util.Arrays.copyOf(array, used * 2);
        }
        array[used++] = entry;
    }

    //              Methode
    public String[] toArray() {
        return java.util.Arrays.copyOf(array, used);
    }
}
```

- Zustandsfelder, Konstruktoren und Methoden einer Klasse gehören logisch zusammen
- Wie teilt man ein großes Programm sinnvoll in mehrere Klassen auf?
- Das ist eine Kunst für sich (siehe z.B. Domain-Driven Design)

### Objektorientierte Grundlagen

- `new StringArrayBuilder(2)` erzeugt ein neues Objekt der Klasse `StringArrayBuilder`:
  - Java reserviert genügend Speicher für die beiden *Zustandsfelder* `array` und `used`
  - Java überschreibt den reservierten Speicher aus Sicherheitsgründen mit `0`-Bytes
  - Java führt den *Konstruktor* `StringArrayBuilder(int capacity)` aus, um die Zustandsfelder zu initialisieren
  - Das Ergebnis von `new StringArrayBuilder(2)` ist eine Referenz auf das soeben erzeugte Objekt
- Anschließend können *Methoden* an dem Objekt aufgerufen werden:
  - `builder.add(name)`
  - `builder.toArray()`
  - allgemein `objekt.methode(argumente)`

### Woran erkenne ich einen Konstruktor?

- Methoden haben *immer* einen Ergebnistyp und können beliebig benannt werden:
  - `public Ergebnistyp beliebigerName()`
- Konstruktoren haben *niemals* einen Ergebnistyp und müssen wie die Klasse heißen:
  - `public NameDerKlasse()`

### Wozu `private` Zustandsfelder?

1. **Konsistenz:** Private Zustandsfelder können nicht von anderen Klassen in einen inkonsistenten Zustand gebracht werden
2. **Mentale Entlastung:** Andere Klassen brauchen gar nicht wissen, dass diese privaten Zustandsfelder überhaupt existieren
3. **Wartung:** Die Auswahl privater Zustandsfelder kann sich über die Jahre ändern
   - Parade-Beispiel `java.lang.String`, siehe unten

### java.lang.String im Wandel der Zeit

- Bis 2017 reservierte Java immer 2 Byte pro `char` eines Strings:

```java
public class String {
    private final char[] value; // UTF16

    // ...

    public char charAt(int index) {
        return value[index]; // einfach
    }

    // ...
}
```

- In der Praxis verwenden viele Strings aber nur die ersten 256 von 65536 möglichen `char`s:

```

 !"#$%&'()*+,-./0123456789:;<=>?
@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_
`abcdefghijklmnopqrstuvwxyz{|}~

 ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿
ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß
àáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ
```

- Für solche Strings braucht Java 9+ nur noch 1 Byte pro `char` eines Strings:

```java
public class String {

    private final byte[] value;
    private final byte   coder; // LATIN1 (0) oder UTF16 (1)

    // ...

    public char charAt(int index) {
        // VIEL komplizierter als früher!
        if (coder == LATIN1) {
            // 1 byte in 1 char umwandeln
        } else {
            // 2 byte zu 1 char kombinieren
        }
    }

    // ...
}
```

- Diese fundamentale Änderung innerhalb der Klasse `String` hat nach außen hin nichts kaputt gemacht
  - Die `String`-Methoden verhalten sich seit 2017 nicht anders als vorher auch
- Viele Java-Entwickler wissen bis heute nichts von dieser Optimierung
  - und *brauchen* das auch gar nicht wissen!

### Tic Tac Toe (★☆☆☆☆)

- Starte die (`main`-Methode in der) Klasse `ttt/TicTacToeGUI.java`
- Klicke zufällig auf Knöpfe
  - Was funktioniert bereits?
  - Was funktioniert noch nicht?
- Studiere die andere Klasse `TicTacToe` (**ohne** `GUI`!) und identifiziere:
  - Zustandsfeld(er)
  - Konstruktor(en)
  - Methode(n)
- Ergänze `hatSpielerGewonnen` um die fehlenden Gewinn-Reihen
- Überprüfe in `istSpielfeldVoll`, ob das Spielfeld voll ist
  - Entweder mit einer Schleife über das `spielfeld`
  - Oder mit einem vierten Zustandsfeld, das in `besetze` angepasst wird
- Beim Besetzen einer Position werden *alle* Gewinn-Reihen geprüft
  - Die besetzte Position liegt aber nur auf höchstens 4 Gewinn-Reihen
  - 🏆 Optimiere die Gewinn-Prüfung basierend auf dieser Erkenntnis

### Point Cloud (★★☆☆☆)

- Starte die (`main`-Methode in der) Klasse `gravity/Cloud.java`
- Studiere die Klasse `Point` und identifiziere:
  - Zustandsfeld(er)
  - Konstruktor(en)
  - Methode(n)
- Wenn zwei Punkte sich sehr nahe kommen, werden sie ins Jenseits geschleudert
  - Physikalisch können sich die Körper-Mittelpunkte aber nicht näher kommen als `2` Pixel
  - Sorge deshalb dafür, dass `distanceSquared` den Wert `4` nicht unterschreitet
- Bisher haben alle Punkte dieselbe (implizite) Masse `5`
  - Ergänze die Klasse `Point` um 1 neues Zustandsfeld `mass`
  - Initialisiere `mass` im Konstruktor mit einem zufälligen Wert
  - Berücksichtige `mass` in der Methode `updateVelocity` bei der Berechnung der Anziehung
  - Berücksichtige `mass` in der Methode `paint` für die Größe des gezeichneten Quadrats
- 🏆 Verpasse den Punkten eine dritte Dimension

### Tetris (★★★★★)

- Starte die (`main`-Methode in der) Klasse `tetris/TetrisGUI.java`
  - Was funktioniert bereits?
  - Was funktioniert noch nicht?
- **Plenum:** Wir implementieren gemeinsam die Methode `rotate`
  - Dazu wühlen wir uns erst mal gemeinsam durch die Klasse `Tetris` (**ohne** `GUI`!)
  - Relevant sind die beiden Zustandsfelder `rotation` und `shape`
  - Was passiert, wenn wir einen vertikalen `I`-Stein am Rand drehen?
- Implementiere die Methode `removeCompleteLines` in 4 Schritten:
  1. Falls die unterste Zeile voll ist, überschreibe sie mit Nullen
     - Tipp: `java.util.Arrays.fill`
  2. Falls die unterste Zeile voll ist, kopiere die Zeilen darüber 1 Zeile nach unten
     - Tipp: `System.arraycopy`
  3. Führe Schritt 2 nicht nur für die unterste Zeile aus, sondern für *alle* Zeilen
  4. 🏆 Führe Schritt 2 nur für die Zeilen aus, in denen der Stein platziert wurde
- Implementiere die Methode `insertPenaltyLines`
  - Strafzeilen werden ganz unten eingefügt
  - Die Strafzeilen haben in einer zufälligen Spalte eine Lücke
  - Verwende als Farbwert `9`
- Bisher fällt der aktuelle Stein 1x pro Sekunde (alle 30 Ticks)
  - Für jede bisher entfernte Reihe soll der Stein 1 Tick schneller fallen
- Angenommen, es werden erst `m` Strafzeilen geschickt und dann `n` Strafzeilen
  - Alle `m` Strafzeilen sollen dieselbe (zufällige) Leer-Spalte haben
  - Alle `n` Strafzeilen sollen dieselbe (zufällige) Leer-Spalte haben
  - 🏆 Aber `m` und `n` sollen *niemals* gleich sein!
- Die scheinbar kleine Klasse `FairLetterSupplier` ist mit 81 kb die größte der gesamten Schulung!
  - Woran liegt das?
  - 🏆 Kannst du das im Klassenkommentar dokumentierte Verhalten auch platzsparender realisieren?
- Vermisst du weitere Features?
  - Dann schreibe (formlose) Tickets dafür!
