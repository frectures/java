## Interfaces

### Motivation

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

- In Tetris-Lingo ist ein "Letter" eine Zahl zwischen 1 und 7
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

### Bekannte Informatiker

- Starte die (`main`-Methode in der) Klasse `Informatiker`
  - Probiere aus, was die 4 Buttons bewirken
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

### Funktions-Plotter

- Starte die (`main`-Methode in der) Klasse `Plotter`
  - Editiere die Formeln und bestätige mit der Enter-Taste
  - Dann sollten sich die Graphen ändern
- Studiere das Interface `Formel` und seine implementierenden Klassen
- Öffne die Klasse `Parser` und studiere folgende Methoden:
  - `strichrechnung`
  - `punktrechnung`
  - `primaer`
- Bisher kann man Formeln nicht negieren, zum Beispiel `-x+1`
  - Auf der Konsole erscheint eine Fehlermeldung
  - Probiere aus, welche der folgenden Ergänzungsversuche in `primaer` sich für `-x+1` mathematisch korrekt verhält:

```java
case '-':
    ++index;                           //////////////
    return new Minus(new Konstante(0), strichrechnung());
```
```java
case '-':
    ++index;                           /////////////
    return new Minus(new Konstante(0), punktrechnung());
```
```java
case '-':
    ++index;                           ///////
    return new Minus(new Konstante(0), primaer());
```

- Schreibe eine neue Klasse `Negiert`, so dass
  - `new Minus(new Konstante(0), ...)` durch
  - `new Negiert(...)` ersetzt werden kann
- Statt `x*x*x` würden wir gerne `x^3` schreiben können
  - Das erfordert eine neue Klasse `Hoch`
  - Und eine neue Methode zwischen `punktrechnung` und `primaer`
    - weil Exponentation stärker bindet als Punktrechnung
