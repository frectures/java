public class Zahlenraten {
    public static void main(String[] args) {

        int ausgedacht = (int) (Math.random() * 10) + 1;
        System.out.println("Ich habe mir eine Zahl zwischen 1 und 10 ausgedacht.");

        int geraten = Konsole.readInt("Welche Zahl mag es wohl sein? ");
        if (geraten == ausgedacht) {
            System.out.println("Nicht schlecht, du kannst wohl hellsehen!");
        } else {
            System.out.println(geraten + " ist falsch, ich hatte mir die " + ausgedacht + " ausgedacht!");
        }
    }
}
