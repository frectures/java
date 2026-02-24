## Case study: Collections

### Java 5

```java
List<String> adultDomains(List<Person> persons) {
    Set<String> domains = new HashSet<String>();
    for (Person person : persons) {
        if (person.isAdult()) {
            domains.add(person.getEmail().getDomain());
        }
    }
    List<String> list = new ArrayList<String>(domains);
    Collections.sort(list);
    return list;
}
```

### Java 8

```java
List<String> adultDomains(List<Person> persons) {
                // source:
    return persons.stream()                           // Stream<Person>
                // intermediate operations:
                  .filter(person -> person.isAdult())
                  .map(person -> person.getEmail())   // Stream<Email>
                  .map(email -> email.getDomain())    // Stream<String>
                  .sorted()
                  .distinct()
                // terminal operation:
                  .toList();                          // List<String>
}
```

excerpt from `Stream<T>` Javadoc:

> - A **stream pipeline** consists of:
>   - a **source**
>   - zero or more **intermediate operations** (which transform a stream into another stream)
>   - a **terminal operation** (which produces a result or side-effect)
> - Streams are **lazy**:
>   - computation on the source data is only performed when the terminal operation is initiated
>   - source elements are consumed only as needed

### Streams are lazy

```java
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;


Stream.iterate(ONE, x -> x.add(TWO))  // 1, 3,  5,  7,  9, ...
      .map(x -> x.multiply(x));       // 1, 9, 25, 49, 81, ...
// How often is x.multiply(x) called?


Stream.iterate(ONE, x -> x.add(TWO))
      .map(x -> x.multiply(x))
// How often is x.multiply(x) called?
      .forEach(x -> IO.println(x) + " should be odd");


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

### Method references

Simple lambdas can often be replaced with method references:

```java
List<String> adultDomains(List<Person> persons) {

    return persons.stream()                // Stream<Person>
                  .filter(Person::isAdult)
                  .map(Person::getEmail)   // Stream<Email>
                  .map(Email::getDomain)   // Stream<String>
                  .sorted()
                  .distinct()
                  .toList();               // List<String>
}


Map<String, List<Email>> emailsByDomain(List<Person> persons) {

    return persons.stream()
                  .map(Person::getEmail)
                  .collect(Collectors.groupingBy(Email::getDomain));
}
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

## more Stream examples

### Freditor

```java
List<Path> sortedFiles() {
    try (Stream<Path> applicationFiles = Files.list(applicationDirectory)) {
        return applicationFiles
                .filter(Files::isRegularFile)
                .filter(TabbedEditors::fileHasPlausibleSize)  // 0 < size < 100_000
                .filter(file -> !isLegacyBackupFile(file))    // 27base64digits.txt
                .sorted()
                .collect(Collectors.toList());
    } catch (IOException directoryAbsent) {
        return Collections.emptyList();
    }
}
```

### Pangit

```java
Stream<GitBlob> findGitBlobs(Path root, Consumer<GitBlob> gitBlobConsumer) throws IOException {
    return Files.walk(root)                   // Stream<Path>
                .filter(GitBlob::isGitObject)
                .map(GitBlob::gitBlobOrNull)  // Stream<GitBlob>
                .filter(Objects::nonNull)
                .peek(gitBlobConsumer)
                .sorted();
}
```

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

### Various

```java
double[] randomNumbers = DoubleStream.generate(new Random()::nextDouble)
                                     .limit(10)
                                     .toArray();
```

```java
int[] powersOfTwo = IntStream.iterate(1, x -> x * 2)
                             .takeWhile(x -> x > 0)
                             .toArray()
```

```java
int[] codePoints = IntStream.rangeClosed('a', 'z')
                            .toArray();                  // { 97, 98, 99, ..., 122}

String s = new String(codePoints, 0, codePoints.length); // "abc...z"
```

> **Exercise:**
> 1. The 10 random numbers should range from -1 to +1
> 2. Instead of 31 powers of two, calculate 64, including `-9223372036854775808`
> 3. The string should contain all 52 letters (upper and lower case)
>    - Hint: `IntStream.concat`
