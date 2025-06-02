## Thymeleaf

### Time

- Ersetze die Klasse `TimeServlet` durch eine neue Klasse `TimeController`:

```java
@Controller
@RequestMapping("/time")
public class TimeController {
    @GetMapping
    // arbitrary method name
    public String get() {
        return "time"; // resources/templates/time.html
    }
}
```

- Lege im Verzeichnis `resources/templates` eine neue Datei `time.html` an:

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<body>
<h1 th:text="${T(java.time.LocalDateTime).now()}"></h1>
</body>
</html>
```

- eventuelle Warnungen in Zeile 1 und 3 kannst du ignorieren
- Starte den Server neu
- Browser `http://localhost:8080`

### Model

- Als wir im Initializr "Spring Web" als Dependency hinzugefügt hatten, stand dort:
  - "Build Web applications using Spring **MVC**"
  - MVC steht für "**M**odel, **V**iew, **C**ontroller"
- Der primäre Zweck eines *Controllers* ist es:
  - das *Model* mit Informationen zu befüllen
  - die der *View* (`time.html`) anzeigen soll
- Der Controller bekommt Zugriff auf das Model, indem wir es als Parameter definieren:

```java
    @GetMapping
    public String get(Model model) {
                      ///////////

        model.addAttribute("now", LocalDateTime.now());
        //////////////////

        return "time"; // .html
    }
```

- Im View sind die Model-Attribute per `${attributname}`-Syntax zugreifbar:

```html
<h1 th:text="${now}"></h1>
            <!----->
```

### Fruit

- Erstelle eine neue Klasse `FruitDto.java`:

```java
public class FruitDto {
    private String fruit;

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }
}
```

- Ersetze die Klasse `FruitServlet` durch eine neue Klasse `FruitController`:

```java
@Controller
@RequestMapping("/fruit")
public class FruitController {
    @GetMapping
    public String get(Model model) {
        FruitDto fruit = new FruitDto();
        fruit.setFruit("apple"); // default fruit
        model.addAttribute("fruit", fruit);

        model.addAttribute("popularFruits", List.of("apple", "banana", "cherry"));

        return "fruit-ask"; // .html
    }

    @PostMapping
    public String post(@ModelAttribute("fruit") FruitDto fruit) {
        return "fruit-like"; // .html
    }
}
```

- `@ModelAttribute("fruit") FruitDto fruit`:
  - erzeugt ein neues `FruitDto`
  - ruft für alle Formular-Daten die entsprechende `set`-Methode auf
  - packt dieses `FruitDto` unter dem namen `fruit` ins Model
- Lege im Verzeichnis `resources/templates` eine neue Datei `fruit-ask.html` an:

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<body>
<h3>What is your favorite fruit?</h3>
<form method="post" th:action="@{/fruit}" th:object="${fruit}">
    <input type="text" th:field="*{fruit}">
    <input type="submit" value="Yummy!">
</form>
<h3>Popular fruits</h3>
<ul>
    <li th:each="fruit: ${popularFruits}" th:text="${fruit}"></li>
</ul>
</body>
</html>
```

- `<form>`
  - `th:action` bestimmt, auf welche URL gepostet werden soll
  - `th:object` bestimmt das Java-Objekt für das Formular
  - `th:field` bestimmt das Feld dieses Java-Objekts
- Lege im Verzeichnis `resources/templates` eine neue Datei `fruit-like.html` an:

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<body>
<div th:text="${'I like ' + fruit.fruit + ', too!'}"></div>
</body>
</html>
```

- Starte den Server neu
- Browser `http://localhost:8080`
- Die eingetragene Frucht soll in die Liste der populären Früchte übernommen werden
  - Jeder Request hat grundsätzlich ein eigenes Model
  - Damit die populären Früchte längerfristig überleben, musst du dafür ein normales Feld definieren
  - Eigentlich sollten Controller zustandslos sein, aber im Tutorial-Rahmen ist das schon okay
- Die `fruit-like.html` brauchen wir dann eigentlich nicht mehr
  - Sinnvoller wäre es, nach dem POST auf die Start-Seite weiterzuleiten
  - Ersetze dazu den Code der `post`-Methode wie folgt:

```java
    @PostMapping
    public String post(@ModelAttribute FruitDto fruit) {
        // ... 

     // return           "fruit-like";
        return "redirect:/fruit";
    }
```

### Greet

- Konvertiere das `Greet`-Beispiel von Servlets nach Spring MVC

### RequestParam

