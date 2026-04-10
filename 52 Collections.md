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
void main() throws IOException {
    Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

    List<String> zeilen = Files.readAllLines(pfad);
    IO.println("Anzahl Zeilen: " + zeilen.size());
    IO.println("Listen-Klasse: " + zeilen.getClass());
}
```

- `Files.readAllLines` liefert eine `List<String>`
- Dabei legt `<String>` den Elementtyp fest: “eine Liste von Strings”
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
Anzahl Zeilen: 21936
Listen-Klasse: class java.util.ArrayList
```

- `Files.readAllLines` liefert also de facto ein `ArrayList`-Objekt zurück
  - `ArrayList` verwendet dieselbe Längen-Verdoppelung wie unser `StringArrayBuilder`
- Den Aufrufer geht die konkrete Klasse `ArrayList` aber gar nichts an
  - Ihn hat nur der abstrakte Ergebnistyp `List` zu interessieren
  - “Program to an Interface, not an Implementation”

### Wörter

- Mit einem regulären Ausdrück kann man alle Wörter hintereinander extrahieren:

```java
    String inhalt = Files.readString(pfad);
    IO.println("Anzahl Zeichen: " + inhalt.length());

    List<String> woerter = new ArrayList<>();

    Matcher matcher = Pattern.compile("\\p{IsAlphabetic}+").matcher(inhalt);
    while (matcher.find()) {           //////////////////
        String wort = matcher.group();
        woerter.add(wort.toLowerCase());
    }
    IO.println("Anzahl Wörter hintereinander: " + woerter.size());
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
    IO.println(after - before + " ms");

    IO.println("Anzahl unterschiedlicher Wörter: " + wortschatz.size());
    IO.println("Die ersten 10 Wörter im Wortschatz:");
    int i = 0;
    for (String wort : wortschatz) {
        IO.println(wort);
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
- Die unteren 4 Bits `hash & 0b1111` legen die “Zeile” von 0 bis 15 fest ¹
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

### StringSet.java

- Vervollständige die Klasse `StringSet.java` und verwende sie für Moby Dick:

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

### Maps

- Angenommen, wir wollen Telefon-Nummern auf Pizza-Firmen abbilden:

```java
var werRuftMichAn = new String[32890521]; // 125 MiB

werRuftMichAn[271025] = "Call a Pizza";
werRuftMichAn[2294010] = "Domino's";
werRuftMichAn[32890520] = "Mundfein";

IO.println(werRuftMichAn[32890519]); // null
IO.println(werRuftMichAn[32890520]); // "Mundfein"
IO.println(werRuftMichAn[32890521]); // ArrayIndexOutOfBoundsException

//                               schleift 32890521x
for (int nummer = 0; nummer < werRuftMichAn.length; ++nummer) {

    String firma = werRuftMichAn[nummer];

    if (firma != null) {
        IO.println(nummer + ": " + firma);
    }
}
```

- Arrays sind für diesen Anwendungsfall nicht geeignet
  - weil fast alle Positionen unbelegt bleiben
  - aber trotzdem Speicherplatz verbrauchen
- Maps eignen sich hier besser:

```java
var werRuftMichAn = new HashMap<Integer, String>();

werRuftMichAn.put(271025, "Call a Pizza");
werRuftMichAn.put(2294010, "Domino's");
werRuftMichAn.put(32890520, "Mundfein");

IO.println(werRuftMichAn.get(32890519)); // null
IO.println(werRuftMichAn.get(32890520)); // "Mundfein"
IO.println(werRuftMichAn.get(32890521)); // null

IO.println(werRuftMichAn.getOrDefault(32890519, "unbekannt")); // "unbekannt"
IO.println(werRuftMichAn.getOrDefault(32890520, "unbekannt")); // "Mundfein"
IO.println(werRuftMichAn.getOrDefault(32890521, "unbekannt")); // "unbekannt"

//                    schleift 3x
for (var entry : werRuftMichAn.entrySet()) {

    int   nummer = entry.getKey();
    String firma = entry.getValue();

    IO.println(nummer + ": " + firma);
}
```

- Eine `Map<Key, Value>` bildet Schlüssel auf Werte ab
  - Schlüssel + Wert = Eintrag
- `map.get(key)` liefert:
  - den zum Schlüssel zugehörigen Wert, sofern ein Eintrag existiert
  - ansonsten `null`
- `map.put(key, value)`
  - legt einen neuen Eintrag an
  - oder ersetzt den alten Eintrag
- `map.getOrDefault(key, defaultValue)` liefert:
  - den zum Schlüssel zugehörigen Wert, sofern ein Eintrag existiert
  - ansonsten `defaultValue`

### ZeichenZaehlen.java

- Rückblick auf eine ältere Beispiel-Methode `zaehleZeichen`:

```java
int[] zaehleZeichen(String text) {

    int[] zaehler = new int[65536];

    for (char ch : text.toCharArray()) {
        zaehler[ch] += 1;
    }

    return zaehler;
}
```

> **Übung**:
> - Ersetze `new int[65536]` durch `new TreeMap<Character, Integer>`

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
