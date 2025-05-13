## HTTP

### HttpUrlConnection (1997)

```java
public static String download(String url) throws IOException {

    var connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
    connection.setRequestMethod("GET");
    connection.connect();
    try {
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP status code: " + connection.getResponseCode());
        }
        try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return in.lines().collect(Collectors.joining("\n"));
        }
    } finally {
        connection.disconnect();
    }
}
```

### HttpClient (2018)

```java
public static String download(String url) throws IOException, InterruptedException {

    HttpClient client = HttpClient.newBuilder().build();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
                                                                               // .ofBytes()
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                                                                               // .ofLines()
    if (response.statusCode() != HttpURLConnection.HTTP_OK) {
        throw new IOException("HTTP status code: " + response.statusCode());
    }

    return response.body();
}
```

### Populäre Passwörter

- `geheim` ist ein populäres Passwort:
  - sein SHA-1-Hash ist `90607 2001EFDDF3E11E6D2B5782F4777FE038739`
  - https://api.pwnedpasswords.com/range/90607 liefert ca. 1200 Zeilen
  - eine davon ist `2001EFDDF3E11E6D2B5782F4777FE038739:78333`
  - d.h. `78333` bekannte Accounts nutzen `geheim`
- Schreibe eine Konsolen-Anwendung, die:
  - den Benutzer nach einem Passwort fragt, und
  - ihm verrät, wie viele bekannte Accounts dieses Passwort nutzen
- Brotkrümel:
  - `Konsole.readString`
  - `MessageDigest.getInstance`
  - `Base64.getEncoder`
  - `String.substring`
  - `HttpUrlConnection` oder `HttpClient`, siehe `download`-Beispielmethoden
  - 🏆 Verarbeite die Server-Antwort nur bis zur relevanten Zeile
