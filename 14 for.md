## Zählschleifen mit `for`

- Die bedingte `while`-Schleife kann man auch zum Zählen verwenden:

```java
//  Initialisierung
int i = 0;

//    Bedingung
while (i < 10) {

    // Schleifenrumpf
    System.out.println(i + " ist eine Ziffer");

    // Veränderung
    ++i;
}
```

- Initialisierung, Bedingung und Veränderung gehören logisch zusammen
- Die `for`-Schleife verheiratet diese 3 Zutaten syntaktisch miteinander:

```java
//   Initialisierung
//   |          Bedingung
//   |          |       Veränderung
//   |          |       |
for (int i = 0; i < 10; ++i) {

    // Schleifenrumpf
    System.out.println(i + " ist eine Ziffer");
}
```

- Dieses `for`-Beispiel verhält sich identisch zum `while`-Beispiel
- ⚠️ Insbesondere wird die Veränderung grundsätzlich *nach* dem Schleifenrumpf ausgeführt
  - egal ob mittels `++i` oder `i++` oder `i += 1` oder `i = i + 1`

### Ziffern

> **Übung:**
> - Beantworte folgende 12 Fragen:

```java
public class Ziffern {
    public static void geradeZiffern() {
        // Was ist der ERSTE Wert von i im Schleifenrumpf?
        // Was ist der LETZTE Wert von i im Schleifenrumpf?
        // WIE OFT wird der Schleifenrumpf betreten?
        // Welcher Wert bewirkt den ABBRUCH der Schleife?
        for (int i = 0; i < 10; i += 2) {
            System.out.println(i + " ist eine gerade Ziffer");
        }
    }

    public static void ungeradeZiffern() {
        // Was ist der ERSTE Wert von i im Schleifenrumpf?
        // Was ist der LETZTE Wert von i im Schleifenrumpf?
        // WIE OFT wird der Schleifenrumpf betreten?
        // Welcher Wert bewirkt den ABBRUCH der Schleife?
        for (int i = 1; i < 10; i += 2) {
            System.out.println(i + " ist eine ungerade Ziffer");
        }
    }

    public static void dreierZiffern() {
        // Was ist der ERSTE Wert von i im Schleifenrumpf?
        // Was ist der LETZTE Wert von i im Schleifenrumpf?
        // WIE OFT wird der Schleifenrumpf betreten?
        // Welcher Wert bewirkt den ABBRUCH der Schleife?
        for (int i = 9; i >= 0; i -= 3) {
            System.out.println(i + " ist durch 3 teilbar");
        }
    }

    public static void main(String[] args) {
        geradeZiffern();
        ungeradeZiffern();
        dreierZiffern();
    }
}
```

### Text-Analyse

```
jshell> "hi".length()
           2

jshell> "hi".charAt(0)
        'h'

jshell> "hi".charAt(1)
         'i'

jshell> "hi".charAt(2)
           java.lang.StringIndexOutOfBoundsException
```

- `length()` ist die Anzahl Zeichen
- `charAt(i)` ist das Zeichen an Stelle `i`
- Idiom zum Schleifen über alle Stellen:
  - `for (int i = 0; i < text.length(); ++i)`

```java
public class TextAnalyse {
    public static boolean enthaeltLeertaste(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == ' ') return true; // erste Leertaste gefunden
        }
        return false; // keine Leertaste gefunden
    }

    public static int anzahlLeertasten(String text) {
        int leertasten = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == ' ') {
                ++leertasten; // weitere Leertaste gefunden
            }
        }
        return leertasten;
    }

    // Jedes Vorkommen eines Vokals zählt
    public static int anzahlVokale(String text) {
        return 0;
    }

    // Mehrfache Vorkommen desselben Vokals zählen nur einmal
    public static int vokalVielfalt(String text) {
        return 0;
    }

    // Ein Palindrom liest sich in beide Richtungen gleich,
    // z.B. Anna, Bob oder Regal-Lager
    public static boolean istPalindrom(String text) {
        return false;
    }

    public static void main(String[] args) {
        String zeile = Konsole.readString("Text? ");

        System.out.println("enthaeltLeertaste: " + enthaeltLeertaste(zeile));
        System.out.println("anzahlLeertasten: " + anzahlLeertasten(zeile));
        System.out.println("anzahlVokale: " + anzahlVokale(zeile));
        System.out.println("vokalVielfalt: " + vokalVielfalt(zeile));
        System.out.println("istPalindrom: " + istPalindrom(zeile));
    }
}
```

> **Übung:**
> - `enthaeltLeertaste`:
>   - Anfänger schreiben häufig `if (text.charAt(i) == ' ') return true; else return false;`
>   - Was passiert dann?
> - `anzahlLeertasten`:
>   - Anfänger schreiben häufig `int leertasten;` statt `int leertasten = 0;`
>   - Was passiert dann?
> - Implementiere `anzahlVokale`
> - Implementiere `vokalVielfalt`
> - Implementiere `istPalindrom`
> - 🏆 Kommt deine Implementation von `istPalindrom` mit Emojis zurecht?
>   - Der Datentyp `char` ist nur 16 Bit breit
>   - Seit Juli 1996 gibt es aber mehr als 2<sup>16</sup> Unicode-Zeichen
>   - Deshalb brauchen neuere Unicode-Zeichen tendenziell 2 `char`s
>   - ☕ `Character.isSurrogate`
