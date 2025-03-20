package ast;

import java.util.ArrayList;
import java.util.List;

import token.Token;

public class WhileExpression implements Expression {
    Token token;
    Expression condition;
    BlockStatement content;


    

    public WhileExpression() {
    }
    

    public WhileExpression(Token token, Expression condition, BlockStatement content) {
        this.token = token;
        this.condition = condition;
        this.content = content;
        
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
        StringBuilder sb = new StringBuilder();
        sb.append("WHILE");
        sb.append(condition.string());
        sb.append("BEGIN WHILE\n" + content.string());
        sb.append("\nEND WHILE");

        return sb.toString();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public BlockStatement getContent() {
        return content;
    }

    public void setContent(BlockStatement content) {
        this.content = content;
    }


}
