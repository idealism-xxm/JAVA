package lexer;

/**
 * Created by idealism on 2017/6/17.
 */
public class Word extends Token {
    private String lexeme;

    public Word(int tag, String lexeme) {
        super(tag);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String toString() {
        return lexeme;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { //如果两个对象指向同一个
            return true;
        }
        if(obj == null || !(obj instanceof Word)) { //如果 obj 为空，或者 obj 不是 Word 实例
            return false;
        }
        return lexeme.equals(((Word) obj).getLexeme());
    }

    @Override
    public int hashCode() { //重写 hashCode ，防止用 HashTable 时造成错误的结果
        return lexeme.hashCode();
    }
}
