## Interfaces

### Motivation

- Gegeben seien 2 Klassen für Quadrate und Kreise:

<table>
<tr>
<th>Square</th>
<th>Circle</th>
</tr>
<tr>
<td>

```java
public class Square {

    private final double length;

    public Square(double length) {
        this.length = length;
    }

    public double area() {
        return length * length;
    }
}
```

</td>
<td>

```java
public class Circle {

    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double area() {
        return Math.PI * (radius * radius);
    }
}
```

</td>
</tr>
</table>

- Wenn wir die Flächen mehrerer Figuren aufaddieren wollen, brauchen wir dazu 2 separate Methoden:

<table>
<tr>
<th>addSquareAreas</th>
<th>addCircleAreas</th>
</tr>
<tr>
<td>

```java
double addSquareAreas(Square[] squares) {
    double sum = 0;
    for (Square square : squares) {
        sum += square.area();
    }
    return sum;
}
```

</td>
<td>

```java
double addCircleAreas(Circle[] circles) {
    double sum = 0;
    for (Circle circle : circles) {
        sum += circle.area();
    }
    return sum;
}
```

</td>
</tr>
</table>

- und 2 Arrays in der `main`-Methode:

```java
void main() {

    Square[] squares = {
            new Square(3),
            new Square(4),
    };

    Circle[] circles = {
            new Circle(1),
            new Circle(10),
    };

    double totalArea = addSquareAreas(squares) + addCircleAreas(circles);

    IO.println(squares.length + circles.length + " shapes with total area " + totalArea);
}
```

- Der Code der 2 `add`-Methoden ist bis auf die Typen (und Bezeichner) identisch
- Schöner wäre 1 `add`-Methode, die mit *beliebigen* Figuren umgehen kann
- Dazu definieren wir einen abstrakten Typ `Shape` mit einer abstrakten `area`-Methode:

```java
public abstract interface Shape {

    public abstract double area(); // Semikolon statt Geschweifte Klammern!
}
```

- und lassen die konkreten Klassen das Interface implementieren:

<table>
<tr>
<th>Square</th>
<th>Circle</th>
</tr>
<tr>
<td>

```java
                    ////////////////
public class Square implements Shape {

    // ... Rumpf wie vorher ...
}
```

</td>
<td>

```java
                    ////////////////
public class Circle implements Shape {

    // ... Rumpf wie vorher ...
}
```

</td>
</tr>
</table>

- Dann brauchen wir nur noch 1 `add`-Methode:

```java
double addShapeAreas(Shape[] shapes) {
    double sum = 0;
    for (Shape shape : shapes) {
        sum += shape.area();
    }
    return sum;
}
```

- und nur noch 1 Array in der `main`-Methode:

```java
void main() {

    Shape[] shapes = {
            new Square(3),
            new Square(4),

            new Circle(1),
            new Circle(10),
    };

    double totalArea = addShapeAreas(shapes);

    IO.println(shapes.length + " shapes with total area " + totalArea);
}
```

> **Übung:**
> - Schreibe eine dritte Klasse `Rectangle implements Shape` mit 2 Zustandsfeldern:
>   - `width`
>   - `height`
> - Schreibe eine vierte Klasse `Triangle implements Shape` mit 2 Zustandsfeldern:
>   - `base`
>     - Die Länge der “Basis” des Dreiecks
>     - Es steht sozusagen “auf dem Boden”
>   - `height`
>     - Die Höhe senkrecht zur Basis

### Eigenschaften von Interfaces

- Ein Interface hat:
  - *keine* Zustandsfelder
  - *keine* Konstruktoren
  - *keine* Methodenrümpfe ¹
- Von Interfaces kann man *keine* Objekte erzeugen
  - Ein Interface definiert lediglich einen *statischen* Supertyp für alle implementierenden Klassen
  - *Dynamisch* getypte Programmiersprachen haben keine Interfaces (siehe JavaScript vs. TypeScript)
- Interfaces sind grundsätzlich abstrakt
  - egal ob man `abstract interface` oder nur `interface` schreibt
- Interface-Methoden sind grundsätzlich öffentlich und abstrakt
  - egal ob man `public` und/oder `abstract` schreibt

¹ Seit Java 8 können Interfaces auch konkrete `default`-Methoden (mit Rümpfen) enthalten

### Tetris

- Die `Tetris`-Klasse verwendet einen `FairLetterSupplier`:

```java
public class Tetris {
    // ...
    private final FairLetterSupplier letterSupplier;
    // ...

    public Tetris() {
        //...
        letterSupplier = new FairLetterSupplier();
        //...
    }
}
```

- In Tetris-Lingo ist ein “Letter” eine Zahl zwischen 1 und 7
  - stellvertretend für die (unrotierten) Basis-Formen I, J, L, O, S, T, Z
- Angenommen, Spieler 2 ist deutlich besser als Spieler 1
  - Spieler 1 soll weiterhin einen `FairLetterSupplier` verwenden
  - Spieler 2 soll einen neuen `UnfairLetterSupplier` verwenden:

```java
public class UnfairLetterSupplier {
    // potenziell beliebig lange Pausen zwischen guten Steinen
    public int nextLetter() {
        return 1 + (int) (Math.random() * 7);
    }
}
```

- Müssen wir dafür die komplette `Tetris`-Klasse duplizieren?!?

<table>
<tr>
<th>FairTetris</th>
<th>UnfairTetris</th>
</tr>
<tr>
<td>

