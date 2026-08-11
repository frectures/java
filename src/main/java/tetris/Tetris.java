package tetris;

import java.awt.Color;

import static java.awt.Color.GRAY;

public class Tetris {
    private static final Color[] EMPTY_BOARD = {
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, null, null, null, null, null, null, null, null, null, null, GRAY,
            GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY, GRAY,
    };
    public static final int WIDTH = 12;
    public static final int HEIGHT = 21;
    private static final int START_POSITION = 4;
    public static final int TICKS_PER_SECOND = 30;

    private final Color[] board;
    private final FairLetterSupplier letterSupplier;
    private Shape shape;
    private int position;
    private int timer;
    private Tetris opponent;
    private int penaltyLines;
    private boolean gameOver;

    public Tetris() {
        board = EMPTY_BOARD.clone();
        letterSupplier = new FairLetterSupplier();
        shape = Shape.fromLetter(letterSupplier.nextLetter());
        position = START_POSITION;
        timer = 0;

        penaltyLines = 0;
        gameOver = false;
    }

    public void setOpponent(Tetris opponent) {
        this.opponent = opponent;
    }

    public Color colorAt(int x, int y) {
        return board[y * WIDTH + x];
    }

    public void storeShape(Color color) {
        board[position + shape.block0.position] = color;
        board[position + shape.block1.position] = color;
        board[position + shape.block2.position] = color;
        board[position + shape.block3.position] = color;
    }

    private boolean shapeCollides() {
        return board[position + shape.block0.position] != null ||
                board[position + shape.block1.position] != null ||
                board[position + shape.block2.position] != null ||
                board[position + shape.block3.position] != null;
    }

    public void moveLeft() {
        --position;
        if (shapeCollides()) {
            ++position;
        }
    }

    public void moveRight() {
        ++position;
        if (shapeCollides()) {
            --position;
        }
    }

    public void tick() {
        ++timer;
        if (timer == TICKS_PER_SECOND) {
            moveDown();
        }
    }

    public void moveDown() {
        timer = 0;
        position += WIDTH;
        if (shapeCollides()) {
            position -= WIDTH;
            storeShape(shape.color);

            opponent.penaltyLines += switch (removeCompleteLines()) {
                case 2 -> 1;
                case 3 -> 2;
                case 4 -> 4;
                default -> 0;
            };
            insertOwnPenaltyLines();

            shape = Shape.fromLetter(letterSupplier.nextLetter());
            position = START_POSITION;
            gameOver = shapeCollides();
        }
    }

    public void drop() {
        do {
            moveDown();
        } while (position != START_POSITION);
    }

    public void rotate() {
        // TODO
    }

    private int removeCompleteLines() {
        // TODO
        return 0;
    }

    private void insertOwnPenaltyLines() {
        // TODO insert this.penaltyLines, NOT opponent.penaltyLines!
    }

    public boolean gameOver() {
        return gameOver;
    }
}
