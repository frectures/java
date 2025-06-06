import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobyDick {
    public static void main(String[] args) throws IOException {
        Path pfad = Path.of(System.getProperty("user.home"), "Downloads", "2701-0.txt");

        String inhalt = Files.readString(pfad);
        System.out.println("Anzahl Zeichen: " + inhalt.length());

        List<String> woerter = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\p{IsAlphabetic}+").matcher(inhalt);
        while (matcher.find()) {           //////////////////
            String wort = matcher.group();
            woerter.add(wort.toLowerCase());
        }
        System.out.println("Anzahl Wörter hintereinander: " + woerter.size());

        Set<String> wortschatz = new HashSet<>();

        long before = System.currentTimeMillis();
        for (String wort : woerter) {
            wortschatz.add(wort);
        }
        long after = System.currentTimeMillis();
        System.out.println(after - before + " ms");

        System.out.println("Anzahl unterschiedlicher Wörter: " + wortschatz.size());
        System.out.println("Die ersten 10 Wörter im Wortschatz:");
        int i = 0;
        for (String wort : wortschatz) {
            System.out.println(wort);
            if (++i == 10) break;
        }
    }
}
