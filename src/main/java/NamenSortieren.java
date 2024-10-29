public class NamenSortieren {
    public static void main(String[] args) {

        String[] namen = new String[3];
        System.out.println("Bitte 3 Namen eingeben:");

        namen[0] = Konsole.readString("> ");
        namen[1] = Konsole.readString("> ");
        namen[2] = Konsole.readString("> ");

        java.util.Arrays.sort(namen);
        System.out.println("\nAufsteigend sortiert:");

        System.out.println(namen[0]);
        System.out.println(namen[1]);
        System.out.println(namen[2]);
    }
}
