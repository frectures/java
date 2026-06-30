package ttt;

import javax.swing.*;
import java.awt.*;

public class TicTacToeGUI {
    static void main() {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(TicTacToeGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        String[] BESCHRIFTUNG = {"", "X", "O"};
        String[] SPIELER = {"Keiner", "Spieler 1 (X)", "Spieler 2 (O)"};

        String GEWONNEN = "%s hat gewonnen. OK drücken für ein weiteres Spiel!";
        String BESETZT = "Diese Position ist bereits besetzt. Bitte eine andere wählen!";

        TicTacToe[] spielRef = {new TicTacToe()}; // Variable used in lambda expression must be (effectively) final

        JFrame frame = new JFrame(SPIELER[spielRef[0].aktuellerSpieler()]);
        frame.setLayout(new GridLayout(3, 3));

        JButton[] buttons = new JButton[9];
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 50);

        for (int i = 0; i < 9; ++i) {
            int position = i; // Variable used in lambda expression must be (effectively) final
            buttons[i] = new JButton();
            buttons[i].setFont(font);
            buttons[i].addActionListener(event -> {

                TicTacToe spiel = spielRef[0];
                if (spiel.istFrei(position)) {
                    spiel.besetze(position);
                    buttons[position].setText(BESCHRIFTUNG[spiel.besitzer(position)]);

                    if (spiel.istSpielVorbei()) {
                        JOptionPane.showMessageDialog(null, String.format(GEWONNEN, SPIELER[spiel.aktuellerSpieler()]), "Spiel vorbei", JOptionPane.INFORMATION_MESSAGE);

                        spiel = spielRef[0] = new TicTacToe();
                        for (JButton button : buttons) {
                            button.setText("");
                        }
                    }
                    frame.setTitle(SPIELER[spiel.aktuellerSpieler()]);
                } else {
                    JOptionPane.showMessageDialog(null, BESETZT, "Bereits besetzt", JOptionPane.ERROR_MESSAGE);
                }
            });
            frame.add(buttons[i]);
        }

        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
