package lexer;
import token.TokenType;
import token.Token;

public class Lexer {
    private String input;
    private int position;
    private int readPosition;
    private char ch;
    private static int line = 1;

    public Lexer(String input){
        this.input = input;
        this.position = 0;
        this.readPosition = 0;
        this.ch = '\0';
        readChar();
    }

    public Lexer(){
        this.position = 0;
        this.readPosition = 0;
        this.ch = '\0';
    }

    

    public void readChar(){
        if(readPosition >= input.length()){
            ch = 0;
        }else {
            ch=input.charAt(readPosition);
        }
        position = readPosition;
        readPosition++;

    }

    public void unreadChar() {
        if (position > 0) { // Ensure we don't move before the start of input
            readPosition--;
            position--;
            ch = input.charAt(position);
        }
    }

    public Token nextToken(){
        Token tok = new Token();

        
        skipWhiteSpace();

        switch (ch) {
            case '=':
                if(peekChar() == '='){
                    char tempCh = ch;
                    readChar();
                    String finalLiteral = Character.toString(tempCh) + Character.toString(ch);
                    tok.setLiteral(finalLiteral);
                    tok.setTokenType(TokenType.EQUAL);
                }else{
                    tok = newToken(TokenType.ASSIGN, ch);
                }
                break;
            case '(':
                tok = newToken(TokenType.LPARA, ch);
                break;
            case ')':
                tok = newToken(TokenType.RPARA, ch);
                break;
            case '+':
                tok = newToken(TokenType.PLUS, ch);
                break;
            case '-':
                tok = newToken(TokenType.SUBTRACT, ch);
                break;
            case '*':
                tok = newToken(TokenType.MULTIPLY, ch);
                break;
            case '/':
                tok = newToken(TokenType.DIVIDE, ch);
                break;
            case '%':
                tok = newToken(TokenType.MODULO, ch);
                break;
            case '>':
                if(peekChar() == '='){
                    char tempCh = ch;
                    readChar();
                    String finalLiteral = Character.toString(tempCh) + Character.toString(ch);
                    tok.setLiteral(finalLiteral);
                    tok.setTokenType(TokenType.GREATEQ);
                }else{
                    tok = newToken(TokenType.GREAT, ch);
                }
                break;
            case '<':
                if(peekChar() == '='){
                    char tempCh = ch;
                    readChar();
                    String finalLiteral = Character.toString(tempCh) + Character.toString(ch);
                    tok.setLiteral(finalLiteral);
                    tok.setTokenType(TokenType.LESSEQ);
                }else if(peekChar() == '>'){
                    char tempCh = ch;
                    
                    readChar();
                    String finalLiteral = Character.toString(tempCh) + Character.toString(ch);
                    tok.setLiteral(finalLiteral);
                    tok.setTokenType(TokenType.NOTEQUAL);
                }else {
                    tok = newToken(TokenType.LESS, ch);
                }
                break;
            case '$':
                tok = newToken(TokenType.EOL, ch);
                break;
            case ',':
                tok = newToken(TokenType.COMMA, ch);
                break;
            case ':':
                tok = newToken(TokenType.COLON, ch);
                break;
            case '&':
                tok = newToken(TokenType.CONCAT, ch);
                break;
            case '[':
                readChar();
                char lit = ch;
                readChar();
                if(ch == ']'){
                    readChar();
                    return newToken(TokenType.ESCAPE, lit);
                }
                return newToken(TokenType.ILLEGAL, ch);
                
            case '\'':
                readChar();
                char tempCh = ch;
                readChar();

                if(ch == '\''){
                    tok = newToken(TokenType.CHARACTER, tempCh);
                }else{
                    tok = newToken(TokenType.ILLEGAL, ch);
                }
                break;
            
            case '\"':
                tok.setLiteral(readBoolean());
                if(tok.getLiteral().equals("OO")){
                    tok.setTokenType(TokenType.TRUE);
                }else if(tok.getLiteral().equals("DILI")){
                    tok.setTokenType(TokenType.FALSE);
                }else{
                    tok.setTokenType(TokenType.STRING);
                }
                break;
            case '{':
                tok.setLiteral("{");
                tok.setTokenType(TokenType.LBRACE);
                break;
            case '}':
                tok.setLiteral("}");
                tok.setTokenType(TokenType.RBRACE);
                break;
            case '|':
                tok.setLiteral("|");
                tok.setTokenType(TokenType.INDEXOPEN);
                break;         
            case '\\':
                tok.setLiteral("\\");
                tok.setTokenType(TokenType.INDEXCLOSE);
                break;                           
            case 0:
                tok.setLiteral("");
                tok.setTokenType(TokenType.EOF);
                break;
            default:
                if(isLetter(ch)){
                    tok.setLiteral(readIdentifier());
                    tok.setTokenType(tok.lookupIdent(tok.getLiteral()));
                    // System.out.println(tok);
                    return tok;
                }else if(isDigit(ch)){
                    tok.setLiteral(readNumber());
                    if(isFloat(tok)){
                        tok.setTokenType(TokenType.FLOATINGPOINT);

                    }else{
                        
                        tok.setTokenType(TokenType.INTEGER);
                    }
                    // System.out.println(tok);
                    return tok;
                }else {
                    tok = newToken(TokenType.ILLEGAL, ch);

                }
        }
        readChar();
        // System.out.println(tok);
        return tok;
    }

    private char peekChar(){
        if (readPosition >= input.length()){
            return 0;
        }else{
            return input.charAt(readPosition);
        }
    }

    private Boolean isFloat(Token tok){
        for(int i = 0; i < tok.getLiteral().length(); i++){
            if(tok.getLiteral().charAt(i) == '.'){
                return true;
            }
        }
        return false;
    }



    private String readIdentifier() {
        int tempPosition = position;
        while (isLetter(ch) || isDigit(ch)) {
            readChar();
        }
        String ident = input.substring(tempPosition, position);

        if (ident.equals("MUGNA")) {
            return "MUGNA"; // Ensures the lexer properly identifies it
        }

        return ident;
    }



    private String readBoolean(){
        readChar();
        int tempPosition = position;
        while(ch != '"'){
            readChar();
        }
        
        return input.substring(tempPosition, position);
    }


    private String readNumber(){
        int tempPosition = position;
        while(isDigit(ch) || ch == '.'){
            readChar();
        }
        

        return input.substring(tempPosition, position);
    }

    private boolean isLetter(char ch){
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_';
    }

    private boolean isDigit(char ch){
        return '0' <= ch && ch <= '9';
    }

    private void skipWhiteSpace() {
        while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            if (ch == '\n') {
                line++;
            }
            readChar();
        }


        if (ch == '-') {
            readChar();
            if (ch == '-') {

                while (ch != '\n' && ch != 0) {
                    readChar();
                }
                line--;


                while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
                    if (ch == '\n') {
                        line++;
                    }
                    readChar();
                }
            } else {

                unreadChar();
            }
        }
    }

    public static Token newToken(TokenType tokenType, char ch){
        return new Token(tokenType, String.valueOf(ch));
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public static int getLine(){
        return line;
    }
}
