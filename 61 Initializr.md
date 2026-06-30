## Spring Initializr

- Browser https://start.spring.io
  - Project: **Maven**
  - Language: Java
  - Project Metadata
    - Java (links unten): **25**
  - ADD DEPENDENCIES (rechts oben)
    - **Spring Web**
  - ADD DEPENDENCIES
    - **Thymeleaf**
  - ADD DEPENDENCIES
    - **Spring Data JPA**
  - ADD DEPENDENCIES
    - **H2 Database**
  - GENERATE (unten links)
    - lädt Datei `demo.zip` herunter
- Datei `demo.zip` entpacken
  - Verzeichnis namens `demo` entsteht
- Datei `demo.zip` löschen
  - sonst verwechselt man ZIP und Verzeichnis später gerne

### IntelliJ IDEA

- File → New → Project From Existing Sources
  - zum Verzeichnis `demo` navigieren
  - enthaltene Datei `pom.xml` anklicken
  - OK
- **Kaffee-Pause** ☕
  - ⚠️ Bloß nix anklicken ca. 1 Minute lang!
  - sonst macht man gerne was kaputt
  - ⚠️ Maven-Dialog ignorieren
  - schließt sich von selbst
  - Irgendwann beruhigen sich bzw. verschwinden die Fortschrittsbalken (unten rechts)

### Erster Start

- Starte die Klasse `DemoApplication`
- Warte, bis die Konsole sich beruhigt
- Die letzten beiden Zeilen sollten wie folgt aussehen:

```
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
com.example.demo.DemoApplication         : Started DemoApplication in _.___ seconds (process running for _.___)
```

- Browser http://localhost:8080
  - oder welcher Port auch immer in der vorletzten Konsolenzeile stand
- Der Browser sollte jetzt folgende Fehlerseite anzeigen:

> # Whitelabel Error Page
>
> This application has no explicit mapping for /error, so you are seeing this as a fallback.
>
> There was an unexpected error (type=Not Found, status=404).

- Diese Fehlerseite wurde soeben von Spring Boot ausgeliefert
  - d.h. alles funktioniert bisher erwartungskonform 👍
- Sollte ein anderer Fehler erscheinen, frage nach Hilfe!
