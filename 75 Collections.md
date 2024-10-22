## Collections

- Iterable
  - Collection
    - List
      - `ArrayList`
      - `LinkedList`
      - `Arrays.asList(array)`
    - Set
      - `HashSet`
      - `TreeSet`
      - `LinkedHashSet`
    - Queue
      - `ArrayDeque`
      - `LinkedList`
      - `PriorityQueue`
- Map
  - `HashMap`
  - `TreeMap`
  - `LinkedHashMap`

### List

```java
public class MobyDick {
    public static void main(String[] args) throws IOException {
        Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

        List<String> zeilen = Files.readAllLines(pfad);
        System.out.println("Anzahl Zeilen: " + zeilen.size());
        System.out.println("Listen-Klasse: " + zeilen.getClass());
    }
}
```

- `Files.readAllLines` liefert eine `List<String>`
- Dabei legt `<String>` den Elementtyp fest: "eine Liste von Strings"
- `List` ist ein Interface, welches das allgemeinere Interface `Collection` erweitert:

```java
package java.util;

public interface Collection<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);


    boolean add(E e);

    boolean remove(Object o);

    void clear();

    // ...
}
```

```java
package java.util;

public interface List<E> extends Collection<E> {

    E get(int index);

    E set(int index, E element);

    void add(int index, E element);

    E remove(int index);


    int indexOf(Object o);

    int lastIndexOf(Object o);


    List<E> subList(int fromIndex, int toIndex);

    // ...
}
```

- Elemente haben also nur in Listen einen Index
- Das Programm schreibt auf die Konsole:

```
Anzahl Zeilen: 22316
Listen-Klasse: class java.util.ArrayList
```

- `Files.readAllLines` liefert also de facto ein `ArrayList`-Objekt zurück
  - `ArrayList` verwendet dieselbe Längen-Verdoppelung wie unser `IntArrayBuilder`
- Den Aufrufer geht die konkrete Klasse `ArrayList` aber gar nichts an
  - Ihn hat nur der abstrakte Ergebnistyp `List` zu interessieren
  - "Program to an Interface, not an Implementation"

### Wörter

- Mit einem regulären Ausdrück kann man alle Wörter hintereinander extrahieren:

```java
        String inhalt = Files.readString(pfad);
        System.out.println("Anzahl Zeichen: " + inhalt.length());

        List<String> woerter = new ArrayList<>();

        Matcher matcher = Pattern.compile("[A-Za-z]+").matcher(inhalt);
        while (matcher.find()) {           /////////
            String wort = matcher.group();
            woerter.add(wort.toLowerCase());
        }
        System.out.println("Anzahl Wörter hintereinander: " + woerter.size());
```

### Wortschatz

- Der Wortschatz besteht aus allen *verschiedenen* Wörtern im Text:

```java
        Collection<String> wortschatz = new ArrayList<>();

        long before = System.currentTimeMillis();
        for (String wort : woerter) {
            if (!wortschatz.contains(wort)) {
                // Duplikate verhindern
                wortschatz.add(wort);
            }
        }
        long after = System.currentTimeMillis();
        System.out.println(after - before + " ms");

        System.out.println("Anzahl unterschiedlicher Wörter: " + wortschatz.size());
        System.out.println("Die ersten 10 Wörter im Wortschatz:");
        int i = 0;
        for (String wort : wortschatz) {
            System.out.println(wort);
            if (++i == 10) break;
        }
```

> **Plenum:**
> - Starte das Programm und fülle Zeile 1 in untiger Tabelle aus
> - Ersetze `new ArrayList` durch `new HashSet` und fülle Zeile 2 aus
> - dto. für `TreeSet` und `LinkedHashSet`
> - Welche Iterations-Reihenfolge haben Wörter in einem `TreeSet` bzw. `LinkedHashSet` offenbar?

| Klasse          | Laufzeit | Die ersten 10 Wörter im Wortschatz |
| --------------- | -------- | ---------------------------------- |
| `ArrayList`     |          |                                    |
| `HashSet`       |          |                                    |
| `TreeSet`       |          |                                    |
| `LinkedHashSet` |          |                                    |

### HashSet

- Ein HashSet kann man sich als zweidimensionale Datenstruktur vorstellen
- So könnte ein HashSet nach dem Einfügen der ersten 8 Wörter intern aussehen:

```
[ 0]
[ 1] the
[ 2]
[ 3] or
[ 4]
[ 5]
[ 6]
[ 7] gutenberg, of
[ 8]
[ 9] project, moby
[10]
[11]
[12]
[13] dick
[14] ebook
[15]
```

- Die Verteilung mag auf den ersten Blick zufällig aussehen, ist sie aber nicht:

