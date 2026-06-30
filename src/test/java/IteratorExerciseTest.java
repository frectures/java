import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;

public class IteratorExerciseTest {

    @Nested
    class JoinWithComma {
        @Test
        void zeroElements() {
            Iterator<?> it = Collections.emptyIterator();

            assertThat(IteratorExercise.joinWithComma(it)).isEqualTo("");
        }

        @Test
        void oneElement() {
            Iterator<?> it = List.of("apple").iterator();

            assertThat(IteratorExercise.joinWithComma(it)).isEqualTo("apple");
        }

        @Test
        void twoElements() {
            Iterator<?> it = List.of("apple", "banana").iterator();

            assertThat(IteratorExercise.joinWithComma(it)).isEqualTo("apple, banana");
        }

        @Test
        void threeElements() {
            Iterator<?> it = List.of("apple", "banana", "cherry").iterator();

            assertThat(IteratorExercise.joinWithComma(it)).isEqualTo("apple, banana, cherry");
        }

        @Test
        void fourElements() {
            Iterator<?> it = List.of("apple", "banana", "cherry", "date").iterator();

            assertThat(IteratorExercise.joinWithComma(it)).isEqualTo("apple, banana, cherry, date");
        }
    }

    @Nested
    class HaveEqualElements {

        private static final String A = "apple";
        private static final String B = "banana";
        private static final String C = "cherry";
        private static final String D = "date";

        private static final List<List<String>> sublists = List.of(
                List.of(),

                List.of(A),
                List.of(B),
                List.of(C),
                List.of(D),

                List.of(A, B),
                List.of(A, C),
                List.of(A, D),
                List.of(B, C),
                List.of(B, D),
                List.of(C, D),

                List.of(A, B, C),
                List.of(A, B, D),
                List.of(A, C, D),
                List.of(B, C, D),

                List.of(A, B, C, D)
        );

        @TestFactory
        Stream<DynamicTest> compareSublists() {
            return sublists.stream().flatMap(list1 -> {
                return sublists.stream().map(list2 -> {
                    // generiert 16*16 = 256 Testfälle
                    String displayName = "%s %s %s".formatted(list1, (list1 == list2) ? "=" : "≠", list2);
                    return dynamicTest(displayName, () -> {
                        boolean expected = (list1 == list2);
                        boolean actual = IteratorExercise.haveEqualElements(list1.iterator(), list2.iterator());
                        assertEquals(expected, actual);
                    });
                });
            });
        }

        @DisplayName("not == but equals")
        @Test
        void equality() {
            var it1 = List.of(0, 127, 128, Integer.MAX_VALUE).iterator();
            var it2 = List.of(0, 127, 128, Integer.MAX_VALUE).iterator();

            assertTrue(IteratorExercise.haveEqualElements(it1, it2));
        }

        @Timeout(value = 1, unit = SECONDS, threadMode = SEPARATE_THREAD)
        @Test
        void infiniteIterators() {
            var it1 = new NaturalNumbers();
            var it2 = new PowersOfTwo();

            assertFalse(IteratorExercise.haveEqualElements(it1, it2));
        }
    }
}
