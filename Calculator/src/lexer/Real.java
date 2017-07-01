package lexer;

/**
 * Created by idealism on 2017/6/17.
 *
 * 实数常量
 */
public class Real extends Value {
    private double value = 0.0;

    public Real(double value) {
        super(Tag.REAL);
        this.value = value;
    }

    public Real(Value val) {
        super(Tag.REAL);
        if(val.getTag() == Tag.NUM) {
            value = ((Num) val).getValue();
        }
        else if(val.getTag() == Tag.REAL){
            value = ((Real) val).getValue();
        }
        else {
            //TODO：其它数值类型待加入
            throw new Error("Error: Wrong Type!");
        }
    }

    public double getValue() {
        return value;
    }

    @Override
    public Real inverse() {
        return new Real(-value);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
