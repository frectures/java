import java.util.List;

abstract class Pet {
    // Jedes Haustier hat einen Namen
    private final String name;

    // Konstruktor nur in Subklassen zugreifbar
    protected Pet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "The " + getClass().getName() + " named " + name + " says: " + noise();
    }

    // Jedes Haustier macht ein TYPisches Geräusch
    protected abstract String noise();


    public static void main(String[] args) {
        List<Pet> pets = List.of(
                new Cat("Snowball"),
                new Dog("Santa's Little Helper")
        );
        for (Pet pet : pets) {
            System.out.println(pet);
        }
    }
}


class Cat extends Pet {

    protected Cat(String name) {
        super(name); // ruft den Pet-Konstruktor auf
    }

    @Override
    protected String noise() {
        return "meow..."; // Katzen miauen
    }
}


class Dog extends Pet {

    protected Dog(String name) {
        super(name); // ruft den Pet-Konstruktor auf
    }

    @Override
    protected String noise() {
        return "wuff!"; // Hunde bellen
    }
}
