package ast;

import token.Token;

public class IntegerLiteral implements Expression{
    Token token;
    int value;

    public IntegerLiteral(Token token, int value) {
        this.token = token;
        this.value = value;
    }
    public IntegerLiteral(){

    }
    @Override
    public String getTokenLiteral() {
        return token.getLiteral();
    }
    @Override
    public String string() {
        return token.getLiteral();
    }
    @Override
    public void expressionNode() {
        
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }


}
