package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisGUI {
    private static final int CELL_SIZE = 32;

    static void main() {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(TetrisGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Tetris[] games = {new Tetris(), new Tetris()};
        games[0].setOpponent(games[1]);
        games[1].setOpponent(games[0]);

        JPanel[] panels = new JPanel[2];
        for (int i = 0; i < panels.length; ++i) {
            int g = i;
            panels[g] = new JPanel() {
                protected void paintComponent(Graphics graphics) {
                    int width = getWidth();
                    int height = getHeight();
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0, 0, width, height);

                    int cellSize = Math.min(width / Tetris.WIDTH, height / Tetris.HEIGHT);
                    int marginX = (width - cellSize * Tetris.WIDTH) / 2;
                    int marginY = (height - cellSize * Tetris.HEIGHT) / 2;

                    Tetris game = games[g];
                    game.storeShape(Color.WHITE);

                    for (int y = 0; y < Tetris.HEIGHT; ++y) {
                        for (int x = 0; x < Tetris.WIDTH; ++x) {
                            Color color = game.colorAt(x, y);
                            if (color != null) {
                                graphics.setColor(color);
                                graphics.fillRect(marginX + x * cellSize, marginY + y * cellSize, cellSize, cellSize);
                            }
                        }
                    }

                    game.storeShape(null);

                    // https://stackoverflow.com/questions/19480076/java-animation-stutters-when-not-moving-mouse-cursor
                    Toolkit.getDefaultToolkit().sync();
                }
            };

            panels[g].setPreferredSize(new Dimension(Tetris.WIDTH * CELL_SIZE, Tetris.HEIGHT * CELL_SIZE));
        }

        JFrame frame = new JFrame("Tetris");
        frame.setLayout(new GridLayout(1, 2));
        frame.add(panels[0]);
        frame.add(panels[1]);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_A -> games[0].moveLeft();
                    case KeyEvent.VK_D -> games[0].moveRight();
                    case KeyEvent.VK_S -> games[0].moveDown();
                    case KeyEvent.VK_Q -> games[0].drop();
                    case KeyEvent.VK_W -> games[0].rotate();

                    case KeyEvent.VK_LEFT -> games[1].moveLeft();
                    case KeyEvent.VK_RIGHT -> games[1].moveRight();
                    case KeyEvent.VK_DOWN -> games[1].moveDown();
                    case KeyEvent.VK_ENTER -> games[1].drop();
                    case KeyEvent.VK_UP -> games[1].rotate();
                }
                frame.repaint();
            }
        });

        new Timer(1000 / Tetris.TICKS_PER_SECOND, (event) -> {
            for (int g = 0; g < games.length; ++g) {
                games[g].tick();
                if (games[g].gameOver()) {
                    games[g] = new Tetris();
                    games[0].setOpponent(games[1]);
                    games[1].setOpponent(games[0]);
                }
            }
            frame.repaint();
        }).start();
    }
}
