## Exceptions

- Beim Lesen einer Datei von der Festplatte können Probleme auftreten:
  - evtl. existiert die Datei nicht
  - evtl. hat der Nutzer kein Leserecht
- Wie gehen Java-Programme mit solchen Problemen um?

```java
public class MobyDick {
    public static void main(String[] args) {

        Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

        String inhalt = Files.readString(pfad);

        System.out.println("Anzahl Zeichen: " + inhalt.length());
    }
}
```

- Interessanterweise kann man das Programm gar nicht starten
- Der Java-Compiler beschwert sich in der mittleren Zeile:

```
unreported exception java.io.IOException; must be caught or declared to be thrown
```

```java
package java.io;

public class IOException extends Exception
```

```java
package java.lang;

/**
 * The class {@code Exception} and its subclasses are a form of {@code Throwable}
 * that indicates conditions that a reasonable application might want to catch.
 */
public class Exception extends Throwable
```

- Java zwingt den Programmierer also, sich mit Problemen auseinanderzusetzen, *bevor* sie auftreten!
- Woher weiß der Java-Compiler, dass `Files.readString(pfad)` evtl. eine `IOException` werfen könnte?

```java
                                           //////////////////
public static String readString(Path path) throws IOException
```

- Mit derselben `throws`-Klausel könnten wir auch unsere `main`-Methode deklarieren:

```java
public class MobyDick {                    //////////////////
    public static void main(String[] args) throws IOException {

        Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

        String inhalt = Files.readString(pfad);

        System.out.println("Anzahl Zeichen: " + inhalt.length());
    }
}
```

- Dann akzeptiert der Java-Compiler das Programm zwar
  - und wir können das Programm zum ersten Mal starten:

```
Exception in thread "main" java.nio.file.NoSuchFileException: /home/fred/Downloads/2701-0.txt
	at java.base/sun.nio.fs.UnixException.translateToIOException(UnixException.java:92)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:106)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:111)
	at java.base/sun.nio.fs.UnixFileSystemProvider.newByteChannel(UnixFileSystemProvider.java:218)
	at java.base/java.nio.file.Files.newByteChannel(Files.java:380)
	at java.base/java.nio.file.Files.newByteChannel(Files.java:432)
	at java.base/java.nio.file.Files.readAllBytes(Files.java:3288)
	at java.base/java.nio.file.Files.readString(Files.java:3366)
	at java.base/java.nio.file.Files.readString(Files.java:3325)
	at MobyDick.main(MobyDick.java:10)
```

```java
package java.nio.file;

public class NoSuchFileException extends FileSystemException
```

```java
package java.nio.file;

public class FileSystemException extends IOException
```

- Aber besonders nutzerfreundlich war diese Fehlermeldung nicht, oder?
- Anstatt die Exception aus `main` entgleiten zu lassen, sollten wir sie fangen und behandeln:

```java
public class MobyDick {
    public static void main(String[] args) {
          try {
              Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

              String inhalt = Files.readString(pfad);

              System.out.println("Anzahl Zeichen: " + inhalt.length());

          } catch (NoSuchFileException ex) {
              System.out.println("Datei nicht gefunden, bitte hier herunterladen:");
              System.out.println("https://www.gutenberg.org/files/2701/2701-0.txt");

          } catch (IOException ex) {
              System.out.println("Sonstiges Problem beim Lesen der Datei: " + ex);
          }
    }
}
```

- Man könnte das Programm sogar weiterlaufen lassen, bis das Problem behoben ist:

```java
public class MobyDick {
    public static void main(String[] args) {
        while (true) {
            try {
                Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

                String inhalt = Files.readString(pfad);

                System.out.println("Anzahl Zeichen: " + inhalt.length());
                return;

            } catch (NoSuchFileException ex) {
                System.out.println("Datei nicht gefunden, bitte hier herunterladen:");
                System.out.println("https://www.gutenberg.org/files/2701/2701-0.txt");

            } catch (IOException ex) {
                System.out.println("Sonstiges Problem beim Lesen der Datei: " + ex);
            }
            Konsole.readString("Drücken Sie Enter, sobald Sie das Problem behoben haben...");
        }
    }
}
```

- Speziellere Exceptions müssen vor allgemeineren Exceptions abgefangen werden
- `Throwable`
  - `Exception`
    - `IOException`
      - `FileSystemException`
        - `NoSuchFileException`

> **Übung:**
> - Falls die Datei `2701-0.txt` nicht existiert, soll sie *automatisch* heruntergeladen werden
>   - Dabei können natürlich weitere Probleme auftreten, die sinnvoll behandelt werden sollen
> - Die Datei soll aber nicht bei jedem Programmstart neu heruntergeladen werden!
>   - `Files.writeString`
> - Die Ausgabe `Anzahl Zeichen: ...` soll bei jedem Programmstart erscheinen
>   - Egal, ob die Datei erst heruntergeladen werden muss oder nicht
> - 🏆 Was passiert, wenn der Server zwar erreichbar ist, aber die Datei nicht existiert?
>   - Das kann man durch einen falschen Dateinamen simulieren, z.B. `2701-9.txt`
> - Vorlage für Http-Download in Java:

```java
HttpClient client = HttpClient.newBuilder().build();

HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://...")).build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

int statusCode = response.statusCode();

String body = response.body();
```
