int[] zaehleZeichen(String text) {

    int[] zaehler = new int[65536];

    for (char ch : text.toCharArray()) {
        zaehler[ch] += 1;
    }

    return zaehler;
}

void main() {
    String text = IO.readln("Text? ");

    int[] zaehler = zaehleZeichen(text);

    for (char ch = 'a'; ch <= 'z'; ++ch) {
        IO.println(ch + ": " + zaehler[ch]);
    }
}
