package gravity;

import javax.swing.*;
import java.awt.*;

public class Cloud {
    public static void main(String[] args) {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(Cloud::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Point[] points = new Point[100];
        for (int i = 0; i < points.length; ++i) {
            points[i] = new Point(screenSize.getWidth(), screenSize.getHeight());
        }

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics graphics) {
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, getWidth(), getHeight());

                for (Point point : points) {
                    point.paint(graphics);
                }

                // https://stackoverflow.com/questions/19480076/java-animation-stutters-when-not-moving-mouse-cursor
                Toolkit.getDefaultToolkit().sync();
            }
        };

        new Timer(1000 / 60, (event) -> {
            for (Point point : points) {
                point.updatePosition();
            }
            // Die getrennten Schleifen garantieren, dass alle Geschwindigkeitseinflüsse
            // von fremden Punkt-Positionen zum SELBEN Zeitpunkt abhängen.
            // (Sonst wäre es ein Mischmasch aus alten und neuen Positionen.)
            for (Point point : points) {
                point.updateVelocity(points);
            }

            panel.repaint();

        }).start();

        JFrame frame = new JFrame("Cloud");
        frame.add(panel);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
