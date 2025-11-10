## Typen

- Primitive Typen
  - Wahrheitswerte: `boolean` nach George Boole
  - Ganzzahlen: `byte`, `short`, `char`, `int`, `long`
  - Kommazahlen: `float`, `double` gemäß IEEE754
- Referenztypen
  - `null` oder Referenz auf Heap-Objekt
  - 4 oder 8 Byte, je nach JVM(-Optionen) und Heap-Größe
  - `T[]`
  - `class A { ... }`
  - `interface B { ... }`
  - `enum C { ... }`
  - `record D { ... }`
  - ⚠️ `v = w` kopiert lediglich die Referenz, *nicht* das referenzierte Objekt!
  - ⚠️ `a == b` vergleicht lediglich die Referenzen, *nicht* die referenzierten Objekte!

### Primitive Typen

|       Typ | Bytes |                Minimalwert |                Maximalwert |
| --------: | :---: | -------------------------: | -------------------------: |
| `boolean` |   1   |                      false |                       true |
|    `byte` |   1   |                       -128 |                       +127 |
|   `short` |   2   |                    -32_768 |                    +32_767 |
|    `char` |   2   |                          0 |                     65_535 |
|     `int` |   4   |             -2_147_483_648 |             +2_147_483_647 |
|    `long` |   8   | -9_223_372_036_854_775_808 | +9_223_372_036_854_775_807 |
|   `float` |   4   |     1.4 * 10<sup>-45</sup> |     3.4 * 10<sup>+38</sup> |
|  `double` |   8   |    4.9 * 10<sup>-324</sup> |    1.8 * 10<sup>+308</sup> |
