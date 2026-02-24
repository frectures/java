import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PalindromTest {
    @Test
    public void leererStringPalindrom() {
        assertTrue(Palindrom.istPalindrom(""));
    }

    @Test
    public void einZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("a"));
        assertTrue(Palindrom.istPalindrom(" "));
        assertTrue(Palindrom.istPalindrom("\n"));
    }

    @Test
    public void zweiZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("aa"));
        assertTrue(Palindrom.istPalindrom("bb"));

        assertFalse(Palindrom.istPalindrom("ab"));
        assertFalse(Palindrom.istPalindrom("ba"));
    }

    @Test
    public void dreiZeichenPalindrom() {
        assertTrue(Palindrom.istPalindrom("aaa"));
        assertTrue(Palindrom.istPalindrom("ada"));
        assertTrue(Palindrom.istPalindrom("dad"));
        assertTrue(Palindrom.istPalindrom("ddd"));

        assertFalse(Palindrom.istPalindrom("aad"));
        assertFalse(Palindrom.istPalindrom("add"));
        assertFalse(Palindrom.istPalindrom("daa"));
        assertFalse(Palindrom.istPalindrom("dda"));
    }

    @Test
    public void bekanntePalindrome() {
        assertTrue(Palindrom.istPalindrom("beheb"));
        assertTrue(Palindrom.istPalindrom("handnah"));
        assertTrue(Palindrom.istPalindrom("level"));
        assertTrue(Palindrom.istPalindrom("neppen"));
        assertTrue(Palindrom.istPalindrom("regal-lager"));
    }

    @Test
    public void grossUndKleinschreibung() {
        assertTrue(Palindrom.istPalindrom("Anna"));
        assertTrue(Palindrom.istPalindrom("Hannah"));
        assertTrue(Palindrom.istPalindrom("Kajak"));
        assertTrue(Palindrom.istPalindrom("Neffen"));
        assertTrue(Palindrom.istPalindrom("Otto"));
        assertTrue(Palindrom.istPalindrom("Radar"));
        assertTrue(Palindrom.istPalindrom("Reittier"));
    }
}
