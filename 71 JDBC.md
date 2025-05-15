## Java DataBase Connectivity

JDBC ermöglicht den Zugriff auf Datenbanken per SQL.

### Treiber

Zunächst benötigt man einen passenden JDBC-Treiber für die Datenbank.
Diesen lassen wir uns bereits automatisch von Maven herunterladen:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
    <scope>runtime</scope>
</dependency>
```

Zur Laufzeit wird das Laden des JDBC-Treibers per Reflection angestoßen:
```java
Class.forName("org.h2.Driver");
```

### Zugriff

Eine `Connection` ist eine Verbindung zur Datenbank, in diesem Fall eine schnelle In-Memory-Datenbank:
```java
Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
```

Von der `Connection` kann man sich ein `Statement` erzeugen lassen:
```java
Statement statement = connection.createStatement();
```

Über dieses `Statement` kann man nun Veränderungen an der Datenbank-vornehmen:
```java
statement.execute("create table ...");
statement.execute("insert into ...");
```
und Datenbank-Abfragen formulieren:
```java
ResultSet resultSet = statement.executeQuery("select .. from .. where ..");
```
Wie man sieht, ist ein `Statement` nicht ein einziges SQL-Statement,
sondern erlaubt das Ausführen beliebig vieler SQL-Statements.

Zuletzt iteriert man über die Zeilen und greift auf die Spalten zu:
```java
while (resultSet.next()) {
    int someInt = resultSet.getInt("...column name...");
    String someString = resultSet.getString("...column name...");
    ...
}
```

Verschiedene Datenbanken verwenden unterschiedliche SQL-Dialekte.
Eine Dokumentation zur SQL-Syntax von H2 findet man auf https://www.h2database.com/html/grammar.html

### Aufgabe

Starte die `main`-Methode der Klasse `JdbcExample`.
Daraufhin sollte folgendes erscheinen:
```
loading h2 driver...
h2 driver loaded!
establishing connection...
connection established!
Syntax error in SQL statement "CREATE TABLE .[*].. "; expected "identifier"
```

Die `main`-Methode liefert ein grobes Skelett zum spielerischen Umgang mit der Datenbank, aber es passiert noch nichts sinnvolles.
Baue das Beispiel "m:1 Beziehung" (*Beliebig viele Personen wohnen in ein und demselben Haushalt*) aus der Datei `70 Kardinal.md` nach.
Beschäftige dich dabei insbesondere mit folgenden Fragen:
1. Wie definiert man einen Primärschlüssel, der automatisch hochgezählt wird?
2. Wie definiert man einen Fremdschlüssel?
3. Wie formuliert man JOIN-Abfragen über mehrere Tabellen?
4. Was passiert, wenn man eine Zeile löscht, die von einem Fremdschlüssel referenziert wird?
