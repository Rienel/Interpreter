package ast;

import java.util.ArrayList;
import java.util.List;
import token.Token;

public class BlockStatement implements Statement {
    private Token token; // the 'PUNDOK' token
    private List<Statement> statements = new ArrayList<>();

    public BlockStatement() {
    }

    public BlockStatement(Token token) {
        this.token = token;
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
        // not needed now
    }

    @Override
    public String getTokenLiteral() {
        return token.getLiteral();
    }

    @Override
    public String string() {

        StringBuilder out = new StringBuilder();
        for (Statement statement : statements) {
            out.append(statement.string());
        }
        return out.toString();
    }
}
