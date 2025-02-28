import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String str ="SUGOD\n" +
                "\tMUGNA NUMERO x,z,b = 5\n" +
                "\tMUGNA NUMERO y = 3\n" +
                //"\tz = x & y\n" +
                //"-- This is a comment\n" +
                "KATAPUSAN\n";

        // Initialize the lexer after all input has been collected
        Lexer lexer = new Lexer(str);
        List<Token> tokens = lexer.tokenize();  // Get the tokens

        // Print out the tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
