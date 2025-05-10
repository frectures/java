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
<form th:action="@{/fruit}" th:object="${fruit}" method="post">
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
        return "redirect:/fruit-ask";
    }
```

### Greet

- Konvertiere das `Greet`-Beispiel von Servlets nach Spring MVC

### Rest des Tages

- Baue eine kleine Thymeleaf-Anwendung für eine Domäne, die dich interessiert:
  - Kochrezepte
  - Yoga-Übungen
  - Weinkeller
  - Manga-Sammlung
  - URL Shortener
  - ...
- Erstrebenswerte technische Features:
  - Daten tabellarisch anzeigen
  - Daten hinzufügen
  - Daten entfernen
  - Daten sortieren
  - Daten filtern
  - ...
