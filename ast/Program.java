package ast;

import java.util.ArrayList;
import java.util.List;


public class Program implements Node{
    private List<Statement> statements;
    

    public Program(List<Statement> statements){
        this.statements = statements;
    }
    public Program(){}
    @Override
    public String getTokenLiteral() {
        if(statements.size() > 0) {
            return statements.get(0).getTokenLiteral();
        } else {
            return "";
        }
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void addStatement(Statement statement){
        System.out.println(statement.getTokenLiteral());
        this.statements.add(statement);
    }
   

    @Override
    public String string() {
        StringBuilder sb = new StringBuilder();
        // Iterate through each statement and append its string representation
        for (Statement stmt : statements) {
            sb.append(stmt.toString()).append("\n");
        }
        return sb.toString();
        
    }

    

    
}
