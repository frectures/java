## Iterator

- Seit Java 5 gibt es die foreach-Schleife für Collections:

```java
List<String> sorten = List.of("Vanille", "Erdbeer", "Schoko");

for (String sorte : sorten) {

    IO.println("1x " + sorte + "-Eis, bitte!");
}
```

- Der Compiler macht daraus folgenden, `Iterator`-basierten Code:

```java
List<String> sorten = List.of("Vanille", "Erdbeer", "Schoko");

Iterator<String> iterator = sorten.iterator();
while (iterator.hasNext()) {
    String sorte = iterator.next();

    IO.println("1x " + sorte + "-Eis, bitte!");
}
```

- Offenbar hat ein `Iterator` 2 essenzielle Methoden:

```java
package java.util;

public interface Iterator<E> {
    /**
     * @return {@code true} if the iteration has more elements
     */
    boolean hasNext();

    /**
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    E next();
}
```

## Iterable

- Die foreach-Schleife funktioniert mit *jedem* Typen, der das Interface `Iterable` implementiert:

```java
package java.lang;

public interface Iterable<T> {

    Iterator<T> iterator();
}
```

> **Übung:**
> - Sorge dafür, dass die Klasse `ArrayView` das Interface `Iterable` sinnvoll implementiert
> - Schleife in der `main`-Methode per foreach-Schleife über `sorten`
> - Erzeuge einen Iterator und rufe `next()` zu häufig auf
>   - Wirft dein Code eine Exception vom korrekten Typ?
> - 🏆 Implementiere die Methode `IterableExercise.equalStrings`
>   - Funktioniert sie insbesondere mit verschieden langen Iterables?
>   - Schreibe eine Testklasse `IterableExerciseTest`!
