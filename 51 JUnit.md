## JUnit

### Motivation

- In der Grundlagen-Schulung hatten wir nur manuell “getestet” bzw. ausprobiert:

```java
void main() {
    String zeile = IO.readln("Text? ");

    IO.println("enthaeltLeertaste: " + enthaeltLeertaste(zeile));
    IO.println("anzahlLeertasten: " + anzahlLeertasten(zeile));
    IO.println("anzahlVokale: " + anzahlVokale(zeile));
    IO.println("vokalVielfalt: " + vokalVielfalt(zeile));
    IO.println("istPalindrom: " + istPalindrom(zeile));
}
```

- Wir mussten uns selber von der Korrektheit der Ergebnisse überzeugen
- Bei jeder Änderung mussten wir erneut sinnvolle Texte eintippen
- Wahrscheinlich denken wir nicht ständig an alle Randfälle
- Deutlich sinnvoller sind automatisierte Tests mit JUnit:

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PalindromTest {
    @Test
    public void leererStringPalindrom() {
        assertTrue(Palindrom.istPalindrom(""));
    }

    @Test
    public void einZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("a"));
        assertTrue(Palindrom.istPalindrom(" "));
        assertTrue(Palindrom.istPalindrom("\n"));
    }

    @Test
    public void zweiZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("aa"));
        assertTrue(Palindrom.istPalindrom("bb"));

        assertFalse(Palindrom.istPalindrom("ab"));
        assertFalse(Palindrom.istPalindrom("ba"));
    }

    @Test
    public void dreiZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("aaa"));
        assertTrue(Palindrom.istPalindrom("ada"));
        assertTrue(Palindrom.istPalindrom("dad"));
        assertTrue(Palindrom.istPalindrom("ddd"));

        assertFalse(Palindrom.istPalindrom("aad"));
        assertFalse(Palindrom.istPalindrom("add"));
        assertFalse(Palindrom.istPalindrom("daa"));
        assertFalse(Palindrom.istPalindrom("dda"));
    }

    @Test
    public void bekanntePalindrome() {
        assertTrue(Palindrom.istPalindrom("beheb"));
        assertTrue(Palindrom.istPalindrom("handnah"));
        assertTrue(Palindrom.istPalindrom("level"));
        assertTrue(Palindrom.istPalindrom("neppen"));
        assertTrue(Palindrom.istPalindrom("regal-lager"));
    }

    @Test
    public void grossUndKleinschreibung() {
        assertTrue(Palindrom.istPalindrom("Anna"));
        assertTrue(Palindrom.istPalindrom("Hannah"));
        assertTrue(Palindrom.istPalindrom("Kajak"));
        assertTrue(Palindrom.istPalindrom("Neffen"));
        assertTrue(Palindrom.istPalindrom("Otto"));
        assertTrue(Palindrom.istPalindrom("Radar"));
        assertTrue(Palindrom.istPalindrom("Reittier"));
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

### AssertJ

- Neben `assertTrue` und `assertFalse` gibt es auch `assertEquals`:

```java
        // Jupiter: links das Ergebnis, rechts die Rechnung
        assertEquals("FRED", "Fred".toUpperCase());
```

- Viele Programmierer finden die Parameter-Reihenfolge unlogisch
- Die Bibliothek AssertJ bietet mit `assertThat` eine Alternative:

```java
        // AssertJ: links die Rechnung, rechts das Ergebnis
        assertThat("Fred".toUpperCase()).isEqualTo("FRED");
```

- Neben `isEqualTo` gibt es noch viele weitere nützliche Methoden

> - **Übung:**
>   - Führe `StackTest.java` aus
>   - Mehrere Tests werden fehlschlagen
>   - Behebe die Bugs in `Stack.java`
>   - Schreibe einen Test, der mehr als 10 Elemente einfügt
>   - 🏆 Das interne Array soll dynamisch wachsen (und schrumpfen) können

> - **Übung:**
>   - Führe `AdventOfCodeTest.java` aus
>   - Alle Tests werden fehlschlagen
>   - 🏆 Implementiere die Methoden in `AdventOfCode.java`

### Annotationen und Reflection

- Wie kommt JUnit an die Test-Methoden ran?
- Über Annotationen und Reflection
- Beides Blindflecken vieler Java-Programmierer
- In `AnnotationenUndReflection.java` erkennen wir die Test-Methoden selber:

```java
import org.junit.jupiter.api.Test;

void main() {
    for (Method method : PalindromTest.class.getDeclaredMethods()) {

        if (method.getAnnotation(Test.class) != null) {

            IO.println("Test-Methode gefunden: " + method);
        }
    }
}
```

> - **Plenum:** Lasst uns die gefundenen Test-Methoden ausführen!
>   - `method.invoke(new PalindromTest());`
>   - Fehlgeschlagene Tests sollen protokolliert werden
>   - Ein Report, wie viele Tests bestanden haben, wäre schön

### TDD

- Test-Driven Development ist eine streng definierte Test-Disziplin:
  - Man darf keine Zeile Produktiv-Code schreiben, zu der es keinen fehlschlagenden Test gibt
  - Ein fehlschlagender Test muss anschließend durch Schreiben von Produktiv-Code erfüllt werden
  - Zwischendurch wird ggf. refaktoriert
- Wer TDD richtig macht, wechselt im Minuten-Takt zwischen Test-Code und Produktiv-Code
- Viele Programmierer meinen “Test First”, wenn sie “TDD” sagen:
  - Erst mal die Test-Klasse schreiben
  - Dann die getestete Klasse schreiben
- Andere populäre Test-Disziplinen sind “Test Last” und “Test Never”

### Mocking

- Angenommene Architektur:
  - Controller
    - Service
      - Repository
- Wenn man den Service unabhängig vom Repository testen möchte, ersetzt man letzteres durch einen Mock
- Was dieses Mock-Repository können muss, ist abhängig vom konkreten Testfall
- Hier zum Beispiel ein Mock-Repository, das nur `findAll` können muss:

```java
public class PersonRepositoryFindAll implements PersonRepository {
    @Override
    public <S extends Person> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Person> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Person> findById(Long aLong) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Person> findAll() {
        return List.of(
                new Person("Jackson", "Michael"),
                new Person("Jackson", "Janet")
        );
    }

    @Override
    public List<Person> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
    }

    @Override
    public void delete(Person entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    @Override
    public void deleteAll(Iterable<? extends Person> entities) {
    }

    @Override
    public void deleteAll() {
    }
}

PersonRepository mockedRepository = new PersonRepositoryFindAll();
```

- Das Schreiben solcher Mock-Klassen wird durch Mockito vereinfacht:

```java
import static org.mockito.Mockito.*;

PersonRepository mockedRepository = mock(PersonRepository.class);

when(mockedRepository.findAll()).thenReturn(
    List.of(
        new Person("Jackson", "Michael"),
        new Person("Jackson", "Janet")
    )
);
```

- Man kann sogar prüfen, ob bzw. wie oft diese Methoden aufgerufen wurden
- In der Praxis führen Mocks oft zu schwer wartbaren Tests
- Mocks können ein Hinweis auf eine bessere Alternativ-Architektur sein:
  - Controller
    - ApplicationService
      - Repository
      - Service
