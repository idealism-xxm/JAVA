package parser;

import bean.ValueBean;
import lexer.*;
import util.Index;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by idealism on 2017/6/17.
 *
 * 语法分析器：从词法分析器获得词法单元，并按照语法直接计算得到结果
 */
public class Parser {
    private Lexer lexer; //词法分析器
    private Token look; //向前看符号
    private Hashtable<Word, Value> values = new Hashtable<Word, Value>(); //将 ID 映射为 数值
    Word ans = new Word(Tag.ID, new String("ans"));

    private String src = null; //算式
    private Index index = new Index();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        setSrc("");
    }

    public ArrayList<ValueBean> getValueBeans() {
        ArrayList<ValueBean> valueBeans = new ArrayList<ValueBean>();
        for (Map.Entry<Word, Value> element : values.entrySet()) {
            String name = element.getKey().getLexeme();
            String type = Tag.getType(element.getValue().getTag());
            String value = element.getValue().toString();
            valueBeans.add(new ValueBean(name, type, value));
        }
        valueBeans.sort(Comparator.comparing(ValueBean::getName)); //按照字典序排序
        return valueBeans;
    }

    public void setSrc(String src) {
        if(src == null) { //如果 src 为 null ，默认为空串
            src = "";
        }
        this.src = src;
        //为了使输入的算式是该文法的句子，
        //需要对用户输入的算式改写：如果没有赋值符，则结果默认赋给ans
        if(src.indexOf('=') == -1) {
            this.src = "ans = " + src;
        }
        else {
            this.src = src;
        }
        index.setValue(0);
        lexer.init();
        nextToken();
    }

    private void nextToken() { //从词法分析器获取下一个词法单元
        look = lexer.scan(src, index);
    }

    private void error(String msg) {
        throw new Error(msg);
    }

    private void match(int tag) { //匹配标识为tag的词素
        if(look.getTag() == tag) {
            nextToken();
        }
        else {
            error("Syntax Error");
        }
    }

    //TODO：想实现赋值表达式的副作用并可以用于算数表达式中，但是那样就无法改写成LL(1)文法，LR(1)又太难构造表
    //返回该语句的结果值字符串
    public String stmt() {
        Value res = assign();
        if(res.getTag() == Tag.NUM) {
            return "" + ((Num) res).getValue();
        }
        else if(res.getTag() == Tag.REAL) {
            return "" + ((Real) res).getValue();
        }
        error("Wrong Type!");
        return null;
    }

    private Value assign() { //S → id = E
        Token cur = look;
        match(Tag.ID); //匹配 ID
        match('='); //匹配 =

        Value value = expr(); //得到算数表达式的结果
        if(look.getTag() != '\0') { //如果没有处理完所有的有效字符，则必定存在语法错误
            error("Syntax Error");
        }
        values.put((Word)cur, value); //将结果赋给 cur
        values.put(ans, value); //每次还要修改 ans 的值
        return value;
    }

    private Value expr() {
        Value val = term(); //得到最左边的 term 的结果
        while(true) { //递归转成循环，所以不必消除左递归
            Token operator = look;
            if(operator.getTag() == '+') { //加法
                nextToken();
                Value nextVal = term();
                int tag = getResultToken(val, nextVal);
                if(tag == Tag.NUM) {
                    val = new Num(((Num)val).getValue() + ((Num)nextVal).getValue());
                }
                else if(tag == Tag.REAL){
                    val = new Real((new Real(val).getValue() + new Real(nextVal).getValue()));
                }
                else {
                    error("Wrong Type!");
                }
            }
            else if(look.getTag() == '-') { //减法
                nextToken();
                Value nextVal = term();
                int tag = getResultToken(val, nextVal);
                if(tag == Tag.NUM) {
                    val = new Num(((Num)val).getValue() - ((Num)nextVal).getValue());
                }
                else if(tag == Tag.REAL){
                    val = new Real((new Real(val).getValue() - new Real(nextVal).getValue()));
                }
                else {
                    error("Wrong Type!");
                }
            }
            else { //其余则跳出
                break;
            }
        }
        return val;
    }

    private Value term() {
        Value val = factor(); //得到最左边的 factor 的结果
        while(true) { //递归转成循环，所以不必消除左递归
            Token operator = look;
            if(operator.getTag() == '*') { //乘法
                nextToken();
                Value nextVal = factor();
                int tag = getResultToken(val, nextVal);
                if(tag == Tag.NUM) {
                    val = new Num(((Num)val).getValue() * ((Num)nextVal).getValue());
                }
                else if(tag == Tag.REAL){
                    val = new Real((new Real(val).getValue() * new Real(nextVal).getValue()));
                }
                else {
                    error("Wrong Type!");
                }
            }
            else if(look.getTag() == '/') { //除法
                nextToken();
                Value nextVal = factor();
                int tag = getResultToken(val, nextVal);
                if(tag == Tag.NUM) {
                    val = new Num(((Num)val).getValue() / ((Num)nextVal).getValue());
                }
                else if(tag == Tag.REAL){
                    val = new Real((new Real(val).getValue() / new Real(nextVal).getValue()));
                }
                else {
                    error("Wrong Type!");
                }
            }
            else { //其余则跳出
                break;
            }
        }
        return val;
    }

    private Value factor() {
        Value val = unary(); //得到最左边的 factor 的结果
        while(true) { //递归转成循环，所以不必消除左递归
            Token operator = look;
            if(operator.getTag() == '^') { //幂
                nextToken();
                Value nextVal = factor(); //因为幂是右结合的，所以要递归调用 factor ，而不是 unary
                int tag = getResultToken(val, nextVal);
                if(tag == Tag.NUM) {
                    val = new Num(new BigInteger("" + ((Num) val).getValue()).pow(((Num)nextVal).getValue()).intValue());
                }
                else if(tag == Tag.REAL){
                    val = new Real(Math.pow(new Real(val).getValue(), new Real(nextVal).getValue()));
                }
                else {
                    error("Wrong Type!");
                }
            }
            else { //其余则跳出
                break;
            }
        }
        return val;
    }

    private Value unary() {
        if(look.getTag() == '-') {
            nextToken();
            return primary().inverse();
        }
        else {
            return primary();
        }
    }

    private Value primary() {
        Value val = null;
        if(look.getTag() == '(') {
            nextToken();
            val = expr();
            match(')');
        }
        else {
            if(look.getTag() == Tag.NUM || look.getTag() == Tag.REAL) {
                val = (Value) look;
            }
            else if(look.getTag() == Tag.ID) {
                val = values.get(look);
                if(val == null) { //如果该变量尚未使用，则赋为默认值：Num(0)
                    val = new Num(0);
                    values.put((Word) look, val); //变量存入变量表中
                }
            }
            else {
                error("Syntax Error");
            }
            nextToken();
        }
        return val;
    }

    private int getResultToken(Token a, Token b) { //返回数值 a 和 b 的结果类型
        return Math.max(a.getTag(), b.getTag());
    }
}
