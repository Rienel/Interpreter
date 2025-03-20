package ast;

import java.util.ArrayList;
import java.util.List;

import token.Token;

public class DisplayExpression implements Expression{
    Token token;
    
    List<Object> body;
   
  
    public DisplayExpression(Token token) {
        this.token = token;
        body = new ArrayList<>();
    }
        
    public DisplayExpression() {
        body = new ArrayList<>();
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

        out.append("IPAKITA");

        return out.toString();

    }

    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }

   

    

    

}
