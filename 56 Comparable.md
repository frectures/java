## Comparable

- Abhängig vom Listen- und Elementtyp können Listen ggf. sortiert werden:

```java
List<String> sorten = Arrays.asList("Vanille", "Erdbeer", "Schoko");

Collections.sort(sorten);

IO.println(sorten); // [Erdbeer, Schoko, Vanille]
```

- Woher weiß Java, dass...
  - Erdbeer vor Schoko gehört,
  - und Schoko vor Vanille?
- `String` definiert eine *natürliche Reihenfolge*
  - durch Implementierung des Interface `Comparable`:

```java
package java.lang;


public final class String implements Comparable<String>, /* ... */ {
    // ...                /////////////////////////////
}


public interface Comparable<T> {
    /**
     * Compares this object with the specified object for order.
     * Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(T o);
}
```

| Mathematik | Java                  |
| :--------: | :-------------------- |
| a `<` b    | a.compareTo(b) `<`  0 |
| a `≤` b    | a.compareTo(b) `<=` 0 |
| a `=` b    | a.compareTo(b) `==` 0 |
| a `≥` b    | a.compareTo(b) `>=` 0 |
| a `>` b    | a.compareTo(b) `>`  0 |

## Comparator

- Um von der *natürliche Reihenfolge* abzuweichen, implementiert man das Interface `Comparator`:

```java
                         /// anonyme innere Klasse
Collections.sort(sorten, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
});
                         /////////// Lambda
Collections.sort(sorten, (o1, o2) -> o2.compareTo(o1));
```

| Interface  | Methode   | Parameter    | vergleicht...  |
| :--------- | :-------- | :----------- | :------------: |
| Comparable | compareTo | (`o`)        | `this` mit `o` |
| Comparator | compare   | (`o1`, `o2`) | `o1` mit `o2`  |

```java
package java.util;

public interface Comparator<T> {
    /**
     * Compares its two arguments for order.
     * Returns a negative integer, zero, or a positive integer
     * as the first argument is less than, equal to, or greater than the second.
     */
    int compare(T o1, T o2);
}
```

- Zum absteigenden Sortieren existiert bereits ein `Comparator` in Java:

```java
Collections.sort(sorten, Comparator.reverseOrder());
```

## Case study: Comparator

### Java 7

```java
private static final Comparator<Person> compareAgeDescendingNameEmail = new Comparator<>() {
    @Override
    public int compare(Person a, Person b) {
        int result = Integer.compare(b.getAge(), a.getAge());
        if (result == 0) {
            result = a.getName().compareTo(b.getName());
            if (result == 0) {
                result = a.getEmail().compareTo(b.getEmail());
            }
        }
        return result;
    }
};

void sortAgeDescendingNameEmail(List<Person> persons) {
    Collections.sort(persons, compareAgeDescendingNameEmail);
}
```

### Java 8

```java
private static final Comparator<Person> compareAgeDescendingNameEmail = Comparator
        .comparingInt(Person::getAge).reversed()
        .thenComparing(Person::getName)
        .thenComparing(Person::getEmail);

void sortAgeDescendingNameEmail(List<Person> persons) {
    persons.sort(compareAgeDescendingNameEmail);
}
```

> **Übung:**
> 1. Verwende einen `new Comparator<String> { ... }`, der:
>    - kurze Strings vor lange Strings einordnet
>    - und gleich lange Strings alphabetisch
> 2. Baue denselben `Comparator` durch `.comparing` und `.thenComparing` nach
