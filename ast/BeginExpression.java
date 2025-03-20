package ast;

import token.Token;

public class BeginExpression implements Expression{
    Token token;
    BlockStatement body;

    public BeginExpression(Token token,Identifier ident, BlockStatement body) {
        this.token = token;
        this.body = body;
    }
    public BeginExpression() {
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public BlockStatement getBody() {
        return body;
    }
    public void setBody(BlockStatement body) {
        this.body = body;
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

        out.append("SUGOD");
        out.append(body.string());

        return out.toString();

    }




}
