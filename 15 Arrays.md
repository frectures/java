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

### Fibonacci-Zahlen

- Die ersten beiden Fibonacci-Zahlen sind 0 und 1
- Jede weitere Fibonacci-Zahl ist die Summe seiner beiden Vorgänger:

|               i | 0 | 1 | 2 | 3 | 4 | 5 | 6 |  7 |  8 |  9 | ... |
| --------------: | - | - | - | - | - | - | - | -: | -: | -: | --- |
| fib<sub>i</sub> | 0 | 1 | 1 | 2 | 3 | 5 | 8 | 13 | 21 | 34 | ... |

- Fibonacci-Zahlen kann man z.B. mit einem Array und einer Schleife berechnen:

#### long

```java
public class Fibonacci {
    public static long[] berechneFibonacciZahlen(int n) {
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

    public static void main(String[] args) {
        int n = Konsole.readInt("Wie viele Fibonacci-Zahlen? ");

        long[] fib = berechneFibonacciZahlen(n);

        System.out.println(fib.length + " Fibonacci-Zahlen:");
        System.out.println(java.util.Arrays.toString(fib));
    }
}
```

> **Plenum:**
> - Welchen Sinn haben die beiden `if`s?
> - Warum fängt die Schleife bei `2` an?
>   - Könnte man auch eine Schleife schreiben, die bei `0` anfängt?

#### BigInteger

```java
import java.math.BigInteger;

public class Fibonacci {
    public static BigInteger[] berechneFibonacciZahlen(int n) {
        if (n < 2) n = 2;

        BigInteger[] fib = new BigInteger[n];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        for (int i = 2; i < n; ++i) {
            fib[i] = fib[i - 2].add(fib[i - 1]);
        }

        return fib;
    }

    public static void main(String[] args) {
        int n = Konsole.readInt("Wie viele Fibonacci-Zahlen? ");

        BigInteger[] fib = berechneFibonacciZahlen(n);

        System.out.println(fib.length + " Fibonacci-Zahlen:");
        System.out.println(java.util.Arrays.toString(fib));
    }
}
```

### Namen sortieren

```java
public class NamenSortieren {
    public static void main(String[] args) {

        String[] namen = new String[3];
        System.out.println("Bitte 3 Namen eingeben:");

        namen[0] = Konsole.readString("> ");
        namen[1] = Konsole.readString("> ");
        namen[2] = Konsole.readString("> ");

        java.util.Arrays.sort(namen);
        System.out.println("\nAufsteigend sortiert:");

        System.out.println(namen[0]);
        System.out.println(namen[1]);
        System.out.println(namen[2]);
    }
}
```

- Die 3 Zuweisungen können mit einer for-Schleife vereinfacht werden
- Die 3 Ausgaben können mit einer foreach-Schleife vereinfacht werden:

```java
public class NamenSortieren {
    public static void main(String[] args) {

        String[] namen = new String[3];
        System.out.println("Bitte 3 Namen eingeben:");
        for (int i = 0; i < namen.length; ++i) {
            namen[i] = Konsole.readString("> ");
        }

        java.util.Arrays.sort(namen);
        System.out.println("\nAufsteigend sortiert:");

        for (String name : namen) {
            System.out.println(name);
        }
    }
}
```

> **Übung:**
> - Anstatt fest 3 Namen zu verarbeiten, soll der Anwender bei Programmstart gefragt werden, wie viele Namen er verarbeiten möchte
> - Falls die eingegebenen Namen bereits aufsteigend sortiert sind, sollen diese nicht erneut auf die Konsole geschrieben werden, sondern lediglich der Hinweis “Die Namen sind bereits aufsteigend sortiert.”
> - Lagere die Logik zum Erkennen der Sortierung in eine eigene Methode `public static boolean istSortiert(String[] namen)` aus

| String-Vergleich      | Relative Lage im Wörterbuch |
| --------------------- | --------------------------- |
| `s.compareTo(t) < 0`  | `s` kommt vor `t`           |
| `s.compareTo(t) == 0` | `s` und `t` sind gleich     |
| `s.compareTo(t) > 0`  | `s` kommt nach `t`          |

### Zeichen-Häufigkeit

```java
public class ZeichenHaeufigkeit {
    public static int[] zaehleZeichen(String text) {
        //                                        65536
        int[] zaehler = new int[Character.MAX_VALUE + 1];

        for (char ch : text.toCharArray()) {
            zaehler[ch] += 1;
        }

        return zaehler;
    }

    public static void main(String[] args) {
        String text = Konsole.readString("Text? ");

        int[] zaehler = zaehleZeichen(text);

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            System.out.println(ch + ": " + zaehler[ch]);
        }
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

### 🏆 Zahlen formatieren

- `int zahl = Konsole.readInt("")` liest Dezimalziffern 0-9 ein
- `System.out.println(zahl)` schreibt Dezimalziffern 0-9 raus
- ⚠️ Deshalb unterliegen viele Anfänger dem Irrtum, `int` sei eine Dezimalzahl
- Tatsächlich ist `int` aber eine 32-Bit-Binärzahl
- `readInt` muss aufwändig von Dezimal-`String` nach `int` umwandeln
- `println` muss aufwändig von `int` nach Dezimal-`String` umwandeln
- Hier beispielhaft die Umwandlung von `int` in einen Hexadezimal-`String`:

```java
public class Formatierung {
    public static String hexadezimal(int zahl) {
        char[] ziffern = new char[8]; // 4 Bit pro Hexadezimalziffer
        int ende = ziffern.length;
        int anfang = ende;
        while (zahl != 0) {
            ziffern[--anfang] = "0123456789abcdef".charAt(zahl % 16); // Divisionsrest
            zahl /= 16; // ganzzahlige Division ohne Restbildung (Runden zur 0 hin)
        }
        return new String(ziffern, anfang, ende - anfang);
    }

    public static void main(String[] args) {
        // Interaktion
        int zahl = Konsole.readInt("    Dezimal? ");
        while (zahl != 0) {
            System.out.println("Hexadezimal: " + hexadezimal(zahl));
            zahl = Konsole.readInt("    Dezimal? ");
        }
        // Randfälle
        System.out.println("hexadezimal(MAX) -> " + hexadezimal(Integer.MAX_VALUE));
        System.out.println("hexadezimal(0) -> " + hexadezimal(0));
        System.out.println("hexadezimal(-1) -> " + hexadezimal(-1));
        System.out.println("hexadezimal(MIN+1) -> " + hexadezimal(Integer.MIN_VALUE + 1));
        System.out.println("hexadezimal(MIN) -> " + hexadezimal(Integer.MIN_VALUE));
    }
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
>   - `public static String oktal(int zahl)` (Basis Acht)
>   - `public static String binaer(int zahl)` (Basis Zwei)
> - Extrahiere den gemeinsamen Code in eine Hilfsmethode:
>   - `public static String formatiere(int zahl, int basis)`
