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

### AbstractList

- Das Interface `List` ist riesig:

```java
package java.util;

public interface List<E> {

    // Query Operations

    int size();
    boolean isEmpty();
    boolean contains(Object o);
    Iterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // Modification Operations

    boolean add(E e);
    boolean remove(Object o);
    boolean containsAll(Collection<?> c);
    boolean addAll(Collection<? extends E> c);
    boolean addAll(int index, Collection<? extends E> c);
    boolean removeAll(Collection<?> c);
    boolean retainAll(Collection<?> c);
    void clear();

    // Comparison and hashing

    boolean equals(Object o);
    int hashCode();

    // Positional Access Operations

    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);

    // Search Operations

    int indexOf(Object o);
    int lastIndexOf(Object o);

    // List Iterators

    ListIterator<E> listIterator();
    ListIterator<E> listIterator(int index);

    // View

    List<E> subList(int fromIndex, int toIndex);
}
```

- Angenommen, wir schreiben eine eigene Klasse, die das Interface `List` implementiert
- Müssen wir dann alle 25 Methoden selber implementieren?!
- Nein, stattdessen erben wir die meisten Methoden von der abstrakten Klasse `AbstractList`:

```java
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class StringArrayWrapper extends AbstractList<String> {
    private final String[] array;

    public StringArrayWrapper(String[] array) {
        this.array = array;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public String get(int index) {
        return array[index];
    }

    @Override
    public String set(int index, String element) {
        return array[index] = element;
    }


    static void main() {
        List<String> words = new StringArrayWrapper(new String[]{"the", "beauty", "and", "the", "beast"});
        int and = words.indexOf("and");

        List<String> before = words.subList(0, and);
        List<String> after = words.subList(and + 1, words.size());

        IO.println(before);
        IO.println(after);

        Collections.sort(words);
        IO.println(words);
    }
}
```

- Die Methoden `indexOf` und `subList` (u.v.a.m.) sind in `AbstractList` implementiert:

```java
package java.util;

/**
 * This class provides a skeletal implementation of the {@link List}
 * interface to minimize the effort required to implement this interface
 * backed by a "random access" data store (such as an array).
 *
 * <p>To implement an unmodifiable list, the programmer needs only to extend
 * this class and provide implementations for the {@link #get(int)} and
 * {@link List#size() size()} methods.
 *
 * <p>To implement a modifiable list, the programmer must additionally
 * override the {@link #set(int, Object) set(int, E)} method (which otherwise
 * throws an {@code UnsupportedOperationException}).  If the list is
 * variable-size the programmer must additionally override the
 * {@link #add(int, Object) add(int, E)} and {@link #remove(int)} methods.
 */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    /**
     * The number of times this list has been <i>structurally modified</i>.
     * Structural modifications are those that change the size of the
     * list, or otherwise perturb it in such a fashion that iterations in
     * progress may yield incorrect results.
     */
    protected int modCount = 0;

    /**
     * Sole constructor.
     * (For invocation by subclass constructors, typically implicit.)
     */
    protected AbstractList() {
        // super();
    }

    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    public abstract E get(int index); // MUSS in konkreten Subklassen überschrieben werden

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    // Search Operations

    public int indexOf(Object o) {
        // ...
    }

    public int lastIndexOf(Object o) {
        // ...
    }

    // Bulk Operations

    public void clear() {
        removeRange(0, size()); // public abstract int size(); geerbt aus AbstractCollection
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }

    // Iterators

    public Iterator<E> iterator() {
        return new Itr();
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());

        return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));
    }

    // Comparison and hashing

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    public int hashCode() {
        int hashCode = 1;
        for (E e : this)
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        return hashCode;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        // ...
    }
}
```

### Einordnung

- Etabliertes Muster: `class KonkreteKlasse extends AbstrakteKlasse (implements Interface)`
  - Von tieferen Vererbungs-Hierarchien wird heutzutage eher abgeraten
- Fachliche Vererbung (`Sparbuch extends Konto`) wird häufig zum Wartungsproblem
  - weil sich die Domäne ändert
  - oder unser Verständnis davon
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
