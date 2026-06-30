package advent;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

class AdventOfCodeTest {
    @Test
    void countIncreases() {
        var measurements = List.of(
                199,
                200,
                208,
                210,
                200,
                207,
                240,
                269,
                260,
                263
        );
        assertThat(Advent.countIncreases(measurements)).isEqualTo(7);
    }

    @Test
    void findBiggestSum() {
        var backpacks = List.of(
                List.of(1000, 2000, 3000),
                List.of(4000),
                List.of(5000, 6000),
                List.of(7000, 8000, 9000),
                List.of(10000)
        );
        assertThat(Advent.findBiggestSum(backpacks)).isEqualTo(7000 + 8000 + 9000);
    }

    @Nested
    class Find2020 {
        @Test
        void leading1010() {
            var entries = List.of(
                    1010,
                    1721,
                    979,
                    366,
                    675,
                    1456,
                    299
            );
            assertThat(Advent.find2020(entries)).isEqualTo(1721 * 299);
        }

        @Test
        void trailing1010() {
            var entries = List.of(
                    1721,
                    979,
                    366,
                    675,
                    1456,
                    299,
                    1010
            );
            assertThat(Advent.find2020(entries)).isEqualTo(1721 * 299);
        }

        @Test
        void different1010s() {
            var entries = List.of(
                    1010,
                    979,
                    366,
                    675,
                    1456,
                    1010
            );
            assertThat(Advent.find2020(entries)).isEqualTo(1010 * 1010);
        }
    }

    @Test
    void addDistances() {
        var pairs = List.of(
                new Advent.Line(3, 4),
                new Advent.Line(4, 3),
                new Advent.Line(2, 5),
                new Advent.Line(1, 3),
                new Advent.Line(3, 9),
                new Advent.Line(3, 3)
        );
        assertThat(Advent.addDistances(pairs)).isEqualTo(11);
    }
}
