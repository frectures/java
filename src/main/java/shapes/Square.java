package shapes;

public class Square implements Shape {

    private final double length;

    public Square(double length) {
        this.length = length;
    }

    public double width() {
        return length;
    }

    public double height() {
        return length;
    }

    public double area() {
        return length * length;
    }
}
