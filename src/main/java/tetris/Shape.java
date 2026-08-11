package tetris;

import java.awt.Color;

import static java.awt.Color.*;
import static tetris.Block.*;

public enum Shape {
    I0(CYAN, x0y2, x1y2, x2y2, x3y2),
    I1(CYAN, x1y0, x1y1, x1y2, x1y3),

    J0(BLUE, x0y0, x0y1, x1y1, x2y1),
    J1(BLUE, x1y0, x2y0, x1y1, x1y2),
    J2(BLUE, x0y1, x1y1, x2y1, x2y2),
    J3(BLUE, x1y0, x1y1, x0y2, x1y2),

    L0(ORANGE, x2y0, x0y1, x1y1, x2y1),
    L1(ORANGE, x1y0, x1y1, x1y2, x2y2),
    L2(ORANGE, x0y1, x1y1, x2y1, x0y2),
    L3(ORANGE, x0y0, x1y0, x1y1, x1y2),

    O0(YELLOW, x1y1, x2y1, x1y2, x2y2),

    S0(GREEN, x1y1, x2y1, x0y2, x1y2),
    S1(GREEN, x0y0, x0y1, x1y1, x1y2),

    T0(MAGENTA, x1y0, x0y1, x1y1, x2y1),
    T1(MAGENTA, x1y0, x1y1, x2y1, x1y2),
    T2(MAGENTA, x0y1, x1y1, x2y1, x1y2),
    T3(MAGENTA, x1y0, x0y1, x1y1, x1y2),

    Z0(RED, x0y1, x1y1, x1y2, x2y2),
    Z1(RED, x1y0, x0y1, x1y1, x0y2);

    Shape(Color color, Block block0, Block block1, Block block2, Block block3) {
        this.color = color;
        this.block0 = block0;
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
    }

    public final Color color;
    public final Block block0;
    public final Block block1;
    public final Block block2;
    public final Block block3;

    public static Shape fromLetter(char letter) {
        return switch (letter) {
            case 'I' -> I0;
            case 'J' -> J0;
            case 'L' -> L0;
            case 'O' -> O0;
            case 'S' -> S0;
            case 'T' -> T0;
            case 'Z' -> Z0;
            default -> throw new IllegalArgumentException("illegal letter: " + letter);
        };
    }
}
