import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class StringArrayWrapper extends AbstractList<String> {
    private final String[] array;

    public StringArrayWrapper(String[] array) {
        this.array = array;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public String get(int index) {
        return array[index];
    }

    @Override
    public String set(int index, String element) {
        return array[index] = element;
    }


    public static void main(String[] args) {
        List<String> words = new StringArrayWrapper(new String[]{"the", "beauty", "and", "the", "beast"});
        int and = words.indexOf("and");

        List<String> before = words.subList(0, and);
        List<String> after = words.subList(and + 1, words.size());

        System.out.println(before);
        System.out.println(after);

        Collections.sort(words);
        System.out.println(words);
    }
}
