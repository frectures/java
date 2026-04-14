package shapes;

public class Circle implements Shape {

    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double width() {
        return 2 * radius;
    }

    public double height() {
        return 2 * radius;
    }

    public double area() {
        return Math.PI * (radius * radius);
    }
}
