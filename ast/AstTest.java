package ast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import token.Token;
import token.TokenType;

public class AstTest {
    Program program;

    public void testString(){
        Program program = new Program();
        program.setStatements(new ArrayList<>());
        program.addStatement(createCharStatement("CHAR", "myChar", "c"));
        assertEquals("CHAR myChar = c$", program.toString());
    }

    private CharStatement createCharStatement(String tokenLiteral, String name, String value){
        CharStatement statement = new CharStatement();
        statement.setToken(new Token(TokenType.CHAR, tokenLiteral));
        statement.setName(new Identifier(new Token(TokenType.IDENT, name), name));
        statement.setValue(new Identifier(new Token(TokenType.IDENT, value), value));
        return statement;
    }
    
}
