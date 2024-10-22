## Operatoren

- Ein Operator repräsentiert eine Rechen-Operation
- Jeder Operator rechnet einen Ergebnis-Wert aus
- Manche Operatoren ändern außerdem eine Variable:
  - `++`, `--`
  - `=`, `+=`, `-=`, `*=`, ...

| Ausdruck           | Variablen-Änderung | Wert bzw. Operation   |
| ------------------ | ------------------ | --------------------- |
|                    |                    |                       |
| `v++`              | inkrementiert `v`  | alter Wert von `v`    |
| `v--`              | dekrementiert `v`  | alter Wert von `v`    |
|                    |                    |                       |
| `++v`              | inkrementiert `v`  | neuer Wert von `v`    |
| `--v`              | dekrementiert `v`  | neuer Wert von `v`    |
| `+a`               |                    | `0 + a`               |
| `-a`               |                    | `0 - a`               |
| `~a`               |                    | bitweise Negation     |
| `!a`               |                    | logische Negation     |
| `(T) w`            |                    | Typumwandlung; "Cast" |
|                    |                    |                       |
| `a * b`            |                    | Multiplikation        |
| `a / b`            |                    | Division              |
| `a % b`            |                    | Divisionsrest         |
|                    |                    |                       |
| `a + b`            |                    | Addition              |
| `a - b`            |                    | Subtraktion           |
|                    |                    |                       |
| `a << b`           |                    | Links-Shift           |
| `a >> b`           |                    | Rechts-Shift signed   |
| `a >>> b`          |                    | Rechts-Shift unsigned |
|                    |                    |                       |
| `a < b`            |                    | kleiner?              |
| `a <= b`           |                    | kleiner oder gleich?  |
| `a > b`            |                    | größer?               |
| `a >= b`           |                    | größer oder gleich?   |
| `r instanceof T`   |                    | würde `(T)r` klappen? |
|                    |                    |                       |
| `a == b`           |                    | gleich?               |
| `a != b`           |                    | ungleich?             |
|                    |                    |                       |
| `a & b`            |                    | bitweise UND          |
|                    |                    |                       |
| `a ^ b`            |                    | bitweise ODER exklusiv|
|                    |                    |                       |
| <code>a &#124; b</code> |               | bitweise ODER inklusiv|
|                    |                    |                       |
| `a && b`           |                    | logisches UND         |
|                    |                    |                       |
| <code>a &#124;&#124; b</code> |         | logisches ODER        |
|                    |                    |                       |
| `w ? dann : sonst` |                    | Fallunterscheidung    |
|                    |                    |                       |
| `v = w`            | kopiert `w` in `v` | neuer Wert von `v`    |
| `v += w`           | `v = v + w`        | neuer Wert von `v`    |
| `v -= w`           | `v = v - w`        | neuer Wert von `v`    |
| `v *= w`           | `v = v * w`        | neuer Wert von `v`    |
| `v /= w`           | `v = v / w`        | neuer Wert von `v`    |
| `v %= w`           | `v = v % w`        | neuer Wert von `v`    |
| `v &= w`           | `v = v & w`        | neuer Wert von `v`    |
| `v ^= w`           | `v = v ^ w`        | neuer Wert von `v`    |
| <code>v &#124;= w</code> | <code>v = v &#124; w</code> | neuer Wert von `v` |
| `v <<= w`          | `v = v << w`       | neuer Wert von `v`    |
| `v >>= w`          | `v = v >> w`       | neuer Wert von `v`    |
| `v >>>= w`         | `v = v >>> w`      | neuer Wert von `v`    |
