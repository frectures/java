## Objektorientierte Programmierung

### Motivation

- Der eigentliche Primzahl-Algorithmus wird durch das Array-Wachstum verkompliziert:

```java
public class Eratosthenes {
    public static int[] berechnePrimzahlen(int grenze) {

        boolean[] prim = new boolean[grenze];
        java.util.Arrays.fill(prim, true);

        int[] primzahlen = new int[10]; // erst mal wenig Speicher reservieren
        int gefunden = 0;               // bisher wurde noch keine Primzahl gefunden

        for (int i = 2; i < prim.length; ++i) {
            if (prim[i]) {
                // Ist kein Platz mehr in dem Array?
                if (gefunden == primzahlen.length) {
                    // Dann kopiere die bisherigen Primzahlen in ein doppelt so großes Array um
                    primzahlen = java.util.Arrays.copyOf(primzahlen, primzahlen.length * 2);
                }

                primzahlen[gefunden++] = i;

                for (int k = 2 * i; k < prim.length; k += i) {
                    prim[k] = false;
                }
            }
        }
        return java.util.Arrays.copyOf(primzahlen, gefunden);
    }
}
```

- Deutlich übersichtlicher wäre es, für das Array-Wachstum einen eigenen Typ zu benutzen:

```java
public class Eratosthenes {
    public static int[] berechnePrimzahlen(int grenze) {

        boolean[] prim = new boolean[grenze];
        java.util.Arrays.fill(prim, true);

        IntArrayBuilder primzahlen = new IntArrayBuilder(10); // Objekt-Erzeugung

        for (int i = 2; i < prim.length; ++i) {
            if (prim[i]) {
                primzahlen.add(i); // Methoden-Aufruf

                for (int k = 2 * i; k < prim.length; k += i) {
                    prim[k] = false;
                }
            }
        }
        return primzahlen.toArray(); // Methoden-Aufruf
    }
}
```

- Leider existiert kein solcher Typ `IntArrayBuilder`
- Wir können ihn aber selber definieren:

```java
//           Klasse
public class IntArrayBuilder {

    //      Zustandsfelder
    private int[] array;
    private int used;

    //     Konstruktor   
    public IntArrayBuilder(int capacity) {
        // Initialisierung der Zustandsfelder
        array = new int[capacity];
        used = 0;
    }

    //     Methode
    public void add(int entry) {
        if (used == array.length) {
            array = java.util.Arrays.copyOf(array, used * 2);
        }
        array[used++] = entry;
    }

    //     Methode
    public int[] toArray() {
        return java.util.Arrays.copyOf(array, used);
    }
}
```

### Objektorientierte Grundlagen

- Eine Java-Klasse hat 2 Daseinsberechtigungen:
  1. Namensraum für statische Methoden und Konstanten
  2. Beschreibung von Zustand und Verhalten ihrer *Objekte*
- `new IntArrayBuilder(10)` erzeugt ein neues Objekt der Klasse `IntArrayBuilder`:
  - Java reserviert genügend Heap-Speicher für die beiden *Zustandsfelder* `array` und `used`
  - Java überschreibt den reservierten Speicher aus Sicherheitsgründen mit `0`-Bytes
  - Java führt den *Konstruktor* `IntArrayBuilder(int capacity)` aus, um die Zustandsfelder zu initialisieren
  - Das Ergebnis von `new IntArrayBuilder(10)` ist eine Referenz auf das soeben erzeugte Objekt
- Anschließend können *Methoden* an dem Objekt aufgerufen werden:
  - `primzahlen.add(i)`
  - `primzahlen.toArray()`
  - allgemein `objekt.methode(argumente)`
  - Beim Methoden-Aufruf werden Argumente wie gewohnt an Parameter gebunden: `entry = i`
  - Außerdem wird das Objekt vor dem Punkt an die `this`-Referenz gebunden: `this = primzahlen`

### Woran erkenne ich einen Konstruktor?

- Methoden haben *immer* einen Ergebnistyp und können beliebig benannt werden:
  - `public Ergebnistyp beliebigerName()`
- Konstruktoren haben *niemals* einen Ergebnistyp und müssen wie die Klasse heißen:
  - `public NameDerKlasse()`

### Wieso steht kein `static` mehr bei den Methoden?

- Statische Methoden werden nicht an Objekten aufgerufen, sondern an der Klasse selbst:
  - `String.valueOf(42)`
  - `String.format("%08x", 123456789)`
  - `String.join(" und ", namen)`
  - allgemein `Klasse.methode(argumente)`
- Normale Methoden werden dagegen an Objekten einer Klasse aufgerufen:
  - `"Montag".length()`
  - `"hallo welt".charAt(5)`
  - `"vodkatrinken".contains("katrin")`
  - allgemein `objekt.methode(argumente)`
