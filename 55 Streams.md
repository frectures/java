## Streams

### eager Vec

- Die Methoden auf `Vec` sind *eager*
- d.h. sie produzieren sofort neue `Vec`-Objekte:

```java
import cafe.Vec;


Vec.of("your", "cat", "is", "a", "doofus")

.filter(s -> {
    IO.println("filtering " + s);
    return s.length() <= 3;
})

.take(2)

.map(s -> {
    IO.println("  mapping " + s);
    return s.toUpperCase();
})
```

```
filtering your
filtering cat
filtering is
filtering a
filtering doofus
  mapping cat
  mapping is
```

- Die (Zwischen-)Ergebnisse von `filter` und `take` interessieren eigentlich niemanden
- Verschwendung von Arbeitsspeicher!

### lazy Stream

- Java umgeht diese Speicherverschwendung mit *lazy* Streams:

```java
import java.util.List;


List.of("your", "cat", "is", "a", "doofus")

.stream()

.filter(s -> {
    IO.println("filtering " + s);
    return s.length() <= 3;
})

.limit(2)

.map(s -> {
    IO.println("  mapping " + s);
    return s.toUpperCase();
})

.toList()
```

```
filtering your
filtering cat
  mapping cat
filtering is
  mapping is
```

> - A **stream pipeline** consists of:
>   1. a **source**
>   2. zero or more **intermediate operations** (which transform a stream into another stream)
>   3. a **terminal operation** (which produces a result or side-effect)
> - Streams are **lazy**:
>   - computation on the source data is only performed when the terminal operation is initiated
>   - source elements are consumed only as needed

### Stream → Liste

```java
// unveränderliche Liste
.toList()

//  There are no guarantees on the type, mutability, serializability, or thread-safety of the List returned
.collect(Collectors.toList())

//   veränderliche Liste
.collect(Collectors.toCollection(ArrayList::new))
```

### Unendliche Streams

```java
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;


Stream.iterate(ONE, x -> x.add(TWO))  // 1, 3,  5,  7,  9, ...
      .map(x -> x.multiply(x));       // 1, 9, 25, 49, 81, ...
// How often is x.multiply(x) called?


Stream.iterate(ONE, x -> x.add(TWO))
      .map(x -> x.multiply(x))
// How often is x.multiply(x) called?
      .forEach(x -> IO.println(x + " should be odd"));


Stream.iterate(ONE, x -> x.add(TWO))
      .map(x -> x.multiply(x))
// How often is x.multiply(x) called?
      .anyMatch(x -> x.mod(TWO).equals(ONE));


Stream.iterate(ONE, x -> x.add(TWO))
      .map(x -> x.multiply(x))
// How often is x.multiply(x) called?
      .allMatch(x -> x.mod(TWO).equals(ONE));


Stream.iterate(ONE, x -> x.add(TWO))
      .map(x -> x.multiply(x))
// How often is x.multiply(x) called?
      .limit(1000000)
      .allMatch(x -> x.mod(TWO).equals(ONE));





// 0 ∞ 1 ∞ 1m
```

## Mehr Stream-Beispiele

### Moby Dick

```java
Path    path    = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");
String  content = Files.readString(path);

Pattern pattern = Pattern.compile("\\p{IsAlphabetic}+");
Matcher matcher = pattern.matcher(content);

String[] words  = matcher.results()                // Stream<MatchResult>
                         .map(MatchResult::group)  // Stream<String>
                         .toArray();               // String[]
```

### Diverse

```java
double[] randomNumbers = DoubleStream.generate(Zufall::nextDouble)
                                     .limit(10)
                                     .toArray();
```

```java
int[] powersOfTwo = IntStream.iterate(1, x -> x * 2)
                             .takeWhile(x -> x > 0)
                             .toArray();
```

```java
int[] codePoints = IntStream.rangeClosed('a', 'z')
                            .toArray();                  // { 97, 98, 99, ..., 122}

String s = new String(codePoints, 0, codePoints.length); // "abc...z"
```

> **Übung:**
> 1. Die 10 Zufallszahlen sollen von -1 bis +1 gehen
> 2. Berechne 64 Zweierpotenzen, inklusive `-9223372036854775808`
> 3. Der String soll alle 52 Groß- und Kleinbuchstaben beinhalten
>    - Tipp: `IntStream.concat`
