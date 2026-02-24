package cafe;

import jdk.jshell.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

public class JGround {

    private static final Path PATH = Path.of(System.getProperty("user.home"), "jground1.txt");

    private static final int TAB_SIZE = 4;

    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);

    private static final String MANUAL = """
            F1  documentation/completion at cursor
            F5  execute whole program
            F8  execute section (separated by 2 empty lines)
            F9  execute section to current line
            F12 execute selection/current line
            
            combine with SHIFT for more details
            """;

    private static String printer() {
        return "🖶 println " + java.time.LocalTime.now().withNano(0) + "\n";
    }

    private static final String[] IMPORTS = {
            "import java.math.*;",
            "import java.nio.file.*;",
            "import java.util.*;",
            "import java.util.stream.*;",
            "void println(Object x) { IO.println(x); }",
            "void println(        ) { IO.println( ); }",
            "void print  (Object x) { IO.print  (x); }",

            "import cafe.Assertions;",
            "import static cafe.Assertions.*;",
            "import cafe.Ints;",
            "import static cafe.Ints.range;",
            "import cafe.Seq;",
            "import static cafe.Seq.seq;",
    };

    private static final String EXAMPLE = """
            1 + 2
            1 + 2 * 3
            (1 + 2) * 3
            
            1_234_567_890
            1_000_000_000 + 2_000_000_000
            Integer.MAX_VALUE
            Integer.MAX_VALUE + 1
            
            
            9 / 2
            9 % 2
            
            9.0 / 2.0
            0.1 + 0.2
            
            Math.PI
            Math.sqrt(2)
            (Math.sqrt(5) + 1) / 2
            Math.pow(2, 9)
            
            
            // square: R -> R
            // square(x) = x²
            double square(double x)
            {
                return x * x;
            }
            
            square(3);
            square(4);
            square(3) + square(4);
            
            Math.pow(square(3) + square(4), 0.5);
            """;

    static void main() {
        Locale.setDefault(Locale.ENGLISH); // JShell diagnostics
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        EventQueue.invokeLater(JGround::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        var results = new JTextArea(null, MANUAL, 0, 0);
        results.getDocument().putProperty("tabSize", TAB_SIZE);
        results.setEditable(false);
        results.setFont(FONT);

        var printer = new JTextArea(null, printer(), 0, 0);
        printer.getDocument().putProperty("tabSize", TAB_SIZE);
        printer.setEditable(false);
        printer.setFont(FONT);

        String text;
        try {
            text = Files.readString(PATH);
        } catch (IOException ex) {
            text = EXAMPLE;
        }
        var code = new RSyntaxTextArea(text, 0, 0);
        code.discardAllEdits(); // prevent Undo from deleting loaded text
        code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        code.setBracketMatchingEnabled(true); // set to false for demo videos (too distracting)
        code.setTabSize(TAB_SIZE);
        code.setFont(FONT);
        code.setCaretPosition(0);
        code.getCaret().setBlinkRate(0);

        var vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(results),
                new JScrollPane(printer)
        );
        vertical.setResizeWeight(0.875);

        var horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                vertical,
                new RTextScrollPane(code)
        );
        horizontal.setResizeWeight(0.25);

        var frame = new JFrame();
        frame.add(horizontal);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    Files.writeString(PATH, code.getText());
                } catch (IOException ignored) {
                }
            }
        });

        var printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                EventQueue.invokeLater(() -> {
                    printer.append(new String(new byte[]{(byte) b}));
                    printer.setCaretPosition(printer.getDocument().getLength());
                });
            }

            @Override
            public void write(byte[] b) {
                String str = new String(b);
                EventQueue.invokeLater(() -> {
                    printer.append(str);
                    printer.setCaretPosition(printer.getDocument().getLength());
                });
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String str = new String(b, off, len);
                EventQueue.invokeLater(() -> {
                    printer.append(str);
                    printer.setCaretPosition(printer.getDocument().getLength());
                });
            }
        });

        System.setOut(printStream);
        System.setErr(printStream);
        var shell = JShell.builder().executionEngine("local").build();
        for (String imp : IMPORTS) {
            shell.eval(imp);
        }
        var analysis = shell.sourceCodeAnalysis();

        code.requestFocusInWindow();
        code.addKeyListener(new KeyAdapter() {
            private boolean moreDetails;

            private void saveCode() {
                try {
                    Files.writeString(PATH, code.getText());
                } catch (IOException ignored) {
                }
            }

            private void reset() {
                results.setText(MANUAL);
                printer.setText(printer());

                shell.snippets()
                        .skip(IMPORTS.length)
                        .toList()
                        .reversed()
                        .forEach(shell::drop);
            }

            @Override
            public void keyPressed(KeyEvent event) {
                moreDetails = (event.getModifiersEx() != 0);
                try {
                    switch (event.getKeyCode()) {
                        case KeyEvent.VK_F1 -> {
                            event.consume();

                            int caretPosition = code.getCaretPosition();
                            int currentLine = code.getLineOfOffset(caretPosition);
                            int startOfSection = getSectionStartOffset(currentLine);
                            int endOfLine = code.getLineEndOffset(currentLine);

                            String prefix = code.getText(startOfSection, endOfLine - startOfSection);
                            caretPosition -= startOfSection;
                            appendLine();

                            if (caretPosition > 0 && prefix.charAt(caretPosition - 1) == '(') {
                                // Math.abs(
                                printDocumentation(prefix, caretPosition);
                            } else {
                                var anchor = new int[1];
                                var suggestions = analysis.completionSuggestions(prefix, caretPosition, anchor);
                                var hidden = new ArrayList<String>();
                                var shown = new HashSet<String>();
                                for (var suggestion : suggestions) {
                                    String continuation = suggestion.continuation();
                                    if (anchor[0] + continuation.length() == caretPosition) {
                                        // Math.PI
                                        printDocumentation(prefix, caretPosition);
                                    } else {
                                        // Math.abs
                                        switch (continuation) {
                                            // ALWAYS HIDE
                                            // legacy
                                            case "clone()":
                                            case "finalize()":
                                                // concurrency
                                            case "notify()":
                                            case "notifyAll()":
                                            case "wait(":
                                                // reflection
                                            case "getClass()":
                                            case "class":
                                                continue;

                                                // OPTIONALLY HIDE
                                                // standard members (zero surprise)
                                            case "equals(":
                                            case "hashCode()":
                                            case "toString()":
                                                // Seq clutter (too distracting)
                                            case "forEach(":
                                            case "iterator()":
                                            case "spliterator()":
                                            case "stream()":
                                            case "toArray(":
                                                if (!moreDetails) {
                                                    hidden.add(continuation);
                                                    continue;
                                                }
                                        }
                                        if (shown.add(continuation)) {
                                            // overloads appear identical
                                            appendLine(continuation);
                                        }
                                    }
                                }
                                if (shown.isEmpty()) {
                                    hidden.forEach(this::appendLine);
                                }
                            }
                        }

                        case KeyEvent.VK_F5 -> {
                            event.consume();
                            saveCode();
                            reset();

                            evaluate(0, code.getText());
                        }

                        case KeyEvent.VK_F8 -> {
                            event.consume();
                            saveCode();
                            reset();

                            int caretPosition = code.getCaretPosition();
                            int currentLine = code.getLineOfOffset(caretPosition);
                            int startOfSection = getSectionStartOffset(currentLine);
                            int endOfSection = getSectionEndOffset(currentLine);

                            evaluate(startOfSection, code.getText(startOfSection, endOfSection - startOfSection));
                        }

                        case KeyEvent.VK_F9 -> {
                            event.consume();
                            saveCode();
                            reset();

                            int caretPosition = code.getCaretPosition();
                            int currentLine = code.getLineOfOffset(caretPosition);
                            int startOfSection = getSectionStartOffset(currentLine);
                            int endOfLine = code.getLineEndOffset(currentLine);

                            evaluate(startOfSection, code.getText(startOfSection, endOfLine - startOfSection));
                        }

                        case KeyEvent.VK_F12 -> {
                            event.consume();
                            saveCode();

                            String selectedText = code.getSelectedText();
                            if (selectedText != null) {
                                evaluate(code.getSelectionStart(), selectedText);
                            } else {
                                int caretPosition = code.getCaretPosition();
                                int currentLine = code.getLineOfOffset(caretPosition);
                                int startOfLine = code.getLineStartOffset(currentLine);
                                int endOfLine = code.getLineEndOffset(currentLine);

                                evaluate(startOfLine, code.getText(startOfLine, endOfLine - startOfLine));
                            }
                        }
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace(printStream);
                }
            }

            private void printDocumentation(String line, int caretPosition) {
                var docs = analysis.documentation(line, caretPosition, moreDetails);
                for (var doc : docs) {
                    String javadoc = doc.javadoc();
                    String signature = doc.signature();
                    if (!moreDetails) {
                        signature = Signatures.lambdify(signature);
                    } else {
                        signature = signature.replace("java.util.function.", "");
                    }
                    signature = signature
                            .replace("? extends ", "")
                            .replace("? super ", "")
                            .replace(" extends Object", "");
                    if (javadoc == null) {
                        appendLine(signature);
                    } else {
                        appendLine(javadoc);
                        appendLine(signature);
                        appendLine();
                    }
                }
            }

            private int getSectionStartOffset(int line) throws BadLocationException {
                while (line >= 2 && code.getLineStartOffset(line - 2) + 2 != code.getLineStartOffset(line)) {
                    --line;
                }
                if (line == 1) line = 0;
                return code.getLineStartOffset(line);
            }

            private int getSectionEndOffset(int line) throws BadLocationException {
                int last = code.getLineCount() - 1;
                while (line + 2 <= last && code.getLineStartOffset(line) + 2 != code.getLineStartOffset(line + 2)) {
                    ++line;
                }
                if (line == last - 1) line = last;
                return code.getLineEndOffset(line);
            }

            private void evaluate(int sourcePosition, String remainingSource) throws BadLocationException {
                cafe.Assertions.passed = 0;
                while (!remainingSource.isBlank()) {
                    appendLine();
                    var completionInfo = analysis.analyzeCompletion(remainingSource);
                    String untrimmed = completionInfo.completeness().isComplete() ? completionInfo.source() : remainingSource;
                    SnippetEvent event = shell.eval(untrimmed).getFirst();
                    String source = untrimmed.trim();

                    Diag diagnostic = shell.diagnostics(event.snippet()).findFirst().orElse(null);
                    if (diagnostic != null) {
                        appendLine(abbreviate(source));
                        appendLine();
                        appendLine("❌ " + diagnosticMessage(diagnostic));
                        code.setCaretPosition(sourcePosition + (int) diagnostic.getPosition());
                        return;
                    }

                    JShellException exception = event.exception();
                    if (exception != null) {
                        code.setCaretPosition(sourcePosition + untrimmed.length());
                        int beforeSource = results.getDocument().getLength();
                        appendLine(abbreviate(source));
                        if (!(exception instanceof EvalException evex)) {
                            appendLine();
                            appendLine("❌ " + exception.getClass().getName() + "\n" + exception.getMessage());
                            return;
                        }
                        String exceptionClassName = evex.getExceptionClassName();
                        if (!exceptionClassName.equals("cafe.AssertionFailed")) {
                            appendLine();
                            appendLine("❌ " + exceptionClassName + "\n" + evex.getMessage());
                            return;
                        }
                        results.insert("✗ ", beforeSource);
                        appendLine(evex.getMessage());
                        int passed = cafe.Assertions.passed;
                        if (passed > 0) {
                            appendLine();
                            appendLine(passed + " previous assertions passed though");
                        }
                        return;
                    }

                    switch (event.snippet()) {
                        case ExpressionSnippet assignmentOrVariableExpression -> {
                            String name = assignmentOrVariableExpression.name();
                            VarSnippet variable = shell.variables()
                                    .filter(v -> v.name().equals(name))
                                    .findFirst()
                                    .orElseThrow();
                            appendResult("expression: ", removeTrailingSemicolon(source),
                                    shell.varValue(variable),
                                    variable.typeName());
                        }

                        case VarSnippet expressionOrVariableDeclaration -> {
                            if (expressionOrVariableDeclaration.name().charAt(0) == '$') {
                                appendResult("expression: ", removeTrailingSemicolon(source),
                                        shell.varValue(expressionOrVariableDeclaration),
                                        expressionOrVariableDeclaration.typeName());
                            } else {
                                appendResult("  variable: ", expressionOrVariableDeclaration.name(),
                                        shell.varValue(expressionOrVariableDeclaration),
                                        expressionOrVariableDeclaration.typeName());
                            }
                        }

                        case MethodSnippet method -> {
                            String signature = method.signature();
                            int boundary = signature.lastIndexOf(')') + 1;
                            appendLine(" function name:  " + method.name());
                            appendLine("parameter types: " + signature.substring(0, boundary));
                            appendLine("   return type:  " + signature.substring(boundary));

                            boolean failed = false;
                            var matcher = EXAMPLE.matcher(untrimmed);
                            if (matcher.find()) {
                                appendLine();
                                do {
                                    String arguments = matcher.group(2);
                                    String expected = matcher.group(3);
                                    String call = method.name() + arguments;
                                    int beforeArguments = results.getDocument().getLength();
                                    appendLine("✗   arguments: " + arguments);

                                    event = shell.eval(call).getFirst();

                                    diagnostic = shell.diagnostics(event.snippet()).findFirst().orElse(null);
                                    if (diagnostic != null) {
                                        appendLine();
                                        appendLine(diagnosticMessage(diagnostic));
                                        code.setCaretPosition(sourcePosition + matcher.start(2) + (int) diagnostic.getPosition() - method.name().length());
                                        return;
                                    }

                                    String valueOrException;
                                    BiPredicate<String, String> satisfies;
                                    String label;
                                    exception = event.exception();
                                    if (exception == null) {
                                        valueOrException = event.value();
                                        satisfies = String::equals;
                                        label = Assertions.ACTUAL;
                                    } else {
                                        satisfies = String::contains;
                                        label = "\n       thrown: ";
                                        if (exception instanceof EvalException evex) {
                                            valueOrException = evex.getExceptionClassName() + ": " + evex.getMessage();
                                        } else {
                                            valueOrException = exception.getClass().getName() + ": " + exception.getMessage();
                                        }
                                    }

                                    if (!satisfies.test(valueOrException, expected)) {
                                        appendLine(Assertions.EXPECTED + expected + label + valueOrException);
                                        failed = true;
                                    } else {
                                        cafe.Assertions.passed++;
                                        results.getDocument().remove(beforeArguments, 1);
                                        results.insert("✓", beforeArguments);
                                    }
                                } while (matcher.find());
                            }
                            if (failed) return;
                        }

                        default -> {
                            if (source.startsWith("assertEquals(")) {
                                append("✓ ");
                            }
                            appendLine(abbreviate(source));
                        }
                    }
                    sourcePosition += untrimmed.length();
                    remainingSource = completionInfo.remaining();
                }
                int passed = cafe.Assertions.passed;
                if (passed > 0) {
                    appendLine();
                    appendLine("ALL " + passed + " assertions PASSED");
                }
            }

            private static final Pattern EXAMPLE = Pattern.compile("^//(.)\\1*(\\(.*?\\))\\1+(.*)$", Pattern.MULTILINE);

            private static String removeTrailingSemicolon(String expression) {
                int lastIndex = expression.length() - 1;
                return expression.charAt(lastIndex) == ';' ? expression.substring(0, lastIndex) : expression;
            }

            private static String abbreviate(String source) {
                if (source.indexOf('\n') == -1) {
                    // 1 line
                    return source;
                }
                // 2+ lines
                String[] lines = source.split("\n");
                int n = lines.length;
                if (n <= 3) {
                    // 2 or 3 lines
                    return source;
                }
                // 4+ lines
                return lines[0] + "\n…\n" + lines[n - 1];
            }

            private void appendResult(String label, String expressionOrVariable, String value, String type) {
                appendLine(label + expressionOrVariable);

                if (moreDetails) {
                    switch (type) {
                        case "double", "Double" -> {
                            double x = Double.parseDouble(value);
                            if (Double.isFinite(x)) {
                                value = new BigDecimal(x).toPlainString();
                                if (value.indexOf('.') == -1) {
                                    value = value + ".0";
                                }
                            }
                        }
                        case "float", "Float" -> {
                            float x = Float.parseFloat(value);
                            if (Float.isFinite(x)) {
                                value = new BigDecimal(x).toPlainString();
                                if (value.indexOf('.') == -1) {
                                    value = value + ".0";
                                }
                            }
                        }
                    }
                }
                appendLine("     value: " + value);

                type = removeAdditionalBounds(type);
                appendLine("      type: " + type);

                if (moreDetails) {
                    switch (type) {
                        case "long", "Long" -> {
                            long x = Long.parseLong(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            appendLine("       sum: " + BinaryString.powersOfTwo(x, 64, "-"));
                        }
                        case "int", "Integer" -> {
                            int x = Integer.parseInt(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            appendLine("       sum: " + BinaryString.powersOfTwo(x, 32, "-"));
                        }
                        case "short", "Short" -> {
                            short x = Short.parseShort(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            appendLine("       sum: " + BinaryString.powersOfTwo(x, 16, "-"));
                        }
                        case "byte", "Byte" -> {
                            byte x = Byte.parseByte(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            appendLine("       sum: " + BinaryString.powersOfTwo(x, 8, "-"));
                        }
                        case "char", "Character" -> {
                            char x = value.length() == 3 ? value.charAt(1) : (char) evalInt("(int) " + value);
                            appendLine("    binary: " + BinaryString.from(x));
                            appendLine("       sum: " + BinaryString.powersOfTwo(x, 16, ""));
                        }
                        case "double", "Double" -> {
                            double x = Double.parseDouble(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            long bits = Double.doubleToRawLongBits(x);
                            int exponent = (int) (bits << 1 >>> 53) - 1023;
                            if (exponent <= 52) {
                                long implicit = Long.MIN_VALUE;
                                if (exponent == -1023) {
                                    // subnormal
                                    implicit = 0;
                                    exponent = -1022;
                                }
                                long mantissa = (bits << 11 | implicit) >>> 11;
                                exponent = 52 - exponent;
                                String numerator = "" + mantissa;
                                String denominator = BigDecimal.TWO.pow(exponent).toPlainString();
                                int n = numerator.length();
                                int d = denominator.length();
                                appendLine();
                                appendLine(" ".repeat(Math.max(0, d - n)) + numerator);
                                appendLine("-".repeat(Math.max(n, d)));
                                appendLine(" ".repeat(Math.max(0, n - d)) + denominator + " (2^" + exponent + ")");
                            }
                        }
                        case "float", "Float" -> {
                            float x = Float.parseFloat(value);
                            appendLine("    binary: " + BinaryString.from(x));
                            int bits = Float.floatToRawIntBits(x);
                            int exponent = (bits << 1 >>> 24) - 127;
                            if (exponent <= 23) {
                                int implicit = Integer.MIN_VALUE;
                                if (exponent == -127) {
                                    // subnormal
                                    implicit = 0;
                                    exponent = -126;
                                }
                                int mantissa = (bits << 8 | implicit) >>> 8;
                                exponent = 23 - exponent;
                                String numerator = "" + mantissa;
                                String denominator = BigDecimal.TWO.pow(exponent).toPlainString();
                                int n = numerator.length();
                                int d = denominator.length();
                                appendLine();
                                appendLine(" ".repeat(Math.max(0, d - n)) + numerator);
                                appendLine("-".repeat(Math.max(n, d)));
                                appendLine(" ".repeat(Math.max(0, n - d)) + denominator + " (2^" + exponent + ")");
                            }
                        }
                    }
                }
            }

            private String diagnosticMessage(Diag diagnostic) {
                String message = diagnostic.getMessage(null);
                if (!moreDetails) {
                    message = Signatures.lambdify(message);
                } else {
                    message = message.replace("java.util.function.", "");
                }
                return message
                        .replace("? extends ", "")
                        .replace("? super ", "")
                        .replace(" extends Object", "");
            }

            // for example, List.of(1, 2.0) has horrible type; shorten to just List<Number>
            private static String removeAdditionalBounds(String type) {
                for (int i = type.indexOf('&'); i != -1; i = type.indexOf('&', i + 1)) {
                    int level = 1; // List<Number&
                    int j = i;
                    do switch (type.charAt(++j)) {
                        case '<' -> ++level;
                        case '>' -> --level;
                    } while (level > 0); // find matching >
                    type = type.substring(0, i) + type.substring(j);
                }
                return type;
            }

            private int evalInt(String expression) {
                return Integer.parseInt(shell.eval(expression).getFirst().value());
            }

            private void append(String text) {
                results.append(text);
            }

            private void appendLine() {
                results.append("\n");
                results.setCaretPosition(results.getDocument().getLength());
            }

            private void appendLine(String text) {
                append(text);
                appendLine();
            }
        });
    }
}
