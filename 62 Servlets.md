## Servlets

- Ein *Servlet* (1996) ist eine objektorientierte Abstraktion des (zustandslosen) http-Protokolls:
  1. Client sendet http-Request an Server
  2. Server antwortet mit http-Response

![](img/servlets.svg)

- GET-Request
  - manuelle Eingabe einer Adresse im Browser
  - Anklicken eines Hyperlinks
  - Abschicken eines Formulars `<form method='get'>`
- POST-Request  
  - Abschicken eines Formulars `<form method='post'>`

### ServletComponentScan

- Normalerweise schreibt man heutzutage keine Servlets mehr selber
  - aber zu pädagogischen Zwecken ist das interessant/sinnvoll
  - Spring Boot verwendet intern z.B. ein `DispatcherServlet`
- Annotiere die vorhandene Klasse `DemoApplication` mit `@ServletComponentScan`:

```java
@SpringBootApplication
@ServletComponentScan // damit Spring Boot unsere Servlets findet
public class DemoApplication
```

### TimeServlet

- Erstelle eine neue Klasse `TimeServlet`:

```java
@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>");
            out.println(LocalDateTime.now());
            out.println("</h1>");
            out.println("</body></html>");
        }
    }
}
```

- Importiere die roten Klassen von oben nach unten
- Starte die Klasse `DemoApplication` erneut
- Browser http://localhost:8080/time
- Jetzt sollte man einen aktuellen Datum/Zeit-Stempel im Browser sehen

### FruitServlet

- Erstelle eine neue Klasse `FruitServlet`:

```java
@WebServlet("/fruit")
public class FruitServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h3>What is your favorite fruit?</h3>");
            out.println("<form method='post'>");
            out.println("<input type='text' name='fruit'>");
            out.println("<input type='submit' value='Yummy!'>");
            out.println("</form>");
            out.println("</body></html>");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            String favoriteFruit = request.getParameter("fruit");
            out.println("I like " + favoriteFruit + ", too!");
            out.println("</body></html>");
        }
    }
}
```

- Importiere die roten Klassen von oben nach unten
- Starte die Klasse `DemoApplication` erneut
- Browser http://localhost:8080/fruit
- Jetzt solltest du deine Lieblingsfrucht nennen dürfen

### GreetServlet

- Erstelle eine Kopie von `FruitServlet` namens `GreetServlet`
  - Du sollst dort Vorname und Nachname eintragen können
  - Anschließend sollst du mit deinem Namen angesprochen/begrüßt werden
- Was passiert, wenn der Anwender HTML in ein Formularfeld eingibt?
  - z.B. `<b>Fred</b>`
  - Woran liegt das?
  - Ist das schlimm?
  - Wie könnte man das Problem beheben?

### Diskussion

- Nachteile von Servlets:
  - Die meisten Webseiten bestehen aus viel HTML mit ein paar dynamischen Inhalten
  - Für dynamische Inhalte hat man die ungebändigte Mächtigkeit von Java
  - Dafür geht das HTML in Servlets leider komplett unter
  - IDEs unterstützen z.B. kein HTML in String-Literalen
  - Dynamische Inhalte muss man überall von Hand escapen
- Die Lösung dieser Probleme sind Template-Engines wie z.B. Thymeleaf:
  - HTML-Dialekte mit Spezialsyntax für dynamische Inhalte
  - Dynamische Inhalte werden automatisch escaped
