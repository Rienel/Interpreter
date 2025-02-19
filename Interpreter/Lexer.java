import java.util.*;
import java.util.regex.*;

public class Lexer {
    private final String input;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> lex() {
        List<Token> tokens = new ArrayList<>();

        // Regular expression with assignment and optional expression
        Pattern pattern = Pattern.compile(
                "(SUGOD)" +                    // Matches "SUGOD"
                        "(MUGNA)" +                 // Matches "MUGNA" with optional leading whitespace
                        "(NUMERO|LETTERS|BOOLEAN)" + // Matches data types (NUMERO, LETTERS, BOOLEAN)
                        "([a-zA-Z_][a-zA-Z0-9_]*)" +  // Matches variable name (identifier)
                        "(=)|" +                 // Optionally matches "=" (assignment operator)
                        "(\\s*\\S*)|" +                  // Optionally matches the expression (value) after assignment
                        "(\\s*)|" +                       // Matches any optional spaces (ignored)
                        "(KATAPUSAN)"                    // Matches "KATAPUSAN"
        );

        Matcher matcher = pattern.matcher(input);

        // Process the input string using the regex
        while (matcher.find()) {
            if (matcher.group(1) != null) { // SUGOD
                tokens.add(new Token(TokenType.KEYWORD, matcher.group(1)));
            } else if (matcher.group(2) != null) { // MUGNA
                tokens.add(new Token(TokenType.KEYWORD, matcher.group(2).trim())); // Trim whitespace
            } else if (matcher.group(3) != null) { // DATA TYPE (NUMERO, LETTERS, BOOLEAN)
                tokens.add(new Token(TokenType.DATA_TYPE, matcher.group(3).trim()));
            } else if (matcher.group(4) != null) { // VARIABLE NAME (IDENTIFIER)
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(4).trim())); // Trim identifier
            } else if (matcher.group(5) != null) { // Assignment operator "=" (optional)
                tokens.add(new Token(TokenType.ASSIGN, matcher.group(5)));
            } else if (matcher.group(6) != null) { // EXPRESSION (optional after assignment)
                if(matcher.group(6).isBlank()){
                    continue;
                }
                tokens.add(new Token(TokenType.EXPRESSION, matcher.group(6).trim())); // Trim expression
            } else if (matcher.group(7) != null) { // WHITE SPACE
                continue;
            }else if (matcher.group(8) != null) { // KATAPUSAN
                tokens.add(new Token(TokenType.KEYWORD, matcher.group(8)));
            }else{
                System.out.println("idk");
            }
        }

        // Add the EOF token at the end
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
