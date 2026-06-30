package cafe;

import java.util.ArrayList;

public class Signatures {
    private final String in;
    private int index;
    private final StringBuilder out;

    private Signatures(String type) {
        in = type;
        index = 0;
        out = new StringBuilder();
    }

    public static String lambdify(String type) {
        var temp = new Signatures(type);
        temp.lambdify();
        return temp.out.toString();
    }

    private void lambdify() {
        int i = in.indexOf("java.util.function.", index);
        while (i != -1) {
            out.append(in, index, i);
            i += 19;
            int j = i + 1;
            while (Character.isJavaIdentifierPart(in.charAt(j))) {
                ++j;
            }
            String functionalInterface = in.substring(i, j);
            index = j;
            String[] a = parseTypeArguments();
            String t = switch (functionalInterface) {
                case "BiConsumer" -> "(" + a[0] + "," + a[1] + ")->void";
                case "BiFunction" -> "(" + a[0] + "," + a[1] + ")->" + a[2];
                case "BiPredicate" -> "(" + a[0] + "," + a[1] + ")->boolean";
                case "BinaryOperator" -> "(" + a[0] + "," + a[0] + ")->" + a[0];
                case "BooleanSupplier" -> "()->boolean";
                case "Consumer" -> a[0] + "->void";
                case "Function" -> a[0] + "->" + a[1];
                case "Predicate" -> a[0] + "->boolean";
                case "Supplier" -> "()->" + a[0];
                case "UnaryOperator" -> a[0] + "->" + a[0];

                case "ObjDoubleConsumer" -> "(" + a[0] + ",double)->void";
                case "ObjIntConsumer" -> "(" + a[0] + ",int)->void";
                case "ObjLongConsumer" -> "(" + a[0] + ",long)->void";

                case "DoubleBinaryOperator" -> "(double,double)->double";
                case "DoubleConsumer" -> "double->void";
                case "DoubleFunction" -> "double->" + a[0];
                case "DoublePredicate" -> "double->boolean";
                case "DoubleSupplier" -> "()->double";
                case "DoubleToIntFunction" -> "double->int";
                case "DoubleToLongFunction" -> "double->long";
                case "DoubleUnaryOperator" -> "double->double";

                case "IntBinaryOperator" -> "(int,int)->int";
                case "IntConsumer" -> "int->void";
                case "IntFunction" -> "int->" + a[0];
                case "IntPredicate" -> "int->boolean";
                case "IntSupplier" -> "()->int";
                case "IntToDoubleFunction" -> "int->double";
                case "IntToLongFunction" -> "int->long";
                case "IntUnaryOperator" -> "int->int";

                case "LongBinaryOperator" -> "(long,long)->long";
                case "LongConsumer" -> "long->void";
                case "LongFunction" -> "long->" + a[0];
                case "LongPredicate" -> "long->boolean";
                case "LongSupplier" -> "()->long";
                case "LongToDoubleFunction" -> "long->double";
                case "LongToIntFunction" -> "long->int";
                case "LongUnaryOperator" -> "long->long";

                case "ToDoubleBiFunction" -> "(" + a[0] + "," + a[1] + ")->double";
                case "ToDoubleFunction" -> a[0] + "->double";
                case "ToIntBiFunction" -> "(" + a[0] + "," + a[1] + ")->int";
                case "ToIntFunction" -> a[0] + "->int";
                case "ToLongBiFunction" -> "(" + a[0] + "," + a[1] + ")->long";
                case "ToLongFunction" -> a[0] + "->long";

                default -> null;
            };
            if (t != null) {
                out.append(t);
            } else {
                out.append(in, i, index);
            }
            i = in.indexOf("java.util.function.", index);
        }
        out.append(in, index, in.length());
    }

    private static final String[] EMPTY_ARGS = new String[0];

    private String[] parseTypeArguments() {
        if (in.charAt(index) != '<') return EMPTY_ARGS;
        ++index;
        if (in.charAt(index) == '>') {
            ++index;
            return EMPTY_ARGS;
        }
        var arguments = new ArrayList<String>();

        int i = index - 1;
        int nesting = 1;
        while (nesting > 0) {
            char current = in.charAt(++i);
            switch (current) {
                case '<' -> ++nesting;
                case '>' -> --nesting;
                case ',' -> {
                    if (nesting == 1) {
                        arguments.add(in.substring(index, i));
                        index = i + 1;
                    }
                }
            }
        }
        arguments.add(in.substring(index, i));
        index = i + 1;
        return arguments.toArray(String[]::new);
    }
}
