## Bedingte Schleifen mit `while`

### Einloggen

```java
public class Einloggen {
    public static void main(String[] args) {
        // Variablen deklarieren und initialisieren
        String passwort = Konsole.readString("Passwort? ");
        int versuche = 1;

        // Solange das Passwort falsch ist...
        while (passwort.hashCode() != -1249690433) {
            System.out.println("falsches Passwort");
            if (versuche == 5) return; // springt aus der aktuellen Methode raus

            // Variablen neu zuweisen
            passwort = Konsole.readString("Passwort? ");
            versuche = versuche + 1;
        }// springt zurück zum while

        System.out.println("Willkommen im System!");
    }
}
```

> **Übung:**
> - Was passiert, wenn man die Zuweisung `passwort = Konsole.readString("Passwort? ");` entfernt?
> - Versuche, das (recht gängige) Passwort zu raten
> - Und/oder ersetze die magische Zahl `-1249690433` durch den hashCode eines von dir gewählten Passworts

### Zahlenraten

```java
public class Zahlenraten {
    public static void main(String[] args) {

        int ausgedacht = (int) (Math.random() * 10) + 1;
        System.out.println("Ich habe mir eine Zahl zwischen 1 und 10 ausgedacht.");

        int geraten = Konsole.readInt("Welche Zahl mag es wohl sein? ");
        if (geraten == ausgedacht) {
            System.out.println("Nicht schlecht, du kannst wohl hellsehen!");
        } else {
            System.out.println(geraten + " ist falsch, ich hatte mir die " + ausgedacht + " ausgedacht!");
        }
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
