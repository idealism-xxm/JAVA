package util;

/**
 * Created by idealism on 2017/6/17.
 *
 * 下标类：实现下标的修改，可以传递到函数内部以实现下标全体修改
 */
public class Index {
    private int value = 0;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void increase() {
        ++value;
    }

    public void decrease() {
        --value;
    }
}
