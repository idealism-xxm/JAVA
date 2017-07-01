package lexer;

/**
 * Created by idealism on 2017/6/17.
 *
 * 数值常量
 */
public abstract class Value extends Token {
    public Value(int tag) {
        super(tag);
    }

    public abstract Value inverse();

    public abstract String toString();
}
