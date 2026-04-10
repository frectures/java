## Vererbung

```java
class Subklasse extends Superklasse {
    // ...
}
```

- Jede Klasse *erbt* von genau 1 anderen Superklasse deren:
  - Zustandsfelder
  - und Methoden
- Die implizite Superklasse ist `java.lang.Object`:

```java
class Subklasse /* extends java.lang.Object */ {
    // ...
}
```

- Nur `java.lang.Object` selber erbt von keiner Superklasse

### java.lang.Object

```java
package java.lang;

public class Object {

    // keine Zustandsfelder

    // trivialer Konstruktor
    public Object() {
    }

    // Jedes Objekt weiß, von welcher Klasse es ist
    public final native Class<?> getClass();

    // Objekt-Gleichheit ist normalerweise Referenz-Gleichheit
    public boolean equals(Object obj) {
        return (this == obj);
    }

    // Generiert beim ersten Aufruf auf einem Objekt
    // eine Zufallszahl und speichert sie im Objekt
    public native int hashCode();
    // Der hashCode basiert NICHT auf der Speicheradresse eines Objekts!

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
```

- Da jede Klasse von `java.lang.Object` erbt, hat jede Klasse obige Methoden:

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

### Überschreiben

- Falls eine geerbte Methode sich unerwünscht verhält, *überschreibt* man sie:

```java
package java.lang;

public class Integer {

    private final int value;

    // ...

    // damit List.of(new Integer(1)).contains(new Integer(1)) funktioniert:
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Integer that) && (this.value == that.value);
    }

    // damit Set.of(new Integer(1), 2, 3).contains(new Integer(1)) funktioniert:
    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
```

- Auch ohne `@Override` würden in der `Integer`-Klasse alle 3 geerbten Methoden überschrieben!
- `@Override` stellt lediglich sicher, dass die Methode in der Superklasse existiert. Gegenbeispiele:
  - `@Override public void equals(Integer that)`: falscher Parametertyp, muss `(Object obj)` sein
  - `@Override public int hashcode()`: falscher Methodenname, Java beachtet Groß/Kleinschreibung
- `getClass()` ist `final` und kann daher nicht überschrieben werden
- Wer `equals` überschreibt, **muss** auch `hashCode` überschreiben
  - damit `HashSet` und `HashMap` funktionieren
  - Implikation: `a.equals(b)` ⇒ `a.hashCode() == b.hashCode()`
  - Kontraposition: `a.hashCode() != b.hashCode()` ⇒ `!a.equals(b)` 
- Können ungleiche Objekte den gleichen hashCode haben?
  - Ja, das verbietet obige Implikation (von links nach rechts) nicht
  - Im Allgemeinen sind eindeutige hashCodes nicht garantierbar
  - Zum Beispiel gibt es viel mehr (mögliche) Strings als hashCodes:

```java
jshell> "aa".hashCode()
$1 ==> 3104

jshell> "bB".hashCode()
$2 ==> 3104

jshell> 'a'*31 + 'a'
$3 ==> 3104

jshell> 'b'*31 + 'B'
$4 ==> 3104

jshell> 'a' - 'B'
$5 ==> 31
```

### Abstrakte Klassen

- Eine abstrakte Klasse vereint Gemeinsamkeiten konkreter Klassen:

```java
import java.util.List;

abstract class Pet {
    // Jedes Haustier hat einen Namen
    private final String name;

    // Konstruktor nur in Subklassen zugreifbar
    protected Pet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "The " + getClass().getName() + " named " + name + " says: " + noise();
    }

    // Jedes Haustier macht ein TYPisches Geräusch
    protected abstract String noise();


    static void main() {
        List<Pet> pets = List.of(
                new Cat("Snowball"),
                new Dog("Santa's Little Helper")
        );
        for (Pet pet : pets) {
            IO.println(pet);
        }
    }
}


class Cat extends Pet {

    protected Cat(String name) {
        super(name); // ruft den Pet-Konstruktor auf
    }

    @Override
    protected String noise() {
        return "meow..."; // Katzen miauen
    }
}


class Dog extends Pet {

    protected Dog(String name) {
        super(name); // ruft den Pet-Konstruktor auf
    }

    @Override
    protected String noise() {
        return "wuff!"; // Hunde bellen
    }
}
```

- `protected` schränkt den Zugriff auf die Klasse und deren Subklassen ein
- Konkrete Methoden können abstrakte Methoden aufrufen:
  - `toString` ruft z.B. `noise` auf
  - bekannt als Entwurfsmuster “Schablonenmethode”
- Abstrakte Methoden müssen in konkreten Subklassen überschrieben werden
  - Das kennen wir bereits von Interfaces
- Von abstrakten Klasse können keine Objekte erzeugt werden
  - weil `new Pet("Fred").noise()` nicht funktionieren würde
  - Auch das kennen wir bereits von Interfaces
- “Trotzdem” haben abstrakte Klassen Konstruktoren
  - zwecks Initialisierung der vererbten Zustandsfelder

### Beispiel `AbstractCollection`

- AbstractCollection (19 Subklassen)
  - ...
  - AbstractList (30 Subklassen)
    - ArrayList
    - LinkedList
    - Vector
    - ...
  - AbstractSet (51 Subklassen)
    - HashSet
    - TreeSet
    - EnumSet
    - EmptySet
    - SingletonSet
    - ...
  - AbstractQueue (14 Subklassen)
    - ArrayBlockingQueue
    - LinkedBlockingQueue
    - ...

### Einordnung

- *Technische* Vererbung (`AbstractList extends AbstractCollection`) funktioniert oft recht gut
  - weil die Autoren sich ihre eigene “Welt” ausdenken können
- *Fachliche* Vererbung (`Sparbuch extends Konto`) wird häufig zum Wartungsproblem
  - weil sich die Domäne ändert
  - oder unser Verständnis davon
  - oder neue Klassen schlecht in die alte Hierarchie passen
  - [Duplication is far cheaper than the wrong abstraction](https://sandimetz.com/blog/2016/1/20/the-wrong-abstraction)

### Was unterscheidet abstrakte Klassen von konkreten Klassen?

- Abstrakte Klassen können abstrakte Methoden definieren
- Von abstrakten Klassen kann man keine Objekte erzeugen

### Wozu Interfaces, wenn abstrakte Klassen existieren?

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
