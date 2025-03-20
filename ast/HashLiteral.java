package ast;

import java.util.HashMap;
import java.util.Map;

import token.Token;

public class HashLiteral implements Expression{
    Token token;
    Map<Expression, Expression> pairs;

    public HashLiteral() {
        pairs = new HashMap<>();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Map<Expression, Expression> getPairs() {
        return pairs;
    }

    public void setPairs(Map<Expression, Expression> pairs) {
        this.pairs = pairs;
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

        return out.toString();
    }
    
    

    
}
