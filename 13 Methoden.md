## Methoden

- Häufig wird Code lesbarer, wenn man komplizierte Logik in eigene Methoden auslagert
- Methoden kommunizieren über Parameter/Argumente und Ergebnisse miteinander

### return

```java
public class Zahlenraten {

    public static int zufallszahl() {
        return (int) (Math.random() * 10) + 1;
    }

    public static void main(String[] args) {

        int ausgedacht = zufallszahl();
        // ...
    }
}
```

- Der Ausdruck `zufallszahl()` ruft die Methode auf...
- ...und bekommt den Ergebnis-Wert hinter dem `return`

### Parameter/Argumente

```java
public class Zahlenraten {

    public static int zufallszahl(int von, int bis) {
        int anzahl = bis - von + 1;
        return (int) (Math.random() * anzahl) + von;
    }

    public static void main(String[] args) {

        int einfach = zufallszahl(1, 10);
        int schwierig = zufallszahl(0, 100);
        // ...
    }
}
```

- Erster Aufruf
  - Der Parameter `von` wird mit dem Argument `1` initialisiert
  - Der Parameter `bis` wird mit dem Argument `10` initialisiert
- Zweiter Aufruf
  - Der Parameter `von` wird mit dem Argument `0` initialisiert
  - Der Parameter `bis` wird mit dem Argument `100` initialisiert

### Einloggen

```java
public class Einloggen {
    public static boolean einloggen(int passwortHashCode, int erlaubteVersuche) {

        String passwort = Konsole.readString("Passwort? ");
        int versuche = 1;

        while (passwort.hashCode() != passwortHashCode) {
            System.out.println("falsches Passwort");
            if (versuche == erlaubteVersuche) return false;

            passwort = Konsole.readString("Passwort? ");
            versuche = versuche + 1;
        }

        return true;
    }

    public static void main(String[] args) {
        boolean erfolgreich = einloggen(-1249690433, 5);
        if (erfolgreich) {
            System.out.println("Willkommen im System!");
        } else {
            System.out.println("zu viele Fehlversuche");
        }
    }
}
```
