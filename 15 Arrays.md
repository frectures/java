## Arrays

```
  0   1   2   3   4   5   6   7   8   9
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
  ^
  |
+-|-+
| º | T[] a = new T[10];
+---+
```

- Ein Array `new T[n]` besteht aus `n` durchnummerierten `T`-Variablen:
  - `a[0]` ist die erste Variable
  - `a[n-1]` ist die letzte Variable
  - ⚠️ `a[n]` ist _keine_ gültige Variable: `java.lang.ArrayIndexOutOfBoundsException`
  - `a.length` ist `n`

### Fibonacci.java

- Die ersten beiden Fibonacci-Zahlen sind 0 und 1
- Jede weitere Fibonacci-Zahl ist die Summe seiner beiden Vorgänger:

|               i | 0 | 1 | 2 | 3 | 4 | 5 | 6 |  7 |  8 |  9 | ... |
| --------------: | - | - | - | - | - | - | - | -: | -: | -: | --- |
| fib<sub>i</sub> | 0 | 1 | 1 | 2 | 3 | 5 | 8 | 13 | 21 | 34 | ... |

- Fibonacci-Zahlen kann man z.B. mit einem Array und einer Schleife berechnen:

#### long

```java
long[] berechneFibonacciZahlen(int n) {
    if (n < 2) n = 2;
    if (n > 93) n = 93;

    long[] fib = new long[n];
    fib[0] = 0L;
    fib[1] = 1L;

    for (int i = 2; i < n; ++i) {
        fib[i] = fib[i - 2] + fib[i - 1];
    }

    return fib;
}

void main() {
    int n = Integer.parseInt(IO.readln("Wie viele Fibonacci-Zahlen? "));

    long[] fib = berechneFibonacciZahlen(n);

    IO.println(fib.length + " Fibonacci-Zahlen:");
    IO.println(Arrays.toString(fib));
}
```

> **Plenum:**
> - Welchen Sinn haben die beiden `if`s?
> - Warum fängt die Schleife bei `2` an?
>   - Könnte man auch eine Schleife schreiben, die bei `0` anfängt?

#### BigInteger

```java
BigInteger[] berechneFibonacciZahlen(int n) {
    if (n < 2) n = 2;

    BigInteger[] fib = new BigInteger[n];
    fib[0] = BigInteger.ZERO;
    fib[1] = BigInteger.ONE;

    for (int i = 2; i < n; ++i) {
        fib[i] = fib[i - 2].add(fib[i - 1]);
    }

    return fib;
}

void main() {
    int n = Integer.parseInt(IO.readln("Wie viele Fibonacci-Zahlen? "));

    BigInteger[] fib = berechneFibonacciZahlen(n);

    IO.println(fib.length + " Fibonacci-Zahlen:");
    IO.println(Arrays.toString(fib));
}
```

### NamenSortieren.java

```java
void main() {

    String[] namen = new String[3];
    IO.println("Bitte 3 Namen eingeben:");

    namen[0] = IO.readln("> ");
    namen[1] = IO.readln("> ");
    namen[2] = IO.readln("> ");

    Arrays.sort(namen);
    IO.println("\nAufsteigend sortiert:");

    IO.println(namen[0]);
    IO.println(namen[1]);
    IO.println(namen[2]);
}
```

- Die 3 Zuweisungen können mit einer for-Schleife realisiert werden
- Die 3 Ausgaben können mit einer foreach-Schleife realisiert werden:

```java
void main() {

    String[] namen = new String[3];
    IO.println("Bitte 3 Namen eingeben:");

    for (int i = 0; i < namen.length; ++i) {
        namen[i] = IO.readln("> ");
    }

    Arrays.sort(namen);
    IO.println("\nAufsteigend sortiert:");

    for (String name : namen) {
        IO.println(name);
    }
}
```

| String-Vergleich      | Relative Lage im Wörterbuch |
| --------------------- | --------------------------- |
| `s.compareTo(t) < 0`  | `s` kommt vor `t`           |
| `s.compareTo(t) == 0` | `s` und `t` sind gleich     |
| `s.compareTo(t) > 0`  | `s` kommt nach `t`          |

