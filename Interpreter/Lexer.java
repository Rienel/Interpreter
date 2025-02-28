import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {
    //The patterns | regex
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern COMMENT = Pattern.compile("--.*");
    private static final Pattern RESERVED_KEYWORDS = Pattern.compile("SUGOD|KATAPUSAN|MUGNA");
    private static final Pattern DATA_TYPE = Pattern.compile("NUMERO|LETRA|TIPIK|TINUOD");
    private static final Pattern ASSIGNMENT_OP = Pattern.compile("=");
    private static final Pattern VARIABLE = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final Pattern MULTIPLE_VARIABLE = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\s*,\\s*[a-zA-Z_][a-zA-Z0-9_]*)*$");
    private static final Pattern NUMERO = Pattern.compile("\\d+");
    private static final Pattern CONCAT = Pattern.compile("&");
    private static final Pattern ESCAPE = Pattern.compile("[\\[\\]]");
    private static final Pattern DOLLAR = Pattern.compile("\\$");

    private static final Pattern START = Pattern.compile("SUGOD");
    private static final Pattern END = Pattern.compile("KATAPUSAN");


    //Line by line pattern
    //A statement can be an expression containing many arguments (words, letters, char, et...)
    private static final Pattern STATEMENT = Pattern.compile(
            "^\\s*MUGNA\\s*(NUMERO|LETRA|TIPIK|TINUOD)\\s*[a-zA-Z_][a-zA-Z0-9_]*" +
                    "|^\\s*MUGNA\\s*(NUMERO|LETRA|TIPIK|TINUOD)\\s*[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*(\\w+|\\d+)" +
                    "|^\\s*MUGNA\\s*(NUMERO|LETRA|TIPIK|TINUOD)\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\s*,\\s*[a-zA-Z_][a-zA-Z0-9_]*)*\\s*=\\s*(\\w+|\\d+)" +
                    "|^\\s*MUGNA\\s*(NUMERO|LETRA|TIPIK|TINUOD)\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\s*,\\s*[a-zA-Z_][a-zA-Z0-9_]*)*"

    );

    private final String input;
    private int position = 0;

    public Lexer(String input) {
        this.input = input;
    }

    //Tokenization part
    public List<Token> tokenize() {
        System.out.println(readLines(input));

        List<Token> tokens = new ArrayList<>();
        List<String> codeLines = readLines(input);
        List<String> choppedStatements;

        // Check if the first and last lines are "SUGOD" and "KATAPUSAN" respectively
        if (!codeLines.getFirst().equals("SUGOD") || !codeLines.getLast().equals("KATAPUSAN")) {
            throw new IllegalArgumentException("Should start with SUGOD and end with KATAPUSAN");
        }
        //Matching one line of code at a time to the given patterns
        //A statement can be an expression containing many arguments (words, letters, char, et...)
        while (position < codeLines.size()) {
            String statement = codeLines.get(position);

            if (START.matcher(statement).matches()) {
                tokens.add(new Token(TokenType.RESERVED_KEYWORD, statement));
                position++;
                continue;
            }
            //The statement needs to be broken down further
            if (STATEMENT.matcher(statement).matches()) {
                choppedStatements = statementChop(statement);
                //Matching the pattern
                int i = 0;
                while (i < choppedStatements.size()) {
                    System.out.println(choppedStatements.get(i));
                    if (RESERVED_KEYWORDS.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.RESERVED_KEYWORD, choppedStatements.get(i)));
                    } else if (DATA_TYPE.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.DATA_TYPE, choppedStatements.get(i)));
                    } else if (VARIABLE.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.VARIABLE, choppedStatements.get(i)));
                    }else if (MULTIPLE_VARIABLE.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.VARIABLE, choppedStatements.get(i)));
                    } else if (NUMERO.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.NUMERO, choppedStatements.get(i)));
                    } else if (ASSIGNMENT_OP.matcher(choppedStatements.get(i)).matches()) {
                        tokens.add(new Token(TokenType.ASSIGN, choppedStatements.get(i)));
                    }
                    i++;
                }

                position++;
                continue;
            }

            if (END.matcher(statement).matches()) {
                tokens.add(new Token(TokenType.RESERVED_KEYWORD, statement));
                position++;
                continue;
            }
            //If the statement or line of code does not follow any of the given patterns
            throw new IllegalArgumentException("Unexpected pattern: " + statement);
        }

        return tokens;
    }

    //Divides the code line by line and store is in a list
    private List<String> readLines(String input) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        int length = input.length();
        int pos = 0;

        while (pos < length) {
            char currentChar = input.charAt(pos);

            if (currentChar == '\n') {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            } else {
                currentLine.append(currentChar);
            }
            pos++;
        }
        return lines;
    }
    //Chops the line of code into individual parts, stored in a list
    private List<String> statementChop(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isWhitespace(currentChar) || currentChar == ',') {
                if (!word.isEmpty()) {
                    parts.add(word.toString());
                    word.setLength(0); // Clear the StringBuilder
                }
            } else {
                word.append(currentChar);
            }
        }

        // Add the last word if there's any remaining
        if (!word.isEmpty()) {
            parts.add(word.toString());
        }
        return parts;
    }


    private String readWord(String input) {
        int i =0;
        StringBuilder word = new StringBuilder();
        while (i < input.length() && Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_') {
            word.append(input.charAt(i));
            i++;
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
