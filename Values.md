## Value Objects im JDK

<table>
<tr>
<th>String</th>
<th>BigInteger</th>
</tr>
<tr>
<td>

```
jshell> String getraenk = "weizen"
"weizen"

jshell> getraenk.concat("bier")
"weizenbier"

jshell> getraenk
"weizen"
```

</td>
<td>

```
jshell> var b = BigInteger.valueOf(9)
9

jshell> b.add(BigInteger.ONE)
10

jshell> b
9
```

</td>
</tr>
</table>



<table>
<tr>
<th>DayOfWeek</th>
<th>LocalTime</th>
</tr>
<tr>
<td>

```
jshell> import java.time.DayOfWeek

jshell> var g = DayOfWeek.MONDAY
MONDAY

jshell> g.plus(4)
FRIDAY

jshell> g
MONDAY
```

</td>
<td>

```
jshell> import java.time.LocalTime

jshell> var t = LocalTime.of(20, 00)
20:00

jshell> t.plusMinutes(15)
20:15

jshell> t
20:00
```

</td>
</tr>
</table>

## Funktionale Sammlungen ([Clojure](https://clojure.org/reference/data_structures), [Scala](https://docs.scala-lang.org/overviews/collections-2.13/overview.html), [Kotlinx](https://github.com/Kotlin/kotlinx.collections.immutable#persistent-collection-interfaces), [Vavr](https://docs.vavr.io/#_persistent_data_structures), [Immutable.js](https://github.com/immutable-js/immutable-js)...)

<table>
<tr>
<th>PersistentVector</th>
<th>PersistentHashMap</th>
</tr>
<tr>
<td>

```
jshell> import clojure.lang.*

jshell> var v = PersistentVector.create("hello")
      ["hello"]

jshell> v.cons("world")
      ["hello" "world"]

jshell> v
      ["hello"]
```

</td>
<td>

```
jshell> import clojure.lang.*

jshell> var m = PersistentHashMap.create("Java", "James Gosling")
      {"Java" "James Gosling"}

jshell> m.assoc("Clojure", "Rich Hickey")
      {"Clojure" "Rich Hickey", "Java" "James Gosling"}

jshell> m
      {"Java" "James Gosling"}
```

</td>
</tr>
</table>

## In einem Parallel-Universum ohne Value Objects...

```java
public class Person {
    private       String name;
    private final LocalDate geburt;
    private       BigInteger gehalt;

    public Person(String name, LocalDate geburt, BigInteger gehalt) {
        this.name   =   name.clone();
        this.geburt = geburt.clone();
        this.gehalt = gehalt.clone();
    }

    public String name() {
        return   name.clone();
    }

    public LocalDate geburt() {
        return geburt.clone();
    }

    public String gehalt() {
        return gehalt.clone();
    }

    public void methode() {
        Fremd.code(name.clone(),
                 geburt.clone(),
                 gehalt.clone());
    }
}
```
