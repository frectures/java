## JUnit

### Motivation

- In der Grundlagen-Schulung hatten wir nur manuell "getestet" bzw. ausprobiert:

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

- Wir mussten uns selber von der Korrektheit der Ausgaben überzeugen
- Bei jeder Änderung mussten wir erneut sinnvolle Texte eingeben
- Wahrscheinlich denken wir nicht ständig an alle Randfälle
- Deutlich sinnvoller sind automatisierte Tests mit JUnit:

```java
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
- **Plenum:** Welches Qualitätsproblem hat die Testmethode `grossUndKleinschreibung`?

### Annotationen

- Wie kommt JUnit an die Test-Methoden ran?
- Über Annotationen und Reflection
- Beides Blindflecken vieler Java-Programmierer
- In `AnnotationenUndReflection.java` erkennen wir die Test-Methoden selber:

```java
import org.junit.jupiter.api.Test;

void main() {
    for (Method method : PalindromTest.class.getDeclaredMethods()) {

        Test annotation = method.getAnnotation(Test.class);
        if (annotation != null) {
            IO.println("Test-Methode gefunden: " + method);
        }
    }
}
```

- **Plenum:** Lasst uns die gefunden Test-Methoden ausführen!
  - `method.invoke(new PalindromTest());`
  - Fehlschlagende Tests sollen protokolliert werden
  - Ein Report, wie viele Test bestanden haben, wäre schön

> - **Übung:** Schreibe Test-Methoden für mindestens 1 der anderen 4 Methoden:
>   - enthaeltLeertaste
>   - anzahlLeertasten
>   - anzahlVokale
>   - vokalVielfalt

### Freditor

- Jede Testmethode wird auf einem frischen Objekt der Testklasse ausgeführt
  - `testMethode.invoke(new TestKlasse());`
- Ansonsten würden sich die Testmethoden gegenseitig beeinflussen

```java
public class IntGapBufferTest {

    private IntGapBuffer buffer = new IntGapBuffer();

    @Test
    public void emptyBuffer() {
        assertTrue(buffer.isEmpty());
        assertFalse(buffer.isFull());
        assertEquals(0, buffer.size());
    }

    @Test
    public void addOneValue() {
        buffer.add(11);

        assertFalse(buffer.isEmpty());
        assertFalse(buffer.isFull());
        assertEquals(1, buffer.size());
        assertEquals(11, buffer.get(0));
    }

    @Test
    public void addTwoValues() {
        buffer.add(11);
        buffer.add(13);

        assertFalse(buffer.isEmpty());
        assertFalse(buffer.isFull());
        assertEquals(2, buffer.size());
        assertEquals(11, buffer.get(0));
        assertEquals(13, buffer.get(1));
    }

    @Test
    public void changeValue() { /* ... */ }

    @Test
    public void removeFirstFromFullBuffer() { /* ... */ }

    @Test
    public void removeLastFromFullBuffer() { /* ... */ }

    @Test
    public void grow() { /* ... */ }

    @Test
    public void playWithTheAlphabet() {
        buffer.add(0, 'q'); // q
        buffer.add(1, 'w'); // qw
        buffer.add(0, 'e'); // eqw
        buffer.add(2, 'r'); // eqrw
        buffer.add(3, 't'); // eqrtw
        buffer.add(5, 'z'); // eqrtwz
        buffer.add(4, 'u'); // eqrtuwz
        buffer.add(1, 'i'); // eiqrtuwz
        buffer.add(2, 'o'); // eioqrtuwz
        buffer.add(3, 'p'); // eiopqrtuwz
        buffer.add(0, 'a'); // aeiopqrtuwz
        buffer.add(7, 's'); // aeiopqrstuwz
        buffer.add(1, 'd'); // adeiopqrstuwz
        buffer.add(3, 'f'); // adefiopqrstuwz
        buffer.add(4, 'g'); // adefgiopqrstuwz
        buffer.add(5, 'h'); // adefghiopqrstuwz
        buffer.add(7, 'j'); // adefghijopqrstuwz
        buffer.add(8, 'k'); // adefghijkopqrstuwz
        buffer.add(9, 'l'); // adefghijklopqrstuwz
        buffer.add(18, 'y'); //adefghijklopqrstuwyz
        buffer.add(18, 'x'); //adefghijklopqrstuwxyz
        buffer.add(1, 'c'); // acdefghijklopqrstuwxyz
        buffer.add(18, 'v'); //acdefghijklopqrstuvwxyz
        buffer.add(1, 'b'); // abcdefghijklopqrstuvwxyz
        buffer.add(12, 'n'); //abcdefghijklnopqrstuvwxyz
        buffer.add(12, 'm'); //abcdefghijklmnopqrstuvwxyz
        assertArrayEquals("abcdefghijklmnopqrstuvwxyz".codePoints().toArray(), buffer.toArray());

        buffer.remove(7, 16);
        assertArrayEquals("abcdefgqrstuvwxyz".codePoints().toArray(), buffer.toArray());

        buffer.remove(3, 14);
        assertArrayEquals("abcxyz".codePoints().toArray(), buffer.toArray());

        buffer.clear();
        assertTrue(buffer.isEmpty());
    }
}
```

### TDD

- Test-Driven Development ist eine streng definierte Test-Disziplin:
  - Man darf keine Zeile Produktiv-Code schreiben, zu der es keinen fehlschlagenden Test gibt
  - Ein fehlschlagender Test muss anschließend durch Schreiben von Produktiv-Code erfüllt werden
  - Zwischendurch wird ggf. refaktoriert
- Wer TDD richtig macht, wechselt im Minuten-Takt zwischen Test-Code und Produktiv-Code
- Viele Programmierer meinen "Test First", wenn sie "TDD" sagen:
  - Erst mal die Test-Klasse schreiben
  - Dann die getestete Klasse schreiben
- Andere populäre Test-Disziplinen sind "Test Last" und "Test Never"

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
