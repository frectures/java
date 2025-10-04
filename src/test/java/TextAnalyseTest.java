import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextAnalyseTest {
    @Test
    public void leererStringPalindrom() {
        assertTrue(TextAnalyse.istPalindrom(""));
    }

    @Test
    public void einZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("a"));
        assertTrue(TextAnalyse.istPalindrom(" "));
        assertTrue(TextAnalyse.istPalindrom("\n"));
    }

    @Test
    public void zweiZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("aa"));
        assertTrue(TextAnalyse.istPalindrom("bb"));

        assertFalse(TextAnalyse.istPalindrom("ab"));
        assertFalse(TextAnalyse.istPalindrom("ba"));
    }

    @Test
    public void dreiZeichenPalindrom() {
        assertTrue(TextAnalyse.istPalindrom("aaa"));
        assertTrue(TextAnalyse.istPalindrom("ada"));
        assertTrue(TextAnalyse.istPalindrom("dad"));
        assertTrue(TextAnalyse.istPalindrom("ddd"));

        assertFalse(TextAnalyse.istPalindrom("aad"));
        assertFalse(TextAnalyse.istPalindrom("add"));
        assertFalse(TextAnalyse.istPalindrom("daa"));
        assertFalse(TextAnalyse.istPalindrom("dda"));
    }

    @Test
    public void bekanntePalindrome() {
        assertTrue(TextAnalyse.istPalindrom("beheb"));
        assertTrue(TextAnalyse.istPalindrom("handnah"));
        assertTrue(TextAnalyse.istPalindrom("level"));
        assertTrue(TextAnalyse.istPalindrom("neppen"));
        assertTrue(TextAnalyse.istPalindrom("regal-lager"));
    }

    @Test
    public void grossUndKleinschreibung() {
        assertTrue(TextAnalyse.istPalindrom("Anna"));
        assertTrue(TextAnalyse.istPalindrom("Hannah"));
        assertTrue(TextAnalyse.istPalindrom("Kajak"));
        assertTrue(TextAnalyse.istPalindrom("Neffen"));
        assertTrue(TextAnalyse.istPalindrom("Otto"));
        assertTrue(TextAnalyse.istPalindrom("Radar"));
        assertTrue(TextAnalyse.istPalindrom("Reittier"));
    }
}
