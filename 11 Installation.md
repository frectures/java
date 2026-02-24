# Java Grundlagen

## Einführung

### Einsatzgebiete/Praxisrelevanz

- 💀 Eingebettete Systeme
  - Interaktives Fernsehen 📺
  - DVD-Abspielgeräte 📀
- 💀 Dynamische Webseiten
  - Applets 🐠
  - [Google Web Toolkit](https://apps.delegs.de/delegseditor) 🧏
  - [CheerpJ](https://fredoverflow.github.io/karel) 🤖
- 😐 Portable Desktop-Anwendungen
  - Abstract Window Toolkit
  - [Swing](https://github.com/fredoverflow/karel) 🤖
  - JavaFX
- 🙂 Backend für Web-Anwendungen
  - Spring (Boot)
  - Quarkus
  - Micronaut

### Wichtige Meilensteine

- 1996 Java 1
  - Bytecode: “write once, run anywhere”
  - Threads
  - Garbage Collection: `malloc` without `free`
- 2004 Java 5
  - Generics: `List<String> names`
  - Enumerations: `enum Direction { EAST, NORTH, WEST, SOUTH }`
  - Annotations: `@Deprecated`
- 2014 Java 8
  - Streams: `filter`, `map`, `reduce`...
  - Lambdas: `names.mapToInt(s -> s.length())`
  - Methoden-Referenzen: `names.mapToInt(String::length)`
- Seitdem
  - [Module](https://openjdk.org/jeps/261): `java.`base, desktop, rmi, sql, xml...
  - neue Java-Versionen jeden März und September
  - [Records](https://openjdk.org/jeps/395)
  - [Pattern Matching](https://openjdk.org/jeps/441)
  - [Virtual Threads](https://openjdk.org/jeps/444)
  - [Compact Source Files](https://openjdk.org/jeps/512)
- Zukunftsmusik
  - [Value Classes and Objects](https://openjdk.org/jeps/401)

## Installation

### Distributionen

- 2019 wurde das OracleJDK kostenpflichtig
- Dies führte zu einer Explosion von konkurrierenden Distributionen (und viel Verwirrung)

| Distribution                                             | Hinweise                      |
| -------------------------------------------------------- | ----------------------------- |
| 👉 https://adoptium.net 👈                               | hieß früher AdoptOpenJDK      |
| https://aws.amazon.com/corretto                          |                               |
| https://www.azul.com/downloads                           | optional mit JavaFX           |
| https://bell-sw.com/pages/downloads                      | optional mit JavaFX           |
| https://developer.ibm.com/languages/java/semeru-runtimes |                               |
| https://www.microsoft.com/openjdk                        |                               |
| https://developers.redhat.com/products/openjdk/download  | erfordert Registrierung       |
| https://sap.github.io/SapMachine                         |                               |
| https://www.oracle.com/java/technologies/downloads       | seit Java 21 wieder kostenlos |
| https://jdk.java.net                                     | nur neueste & nächste Version |

- Die Standard-Empfehlung ist https://adoptium.net
- Alle Distributionen werden aus demselben Quellcode gebaut https://github.com/openjdk/jdk

### Greet.java

```java
// Die Programm-Ausführung startet in der main-Methode
void main() {
    // Fordert den Benutzer auf, seinen Namen auf der Tastatur einzutippen.
    // Die eingetippte Zeichenkette wird in der Variable 'name' gespeichert.
    String name = IO.readln("Ihr Name? ");

    // Wünscht dem Benutzer einen guten Tag.
    IO.println("Guten Tag, " + name + "!");
}
```

- getrennt übersetzen und ausführen:
  - `javac Greet.java` übersetzt nach Bytecode `Greet.class`
  - `java Greet` führt Bytecode `Greet.class` aus
  - `javap -c Greet` zeigt Bytecode bei Interesse an
- Seit Java 11 kann man einzelne `.java`-Dateien direkt starten, ohne Erzeugung einer `.class`-Datei:
  - `java Greet.java`

> **Übung:**
> - Installiere Java von https://adoptium.net
> - Tippe obiges Programm mit einem beliebigen Text-Editor ab
>   - Die Kommentare (beginnend mit `//`) sind optional
>   - Speichere die Datei als `Greet.java` in deinem Home-Verzeichnis
> - Starte das Programm von der Konsole
>   - Windows: Eingabeaufforderung
>   - macOS: Terminal

### IntelliJ IDEA

- Für größere Projekte ist eine integrierte Entwicklungsumbegung sinnvoller als ein Text-Editor:
  - IntelliJ IDEA
  - Eclipse
  - Netbeans
  - ...

> **Übung:**
> - Prüfe, ob IntelliJ IDEA bereits auf deinem Rechner installiert ist
>   - Falls nicht, installiere IntelliJ IDEA von https://www.jetbrains.com/idea/download
>   - Nach dem Download ⚠️ **NUR 1x KLICKEN** ⚠️ auf `ideaIC...` und Geduld haben, sonst öffnet sich der Installierer mehrfach
> - Starte IntelliJ IDEA
> - Erster Start? **dann** Clone Repository, **sonst** Main Menu/File/New/Project from Version Control
>   - URL: https://github.com/frectures/java.git
>   - Clone
>   - dazu muss ggf. Git nachinstalliert werden
> - Doppelklick auf `java/src/main/java/Alter.java`
>   - ggf. Warnhinweis bzgl. JDK-Konfiguration folgen
>   - Klick auf das grüne Dreieck in Zeile 1
>   - Run 'Alter.main()'
>   - Klick in die Konsole unten, um ihr den Fokus zu geben
>   - Name und Alter eintippen
> - Modifiziere das Programm, so dass Vorname und Nachname getrennt abgefragt werden
> - Was passiert, wenn man die Klammern um `(alter + 1)` entfernt?

```java
void main() {
    // Liest einen String von der Konsole
    String name = IO.readln("Wie heißt du? ");

    // Liest eine Ganzzahl von der Konsole
    int alter = Integer.parseInt(IO.readln("Wie alt bist du? "));

    if (alter < 0 || alter > 123) {
        IO.println("Das glaube ich nicht...");
    } else {
        IO.println("In einem Jahr bist du " + (alter + 1) + ", " + name + "!");
    }
}
```
