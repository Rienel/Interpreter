package ast;

import token.Token;

public class InfixExpression implements Expression{
    private Token token;
    private Expression left;
    private String operator;
    private Expression right;


    public InfixExpression() {
    }


    public InfixExpression(Token token, Expression left, String operator, Expression right) {
        this.token = token;
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


    public Token getToken() {
        return token;
    }


    public void setToken(Token token) {
        this.token = token;
    }


    public Expression getLeft() {
        return left;
    }


    public void setLeft(Expression left) {
        this.left = left;
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
        out.append(left.string());
        out.append(" " + operator + " ");
        out.append(right.string());
        out.append(")");

        return out.toString();
    }

    

    
   
}
