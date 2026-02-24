void main() {

    int ausgedacht = (int) (Math.random() * 10) + 1;
    IO.println("Ich habe mir eine Zahl zwischen 1 und 10 ausgedacht.");

    int geraten = Integer.parseInt(IO.readln("Welche Zahl mag es wohl sein? "));
    if (geraten == ausgedacht) {
        IO.println("Nicht schlecht, du kannst wohl hellsehen!");
    } else {
        IO.println(geraten + " ist falsch, ich hatte mir die " + ausgedacht + " ausgedacht!");
    }
}
