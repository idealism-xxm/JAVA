package lexer;

/**
 * Created by idealism on 2017/6/17.
 *
 * 整数常量
 */
public class Num extends Value {
    private int value;

    public Num(int value) {
        super(Tag.NUM);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Num inverse() {
        return new Num(-value);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
