import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class AnnotationenUndReflection {
    public static void main(String[] args) {
        for (Method method : TextAnalyseTest.class.getDeclaredMethods()) {

            Test annotation = method.getAnnotation(Test.class);
            if (annotation != null) {
                System.out.println("Test-Methode gefunden: " + method);
            }
        }
    }
}
