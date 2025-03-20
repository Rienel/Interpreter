package ast;

import token.Token;

public class PrefixExpression implements Expression{
    private Token token;
    private String operator;
    private Expression right;
    
    public PrefixExpression(Token token, String operator, Expression right) {
        this.token = token;
        this.operator = operator;
        this.right = right;
    }

    public PrefixExpression() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
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
        out.append("(");
        out.append(operator);
        out.append(right.string());
        out.append(")");

        return out.toString();
    }

    
    
}
