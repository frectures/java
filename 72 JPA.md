## Java Persistence API

JPA erleichtert das Persistieren von Objekt-Graphen in relationalen Datenbanken. Grob vereinfacht:
- 1 Tabelle pro Klasse
- 1 Zeile pro Objekt
- 1 Spalte pro Basis-Feld:
  - `boolean`, `byte`, `char`, `short`, `int`, `long`, `float`, `double`
  - `Boolean`, `Byte`, `Char`, `Short`, `Integer`, `Long`, `Float`, `Double`
  - `java.math.BigInteger`, `java.math.BigDecimal`
  - `String`, `char[]`, `Character[]`
  - diverse Datum/Uhrzeit-Typen
  - `java.sql.Blob`, `java.sql.Clob`, `byte[]`, `Byte[]`, `java.io.Serializable`
  - `java.util.UUID`
  - `Enum`, `Class`, `java.util.Currency`, `java.util.Locale`, `java.net.URL`
- Objekt-Beziehungen werden über Fremdschlüssel realisiert

### Entity

Objekte, die in der Datenbank persistiert werden, bezeichnet man als Entities.
Lebenszyklus einer Entity:

![](img/entity.svg)

Erst ein Aufruf von `flush` führt verändernde SQL-Statements aus:
- `persist` bewirkt ein `INSERT`-Statement
- Änderungen an managed Entities bewirken `UPDATE`-Statements
- `remove` bewirkt ein `DELETE`-Statement

Wann wird `flush` normalerweise automatisch aufgerufen?
- grundsätzlich beim Commit einer Transaktion
- vor der Ausführung einer Query

```java
@Entity
// @Table(name = "Person")
// @Access(AccessType.FIELD)
public class Person {
    @Id
    @GeneratedValue
    // @Column(name = "id")
    private Long id;

    private String surname;
    private String forename;

    protected Person() {
        // required by JPA
    }

    // ...
}
```

Damit eine Klasse von JPA als Tabelle persistiert werden kann, muss sie:

- Vererbung zulassen (also nicht `final` sein)
- mit `@Entity` annotiert sein
- einen Default-Konstruktor anbieten, der `protected` oder `public` ist
- ein mit `@Id` annotiertes Feld für den Primärschlüssel besitzen

Falls es keinen fachlichen Primärschlüssel gibt, kann man per `@GeneratedValue` automatisch einen technischen Primärschlüssel generieren lassen.

Normalerweise arbeitet JPA per Reflection direkt auf den privaten Feldern.
Alternativ kann JPA mit Properties arbeiten, dafür müssen die entsprechenden Getter annotiert werden (nicht die Setter).

### Ein Dutzend CRUD-Methoden

- Für gängige Datenbank-Zugriffe reicht das Erben von `ListCrudRepository<Entity, ID>`:

```java
public interface PersonRepository extends ListCrudRepository<Person, Long> {
    // bisher leer
}
```

- `ListCrudRepository` definiert hierfür ein dutzend Methoden:

```java
     T  save   (         T  entity);
List<T> saveAll(Iterable<T> entities);

Optional<T> findById   (ID id);
boolean     existsById (ID id);
    List<T> findAll    ();
    List<T> findAllById(Iterable<ID> ids);

long count();

void deleteById   (ID id);
void delete       (T entity);
void deleteAllById(Iterable<? extends ID> ids);
void deleteAll    (Iterable<? extends T> entities);
void deleteAll    ();
```

### Mehr CRUD-Methoden

- Man kann Repository-Interfaces um eigene Methoden ergänzen
- Die Semantik der Methoden leitet Spring aus den Methodennamen ab:

```java
public interface PersonRepository extends ListCrudRepository<Person, Long> {

    List<Person> findBySurname(String surname);
}
```

- Eine vollständige Auflistung findet man in der offiziellen Dokumentation:
  - [Table 1. Supported keywords inside method names](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation)

### EntityManager

- Sind dir CRUD-Interfaces zu magisch?
  - Dann verwende einen `EntityManager`
  - und formulieren die Datenbank-Zugriffe selber:

```java
@Repository
public class PersonRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Person person) {
        entityManager.persist(person);
    }

    public List<Person> findBySurname_SQL(String surname) {
        //                                                    Tabelle          Spalte
        String sql = "select p.id, p.surname, p.forename from Person p where p.surname = ?";
        Query query = entityManager.createNativeQuery(sql, Person.class);
        query.setParameter(1, surname);
        @SuppressWarnings("unchecked")
        List<Person> result = (List<Person>) query.getResultList();
        return result;
    }

    public List<Person> findBySurname_JPQL(String surname) {
        //                           Klasse           Feld
        String jpql = "select p from Person p where p.surname = :sur";
        TypedQuery<Person> query = entityManager.createQuery(jpql, Person.class);
        query.setParameter("sur", surname);
        return query.getResultList();
    }
}
```

- `createNativeQuery` verwendet SQL als Abfragesprache
  - JPA definiert durchnummerierte Parameter, aber keine benannten Parameter
  - `getResultList` liefert eine ungetypte `List`
  - Hibernate flusht nicht automatisch vor dem Ausführen von Native Queries
- `createQuery` verwenden JPQL als Abfragesprache
  - Klassen können Refactoring-sicher referenziert werden:
  - `"select p from " + Person.class.getName() + ...`

### Aufgabe

- Gestern hast du eine XKCD-Thymeleaf-RestTemplate-Anwendung geschrieben
- Heute wollen wir die abgefragten Comic-Informationen in einer lokalen Datenbank cachen
  - damit wir den Server nicht mit redundanten Anfragen belästigen
- Verwende dazu im Controller ein Repository
  - Eigentlich sollte ein Controller kein Repository kennen
  - aber im Tutorial-Rahmen ist das schon okay
- Wie überprüfen wir, ob die Datenbank auch tatsächlich befüllt wird?
  - siehe nächster Abschitt

### H2 extras

- Füge folgende Zeile in die Datei `src/main/resources/application.properties` hinzu:
  - `spring.h2.console.enabled=true`
  - Starte den Server neu
- Browser http://localhost:8080/h2-console
  - Die blau hervorgehobene JDBC-URL findest du recht weit unten in den Logs:

```
H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:13f81235-c7cd-406a-8647-86949a1df0f1'
Tomcat started on port 8080 (http) with context path '/'
Started DemoApplication in 3.058 seconds (process running for 3.399)
```

- Wenn du den Server neu startest, ist die Datenbank wieder leer
  - weil die Datenbank normalerweise im Arbeitsspeicher abgelegt wird
- Um das zu verhindern, kannst du die Datenbank in einer Datei ablegen:

```
spring.datasource.url=jdbc:h2:file:~/xkcd
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

- Falls du deine Entity-Klassen irgendwann änderst, einfach die Datenbank-Dateien löschen
