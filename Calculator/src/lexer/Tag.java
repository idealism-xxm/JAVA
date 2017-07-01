package lexer;

/**
 * Created by idealism on 2017/6/17.
 *
 * Tag 表明除去标准的ASCII码规定的基本字符可以当作 Token name 的唯一标识以外
 * 其余能直接作 Token name 的唯一标识
 */
public class Tag {

    //ID：变量，NUM：整数常量，REAL：实数常量
    public static final int
        ID = 256, MINUS = 257, NUM = 258, REAL = 259;

    public static String getType(int tag) {
        if(tag == NUM) {
            return "int";
        }
        if(tag == REAL) {
            return "double";
        }
        if(tag == ID) {
            return "variable";
        }
        if(tag == MINUS) {
            return "minus";
        }
        return "UNDEFINED";
    }
}
