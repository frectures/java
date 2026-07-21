package tetris;

import cafe.Zufall;

public class UnfairLetterSupplier {
    // potenziell beliebig lange Pausen zwischen guten Steinen
    public char nextLetter() {
        return "IJLOSTZ".charAt(Zufall.nextInt() % 7);
    }
}
