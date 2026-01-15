package gravity;

import java.awt.*;

public class Point {
    private double positionX;
    private double positionY;

    private double velocityX;
    private double velocityY;

    public Point(double screenWidth, double screenHeight) {
        positionX = Math.random() * screenWidth;
        positionY = Math.random() * screenHeight;

        velocityX = Math.random() * 2 - 1;
        velocityY = Math.random() * 2 - 1;
    }

    public void paint(Graphics graphics) {
        int x = (int) positionX;
        int y = (int) positionY;

        graphics.setColor(Color.WHITE);
        graphics.fillRect(x, y, 2, 2);
    }

    public void updatePosition() {
        positionX += velocityX;
        positionY += velocityY;
    }

    public void updateVelocity(Point[] points) {
        double attractionX = 0;
        double attractionY = 0;

        for (Point that : points) {
            // Plenum: Warum ist diese Fallunterscheidung notwendig?
            // Wie würde sich der Rumpf verhalten, wenn this==that wäre?
            if (this != that) {
                double distanceX = that.positionX - this.positionX;
                double distanceY = that.positionY - this.positionY;
                double distanceSquared = distanceX * distanceX + distanceY * distanceY;

                double inverseDistanceCubed = Math.pow(distanceSquared, -1.5);
                double attractionOverDistance = 5 * inverseDistanceCubed;

                attractionX += attractionOverDistance * distanceX;
                attractionY += attractionOverDistance * distanceY;
            }
        }

        velocityX += attractionX;
        velocityY += attractionY;
    }
}
