package ttt;

public class TicTacToe {
    private final int[] spielfeld;
    private int spieler;
    private boolean spielVorbei;

    public TicTacToe() {
        spielfeld = new int[9];
        spieler = 1;
        spielVorbei = false;
    }

    public int besitzer(int position) {
        return spielfeld[position];
    }

    public boolean istFrei(int position) {
        return spielfeld[position] == 0;
    }

    public int aktuellerSpieler() {
        return spieler;
    }

    public boolean istSpielVorbei() {
        return spielVorbei;
    }

    public void besetze(int position) {
        spielfeld[position] = spieler;

        if (hatSpielerGewonnen()) {
            spielVorbei = true;
        } else if (istSpielfeldVoll()) {
            spieler = 0;
            spielVorbei = true;
        } else {
            spieler = 3 - spieler;
        }
    }

    private boolean hatSpielerGewonnen() {
        // TODO
        return spielfeld[0] == spieler && spielfeld[1] == spieler && spielfeld[2] == spieler ||

                spielfeld[0] == spieler && spielfeld[3] == spieler && spielfeld[6] == spieler ||

                spielfeld[0] == spieler && spielfeld[4] == spieler && spielfeld[8] == spieler;
    }

    private boolean istSpielfeldVoll() {
        // TODO
        return false;
    }
}
