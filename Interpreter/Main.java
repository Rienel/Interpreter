import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder input = new StringBuilder();

        // Read lines from the user until "KATAPUSAN" is encountered
        while (true) {
            String text = sc.nextLine();  // Read the full line

            // Append each line to the input (with a newline for clarity)
            input.append(text).append("\n");

            // If "KATAPUSAN" is found, break out of the loop
            if (text.contains("KATAPUSAN")) {
                System.out.println(input);
                break;
            }
        }

        // Initialize the lexer after all input has been collected
        Lexer lexer = new Lexer(input.toString());
        List<Token> tokens = lexer.lex();  // Get the tokens

        // Print out the tokens
        for (Token token : tokens) {
            System.out.println(token);
        }

        // to initialize parser
        Parser parser = new Parser(tokens);
        parser.parse();
    }
}
