package token;

public enum TokenType {
    ILLEGAL("ILLEGAL"),
    EOF("EOF"),

    //Identifiers + literals
    IDENT("IDENT"),
    INTEGER("INTEGER"),
    FLOATINGPOINT("FLOATINGPOINT"),
    TRUE("OO"),
    FALSE("DILI"),
    CHARACTER("CHARACTER"),

    //Operators
    ASSIGN("="),
    LPARA("("),
    RPARA(")"),
    PLUS("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    GREAT(">"),
    LESS("<"),
    GREATEQ(">="),
    LESSEQ("<="),
    EQUAL("=="),
    NOTEQUAL("<>"),
    ESCAPE("["),



    //KEYWORD

    START("SUGOD"),
    MUGNA("MUGNA"),
    END("KATAPUSAN"),
    INT("NUMERO"),
    CHAR("LETRA"),
    BOOL("TINUOD"),
    FLOAT("TIPIK"),
    DISPLAY("IPAKITA"),
    SCAN("DAWAT"),
    IF("IF"),
    ELSE("ELSE"),
    AND("UG"),
    OR("O"),
    NOT("DILI"),
    STRING("STRING"),
    WHILE("WHILE"),
    RETURN("RETURN"),
    HASH("HASH"),


    //SPECIAL
//    COMMENT("#"),
    COLON(":"),
    LBRACE("{"),
    RBRACE("}"),
    COMMA(","),
    EOL("$"),
    INDEXOPEN("|"),
    INDEXCLOSE("\\"),
    CONCAT("&");




    private final String literal;

    private TokenType(String literal){
        this.literal = literal;
    }

    public String getLiteral(){
        return literal;
    }
}