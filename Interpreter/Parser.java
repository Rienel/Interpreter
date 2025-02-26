import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            Token token = advance();
            
            if (token.type == TokenType.KEYWORD && token.value.equals("MUGNA")) {
                parseVariableDeclaration();
            } else if (token.type == TokenType.KEYWORD && token.value.equals("KATAPUSAN")) {
                System.out.println("End of program detected.");
                break;
            } else {
                error("Unexpected token: " + token);
            }
        }
    }

    private void parseVariableDeclaration() {
        if (match(TokenType.DATA_TYPE)) {
            Token dataType = previous();
            if (match(TokenType.IDENTIFIER)) {
                Token identifier = previous();
                if (match(TokenType.ASSIGN)) {
                    if (match(TokenType.EXPRESSION)) {
                        Token expression = previous();
                        System.out.println("Declared variable: " + identifier.value + " of type " + dataType.value + " with value " + expression.value);
                    } else {
                        error("Expected an expression after '='");
                    }
                } else {
                    error("Expected '=' after variable name");
                }
            } else {
                error("Expected variable name after data type");
            }
        } else {
            error("Expected data type after 'MUGNA'");
        }
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return tokens.get(current).type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return current >= tokens.size() || tokens.get(current).type == TokenType.EOF;
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void error(String message) {
        System.err.println("[Parser Error] " + message);
    }
}
