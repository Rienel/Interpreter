package ast;

import token.Token;

public class BooleanExpression implements Expression{
    private Token token;
    private boolean value;

    public BooleanExpression() {
    }
    public BooleanExpression(Token token, boolean value) {
        this.token = token;
        this.value = value;
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public boolean getValue() {
        return value;
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    @Override
    public void expressionNode() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public String getTokenLiteral() {
        return token.getLiteral();
    }
    @Override
    public String string() {
        return token.getLiteral();
    }

    
    
}
