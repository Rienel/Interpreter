package ast;

import token.Token;

public class StringValue implements Expression{
    Token token;
    String value;
    public Token getToken() {
        return token;
    }
    
    public StringValue() {
    }
    

    public StringValue(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void expressionNode() {
        
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
