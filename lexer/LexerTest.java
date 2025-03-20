package lexer;

import java.util.Arrays;
import java.util.List;

import token.Token;
import token.TokenType;

public class LexerTest {

    // public static void main(String[] args){
    //     String input = "<+><>==-=*<=>=/%[]()";

    //     List<TestToken> tests = Arrays.asList(
    //         new TestToken(TokenType.LESS, "<"),
    //         new TestToken(TokenType.PLUS, "+"),
    //         new TestToken(TokenType.GREAT, ">"),
    //         new TestToken(TokenType.NOTEQUAL, "<>"),
    //         new TestToken(TokenType.EQUAL, "=="),
    //         new TestToken(TokenType.SUBTRACT, "-"),
    //         new TestToken(TokenType.ASSIGN, "="),
    //         new TestToken(TokenType.MULTIPLY, "*"),
    //         new TestToken(TokenType.LESSEQ, "<="),
    //         new TestToken(TokenType.GREATEQ, ">="),
    //         new TestToken(TokenType.DIVIDE, "/"),
    //         new TestToken(TokenType.MODULO, "%"),
    //         new TestToken(TokenType.LESCAPE, "["),
    //         new TestToken(TokenType.RESCAPE, "]"),
    //         new TestToken(TokenType.LPARA, "("),
    //         new TestToken(TokenType.RPARA, ")")

    //     );

    //     Lexer lexer = new Lexer(input);



    //     for (int i = 0; i < tests.size(); i++){
    //         TestToken tt = tests.get(i);
    //         Token tok = lexer.nextToken();

    //         System.out.println(String.format("test[%s]",i));

    //         if(tok.getTokenType() != tt.getExpectedType()){
    //             throw new AssertionError(String.format("test[%d] - token type wrong. expected= %s, got= %s",i,tt.getExpectedType(),tok.getTokenType()));
    //         }else{
    //             System.out.println(String.format("Token Type = %s", tok.getTokenType()));
    //         }

    //         if (!tok.getLiteral().equals(tt.getExpectedLiteral())) {
    //             throw new AssertionError(String.format("tests[%d] - literal wrong. expected= %s, got= %s", i, tt.getExpectedLiteral(), tok.getLiteral()));
    //         }else {
    //             System.out.println(String.format("Literal = %s", tok.getLiteral()));
    //         }
    //         System.out.println("");
    //     }
    // }
    // static class TestToken {
    //     private TokenType expectedType;
    //     private String expectedLiteral;

    //     public TestToken(TokenType expectedType, String expectedLiteral){
    //         this.expectedType = expectedType;
    //         this.expectedLiteral = expectedLiteral;
    //     }

    //     public TokenType getExpectedType(){
    //         return expectedType;
    //     }

    //     public String getExpectedLiteral(){
    //         return expectedLiteral;
    //     }
    // }
}
