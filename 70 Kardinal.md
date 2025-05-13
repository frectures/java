## m:n Beziehungen

- Ein und dieselbe Person wohnt in beliebig vielen Haushalten
- Beliebig viele Personen wohnen in ein und demselben Haushalt

### Person

| <u>id</u> | Vorname | Nachname   |
| --------: | ------- | ---------- |
|         1 | Max     | Mustermann |
|         2 | Moritz  | Mustermann |
|         3 | Karla   | Karriere   |
|         4 | Heinz   | Einsiedler |
|         5 | Peter   | Penner     |

&nbsp;

### Person_Haushalt

| *Person_id* | *Haushalt_id* |
| ----------: | :------------ |
|           1 | 6             |
|           2 | 6             |
|           3 | 7             |
|           3 | 8             |
|           4 | 9             |

&nbsp;

### Haushalt

| <u>id</u> | Straße      | Hausnummer |
| --------: | ----------- | ---------: |
|         6 | Apfelstraße |         23 |
|         7 | Birnengasse |         42 |
|         8 | Zitronenweg |         97 |
|         9 | Dattelkamp  |          7 |
|        10 | Bruchbude   |         1a |

&nbsp;

## 1:n Beziehungen

- Ein und dieselbe Person wohnt in beliebig vielen Haushalten
- Höchstens eine Personen wohnt in ein und demselben Haushalt

### Person

| <u>id</u> | Vorname | Nachname   |
| --------: | ------- | ---------- |
|         1 | Max     | Mustermann |
|         2 | Moritz  | Mustermann |
|         3 | Karla   | Karriere   |
|         4 | Heinz   | Einsiedler |
|         5 | Peter   | Penner     |

&nbsp;

### Haushalt

| <u>id</u> | Straße      | Hausnummer | *Person_id* |
| --------: | ----------- | ---------: | :---------- |
|         6 | Apfelstraße |         23 | 1           |
|         7 | Birnengasse |         42 | 3           |
|         8 | Zitronenweg |         97 | 3           |
|         9 | Dattelkamp  |          7 | 4           |
|        10 | Bruchbude   |         1a |             |

&nbsp;

## m:1 Beziehungen

- Ein und dieselbe Person wohnt in höchstens einem Haushalt
- Beliebig viele Personen wohnen in ein und demselben Haushalt

### Person

| <u>id</u> | Vorname | Nachname   | *Haushalt_id* |
| --------: | ------- | ---------- | :------------ |
|         1 | Max     | Mustermann | 6             |
|         2 | Moritz  | Mustermann | 6             |
|         3 | Karla   | Karriere   | 8             |
|         4 | Heinz   | Einsiedler | 9             |
|         5 | Peter   | Penner     |               |

&nbsp;

### Haushalt

| <u>id</u> | Straße      | Hausnummer |
| --------: | ----------- | ---------: |
|         6 | Apfelstraße |         23 |
|         7 | Birnengasse |         42 |
|         8 | Zitronenweg |         97 |
|         9 | Dattelkamp  |          7 |
|        10 | Bruchbude   |         1a |

&nbsp;

## 1:1 Beziehungen

- Ein und dieselbe Person wohnt in höchstens einem Haushalt
- Höchstens eine Person wohnt in ein und demselben Haushalt

Eindeutiger Fremdschlüssel in einer der beiden Tabellen
