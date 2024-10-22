public class ZeichenHaeufigkeit {
    public static int[] zaehleZeichen(String text) {
        //                                        65536
        int[] zaehler = new int[Character.MAX_VALUE + 1];

        for (char ch : text.toCharArray()) {
            zaehler[ch] += 1;
        }

        return zaehler;
    }

    public static void main(String[] args) {
        String text = Konsole.readString("Text? ");

        int[] zaehler = zaehleZeichen(text);

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            System.out.println(ch + ": " + zaehler[ch]);
        }
    }
}
