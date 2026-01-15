public class Fibonacci {
    public static long[] berechneFibonacciZahlen(int n) {
        if (n < 2) n = 2;
        if (n > 93) n = 93;

        long[] fib = new long[n];
        fib[0] = 0L;
        fib[1] = 1L;

        for (int i = 2; i < n; ++i) {
            fib[i] = fib[i - 2] + fib[i - 1];
        }

        return fib;
    }

    public static void main(String[] args) {
        int n = Konsole.readInt("Wie viele Fibonacci-Zahlen? ");

        long[] fib = berechneFibonacciZahlen(n);

        System.out.println(fib.length + " Fibonacci-Zahlen:");
        System.out.println(java.util.Arrays.toString(fib));
    }
}
