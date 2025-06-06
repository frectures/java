package tetris;

public class CheatingLetterSupplier implements LetterSupplier {
    private boolean cheat;

    @Override
    public int nextLetter() {
        cheat = !cheat;
        if (cheat) {
            return 1;
        } else {
            return 2 + (int) (Math.random() * 6);
        }
    }
}
