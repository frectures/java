import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {

        double[] randomNumbers = new Random().doubles(10, -1, 1)
                .toArray();
        System.out.println(java.util.Arrays.toString(randomNumbers));

        long[] powersOfTwo = LongStream.iterate(1, x -> x != 0, x -> x * 2)
                .toArray();
        System.out.println(java.util.Arrays.toString(powersOfTwo));

        int[] codePoints = IntStream.concat(
                IntStream.rangeClosed('A', 'Z'),
                IntStream.rangeClosed('a', 'z')
        ).toArray();

        String s = new String(codePoints, 0, codePoints.length);
        System.out.println(s);
    }
}
