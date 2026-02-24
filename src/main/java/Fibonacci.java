long[] berechneFibonacciZahlen(int n) {
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

void main() {
    int n = Integer.parseInt(IO.readln("Wie viele Fibonacci-Zahlen? "));

    long[] fib = berechneFibonacciZahlen(n);

    IO.println(fib.length + " Fibonacci-Zahlen:");
    IO.println(Arrays.toString(fib));
}