```java
public class FairTetris {
    // ...
    private final FairLetterSupplier letterSupplier;
    // ...

    public FairTetris() {
        //...
        letterSupplier = new FairLetterSupplier();
        //...
    }
}
```

</td>
<td>

```java
public class UnfairTetris {
    // ...
    private final UnfairLetterSupplier letterSupplier;
    // ...

    public UnfairTetris() {
        //...
        letterSupplier = new UnfairLetterSupplier();
        //...
    }
}
```

</td>
</tr>
</table>

- Dann müssten wir ab sofort:
  - jedes neue Feature in *beide* Tetris-Klassen einbauen
  - jeden gefundenen Bug in *beiden* Tetris-Klassen fixen
  - jedes Refactoring in *beiden* Tetris-Klassen vornehmen
  - ...ein Wartungs-Albtraum!
- Ideal wäre *eine* `Tetris`-Klasse, die mit *beliebigen* `LetterSupplier`n zurechtkommt:

```java
public class Tetris {
    // ...
    private final LetterSupplier letterSupplier;
    // ...

    public Tetris(LetterSupplier letterSupplier) {
        //...
        this.letterSupplier = letterSupplier;
        //...
    }
}
```

- Der neue Typ `LetterSupplier` ist keine konkrete Klasse, sondern ein abstraktes Interface:

```java
public abstract interface LetterSupplier {

    public abstract int nextLetter();
}
```

- Die beiden konkreten Klassen müssen das abstrakte Interface ausdrücklich implementieren:

<table>
<tr>
<th>FairLetterSupplier</th>
<th>UnfairLetterSupplier</th>
</tr>
<tr>
<td>

```java
public class FairLetterSupplier
      implements LetterSupplier {
    // ...
}
```

</td>
<td>

```java
public class UnfairLetterSupplier
        implements LetterSupplier {
    // ...
}
```

</td>
</tr>
</table>

- Zuletzt müssen wir beim Erzeugen der `Tetris`-Objekte konkrete `Un/FairLetterSupplier`-Objekte übergeben:

```java
Tetris[] games = {
    new Tetris(new   FairLetterSupplier()),
    new Tetris(new UnfairLetterSupplier()),
};
```

- `new LetterSupplier()` würde *nicht* funktionieren!

> **Übung:**
> - Schreibe eine dritte Klasse `CheatingLetterSupplier`
>   - `nextLetter` soll bei jedem 2. Aufruf (und sonst nicht!) den langen `I`-Stein liefern
>   - Benutze `CheatingLetterSupplier` gemäß obiger Anleitung für Spieler 1

### Prägende Informatiker

- Starte die (`main`-Methode in der) Klasse `informatiker/Informatiker.java`
- Probiere aus, was die 3 Buttons bewirken
  - Was passiert, wenn du mehrfach auf den Button `Alter` klickst?
  - Warum tritt dieser Effekt nicht bei den anderen 2 Buttons auf?
- Studiere das Interface `Vergleicher` und seine implementierenden Klassen:
  - Rechte Maustaste in Zeile 3 auf `Vergleicher`
  - Go To
  - Implementations
- Schreibe eine Klasse `PerVorname`, welche die Vornamen der Personen miteinander vergleicht
  - Ergänze das Array `Vergleicher[] alleVergleicher` in der Klasse `Informatiker` passend
  - Dann erscheint ein entsprechender Button an der grafischen Oberfläche
  - Das musst du im Folgenden auch für alle weiteren Vergleicher tun!
- Schreibe eine Klasse `PerGeschlecht`, welche Frauen vor Männern einstuft
- Kannst du durch geschickten Einsatz der Klasse `Zweistufig` auch 3 Vergleicher hintereinander schalten?
  - erst per Geschlecht
  - dann per Alter
  - dann per Nachname
- Schreibe eine Klasse `Umgekehrt`, welche sich genau umgekehrt zu einem anderen
Vergleicher verhält
  - Beispielsweise soll `new Umgekehrt(new PerAlter())` junge Personen vor alten
einstufen
  - Als Inspiration kann hierbei die (deutlich kompliziertere) Klasse `Zweistufig` dienen

### JSON

- Starte die (`main`-Methode in der) Klasse `json/Freddy.java`
  - Kennst du das JSON-Austauschformat?
  - Fällt dir in der Konsole **ein Fehler** auf?
- Studiere folgende Typen in dieser Reihenfolge:
  1. `Wert`
  2. `Zahl`
  3. `Zeichenkette`
  4. `Symbolisch`
  5. `Objekt`
- Behebe **o.g. Fehler**, der dir in der Konsole aufgefallen war
- Füge 2 weitere Konstruktoren zu `Objekt` zu
  - damit auch 4 und 5 Schlüssel/Wert-Pärchen funktionieren
- Füge einen neuen Typ `class Array implements Wert` hinzu
  - z.B. `new Array(Symbolisch.TRUE, new Zahl(42), new Zeichenkette("test")).stringify()`
  - sollte `[true, 42.0, "test"]` ergeben
  - Tipp: Der Parametertyp `Wert...` erlaubt beliebig viele Argumente
- Die Methode `Zeichenkette.appendLiteral` kann noch nicht mit Sonderzeichen umgehen:
  - Gänsefüßchen
  - Zeilenumbruch
  - Backslash
  - ...
  - 🏆 Behandle diese Sonderzeichen sinnvoll
    - https://www.google.com/search?q=escape+sequenz
- Bisher schreibt `append` alles in eine Zeile
  - 🏆 Schöner wäre eine Zeile pro Schlüssel/Wert-Pärchen bzw. Array-Eintrag
