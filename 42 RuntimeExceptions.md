## RuntimeExceptions

> **Maurice Wilkes (1949):** “As soon as we started programming, we found to our surprise that it wasn’t as easy to get programs right as we had thought it would be.  
> I can remember the exact instant when I realized that a large part of my life from then on was going to be spent in finding mistakes in my own programs.”

- Bugs führen idealerweise zu einem zeitnahen Abbruch der Programm-Ausführung:

```java
public class Main {
    public static void main(String[] args) {
        int[] numbers = new int[2];

        numbers[1] = 42;
        numbers[2] = 97;

        System.out.println(java.util.Arrays.toString(numbers));
    }
}
```

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException:
 Index 2 out of bounds for length 2
	at Main.main(Main.java:6)
```

```java
package java.lang;

public class IndexOutOfBoundsException extends RuntimeException
```

```java
package java.lang;

/**
 * {@code RuntimeException} is the superclass of those exceptions that
 * can be thrown during the normal operation of the Java Virtual Machine.
 */
public class RuntimeException extends Exception
```

- Obwohl `RuntimeException` eine `Exception` ist, erfordert der Compiler:
  - *weder* `throws ArrayIndexOutOfBoundsException`
  - *noch* `catch (ArrayIndexOutOfBoundsException ex)`
- Mit einer `ArrayIndexOutOfBoundsException` *kann* ein Programm *nicht* sinnvoll umgehen!
  - (Oder wie könnte ein sinnvoller `catch`-Block aussehen? Irgendwelche Vorschläge?)
  - Stattdessen muss der Programmierer das Programm modifizieren, um den Bug zu fixen:

```diff
-        numbers[1] = 42;
-        numbers[2] = 97;
+        numbers[0] = 42;
+        numbers[1] = 97;
```

```bash
git commit -am "fix bug #123"
```

- Ein zweites Beispiel für einen Bug:

```java
public class Main {
    public static void main(String[] args) {
        String[] strings = new String[2];

        System.out.println(strings[0].length());
        System.out.println(strings[1].length());

        System.out.println(java.util.Arrays.toString(strings));
    }
}
```

```
Exception in thread "main" java.lang.NullPointerException:
 Cannot invoke "String.length()" because "strings[0]" is null
	at Main.main(Main.java:5)
```

```java
package java.lang;

public class NullPointerException extends RuntimeException
```

- `new String[2]` erzeugt ein Array mit 2 `String`-Variablen
- Der Default-Wert von `String` ist aber nicht `""` sondern `null`

```diff
-        String[] strings = new String[2];
+        String[] strings = {"", ""};
```

```bash
git commit -am "fix bug #456"
```

> **Tony Hoare (2009):** “I call it my billion-dollar mistake. It was the invention of the null reference in 1965.  
> At that time, I was designing the first comprehensive type system for references in an object oriented language (ALGOL W). My goal was to ensure that all use of references should be absolutely safe, with checking performed automatically by the compiler.  
> But I couldn't resist the temptation to put in a null reference, simply because it was so easy to implement. This has led to innumerable errors, vulnerabilities, and system crashes, which have probably caused a billion dollars of pain and damage in the last forty years.”

- `Throwable`
  - `Exception` (für Probleme; außer RuntimeException)
    - `IOException`
      - `FileSystemException`
        - `NoSuchFileException`
    - `RuntimeException` (für Bugs im Programm)
      - `ArrayIndexOutOfBoundsException`
      - `NullPointerException`
      - `IllegalArgumentException`
      - `IllegalStateException`
  - `Error` (insbesondere VM-Erschöpfung)
    - `VirtualMachineError`
      - `StackOverflowError`
      - `OutOfMemoryError`

### throw

- `IllegalArgumentException` wird bei ungültigen Argumenten geworfen:

```java
package json;

public class Zahl implements Wert {

    private final double number;

    public Zahl(double number) {

        if (Double.isInfinite(number)) throw new IllegalArgumentException("JSON forbids Infinity");
        if (Double.isNaN     (number)) throw new IllegalArgumentException("JSON forbids NaN");

        this.number = number;
    }

    // ...
}
```

- `IllegalStateException` wird bei ungültigen Zuständen geworfen:

```java
public class ByteVector {

    private int size;
    // ...

    public byte top() {

        if (size == 0) throw new IllegalStateException("top on empty vector");

        // ...
    }

    // ...
}
```
