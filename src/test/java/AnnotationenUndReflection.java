import org.junit.jupiter.api.Test;

void main() {
    for (Method method : PalindromTest.class.getDeclaredMethods()) {

        Test annotation = method.getAnnotation(Test.class);
        if (annotation != null) {
            IO.println("Test-Methode gefunden: " + method);
        }
    }
}
