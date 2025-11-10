## Generics

### Kovarianz

- Was schreibt folgendes Programm auf die Konsole?

```java
public static void printAll(List<Object> objects) {
    for (Object object : objects) {
        System.out.print(object);
    }
}

public static void main(String[] args) {
    List<Object> objects = List.of("hello", "world");
    printAll(objects);

    List<String> strings = List.of("hello", "world");
    printAll(strings);
}
```

- Man kann das Programm gar nicht erst starten
- Der Compiler meckert beim zweiten `printAll`-Aufruf:

```
Required type: List<Object>
Provided:      List<String>
```

- Jeder `String` ist ein `Object`
- Aber eine `List<String>` scheint keine `List<Object>` zu sein
- Warum nicht?
- Weil `printAll` auch ein Objekt hinzufügen *könnte*:

```java
public static void printAll(List<Object> objects) {
    for (Object object : objects) {
        System.out.print(object);
    }
    objects.add(123);
}
```

- Spätestens zur Laufzeit würde es dann knallen
- Aber der Compiler verbietet `printAll(strings)` bereits mit folgender Begründung:
  - `List<Object>` hat eine Methode `add(Object)`
  - `List<String>` hat *keine* Methode `add(Object)`
  - Also kann eine `List<String>` keine `List<Object>` sein!
- Wie lösen wir das Problem?
- Der Elementtyp muss `Object` *oder spezieller* sein:

```java
public static void printAll(List<? extends Object> list) {
    for (Object object : list) {
        System.out.print(object);
    }
    list.add(null);
}
```

- Jetzt funktionieren beide `printAll`-Aufrufe
- Objekte kann man keine mehr einfügen, egal welchen Typs
  - nur noch die `null`-Referenz
  - die ist schließlich in allen Listen erlaubt

- Für `<? extends Object>` gibt es die Abkürzung `<?>`:

```java
public static void printAll(List<?> list)
```

### Kontravarianz

- Was schreibt folgendes Programm auf die Konsole?

```java
public static void greet(List<String> strings) {
    strings.add("hello");
}

public static void main(String[] args) {
    List<String> strings = new ArrayList<>();
    greet(strings);
    System.out.println(strings);

    List<Object> objects = new ArrayList<>();
    greet(objects);
    System.out.println(objects);
}
```

- Man kann das Programm gar nicht erst starten
- Der Compiler meckert beim zweiten `greet`-Aufruf:

```
Required type: List<String>
Provided:      List<Object>
```

- Dann ändern wir den Parametertyp von `greet` halt:

```java
public static void greet(List<Object> objects) {
    objects.add("hello");     //////
}
```

- Jetzt meckert der Compiler beim ersten `greet`-Aufruf:

```
Required type: List<Object>
Provided:      List<String>
```

- Können wir nicht einfache beide `greet`-Methoden anbieten?

```java
public static void greet(List<Object> objects) {
    objects.add("hello");
}

public static void greet(List<String> strings) {
    strings.add("hello");
}
```

- Nein; der Compiler entfernt Generics aus Parametertypen:

```
greet(List<Object>) clashes with greet(List<String>)

both methods have the same erasure
```

- Wie lösen wir das Problem?
- Der Elementtyp muss `String` *oder allgemeiner* sein:

```java
public static void greet(List<? super String> list) {
    list.add("hello");   ////////////////
}
```

### PECS (Producer extends, Consumer super)

- Populäre Merkregel
- **Produziert der Parameter** Elemente?
  - so dass die Methode sie umgekehrt konsumieren kann?
  - `extends`
- **Konsumiert der Parameter** Elemente?
  - so dass die Methode sie umgekehrt produzieren muss?
  - `super`

> **Übung:**
> - Suche im Interface `List` nach `? extends E`
>   - Schreibe kurz auf, warum `? extends E` dort sinnvoller ist als `E`
> - Suche im Interface `List` nach `? super E`
>   - Schreibe kurz auf, warum `? super E` dort sinnvoller ist als `E`
