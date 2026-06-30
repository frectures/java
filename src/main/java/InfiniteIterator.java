import java.util.Iterator;

abstract class InfiniteIterator implements Iterator<Long> {

    protected long x = 1;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Long next() {
        var result = x;
        update();
        return result;
    }

    protected abstract void update();
}

class NaturalNumbers extends InfiniteIterator {
    @Override
    protected void update() {
        x += 1;
    }
}

class PowersOfTwo extends InfiniteIterator {
    @Override
    protected void update() {
        x += x;
    }
}
