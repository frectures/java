package tetris;

public class Tetris {
    public static final int TICKS_PER_SECOND = 30;
    public static final int WIDTH = 16;
    public static final int HEIGHT = 21;
    private static final int START_POSITION = 6;

    private final int[] board;
    private final FairLetterSupplier letterSupplier;
    private int letter, rotation;
    private int[] shape;
    private int position;
    private int timer;
    private Tetris opponent;
    private int penaltyLines;
    private boolean gameOver;

    public Tetris() {
        // Plenum: Warum wird dieses Array geklont ...
        board = EMPTY_BOARD.clone();
        // Plenum: ... Aber dieses Array nicht?
        shape = SHAPES[letter = 4][rotation = 0]; // the O shape
        letterSupplier = new FairLetterSupplier();

        position = START_POSITION;
        timer = 0;

        penaltyLines = 0;
        gameOver = false;
    }

    // Plenum: Warum ist diese Methode notwendig?
    // Warum kann opponent kein Konstruktor-Parameter sein?
    // Schauen wir uns den Code an, der Tetris-Objekte erzeugt...
    public void setOpponent(Tetris opponent) {
        this.opponent = opponent;
    }

    public int cell(int x, int y) {
        return board[y * WIDTH + x];
    }

    public void storePiece(int value) {
        board[position + shape[0]] = value;
        board[position + shape[1]] = value;
        board[position + shape[2]] = value;
        board[position + shape[3]] = value;
    }

    private boolean pieceCollides() {
        // Plenum: Was würde passieren, wenn man einfach immer false liefert?
        return board[position + shape[0]] != 0 ||
                board[position + shape[1]] != 0 ||
                board[position + shape[2]] != 0 ||
                board[position + shape[3]] != 0;
    }

    public void moveLeft() {
        --position;
        if (pieceCollides()) {
            ++position;
        }
    }

    public void moveRight() {
        ++position;
        if (pieceCollides()) {
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
        if (pieceCollides()) {
            position -= WIDTH;
            storePiece(letter);

            opponent.penaltyLines += switch (removeCompleteLines()) {
                case 2 -> 1;
                case 3 -> 2;
                case 4 -> 4;
                default -> 0;
            };
            insertPenaltyLines();

            letter = letterSupplier.nextLetter();
            rotation = (int) (Math.random() * SHAPES[letter].length);
            shape = SHAPES[letter][rotation];
            position = START_POSITION;
            gameOver = pieceCollides();
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

    private void insertPenaltyLines() {
        // TODO
    }

    public boolean gameOver() {
        return gameOver;
    }

    // 0 = empty
    // 8 = wall
    private static final int[] EMPTY_BOARD = {
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0,
            0, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 0, 0,
    };

    private static final int[][][] SHAPES = {
            {},
            // I
            {
                    {0x20, 0x21, 0x22, 0x23},
                    {0x01, 0x11, 0x21, 0x31},
            },
            // J
            {
                    {0x00, 0x10, 0x11, 0x12},
                    {0x01, 0x02, 0x11, 0x21},
                    {0x10, 0x11, 0x12, 0x22},
                    {0x01, 0x11, 0x20, 0x21},
            },
            // L
            {
                    {0x02, 0x10, 0x11, 0x12},
                    {0x01, 0x11, 0x21, 0x22},
                    {0x10, 0x11, 0x12, 0x20},
                    {0x00, 0x01, 0x11, 0x21},
            },
            // O
            {
                    {0x11, 0x12, 0x21, 0x22},
            },
            // S
            {
                    {0x11, 0x12, 0x20, 0x21},
                    {0x00, 0x10, 0x11, 0x21},
            },
            // T
            {
                    {0x01, 0x10, 0x11, 0x12},
                    {0x01, 0x11, 0x12, 0x21},
                    {0x10, 0x11, 0x12, 0x21},
                    {0x01, 0x10, 0x11, 0x21},
            },
            // Z
            {
                    {0x10, 0x11, 0x21, 0x22},
                    {0x01, 0x10, 0x11, 0x20},
            },
    };
}
