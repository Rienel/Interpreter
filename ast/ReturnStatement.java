package ast;

import token.Token;

public class ReturnStatement implements Statement{
    Token token;
    Expression returnValue;

    public ReturnStatement(Token token, Expression returnValue) {
        this.token = token;
        this.returnValue = returnValue;
    }

    public ReturnStatement() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Expression returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void statementNode() {
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