| `wort`    | `wort.hashCode()`                    | `wort.hashCode() & 0b1111` |
| --------- | -----------------------------------: | -------------------------- |
| the       |                1110000000111**0001** | 1                          |
| project   | 1110110110010000010010110001**1001** | 9                          |
| gutenberg | 1100110001011010101010101010**0111** | 7                          |
| ebook     |      10110111101100000001100**1110** | 14                         |
| of        |                     11011101**0111** | 7                          |
| moby      |           110011001110010111**1001** | 9                          |
| dick      |           101111000010111010**1101** | 13                         |
| or        |                     11011110**0011** | 3                          |
| the 👈    |                1110000000111**0001** | 1                          |
| whale     |      11010111101101011010010**1001** | 9                          |

- `"the".hashCode()` = `'t'*31*31 + 'h'*31 + 'e'` = `114801` = `0b11100000001110001`
- Die unteren 4 Bits `hash & 0b1111` legen die "Zeile" von 0 bis 15 fest ¹
- Das neunte Wort `"the"` 👈 wird *nicht* erneut gespeichert
  - weil in Zeile 1 bereits ein `"the"` gespeichert ist
  - Andere Zeilen mussten dafür gar nicht untersucht werden! 🚀

¹ Idealerweise verwendet man so viele Bits, dass Anzahl Zeilen ≈ Anzahl Elemente gilt

### Keine Duplikate

```java
        Set<String> wortschatz = new HashSet<>();

        for (String wort : woerter) {
            if (!wortschatz.contains(wort)) {
                // Duplikate verhindern
                wortschatz.add(wort);
            }
        }
```

- Mengen enthalten grundsätzlich keine Duplikate
- Die `add`-Methode ignoriert das Element, wenn es schon enthalten ist
- Deshalb ist das `if` bei den `Set`-Klassen redundant:

```java
        Set<String> wortschatz = new HashSet<>();

        for (String wort : woerter) {
            wortschatz.add(wort);
        }
```

- Anstatt jedes Element einzeln hinzuzufügen, kann man `addAll` verwenden:

```java
        Set<String> wortschatz = new HashSet<>();

        wortschatz.addAll(woerter);
```

- Zuletzt kann man diese beiden Zeilen zu einer einzigen verschmelzen:

```java
        Set<String> wortschatz = new HashSet<>(woerter);
```

- Lustigerweise führt das zu einer anderen Iterations-Reihenfolge der Elemente
- `HashSet` verspricht aber grundsätzlich keine bestimmte Iterations-Reihenfolge

> **Hyrum's Law:** “With a sufficient number of users of an API,  
> it does not matter what you promise in the contract;  
> All observable behaviors of your system will be depended on by somebody.”

### StringSet

- Vervollständige die Klasse `StringSet` und verwende sie für Moby Dick:

```java
public class StringSet {
    // Grundidee: Viele sehr kurze Arrays anstatt einem langen Array
    private final String[][] arrays;

    public StringSet() {
        arrays = new String[32768][]; // MUSS eine Zweierpotenz sein!
        java.util.Arrays.fill(arrays, new String[0]); // anstatt null
    }

    public void add(String s) {
        // Aus jedem String kann man seine zugehörige Zeile berechnen
        int zeile = s.hashCode() & (arrays.length - 1);

        // Strings in derselben Zeile teilen sich dasselbe innere Array
        String[] array = arrays[zeile];

        // TODO Wenn das Wort noch nicht enthalten ist, füge es hinzu.
        // TODO Dazu muss das innere Array um 1 Element vergrößert werden. (+1)
    }

    public int size() {
        // TODO Entweder die Längen aller inneren Arrays aufaddieren
        // TODO oder ein neues Zustandsfeld beim Einfügen pflegen
        return 123;
    }

    public String[] first10() {
        // TODO
        return arrays[0];
    }
}
```

- Probiere verschiedene 2er-Potenzen für die Länge von `arrays` aus
  - Welchen Einfluss hat das auf die gemessene Zeit?

### Map

- Rückblick auf eine ältere Beispiel-Methode `zaehleZeichen`:

```java
    public static long[] zaehleZeichen(String text) {
        //                                          65536
        long[] zaehler = new long[Character.MAX_VALUE + 1];

        for (char codeUnit : text.toCharArray()) {
            zaehler[codeUnit] += 1;
        }

        return zaehler;
    }
```

- Leider kommt die Methode nicht mit Zeichen zurecht, die 2 `char`s benötigen 😭
- Lösung: `int` statt `char`

```java
    public static long[] zaehleZeichen(String text) {
        //                                             1114112
        long[] zaehler = new long[Character.MAX_CODE_POINT + 1];

        for (int codePoint : text.codePoints().toArray()) {
            zaehler[codePoint] += 1;
        }

        return zaehler;
    }
```