- Eine URL kann einen Query-String enthalten:
  - z.B. `https://www.google.com/search?client=firefox&q=james+gosling`
  - So greift man im Controller darauf zu:

```java
@GetMapping("/search")
public String search(@RequestParam                   String q,
                     @RequestParam(required = false) String client)
```

- Ein Controller kann beliebig viele `@GetMapping`/`@PostMapping`-Methoden definieren:
  - s.o. `@GetMapping("/search")`
  - Das definierte Pfad-Suffix muss Controller-weit eindeutig sein
- So generiert man Query-Strings mit Thymeleaf:
  - `<a th:href="@{/search(client='firefox')}">`
  - `<a th:href="@{/search(client=${hierKannstDuAufsModelZugreifen})}">`
- ⚠️ Query-Strings in der `action` eines `form`s werden ignoriert!
  - Verwende stattdessen `<input type="hidden" name="client" th:value="${hierKannstDuAufsModelZugreifen}">`

### PathVariable

- Alternativ zum Query-String kann man Daten auch in der Adresse selber unterbringen:
  - z.B. `https://xkcd.com/927/`
  - So greift man im Controller darauf zu:

```java
@GetMapping("/{id}")
public String get(@PathVariable int id)
```

- So generiert man Pfad-Variablen mit Thymeleaf:
  - `<form th:action="@{/{id}(id=927)}">`
  - `<form th:action="@{/{id}(id=${hierKannstDuAufsModelZugreifen})}">`

### Populäre Passwörter

- Heute morgen hast du eine Konsolen-Anwendung für populäre Passwörter geschrieben
- Portiere diese Anwendung nach Thymeleaf

### XKCD

- Schreibe eine Thymeleaf-Anwendung, die zufällig eines der folgenden Bilder anzeigt:
  - https://imgs.xkcd.com/comics/compiler_complaint.png
  - https://imgs.xkcd.com/comics/exploits_of_a_mom.png
  - https://imgs.xkcd.com/comics/compiling.png
  - https://imgs.xkcd.com/comics/goto.png
  - https://imgs.xkcd.com/comics/pointers.png
- Baue `prev`/`next`-Links oder -Buttons ein, die zum vorherigen/nächsten Bild aus der Liste wechseln
  - ⚠️ Der Server darf sich *nicht* Request-übergreifend merken, welcher Comic gerade angezeigt wird
  - Sonst würden sich verschiedene Benutzer gegenseitig beeinflussen
- Der Benutzer soll per Zahleneingabe (1 bis 5) direkt zu einem Bild springen können

### RestTemplate

- Statt 5 Bilder wollen wir nun 3000+ anbieten
- Die 5 obigen Links stammen aus folgender JSON-Schnittstelle:
  - https://xkcd.com/371/info.0.json
  - https://xkcd.com/327/info.0.json
  - https://xkcd.com/303/info.0.json
  - https://xkcd.com/292/info.0.json
  - https://xkcd.com/138/info.0.json
- So spricht man JSON-Schnittstellen aus Spring an:

```java
RestTemplate restTemplate = new RestTemplate();

ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
if (response.getStatusCode() == HttpStatus.OK) {
    String body = response.getBody();
    // ...
}
```

- Theoretisch kann man jetzt von Hand im `String body` suchen
- Aber sauberer ist das Navigieren eines JSON-Baums:

```java
ObjectMapper mapper = new ObjectMapper();

JsonNode root = mapper.readTree(body);
String hoverText = root.path("alt").textValue();
```

- Oder man lässt sich das JSON in ein DTO mappen:

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComicDto implements Serializable {
    // fields of interest

    // getters and setters
}
```

```java
ComicDto comic = restTemplate.getForObject(url, ComicDto.class);
```

### Weitere Features für besonders Schnelle

- Comic-Titel anzeigen
- Tag, Monat, Jahr anzeigen
- Wenn die Maus auf dem Bild ist, `alt`-Text anzeigen
- Falls dein System fest auf 3000 Comics verdrahtet ist:
  - `https://xkcd.com/info.0.json` liefert den neuesten Comic
  - dessen `num` ist die Anzahl existierender Comics
  - Prüfe 1x pro Stunde, ob es einen neueren Comic gibt
- 3 Comics nebeneinander anzeigen
  - mittlerer Comic normale Größe
  - links/rechts kleiner
  - links/rechts anklickbar, ersetzt prev/next-Buttons
- Bilder lokal im Dateisystem cachen
- Comics bewerten
  - mit 0-5 Sternen
  - oder Thumbs up/down
