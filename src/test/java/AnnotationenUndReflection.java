import org.junit.jupiter.api.Test;

void main() {
    for (Method method : PalindromTest.class.getDeclaredMethods()) {

        if (method.getAnnotation(Test.class) != null) {

            IO.println("Test-Methode gefunden: " + method);
        }
    }
}
