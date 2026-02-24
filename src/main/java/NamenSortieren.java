void main() {

    String[] namen = new String[3];
    IO.println("Bitte 3 Namen eingeben:");

    namen[0] = IO.readln("> ");
    namen[1] = IO.readln("> ");
    namen[2] = IO.readln("> ");

    Arrays.sort(namen);
    IO.println("\nAufsteigend sortiert:");

    IO.println(namen[0]);
    IO.println(namen[1]);
    IO.println(namen[2]);
}
