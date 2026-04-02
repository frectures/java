## Methoden

- Häufig wird Code lesbarer, wenn man komplizierte Logik in eigene Methoden auslagert
- Methoden kommunizieren über Parameter/Argumente und Ergebnisse miteinander

### return

```java
int zufallszahl() {
    return (int) (Math.random() * 10) + 1;
}

void main() {

    int ausgedacht = zufallszahl();
    // ...
}
```

- Der Ausdruck `zufallszahl()` ruft die Methode auf...
- ...und bekommt den Ergebnis-Wert hinter dem `return`

### Parameter/Argumente

```java
int zufallszahl(int von, int bis) {
    int anzahl = bis - von + 1;
    return (int) (Math.random() * anzahl) + von;
}

void main() {

    int einfach = zufallszahl(1, 10);
    int schwierig = zufallszahl(0, 100);
    // ...
}
```

- Erster Aufruf
  - Der Parameter `von` wird mit dem Argument `1` initialisiert
  - Der Parameter `bis` wird mit dem Argument `10` initialisiert
- Zweiter Aufruf
  - Der Parameter `von` wird mit dem Argument `0` initialisiert
  - Der Parameter `bis` wird mit dem Argument `100` initialisiert

### Mathematische Funktionen

- In der Mathematik werden Funktionen wie folgt notiert:

```
square: N -> N

square(x) = x²
```

- Obige Beispiel-Funktion kann mit folgender Java-Methode umgesetzt werden:

```java
int square(int x)
{
    return x * x;
}

square(5); // 25
square(7); // 49
```

> **Plenum:**
> - Schreibe eine Java-Methode für die Funktion `cube(x) = x³`
> - Schreibe eine Java-Methode für die Funktion `hypercube(x) = x^4`
> - Kannst du `hypercube` mit 2 Multiplikationen und 1 Variable lösen?
> - Kannst du `hypercube` mit `square` lösen, ohne Multiplikation und Variable?
