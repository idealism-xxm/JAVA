package lexer;

import util.Index;

import java.util.Hashtable;

/**
 * Created by idealism on 2017/6/17.
 *
 * 词法分析器：将一行算式组织成有意义的词素的序列
 */
public class Lexer {
    private char peek = ' '; //初始赋值为空格，使得在scan函数中能够顺利扫描并从字符流中读入一个字符
    Hashtable<String, Word> words = new Hashtable<String, Word>(); //符号表：存储词法单元

    private void reserve(Word word) { //将词法单元存储至符号表
        words.put(word.getLexeme(), word);
    }

    private void nextChar(String src, Index index) {
        if(index.getValue() == src.length()) {
            peek = '\0';
        }
        else {
            peek = src.charAt(index.getValue());
            index.increase();
        }
    }

    public void init() { //初始赋值为空格，使得在scan函数中能够顺利扫描并从字符流中读入一个字符
        peek = ' ';
    }

    /**
     * @param src ：需要扫描的字符串
     * @param index ：扫描开始位置
     * @return 一个词法单元
     */
    public Token scan(String src, Index index) {
        while(peek == ' ' || peek == '\t' || peek == '\n') { //过滤所有空白符
            nextChar(src, index);
        }

        if(Character.isDigit(peek)) { //如果是数字，则必定是整数或者浮点数常量
            int num = 0;
            do { //统计当前数字的整数部分
                num = num * 10 + Character.digit(peek, 10);
                nextChar(src, index);
            } while(Character.isDigit(peek));

            if(peek != '.') { //如果当前数字是整数，则直接返回整数常量词法单元
                return new Num(num);
            }

            //统计当前数字的小数部分
            double real = num, weight = 0.1;
            nextChar(src, index);
            while(Character.isDigit(peek)) { //如果当前是数字，则继续统计
                real = real + Character.digit(peek, 10) * weight;
                weight *= 0.1;
                nextChar(src, index);
            }
            return new Real(real);
        }

        if(Character.isLetter(peek)) {
            StringBuffer srcBuffer = new StringBuffer();
            do { //找到这个完整的词素
                srcBuffer.append(peek);
                nextChar(src, index);
            } while(Character.isLetterOrDigit(peek));
            String lexeme = srcBuffer.toString();
            Word word = (Word) words.get(lexeme);
            if(word == null) { //如果符号表中还没有当前词法单元，则存入
                word = new Word(Tag.ID, lexeme);
                reserve(word);
            }
            return word;
        }

        Token token = new Token(peek);
        peek = ' ';
        return token;
    }
}
