package ast;

import token.Token;

public class ExpressionStatement implements Statement {
    private Token token;
    private Expression expression;

    
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public Expression getExpression() {
        return expression;
    }
    public void setExpression(Expression expression) {
        this.expression = expression;
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
        if(expression != null){
            return expression.string();
        }
        return "";
    }

    

    
}