- Normale Methoden kann man nicht an einer Klasse aufrufen:
  - ❌ `String.length()` — Die Länge *welchen* Strings? Es schwirren Tausende im Arbeitsspeicher rum...
  - ❌ `IntArrayBuilder.toArray()` — *Welchen* IntArrayBuilder in ein Array kopieren?

### Wozu `private` Zustandsfelder?

1. **Konsistenz:** Private Zustandsfelder können nicht von anderen Klassen in einen inkonsistenten Zustand gebracht werden
2. **Mentale Entlastung:** Andere Klassen brauchen gar nicht wissen, dass es diese privaten Zustandsfelder überhaupt gibt
3. **Kapselung/Wartung:** Die Auswahl privater Zustandsfelder kann sich über die Jahre ändern
   - siehe `java.lang.String`

### java.lang.String im Wandel der Zeit

- Bis 2017 reservierte Java immer 2 Byte pro `char` eines Strings:

```java
public class String {
    private final char[] value; // UTF16

    // ...

    public char charAt(int index) {
        return value[index];
    }

    // ...
}
```

- Sehr viele Strings verwenden aber nur die ersten 256 von 65536 möglichen `char`s:

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
    // LATIN1 oder UTF16
    private final byte coder;

    // ...

    public char charAt(int index) {
        // stark vereinfacht dargestellt:
        if (coder == LATIN1) {
            // 8 Bit vorzeichenlos auf 16 Bit erweitern
            return (char) (value[index] & 255);
        } else {
            // zwei Bytes zu 16 Bit zusammenkleben:
            index *= 2;
            return (value[index] & 255) << 8 | (value[index + 1] & 255);
        }
    }

    // ...
}
```

- Diese fundamentale Änderung innerhalb der Klasse `String` hat nach außen hin nichts kaputt gemacht
  - Die `String`-Methoden verhalten sich seit 2017 nicht anders als vorher auch
- Viele Java-Entwickler wissen bis heute nichts von dieser Optimierung
  - und *brauchen* das auch gar nicht wissen!

### Tic Tac Toe

- Starte die (`main`-Methode in der) Klasse `TicTacToeGUI`
- Klicke zufällig auf Knöpfe
  - Was funktioniert bereits?
  - Was funktioniert noch nicht?
- Studiere die Klasse `TicTacToe` und identifiziere:
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

### Point Cloud

- Starte die (`main`-Methode in der) Klasse `Cloud`
- Studiere die Klasse `Point` und identifiziere:
  - Zustandsfeld(er)
  - Konstruktor(en)
  - Methode(n)
- Wenn zwei Punkte sich sehr nahe kommen, werden sie ins Jenseits geschleudert
  - Sorge dafür, dass `distanceSquared` einen sinnvollen Mindestwert nicht unterschreitet
- Bisher haben alle Punkte dieselbe (implizite) Masse
  - Ergänze die Klasse `Point` um 1 neues Zustandsfeld `mass`
  - Initialisiere `mass` im Konstruktor mit einem zufälligen Wert
  - Berücksichtige `mass` in der Methode `updateVelocity` bei der Berechnung der Anziehung
  - Berücksichtige `mass` in der Methode `paint` für die Größe des gezeichneten Quadrats
- 🏆 Verpasse den Punkten eine dritte Dimension

### Tetris

- Starte die (`main`-Methode in der) Klasse `TetrisGUI`
  - Was funktioniert bereits?
  - Was funktioniert noch nicht?
- Implementiere die Methode `rotate`
  - Beachte die Zustandsfelder `rotation` und `shape`
- Implementiere die Methode `removeCompleteLines`
  - Probiere ausführlich mit dem langen `I`-Stein
- Implementiere die Methode `insertPenaltyLines`
  - Strafzeilen werden ganz unten eingefügt
  - Die Strafzeilen haben in einer zufälligen Spalte eine Lücke
  - Verwende als Farbwert `9`
- Bisher fällt der aktuelle Stein 1x pro Sekunde (alle 30 Ticks)
  - Je mehr Reihen man insgesamt entfernt hat, desto schneller soll er fallen
  - Standard ist alle 10 Reihen einen Tick schneller
- Vermisst du weitere Features?
  - Dann schreibe (formlose) Tickets dafür!
- Die 5 Zustandsfelder `letterSupplier`, `letter`, `rotation`, `shape` und `position` gehören logisch zusammen
  - Wahrscheinlich ist es sinnvoll, diese 5 Zustandsfelder eine eigene Klasse `Piece` auszulagern
  - Welcher Code kann in die neue Klasse `Piece` mitwandern, damit sie nicht bloß ein Datensack ist?
- Die scheinbar kleine Klasse `FairLetterSupplier` ist mit 81 kb die größte der gesamten Schulung!
  - Woran liegt das?
  - 🏆 Kannst du das im Klassenkommentar dokumentierte Verhalten auch platzsparender realisieren?
