import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobyDick {
    public static void main(String[] args) throws IOException {

        Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

        String inhalt = Files.readString(pfad);

        System.out.println("Anzahl Zeichen: " + inhalt.length());
    }
}
