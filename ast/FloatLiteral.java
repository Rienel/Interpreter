package ast;

import token.Token;

public class FloatLiteral implements Expression{
    Token token;
    float value;

    public FloatLiteral(Token token, int value) {
        this.token = token;
        this.value = value;
    }
    public FloatLiteral(){

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
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
}
