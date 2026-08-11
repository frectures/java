package tetris;

public enum Block {
    x0y0(00), x1y0(01), x2y0(02), x3y0(03),
    x0y1(12), x1y1(13), x2y1(14), x3y1(15),
    x0y2(24), x1y2(25), x2y2(26), x3y2(27),
    x0y3(36), x1y3(37), x2y3(38), x3y3(39);

    Block(int position) {
        this.position = position;
    }

    public final int position;
}
