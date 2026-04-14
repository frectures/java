import shapes.*;

double addShapeAreas(Shape[] shapes) {
    double sum = 0;
    for (Shape shape : shapes) {
        sum += shape.area();
    }
    return sum;
}

void main() {

    Shape[] shapes = {
            new Square(3),
            new Square(4),

            new Circle(1),
            new Circle(10),
    };

    double totalArea = addShapeAreas(shapes);

    IO.println(shapes.length + " shapes with total area " + totalArea);
}
