## Lambdas

### Anonyme innere Klassen (1997)

- Starte `cafe.JGround`
- Füge untenstehenden Code ein
- Drücke in den kommentierten Zeilen jeweils F9

```java
import cafe.Vec;
import java.util.function.*;


Vec.of("your", "cat", "is", "a", "doofus")
// F9
.filter(new Predicate<String>() {
    @Override
    public boolean test(String s) {
        return s.length() <= 3;
    }
})
// F9
.map(new Function<String, String>() {
    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
})
// F9
```

- “Funktionale Programmierung” war früher offenbar *sehr* Syntax-lastig!
- Kannst du dir vorstellen, wie `filter` und `map` programmiert sind?

### Lambdas (2014)

- `Predicate` und `Function` definieren genau 1 abstrakte Methode
- Für diesen Sonderfall sind Lambdas eine leichtgewichtige Alternative:

```java
 Vec.of("your", "cat", "is", "a", "doofus")

.filter((String s) -> s.length() <= 3)

   .map((String s) -> s.toUpperCase())
```

- Die Parameter-Typen von Lambdas kann Java inferieren:

```java
 Vec.of("your", "cat", "is", "a", "doofus")

.filter((s) -> s.length() <= 3)

   .map((s) -> s.toUpperCase())
```

- Wenn genau 1 Parameter existiert, sind die runden Klammern optional:

```java
 Vec.of("your", "cat", "is", "a", "doofus")

.filter( s  -> s.length() <= 3)

   .map( s  -> s.toUpperCase())
```

- Für komplexere Logik nutzt man wie gehabt Rümpfe:

```java
.filter(s -> {
    int n = s.length();
    return n <= 3;
})
```

- Für reine Delegation bietet Java Methoden-Referenzen:

```java
   .map(String::toUpperCase)
```

> [Effective Java 3rd Edition](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097)
> Item 43: Prefer method references to lambdas
>
> | Method Ref Type   | Example                  | Lambda Equivalent               |
> | ----------------- | ------------------------ | ------------------------------- |
> | Static            | `Integer::parseInt`      | `str -> Integer.parseInt(str)`  |
> | Bound             | `Instant.now()::isAfter` | `Instant then = Instant.now();` |
> |                   |                          | `t -> then.isAfter(t)`          |
> | Unbound           | `String::toLowerCase`    | `str -> str.toLowerCase()`      |
> | Class Constructor | `TreeMap<K, V>::new`     | `() -> new TreeMap<K, V>()`     |
> | Array Constructor | `int[]::new`             | `len -> new int[len]`           |

### Lambdas unter der Haube

- Lambdas (und Methoden-Referenzen) sind *nicht* bloß syntaktischer Zucker für Anonyme innere Klassen!
- 📺 [Lambdas in Java: A Peek under the Hood](https://www.youtube.com/watch?v=MLksirK9nnE)
