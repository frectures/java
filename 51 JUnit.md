## JUnit

### Motivation

- In der Grundlagen-Schulung haben wir nur manuell "getestet" bzw. ausprobiert:

```java
public class TextAnalyse {
    public static boolean enthaeltLeertaste(String text) { /* ... */ }

    public static int anzahlLeertasten(String text) { /* ... */ }

    public static int anzahlVokale(String text) { /* ... */ }

    public static int vokalVielfalt(String text) { /* ... */ }

    public static boolean istPalindrom(String text) { /* ... */ }

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

- Wir müssen uns selber von der Korrektheit der Ausgaben erzeugen
- Bei jeder Änderung mussten wir erneut sinnvolle Texte eingeben
- Wahrscheinlich denken wir nicht ständig an alle Randfälle
- Deutlich sinnvoller sind automatisierte Tests mit JUnit:

```java
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextAnalyseTest {
    @Test
    public void leererStringPalindrom() {
        assertTrue(TextAnalyse.istPalindrom(""));
    }

    @Test
    public void einZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("a"));
        assertTrue(TextAnalyse.istPalindrom(" "));
        assertTrue(TextAnalyse.istPalindrom("\n"));
    }

    @Test
    public void zweiZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("aa"));
        assertTrue(TextAnalyse.istPalindrom("bb"));

        assertFalse(TextAnalyse.istPalindrom("ab"));
        assertFalse(TextAnalyse.istPalindrom("ba"));
    }

    @Test
    public void dreiZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("aaa"));
        assertTrue(TextAnalyse.istPalindrom("ada"));
        assertTrue(TextAnalyse.istPalindrom("dad"));
        assertTrue(TextAnalyse.istPalindrom("ddd"));

        assertFalse(TextAnalyse.istPalindrom("aad"));
        assertFalse(TextAnalyse.istPalindrom("add"));
        assertFalse(TextAnalyse.istPalindrom("daa"));
        assertFalse(TextAnalyse.istPalindrom("dda"));
    }

    @Test
    public void bekanntePalindrome() {
        assertTrue(TextAnalyse.istPalindrom("beheb"));
        assertTrue(TextAnalyse.istPalindrom("handnah"));
        assertTrue(TextAnalyse.istPalindrom("level"));
        assertTrue(TextAnalyse.istPalindrom("neppen"));
        assertTrue(TextAnalyse.istPalindrom("regal-lager"));
    }

    @Test
    public void grossUndKleinschreibung() {
        assertTrue(TextAnalyse.istPalindrom("Anna"));
        assertTrue(TextAnalyse.istPalindrom("Hannah"));
        assertTrue(TextAnalyse.istPalindrom("Kajak"));
        assertTrue(TextAnalyse.istPalindrom("Neffen"));
        assertTrue(TextAnalyse.istPalindrom("Otto"));
        assertTrue(TextAnalyse.istPalindrom("Radar"));
        assertTrue(TextAnalyse.istPalindrom("Reittier"));
    }
}
```

- Tests starten, Granularität:
  - einzelne Testmethode
  - einzelne Testklasse
  - alle Testklassen im selben Paket
  - alle Testklassen im Projekt
- Wir können unmöglich alle (unendlich vielen) String-Eingaben testen
- Offensichtliche Randfälle sind meist sinnvoll:
  - leerer String
  - String mit 1 Zeichen
  - ...
- **Plenum:** Welches Qualitätsproblem hat die Testmethode `grossUndKleinschreibung`?

### Annotationen

- Wie kommt JUnit an die Test-Methoden ran?
- Über Annotationen und Reflection
- Beides Blindflecken vieler Java-Programmierer
- In `AnnotationenUndReflection` erkennen wir die Test-Methoden selber:

```java
for (Method method : TextAnalyseTest.class.getDeclaredMethods()) {

    Test annotation = method.getAnnotation(Test.class);
    if (annotation != null) {
        System.out.println("Test-Methode gefunden: " + method);
    }
}
```

- **Plenum:** Lasst uns die gefunden Test-Methoden ausführen!
  - `method.invoke(new TextAnalyseTest());`
  - Fehlschlagende Tests sollen protokolliert werden
  - Ein Report, wie viele Test bestanden haben, wäre schön

> - **Übung:** Schreibe Test-Methoden für mindestens 1 der anderen 4 Methoden:
>   - enthaeltLeertaste
>   - anzahlLeertasten
>   - anzahlVokale
>   - vokalVielfalt
