## Objekt-Beziehungen

JPA bietet für verschiedene Kardinalitäten passende Annotationen:

- `@OneToOne`
- `@ManyToOne`
- `@OneToMany`
- `@ManyToMany`

Im Folgenden werden `@OneToMany` und `@ManyToOne` beispielhaft demonstriert.

### OneToMany (unidirektional)

Mit `@OneToMany` legt man z.B. fest, dass eine Familie aus beliebig vielen Personen besteht, aber eine Person nur zu höchstens einer Familie gehört:
```java
@Entity
public class Family {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Person> persons;

    // ...
}
```

Beim Persistieren einer Familie müssen die zugehörigen Personen innerhalb derselben Transaktion manuell mitpersistiert werden, wobei die Reihenfolge keine Rolle spielt:
```java
entityManager.persist(jacksonFamily);
entityManager.persist(michaelJackson);
entityManager.persist(janetJackson);
```

Wenn man die Personen nicht manuell mitpersistiert, kommt es zu folgendem Fehler:

> object references an unsaved transient instance - save the transient instance before flushing

Alternativ kann man die Persistierung einer Familie automatisch auf ihre Personen kaskadieren:
```java
@Entity
public class Family {
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Person> persons;

    // ...
}
```

Mit `CascadeType.REMOVE` werden beim Löschen einer Familie deren Personen ebenfalls gelöscht:
```java
entityManager.remove(theJacksons);
// theJacksons.persons.forEach(entityManager::remove);
```

Mit `orphanRemoval=true` wird eine Person beim Entfernen aus seiner Familie komplett gelöscht:
```java
theJacksons.persons.remove(michael);
// entityManager.remove(michael);
```

`orphanRemoval=true` existiert nur für `@OneTo...`-Beziehungen und impliziert `CascadeType.REMOVE`.

### OneToMany mit JoinColumn (unidirektional)

Normalerweise generiert `@OneToMany` eine unnötige Join-Tabelle `Family_Person`.
Diese kann man einsparen, indem man die *Tabelle* `Person` um eine Join-Spalte für den Fremdschlüssel zu `Family` ergänzt:
```java
public class Family {
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fam_id")
    private List<Person> persons;

    // ...
}
```

### OneToMany mit ManyToOne (bidirektional)

Alternativ kann man die Zugehörigkeit zu einer Familie redundant in der *Klasse* `Person` abspeichern:
```java
public class Person {
    @ManyToOne
    // @JoinColumn(name = "fam_id")
    Family fam;

    // ...
}

public class Family {
    @OneToMany(cascade = CascadeType.PERSIST,
               mappedBy = "fam")
    private List<Person> persons;

    // ...
}
```

Diese Variante bewirkt beim Einfügen von Personen weniger SQL-Statements,
dafür muss man aber beide Seiten der Relation manuell synchron halten.

## lazy vs. eager fetching

Beim Zugriff auf die Personen einer geladenen Familie kann folgender Fehler auftreten:

> failed to lazily initialize a collection of role: Family.persons, could not initialize proxy - no Session

Die Personen werden normalerweise nur bei Bedarf geladen (das gilt für alle `@...ToMany`-Beziehungen).
Ein späteres Nachladen außerhalb der ursprünglichen Transaktion ist verboten.
Mögliche Lösungen:

- Die ladende bzw. verarbeitende Methode mit `@Transactional` annotieren
- Die Abfrage so anpassen, dass die Personen sofort mitgeladen werden:
```java
String q = "select DISTINCT f from Family f"
        + " LEFT JOIN FETCH f.persons WHERE ...";
TypedQuery<Family> query = entityManager.createQuery(q, Family.class);
List<Family> eagerFamilies = query.getResultList();
```
- Die Personen einer Familie grundsätzlich sofort mitladen:
```java
@Entity
public class Family {
    @OneToMany(cascade = CascadeType.PERSIST,
               mappedBy = "fam",
               fetch = FetchType.EAGER)
    private List<Person> persons;

    // ...
}
```

## Basis-Sammlungen

Für Sammlungen von Objekten, die keine Entitäten sind, verwenden man `@ElementCollection`:
```java
@Entity
public class Person {
    @ElementCollection
    // @CollectionTable(name = "Person_middleNames",
    //                  joinColumns = @JoinColumn(name = "Person_id"))
    private List<String> middleNames;

    // ...
}
```

## Aufgabe

- Benutzer sollen XKCD-Comics mit Kommentaren versehen können
  - d.h. ein XKCD-Comic hat beliebig viele Kommentare

## Optimistisches Locking

Entities, die ein mit `@Version` annotiertes Feld deklarieren, verwenden automatisch *optimistisches Locking*:
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
