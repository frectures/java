package stack;

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class StackTest {
    private final Stack stack = new Stack();

    @Test
    void newStackIsEmpty() {
        assertThat(stack.isEmpty()).isTrue();
    }

    @Test
    void stackNoLongerEmptyAfterPush() {
        stack.push("something");

        assertThat(stack.isEmpty()).isFalse();
    }

    @Test
    void stackEmptyAgainAfterPop() {
        stack.push("something");
        stack.pop();

        assertThat(stack.isEmpty()).isTrue();
    }

    @Test
    void pushedStringIsPopped() {
        stack.push("hi");

        assertThat(stack.pop()).isEqualTo("hi");
    }

    @Test
    void cantPopEmptyStack() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(stack::pop);
    }

    @Test
    void lastInFirstOut() {
        stack.push("hello");
        stack.push("world");

        assertThat(stack.pop()).isEqualTo("world");
        assertThat(stack.pop()).isEqualTo("hello");
    }

    @Test
    void convertStackToArray() {
        stack.push("one");
        stack.push("two");
        stack.push("three");

        assertThat(stack.toArray()).containsExactly(
                "one",
                "two",
                "three"
        );
    }

    @Test
    void nullsSurvive() {
        stack.push(null);
        stack.push("Achtzehn");
        stack.push("Zwanzig");
        stack.push("Zwo");
        stack.push(null);
        stack.push("Vier");
        stack.push(null);

        assertThat(stack.toArray()).containsExactly(
                null,
                "Achtzehn",
                "Zwanzig",
                "Zwo",
                null,
                "Vier",
                null
        );
    }
}
