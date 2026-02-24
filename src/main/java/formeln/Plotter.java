package formeln;

import javax.swing.*;
import java.awt.*;

public class Plotter {
    static void main() {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(Plotter::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Formel[] formeln = {Math::cos, Math::sin};
        Color[] farben = {Color.BLACK, Color.BLUE};

        JPanel plot = new JPanel() {
            public void paintComponent(Graphics graphics) {
                int width = getWidth();
                int height = getHeight();

                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, width, height);

                for (int i = 0; i < formeln.length; i++) {
                    Formel formel = formeln[i];
                    graphics.setColor(farben[i]);

                    for (int screenX = 0; screenX < width; ++screenX) {
                        double x = (screenX - width * 0.5) / height * 2;
                        double y = formel.ausrechnen(x);
                        int screenY = (int) (height * (1 - y) * 0.5);
                        graphics.fillRect(screenX, screenY, 2, 2);
                    }
                }
            }
        };

        JTextField links = new JTextField("1 - x*x/2 + x*x*x*x/24 - x*x*x*x*x*x/720");
        links.setFont(links.getFont().deriveFont(20f));
        links.addActionListener(event -> {
            String text = event.getActionCommand();
            formeln[0] = Parser.parse(text);
            plot.repaint();
        });

        JTextField rechts = new JTextField("x - x*x*x/6 + x*x*x*x*x/120");
        rechts.setFont(rechts.getFont().deriveFont(20f));
        rechts.addActionListener(event -> {
            String text = event.getActionCommand();
            formeln[1] = Parser.parse(text);
            plot.repaint();
        });

        JPanel oben = new JPanel(new GridLayout(1, 2));
        oben.add(links);
        oben.add(rechts);

        JFrame frame = new JFrame("Plot");
        frame.add(oben, BorderLayout.NORTH);
        frame.add(plot, BorderLayout.CENTER);

        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
