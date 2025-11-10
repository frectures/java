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

### Echo

```java
// Datei Echo.java
public class Echo {

    // Die Programm-Ausführung startet in der main-Methode
    public static void main(String[] args) {

        // Schreibt ohne Zeilenumbruch auf die Konsole
        System.out.print("Sie haben das Echo-Programm gestartet ");

        // Schreibt mit Zeilenumbruch auf die Konsole
        System.out.println("und folgende Kommandozeilen-Argumente übergeben:");

        // Schleift über alle Kommandozeilen-Argumente
        for (String arg : args) {

            // Strings werden mit + verkettet
            System.out.println("- " + arg);
        }
    }
}
```

- getrennt übersetzen und ausführen:
  - `javac Echo.java` übersetzt nach Bytecode `Echo.class`
  - `java Echo apfel banane` führt Bytecode `Echo.class` aus
  - `javap -c Echo` zeigt Bytecode bei Interesse an
- Seit Java 11 kann man einzelne `.java`-Dateien direkt starten, ohne Erzeugung einer `.class`-Datei:
  - `java Echo.java apfel banane`

> **Übung:**
> - Installiere Java von https://adoptium.net
> - Tippe `Echo.java` mit einem beliebigen Text-Editor ab
>   - die grünen Kommentare sind optional
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
>   - Für die Schulung ist die 👇Community-Edition👇 ausreichend
>   - Nach dem Download ⚠️ **NUR 1x KLICKEN** ⚠️ auf `ideaIC...` und Geduld haben, sonst öffnet sich der Installierer mehrfach
> - Starte IntelliJ IDEA
> - Erster Start? **dann** Clone Repository, **sonst** Main Menu/File/New/Project from Version Control
>   - URL: https://github.com/frectures/java.git
>   - Clone
>   - dazu muss ggf. Git nachinstalliert werden
> - Doppelklick auf `java/src/main/java/Main.java`
>   - ggf. Warnhinweis bzgl. JDK-Konfiguration folgen
>   - Klick auf das grüne Dreieck in Zeile 1
>   - Run 'Main.main()'
> - Modifiziere das Programm, so dass Vorname und Nachname getrennt abgefragt werden
> - Was passiert, wenn man die Klammern um `(alter + 1)` entfernt?

```java
public class Main {
    public static void main(String[] args) {

        // Liest einen String von der Konsole
        String name = Konsole.readString("Wie heißt du? ");

        // Liest eine Ganzzahl von der Konsole
        int alter = Konsole.readInt("Wie alt bist du? ");
        
        if (alter < 0 || alter > 123) {
            System.out.println("Das glaube ich nicht...");
        } else {
            System.out.println("In einem Jahr bist du " + (alter + 1) + ", " + name + "!");
        }
    }
}
```
