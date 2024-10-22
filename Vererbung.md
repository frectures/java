## Vererbung

```java
class Subklasse extends Superklasse {
    // ...
}
```

- Jede Klasse *erbt* von genau 1 anderen Super-Klasse:
  - Zustandsfelder
  - Methoden
- Die implizite Super-Klasse ist `java.lang.Object`:

```java
class Subklasse /* extends java.lang.Object */ {
    // ...
}
```

### java.lang.Object

```java
package java.lang;

public class Object {

    public Object() {}

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public final native Class<?> getClass();

    public native int hashCode();

    public boolean equals(Object obj) {
        return (this == obj);
    }
}
```

- keine Zustandsfelder
- trivialer Konstruktor

```
jshell> class Fred {}
|  created class Fred


jshell> Fred a = new Fred();
   ==> Fred@5b6f7412

jshell> Object b = new Fred();
   ==> Fred@312b1dae


jshell> a.toString()
   ==> "Fred@5b6f7412"

jshell> b.toString()
   ==> "Fred@312b1dae"


jshell> a.getClass()
   ==> class Fred

jshell> a.getClass().getName()
   ==> "Fred"


jshell> a.hashCode()
   ==> 1534030866

jshell> Integer.toHexString(a.hashCode())
   ==> "5b6f7412"


jshell> a.equals(a)
   ==> true

jshell> a.equals(b)
   ==> false
```

### Abstrakte Klassen

- Abstrakte Klassen können abstrakte Methoden beinhalten
- Bisher ist der `Vergleicher` ein abstraktes Interface:

```java
public abstract interface Vergleicher {

    public abstract int vergleiche(Person a, Person b);

    public abstract String toString();
}
```
- `Vergleicher`
  - `PerNachname`
  - `PerAlter`
  - `Zweistufig`
  - `Umgekehrt`
- In den implementierenden Klassen `Zweistufig` und `Umgekehrt` verbirgt sich ein Performance-Problem:

```java
public class Zweistufig implements Vergleicher {
    // ...

    public String toString() {
        return "(" + primaer + " dann " + sekundaer + ")";
    }
}
```

```java
public class Umgekehrt implements Vergleicher {
    // ...

    public String toString() {
        return "(" + vergleicher + " umgekehrt)";
    }
}
```

- ⚠️ `primaer`, `sekundaer` und `vergleicher` können ebenfalls geschachtelt sein
  - Alle `toString`-Aufrufe liefern Teil-Strings, die jeweils vollständig zusammengebaut werden müssen
  - Bei tief geschachtelten Strukturen (z.B. Syntax-Bäume) explodieren Zeit- und Speicher-Kosten
- Mit einem `StringBuilder` entfallen die Teil-Strings, und wir können lineare Komplexität garantieren:

```java
public abstract interface Vergleicher {

    public abstract int vergleiche(Person a, Person b);

    public abstract String toString();

    public abstract void appendTo(StringBuilder sb);
}
```

```java
public class Zweistufig implements Vergleicher {
    // ...

    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }

    public void appendTo(StringBuilder sb) {
        sb.append("(");
        primaer.appendTo(sb);
        sb.append(" dann ");
        sekundaer.appendTo(sb);
        sb.append(")");
    }
}
```

```java
public class Umgekehrt implements Vergleicher {
    // ...

    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }

    public void appendTo(StringBuilder sb) {
        sb.append("(");
        vergleicher.appendTo(sb);
        sb.append(" umgekehrt)");
    }
}
```

- Die `toString`-Methode ist in beiden Klassen identisch implementiert
- Stattdessen kann man die `toString`-Methode von einer abstrakten Klasse *erben*:

```java
public abstract class KomplexerVergleicher implements Vergleicher {

    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
}
```

```java
public class Zweistufig extends KomplexerVergleicher {

    // ... eigene toString-Methode entfernt ...

    public void appendTo(StringBuilder sb) {
        sb.append("(");
        primaer.appendTo(sb);
        sb.append(" dann ");
        sekundaer.appendTo(sb);
        sb.append(")");
    }
}
```

```java
public class Umgekehrt extends KomplexerVergleicher {

    // ... eigene toString-Methode entfernt ...

    public void appendTo(StringBuilder sb) {
        sb.append("(");
        vergleicher.appendTo(sb);
        sb.append(" umgekehrt)");
    }
}
```

- `Vergleicher`
  - `PerNachname`
  - `PerAlter`
  - `KomplexerVergleicher`
    - `Zweistufig`
    - `Umgekehrt`
- Für Vergleicher, deren `toString`-Methode immer dasselbe Literal liefert, ist der Umweg über `StringBuilder` unnötig teuer
- Stattdessen kann man in beiden Methoden einfach mit dem Literal arbeiten:

```java
public class PerNachname {

    // ...

    public String toString() {
        return "Nachname";
    }

    public void appendTo(StringBuilder sb) {
        sb.append("Nachname");
    }
}
```

```java
public class PerAlter {

    // ...

    public String toString() {
        return "Alter";
    }

    public void appendTo(StringBuilder sb) {
        sb.append("Alter");
    }
}
```

- Das duplizierte Auftreten desselben Literals in beiden Methoden lässt sich durch Erben von `appendTo` lösen:

```java
public abstract class SimplerVergleicher implements Vergleicher {

    public void appendTo(StringBuilder sb) {
        sb.append(this.toString()); // Performance-Annahme: toString liefert Literal
    }
}
```

```java
public class PerNachname extends SimplerVergleicher {

    public String toString() {
        return "Nachname";
    }

    // ... eigene appendTo-Methode entfernt ...
}
```

```java
public class PerAlter extends SimplerVergleicher {

    public String toString() {
        return "Alter";
    }

    // ... eigene appendTo-Methode entfernt ...

}
```

- `Vergleicher`
  - `SimplerVergleicher`
    - `PerNachname`
    - `PerAlter`
  - `KomplexerVergleicher`
    - `Zweistufig`
    - `Umgekehrt`

### Einordnung

- Etabliertes Muster `KonkreteKlasse extends AbstrakteKlasse implements Interface`
  - Von tieferen Vererbungs-Hierarchien wird heutzutage eher abgeraten
- Fachliche Vererbung (`Sparbuch extends Konto`) wird häufig zum Wartungsproblem
  - weil die Domäne sich ändert
  - oder unser Verständnis davon
  - [Duplication is far cheaper than the wrong abstraction](https://sandimetz.com/blog/2016/1/20/the-wrong-abstraction)

### Was unterscheidet abstrakte Klassen von konkreten Klassen?

- Abstrakte Klassen können abstrakte Methoden definieren
- Von abstrakten Klassen kann man keine Objekte erzeugen

### Wozu überhaupt Interfaces statt abstrakter Klassen?

- Eine Klasse kann `n` Interfaces implementieren
  - aber nur `1` andere Klasse erweitern
- Interfaces sind deutlich *einfacher* als abstrakte Klassen:
  - Sie enthalten lediglich Methodenköpfe
  - und dienen ausschließlich dem Subtyping
- Neuere Programmiersprachen haben oft gar keine Klassen mehr, aber Interfaces/Traits:
  - 2008 Nim
  - 2012 Go
  - 2015 Rust
  - 2016 Zig
