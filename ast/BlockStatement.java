package ast;

import java.util.ArrayList;
import java.util.List;

import token.Token;

public class BlockStatement implements Statement{
    Token token;
    List<Statement> statements;

    public BlockStatement(Token token) {
        this.token = token;
        this.statements = new ArrayList<>();
    }
    public BlockStatement(){
        this.statements = new ArrayList<>();
    }
    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void addStatement(Statement statement){
        this.statements.add(statement);
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
        StringBuilder out = new StringBuilder();

        for(Statement s: statements){
            out.append(s.string());
        }

        return out.toString();
    }

    
    

    
}
