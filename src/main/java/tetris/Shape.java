package tetris;

import java.awt.Color;

import static java.awt.Color.*;

public enum Shape {
    I0(CYAN, 0x20, 0x21, 0x22, 0x23),
    I1(CYAN, 0x01, 0x11, 0x21, 0x31),

    J0(BLUE, 0x00, 0x10, 0x11, 0x12),
    J1(BLUE, 0x01, 0x02, 0x11, 0x21),
    J2(BLUE, 0x10, 0x11, 0x12, 0x22),
    J3(BLUE, 0x01, 0x11, 0x20, 0x21),

    L0(ORANGE, 0x02, 0x10, 0x11, 0x12),
    L1(ORANGE, 0x01, 0x11, 0x21, 0x22),
    L2(ORANGE, 0x10, 0x11, 0x12, 0x20),
    L3(ORANGE, 0x00, 0x01, 0x11, 0x21),

    O0(YELLOW, 0x11, 0x12, 0x21, 0x22),

    S0(GREEN, 0x11, 0x12, 0x20, 0x21),
    S1(GREEN, 0x00, 0x10, 0x11, 0x21),

    T0(MAGENTA, 0x01, 0x10, 0x11, 0x12),
    T1(MAGENTA, 0x01, 0x11, 0x12, 0x21),
    T2(MAGENTA, 0x10, 0x11, 0x12, 0x21),
    T3(MAGENTA, 0x01, 0x10, 0x11, 0x21),

    Z0(RED, 0x10, 0x11, 0x21, 0x22),
    Z1(RED, 0x01, 0x10, 0x11, 0x20);

    Shape(Color color, int block0, int block1, int block2, int block3) {
        this.color = color;
        this.block0 = (byte) block0;
        this.block1 = (byte) block1;
        this.block2 = (byte) block2;
        this.block3 = (byte) block3;
    }

    public final Color color;
    public final byte block0;
    public final byte block1;
    public final byte block2;
    public final byte block3;

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
