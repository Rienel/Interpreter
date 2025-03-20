package ast;

import token.Token;

public class EndExpression implements Expression{
     Token token;
    Identifier ident;
   

    public EndExpression(Token token,Identifier ident) {
        this.token = token;
    }
    public EndExpression() {
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
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
        StringBuilder out = new StringBuilder();

        out.append("KATAPUSAN");

        return out.toString();

    }
    public Identifier getIdent() {
        return ident;
    }
    public void setIdent(Identifier ident) {
        this.ident = ident;
    }

}
