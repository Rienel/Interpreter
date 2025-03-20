package token;

import java.util.HashMap;
import java.util.Map;

public class Token {
    private TokenType tokenType;
    private String literal;
    private final Map<String, TokenType> keywords;

    public Token(TokenType tokenType, String literal){
        this.tokenType = tokenType;
        this.literal = literal;
        this.keywords = new HashMap<>();
        initKeywords();
    }
    public Token(){
        this.keywords = new HashMap<>();
        initKeywords();
    }

    private void initKeywords(){
        keywords.put("MUGNA", TokenType.MUGNA);
        keywords.put("LETRA", TokenType.CHAR);
        keywords.put("NUMERO", TokenType.INT);
        keywords.put("TINUOD", TokenType.BOOL);
        keywords.put("TIPIK", TokenType.FLOAT);
        keywords.put("SUGOD", TokenType.START);
        keywords.put("KATAPUSAN", TokenType.END);
        keywords.put("IPAKITA", TokenType.DISPLAY);
        keywords.put("DAWAT", TokenType.SCAN);
        keywords.put("IF", TokenType.IF);
        keywords.put("ELSE", TokenType.ELSE);
        keywords.put("UG", TokenType.AND);
        keywords.put("O", TokenType.OR);
        keywords.put("DILI", TokenType.NOT);
        keywords.put("WHILE", TokenType.WHILE);
        keywords.put("RETURN", TokenType.RETURN);
        keywords.put("HASH", tokenType.HASH);
    }

    public TokenType lookupIdent(String ident){
        if(keywords.containsKey(ident)){
            return keywords.get(ident);
        }
        return TokenType.IDENT;
    }

    public void setTokenType(TokenType tokenType){
        this.tokenType = tokenType;
    }
    public TokenType getTokenType(){
        return tokenType;
    }

    public void setLiteral(String literal){
        this.literal = literal;
    }
    public String getLiteral(){
        return literal;
    }

    public String toString(){
        return "Token{" + "type = \"" + tokenType + "\" literal = \"" +  literal + "\"}";
    }
}