- Nun verbraucht das Array allerdings 4,4 MB statt 0,26 MB...
- Wenn die meisten Einträge in einem `T[]` nie angefasst werden, ist `Map<Integer, T>` eine speichersparende Alternative:

```java
public class ZeichenHaeufigkeit {
    public static Map<Integer, Long> zaehleZeichen(String text) {

        Map<Integer, Long> zaehler = new TreeMap<>();

        for (int codePoint : text.codePoints().toArray()) {

            Long n = zaehler.get(codePoint);
            // get liefert null, falls kein Eintrag zu dem Schlüssel existiert
            if (n == null) {
                // neuen Eintrag anlegen
                zaehler.put(codePoint, 1L);
            } else {
                // alten Eintrag ersetzen
                zaehler.put(codePoint, n + 1);
            }
        }

        return zaehler;
    }

    public static void main(String[] args) {
        String text = Konsole.readString("Text? ");

        Map<Integer, Long> zaehler = zaehleZeichen(text);

        // Kleinbuchstaben
        for (char ch = 'a'; ch <= 'z'; ++ch) {

            Long n = zaehler.get((int) ch);
            if (n != null) {
                System.out.println(ch + ": " + n);
            }
        }

        // Emojis
        for (Map.Entry<Integer, Long> entry : zaehler.entrySet()) {

            Integer codePoint = entry.getKey();
            if (codePoint > Character.MAX_VALUE) {
                System.out.print(Character.toChars(codePoint));

                Long n = entry.getValue();
                System.out.println(": " + n);
            }
        }
    }
}
```

- Eine `Map<Key, Value>` bildet Schlüssel auf Werte ab
  - Schlüssel plus Wert = Eintrag
- `map.get(key)` liefert:
  - den zum Schlüssel zugehörigen Wert, sofern ein Eintrag existiert
  - ansonsten `null`
- `map.put(key, value)`
  - legt einen neuen Eintrag an
  - oder ersetzt den alten Eintrag
- `map.getOrDefault(key, defaultValue)` liefert:
  - den zum Schlüssel zugehörigen Wert, sofern ein Eintrag existiert
  - ansonsten `defaultValue`

```java
        for (int codePoint : text.codePoints().toArray()) {

            Long n = zaehler.getOrDefault(codePoint, 0L);

            zaehler.put(codePoint, n + 1);
        }
```

> **Übung:**
> - Vervollständige folgende Methode zum Zählen von *Wörtern* (statt *Buchstaben*)
> - Welches Wort kommt am häufigsten vor?
> - Welches Wort kommt am seltensten vor?

```java
    public static Map<String, Long> zaehleWoerter(String text) {
        // TODO
        return null;
    }
```

### Wieso `Integer` statt `int`?

- Zur Erinnerung, Java hat 8 primitive Typen:
  - `boolean`
  - `byte`, `short`, `char`, `int`, `long`
  - `float`, `double`
- Die Typen in spitzen Klammern müssen aber Referenztypen sein:
  - `Collection<E>`
  - `Map<K, V>`
- Deshalb definiert Java zu jedem primitiven Typ einen ähnlich buchstabierten Referenztyp:

| Primitiver Typ | Referenztyp |
| -------------- | ----------- |
| `boolean`      | `Boolean`   |
| `byte`         | `Byte`      |
| `short`        | `Short`     |
| `char`         | `Character` |
| `int`          | `Integer`   |
| `long`         | `Long`      |
| `float`        | `Float`     |
| `double`       | `Double`    |

- In beide Richtungen existieren implizite Typumwandlungen:

```java
int a = 123;

Integer b = a;
//      b = Integer.valueOf(a);

int c = b;
//  c = b.intValue();
```

- Obige 8 Referenztypen sind im Prinzip ganz normale Klassen mit einem primitiven Zustandsfeld:

```java
package java.lang;

public class Integer {

    private final int value;

    @Deprecated // use Integer.valueOf instead
    public Integer(int i) {
        value = i;
    }

    public int intValue() {
        return value;
    }

    public static Integer valueOf(int i) {
        if (-128 <= i && i <= 127) {
            return cache[i + 128];
        } else {
            return new Integer(i);
        }
    }

    private static final Integer[] cache = {
        new Integer(-128),
        new Integer(-127),
        new Integer(-126),
        // ...
        new Integer(125),
        new Integer(126),
        new Integer(127),
    };

    // ...
}
```

- Referenztypen enthalten `null` als Spezialwert
  - Referenz auf *kein* Objekt
- Primitive Typen haben keinen solchen Spezialwert
  - Insbesondere ist `0` *nicht* `null`
