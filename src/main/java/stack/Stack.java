package stack;

// This class contains multiple bugs, on purpose
class Stack {
    private final String[] array = new String[10];
    private int index = 0;

    boolean isEmpty() {
        return index == 0;
    }

    void push(String element) {
        array[index++] = element;
    }

    String pop() {
        return array[index--];
    }

    String[] toArray() {
        return array.clone();
    }
}
