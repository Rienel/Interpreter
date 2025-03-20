package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import token.Token;

public class IfExpression implements Expression{
    Token token;
    Expression condition;
    BlockStatement consequence;
    List<Expression> elseConditions;
    List<BlockStatement> elseConsequences;
    BlockStatement alternative;

    

    public IfExpression() {
        elseConditions = new ArrayList<>();
        elseConsequences = new ArrayList<>();
    }
    

    public IfExpression(Token token, Expression condition, BlockStatement consequence, BlockStatement alternative) {
        this.token = token;
        this.condition = condition;
        this.consequence = consequence;
        this.alternative = alternative;
        elseConditions = new ArrayList<>();
        elseConsequences = new ArrayList<>();
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
        sb.append("IF");
        sb.append(condition.string());
        sb.append("BEGIN IF\n" + consequence.string());
        sb.append("\nEND IF");

        if(alternative != null){
            sb.append("ELSE");
            sb.append("BEGIN IF\n" + alternative.string());
            sb.append("\nEND IF");
        }
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

    public BlockStatement getConsequence() {
        return consequence;
    }

    public void setConsequence(BlockStatement consequence) {
        this.consequence = consequence;
    }

    public BlockStatement getAlternative() {
        return alternative;
    }

    public void setAlternative(BlockStatement alternative) {
        this.alternative = alternative;
    }


    public List<Expression> getElseConditions() {
        return elseConditions;
    }


    public void setElseConditions(List<Expression> elseConditions) {
        this.elseConditions = elseConditions;
    }
    public void addElseCondition(Expression bs){
        elseConditions.add(bs);
    }


    public List<BlockStatement> getElseConsequences() {
        return elseConsequences;
    }
    public void addElseConsequene(BlockStatement bs){
        elseConsequences.add(bs);
    }


    public void setElseConsequences(List<BlockStatement> elseConsequences) {
        this.elseConsequences = elseConsequences;
    }
    
}
