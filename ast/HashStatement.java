package ast;

import token.Token;

public class HashStatement implements Statement{
    Token token;
    Identifier ident;
    Expression value;

    public HashStatement() {
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

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public void statementNode() {
       
    }

    @Override
    public String getTokenLiteral() {
       return token.getLiteral();
    }

    @Override
    public String string() {
        return "";
    }

    
    
}
