public class Count {
    public static void main(String[] args) {
        int[] count = new int[65536];
        for (String arg : args) {
            for (char ch : arg.toCharArray()) {
                count[ch] += 1;
            }
        }
        char ch = 0;
        for (int n : count) {
            if (n > 0) {
                System.out.println(ch + ": " + n);
            }
            ++ch;
        }
    }
}