> **Übung:**
> - Sind die eingegebenen Namen bereits aufsteigend sortiert?
>   - Dann sollen diese *nicht* erneut auf die Konsole geschrieben werden
>   - sondern lediglich der Hinweis “Die Namen sind bereits aufsteigend sortiert”
> - Probiere alle 6 Permutationen von `Alice`, `Bob`, `Charlie` aus:
>   - `Alice`, `Bob`, `Charlie` → “Die Namen sind bereits aufsteigend sortiert”
>   - `Alice`, `Charlie`, `Bob` → sortieren wie bisher
>   - `Bob`, `Alice`, `Charlie` → sortieren wie bisher
>   - `Bob`, `Charlie`, `Alice` → sortieren wie bisher
>   - `Charlie`, `Alice`, `Bob` → sortieren wie bisher
>   - `Charlie`, `Bob`, `Alice` → sortieren wie bisher
> - Lagere die Logik zum Erkennen der Sortierung in eine eigene Methode `boolean istSortiert(String[] namen)` aus
> - Anstatt fest 3 Namen zu verarbeiten, soll der Anwender bei Programmstart gefragt werden, wie viele Namen er verarbeiten möchte

### ZeichenZaehlen.java

```java
int[] zaehleZeichen(String text) {

    int[] zaehler = new int[65536];

    for (char ch : text.toCharArray()) {
        zaehler[ch] += 1;
    }

    return zaehler;
}

void main() {
    String text = IO.readln("Text? ");

    int[] zaehler = zaehleZeichen(text);

    for (char ch = 'a'; ch <= 'z'; ++ch) {
        IO.println(ch + ": " + zaehler[ch]);
    }
}
```

> **Übung:**
> - Schreibe nur Häufigkeiten von Buchstaben auf die Konsole, die auch im Text vorkommen
> - Ignoriere schon beim Zählen Groß- und Kleinschreibung im Text
>   - ☕ `Character.toLowerCase`
> - Lies einen zweiten Text ein und bestimme, ob dieser ein Anagramm des ersten Texts ist, z.B.
>   1. New York Times
>   2. Monkeys Write

### 🏆 ZahlenFormatieren.java

- `Integer.parseInt` erwartet Dezimalziffern 0-9
- `IO.println` generiert Dezimalziffern 0-9
- ⚠️ Deshalb unterliegen viele Anfänger dem Irrtum, `int` sei eine Dezimalzahl
- Tatsächlich ist `int` aber eine 32-Bit-Binärzahl
- `parseInt` muss aufwändig von Dezimal-`String` nach `int` umwandeln
- `println` muss aufwändig von `int` nach Dezimal-`String` umwandeln
- Hier beispielhaft die Umwandlung von `int` in einen Hexadezimal-`String`:

```java
String hexadezimal(int zahl) {
    char[] ziffern = new char[8]; // 4 Bit pro Hexadezimalziffer
    int ende = ziffern.length;
    int anfang = ende;
    while (zahl != 0) {
        ziffern[--anfang] = "0123456789abcdef".charAt(zahl % 16); // Divisionsrest
        zahl /= 16; // ganzzahlige Division ohne Restbildung (Runden zur 0 hin)
    }
    return new String(ziffern, anfang, ende - anfang);
}

void main() {
    // Interaktion
    int zahl = Integer.parseInt(IO.readln("    Dezimal? "));
    while (zahl != 0) {
        IO.println("Hexadezimal: " + hexadezimal(zahl));
        zahl = Integer.parseInt(IO.readln("    Dezimal? "));
    }
    // Randfälle
    IO.println("hexadezimal(MAX) -> " + hexadezimal(Integer.MAX_VALUE));
    IO.println("hexadezimal(0) -> " + hexadezimal(0));
    IO.println("hexadezimal(-1) -> " + hexadezimal(-1));
    IO.println("hexadezimal(MIN+1) -> " + hexadezimal(Integer.MIN_VALUE + 1));
    IO.println("hexadezimal(MIN) -> " + hexadezimal(Integer.MIN_VALUE));
}
```

> **Übung:**
> - Der Randfall 0 funktioniert noch nicht
>   - Warum nicht?
>   - Behebe das Problem
> - Negative Zahlen funktionieren noch nicht
>   - Warum nicht?
>   - Behebe das Problem
> - Die kleinste negative Zahl funktioniert noch nicht
>   - Warum nicht?
>   - Behebe das Problem
> - Schreibe 2 weitere Methoden:
>   - `String oktal(int zahl)` (Basis Acht)
>   - `String binaer(int zahl)` (Basis Zwei)
> - Extrahiere den gemeinsamen Code in eine Hilfsmethode:
>   - `String formatiere(int zahl, int basis)`
