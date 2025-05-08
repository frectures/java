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
import java.util.List;

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
Anzahl Zeilen: 21940
Listen-Klasse: class java.util.ArrayList
```

- `Files.readAllLines` liefert also de facto ein `ArrayList`-Objekt zurück
  - `ArrayList` verwendet intern ein Array, daher der Name
- Den Aufrufer geht die konkrete Klasse `ArrayList` aber gar nichts an
  - Ihn hat nur der abstrakte Ergebnistyp `List` zu interessieren
  - "Program to an Interface, not an Implementation"

### Wörter

- Mit einem regulären Ausdrück kann man alle Wörter hintereinander extrahieren:

```java
        String inhalt = Files.readString(pfad);
        System.out.println("Anzahl Zeichen: " + inhalt.length());

        List<String> woerter = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\p{IsAlphabetic}+").matcher(inhalt);
        while (matcher.find()) {           //////////////////
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

- Nun verbraucht das Array allerdings 8,8 MB statt 0,52 MB...
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

        for (Map.Entry<Integer, Long> entry : zaehler.entrySet()) {

            int codePoint = entry.getKey();
            long count = entry.getValue();

            if (codePoint <= Character.MAX_VALUE) {
                System.out.println(            (char) codePoint  + ": " + count);
            } else {
                System.out.println(Character.toString(codePoint) + ": " + count);
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
> - Vervollständige folgende 3 Methoden zum Zählen von *Wörtern* (statt *Buchstaben*)
> - 🏆 Ändere den Ergebnistyp von `haeufigstesWort` auf `Map.Entry<String, Long>`
>   - damit man nicht nur das häufigste Wort selbst liefert, sondern auch dessen Häufigkeit
> - 🏆 Ändere den Ergebnistyp von `seltenstesWort` auf `Map<String, Long>`
>   - weil wahrscheinlich mehrere Wörter gleich selten sind

```java
    public static Map<String, Long> zaehleWoerter(String text) {
        // TODO
        return null;
    }

    public static String haeufigstesWort(Map<String, Long> zaehler) {
        // TODO
        return null;
    }

    public static String seltenstesWort(Map<String, Long> zaehler) {
        // TODO
        return null;
    }
```

## Collection 'literals'

### ArrayList (Java 1.2)

```java
List<Integer> primes = new ArrayList<>();
primes.add(2);
primes.add(3);
primes.add(5);
primes.add(7);

primes.set(0, 1); // ok
primes.add(11);   // ok
```

### Array view (Java 1.2)

```java
List<Integer> primes = Arrays.asList(2, 3, 5, 7);

primes.set(0, 1); // ok
primes.add(11);   // java.lang.UnsupportedOperationException
```

### Read-only view (Java 1.2)

```java
List<Integer> primes = Collections.unmodifiableList(Arrays.asList(2, 3, 5, 7));

primes.set(0, 1); // java.lang.UnsupportedOperationException
primes.add(11);   // java.lang.UnsupportedOperationException
```

### Immutable lists (Java 9)

```java
List<Integer> primes = List.of(2, 3, 5, 7);

primes.set(0, 1); // java.lang.UnsupportedOperationException
primes.add(11);   // java.lang.UnsupportedOperationException
```

- `List.of` is overloaded for up to 10 arguments
  - More arguments are handled via varargs
  - `List.of()` always returns the same empty list
  - 1 or 2 elements do not require a backing array
- `List.copyOf` creates an immutable list from a source collection
  - If the source collection is already an immutable list, no copy is performed

### Immutable sets (Java 9)

```java
Set<Integer> primes = Set.of(2, 3, 5, 7);
```

- All `List.of` and `List.copyOf` bullet points also apply to `Set.of` and `Set.copyOf`

### Immutable maps (Java 9)

- For up to 10 mappings, pass the keys and values directly to `Map.of`:

```java
Map<String, String> capitals = Map.of(
        "China", "Peking",
        "Japan", "Tokio",
        "Kongo", "Kinshasa",
        "Russland", "Moskau",
        "Korea", "Seoul",
        "Indonesion", "Jakarta",
        "Mexiko", "Mexiko-Stadt",
        "Thailand", "Bangkok",
        "Peru", "Lima",
        "Vereinigtes Königreich", "London");
```

- For 11 or more mappings, use `Map.ofEntries`: 

```java
import static java.util.Map.entry;

Map<String, String> capitals = Map.ofEntries(
        entry("China", "Peking"),
        entry("Japan", "Tokio"),
        entry("Kongo", "Kinshasa"),
        entry("Russland", "Moskau"),
        entry("Korea", "Seoul"),
        entry("Indonesion", "Jakarta"),
        entry("Mexiko", "Mexiko-Stadt"),
        entry("Thailand", "Bangkok"),
        entry("Peru", "Lima"),
        entry("Vereinigtes Königreich", "London"),
        entry("Iran", "Teheran"));
```

- All `Set.of` and `Set.copyOf` bullet points also apply to `Map.of` and `Map.copyOf`
- The backing array contains the keys and values directly, not references to entry objects
  - Immutable maps require significantly less memory than `java.util.HashMap`
