package lexer;

/**
 * Created by idealism on 2017/6/17.
 *
 * Token 表明当前词法单元，tag 表明其各类词法单元的唯一标识
 * 其他类继承 Token 以细化词法单元
 */
public class Token {
    private int tag;

    public Token(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public String toString() {
        return "" + (char) tag;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { //如果两个对象指向同一个
            return true;
        }
        if(obj == null || !(obj instanceof Word)) { //如果 obj 为空，或者 obj 不是 Token 实例
            return false;
        }
        return tag == ((Token) obj).getTag();
    }

    @Override
    public int hashCode() { //重写 hashCode ，防止用 HashTable 时造成错误的结果
        return tag;
    }
}
