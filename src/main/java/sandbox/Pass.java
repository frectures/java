package sandbox;

class Box {
    int content;
}

class Pass {
    static void pass(Box x) {
        x.content = 1;
        x = new Box();
        x.content = 2;
    }

    public static void main(String[] args) {
        Box a = new Box();
        pass(a);
        System.out.println(a.content);
    }
}
