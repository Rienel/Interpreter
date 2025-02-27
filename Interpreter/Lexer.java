import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern COMMENT = Pattern.compile("--.*");
    private static final Pattern RESERVED_KEYWORDS = Pattern.compile("SUGOD|KATAPUSAN|MUGNA");
    private static final Pattern VARIABLE = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final Pattern NUMBER = Pattern.compile("\\d+");
    private static final Pattern CONCAT = Pattern.compile("&");
    private static final Pattern ESCAPE = Pattern.compile("[\\[\\]]");
    private static final Pattern DOLLAR = Pattern.compile("\\$");

    private final String input;
    private int position = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);

            // Skip whitespace
            if (Character.isWhitespace(currentChar)) {
                position++;
                continue;
            }

            // Match comments
            if (currentChar == '-' && position + 1 < input.length() && input.charAt(position + 1) == '-') {
                // Skip the comment line
                String comment = readComment();
                tokens.add(new Token(TokenType.COMMENT, comment));
                continue;
            }

            // Match reserved keywords (SUGOD, KATAPUSAN, MUGNA)
            if (Character.isLetter(currentChar)) {
                String word = readWord();
                TokenType type = getReservedWordType(word);
                if (type != null) {
                    tokens.add(new Token(TokenType.RESERVED_KEYWORD, word));
                } else if (VARIABLE.matcher(word).matches()) {
                    tokens.add(new Token(TokenType.VARIABLE, word));
                } else {
                    throw new IllegalArgumentException("Unknown token: " + word);
                }
                continue;
            }

            // Match numbers
            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.NUMERO, readNumber()));
                continue;
            }

            // Match concatenation operator (&)
            if (currentChar == '&') {
                tokens.add(new Token(TokenType.CONCAT, String.valueOf(currentChar)));
                position++;
                continue;
            }

            // Match escape codes ([])
            if (currentChar == '[' || currentChar == ']') {
                tokens.add(new Token(TokenType.ESCAPE, String.valueOf(currentChar)));
                position++;
                continue;
            }

            // Match dollar sign ($)
            if (currentChar == '$') {
                tokens.add(new Token(TokenType.DOLLAR, String.valueOf(currentChar)));
                position++;
                continue;
            }

            // Match dollar equal (=)
            if (currentChar == '=') {
                tokens.add(new Token(TokenType.ASSIGN, String.valueOf(currentChar)));
                position++;
                continue;
            }

            throw new IllegalArgumentException("Unexpected character: " + currentChar);
        }

        // End of file token
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private String readWord() {
        StringBuilder word = new StringBuilder();
        while (position < input.length() && Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_') {
            word.append(input.charAt(position));
            position++;
        }
        return word.toString();
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

    private String readComment() {
        StringBuilder comment = new StringBuilder();
        while (position < input.length() && input.charAt(position) != '\n') {
            comment.append(input.charAt(position));
            position++;
        }
        return comment.toString();
    }

    private TokenType getReservedWordType(String word) {
        switch (word) {
            case "SUGOD":
                return TokenType.SUGOD;
            case "KATAPUSAN":
                return TokenType.KATAPUSAN;
            case "MUGNA":
                return TokenType.MUGNA;
            default:
                return null;
        }
    }
}
