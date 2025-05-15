## Java Persistence Query Language

JPQL ist syntaktisch an SQL angelehnt, bezieht sich aber auf Entities, nicht auf Tabellen/Spalten.

### Ergebnis-Liste

```java
String q = "select p from Person p where p.forename = 'Michael'";

Query query = entityManager.createQuery(q);

@SuppressWarnings("unchecked")
List<Person> michaels = (List<Person>) query.getResultList();
```

### Typsicherheit

```java
String q = "select p from Person p where p.forename = 'Michael'";

TypedQuery<Person> query = entityManager.createQuery(q, Person.class);

List<Person> michaels = query.getResultList();
```

### Query-Parameter

```java
String q = "select p from Person p where p.forename = :fore";

TypedQuery<Person> query = entityManager.createQuery(q, Person.class);

query.setParameter("fore", untrustedUserInput);
List<Person> peopleWithGivenForename = query.getResultList();
```

### Sortieren

```java
String q = "select p from Person p"
        + " order by p.surname asc, p.forename desc";

TypedQuery<Person> query = entityManager.createQuery(q, Person.class);

List<Person> sortedPeople = query.getResultList();
```

### Zählen

```java
String q = "select count(p) from Person p";

TypedQuery<Long> query = entityManager.createQuery(q, Long.class);

long count = query.getSingleResult().longValue();
```

### Projektion einer Spalte

```java
String q = "select distinct p.surname from Person p";

TypedQuery<String> query = entityManager.createQuery(q, String.class);

List<String> surnames = query.getResultList();
```

### Projektion mehrerer Spalten

```java
String q = "select p.surname, p.forename from Person p";

Query query = entityManager.createQuery(q);

List<Object[]> names = query.getResultList();
for (Object[] name : names) {
    String surname = (String) name[0];
    String forename = (String) name[1];
    // ...
}
```

### Projektion mit Konstruktor

```java
String q = "select new util.Pair(p.surname, p.forename) from Person p";

TypedQuery<Pair> query = entityManager.createQuery(q, Pair.class);

List<Pair> pairs = query.getResultList();
```

### Unterabfragen

```java
String s = "select count(p) from Person p where p member of f.persons";
String q = "select f from Family f where (" + s + ") > 1";

TypedQuery<Family> query = entityManager.createQuery(q, Family.class);

List<Family> bigFamilies = query.getResultList();
```

```java
String easier = "select f from Family f where size(f.persons) > 1";
```
