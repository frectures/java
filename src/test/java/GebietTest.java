import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GebietTest {
    @Test
    public void plainToString() {
        String string = new Gebiet(33615, "Bielefeld").toString();
        assertEquals("33615 Bielefeld", string);
    }

    @Test
    public void liste() {
        List<Gebiet> liste = new ArrayList<>();
        liste.add(new Gebiet(33615, "Bielefeld"));
        assertTrue(liste.contains(new Gebiet(33615, "Bielefeld")));
    }

    @Test
    public void menge() {
        Set<Gebiet> menge = new HashSet<>();
        menge.add(new Gebiet(33615, "Bielefeld"));
        assertTrue(menge.contains(new Gebiet(33615, "Bielefeld")));
    }

    @Test
    public void baum() {
        Set<Gebiet> baum = new TreeSet<>();
        baum.add(new Gebiet(33615, "Bielefeld"));
        assertTrue(baum.contains(new Gebiet(33615, "Bielefeld")));
    }
}
