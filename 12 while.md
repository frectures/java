## Bedingte Schleifen mit `while`

### Einloggen.java

```java
void main() {
    // Variablen deklarieren und initialisieren
    String passwort = IO.readln("Passwort? ");
    int versuche = 1;

    // Solange das Passwort falsch ist...
    while (passwort.hashCode() != -1249690433) {
        IO.println("falsches Passwort");
        if (versuche == 5) return; // springt aus der aktuellen Methode raus

        // Variablen neu zuweisen
        passwort = IO.readln("Passwort? ");
        versuche = versuche + 1;
    }// springt zurück zum while

    IO.println("Willkommen im System!");
}
```

> **Übung:**
> - Was passiert, wenn man die Zuweisung `passwort = IO.readln("Passwort? ");` entfernt?
> - Versuche, das (recht gängige) Passwort zu raten
> - Und/oder ersetze die magische Zahl `-1249690433` durch den hashCode eines von dir gewählten Passworts

### Zahlenraten.java

```java
void main() {

    int ausgedacht = (int) (Math.random() * 10) + 1;
    IO.println("Ich habe mir eine Zahl zwischen 1 und 10 ausgedacht.");

    int geraten = Integer.parseInt(IO.readln("Welche Zahl mag es wohl sein? "));
    if (geraten == ausgedacht) {
        IO.println("Nicht schlecht, du kannst wohl hellsehen!");
    } else {
        IO.println(geraten + " ist falsch, ich hatte mir die " + ausgedacht + " ausgedacht!");
    }
}
```

| Ausdruck                         | Minimalwert | Maximalwert |
| :------------------------------: | ----------- | ----------- |
| `Math.random()`                  | 0.0         | 0.999...    |
| `Math.random() * 10`             | 0.0         | 9.999...    |
| `(int) (Math.random() * 10)`     | 0           | 9           |
| `(int) (Math.random() * 10) + 1` | 1           | 10          |

> **Übung:**
> - Lasse den Benutzer beliebig oft raten
> - Zähle die Rateversuche mit
> - Ändere den Zahlenbereich auf 0 bis 100
>   - Dann sollte der Computer immer verraten, ob seine eigene Zahl kleiner oder größer ist
> - Bewerte die Anzahl Rateversuche am Ende
> - Belehre den Nutzer bei unsinnigen Rateversuchen
>   - Negative Zahlen
>   - Zahlen größer als 100
>   - selbe Zahl wie vorher
>   - ...
