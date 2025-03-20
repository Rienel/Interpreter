package ast;

import token.Token;

public class ScanExpression implements Expression{
    Token token;
    Identifier ident;
    Expression expression;

    public ScanExpression() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Identifier getIdent() {
        return ident;
    }

    public void setIdent(Identifier ident) {
        this.ident = ident;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void expressionNode() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getTokenLiteral() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String string() {
        // TODO Auto-generated method stub
        return null;
    }
    

    
   
  
   
}
