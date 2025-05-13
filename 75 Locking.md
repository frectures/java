## Locking

Locking dient der Koordination konkurrierender Transaktionen.

### Pessimistisches Locking

Pro Entity kann *entweder* genau ein Schreibzugriff erfolgen *oder* beliebig viele Lesezugriffe:

![](img/pessimistic.svg)

Über den EntityManager kann sowohl der Lock-Modus einer bereits geladenen Entity geregelt werden:
```java
entityManager.lock(entityToBeReadFrom, LockModeType.PESSIMISTIC_READ);
entityManager.lock(entityToBeWrittenTo, LockModeType.PESSIMISTIC_WRITE);
```

als auch der Lock-Modus einer noch zu ladenden Entity:
```java
Person person = entityManager.find(Person.class, personId, lockMode);
```

Alternativ kann der Lock-Modus vor dem Ausführen einer Query bestimmt werden:
```java
query.setLockMode(lockMode);
```

Ein fundamentales Problem von pessimistischem Locking sind potenzielle Deadlocks.

### Optimistisches Locking

Entities, die ein mit `@Version` annotiertes Feld deklarieren, verwenden automatisch optimistisches Locking:
```java
public class Person {
    @Version
    private Long version;

    // ...
}
```

Lesezugriffe sind immer erlaubt und merken sich die aktuelle Version.
Schreibzugriffe vergleichen die aktuelle Version mit der gemerkten.
Bei Gleichheit wird die Version hochgezählt.
Andernfalls wird eine `OptimisticLockException` geworfen und die Transaktion zurückgerollt.

## Aufgabe

- Benutzer sollen XKCD-Kommentare editieren können
  - verwende dazu optimistisches Locking
  - d.h. der erste Editierer gewinnt
