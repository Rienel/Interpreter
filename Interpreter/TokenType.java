public enum TokenType {
    // Control Keywords
    SUGOD,
    KATAPUSAN,

    // Variable Declarations
    MUGNA,
    RESERVED_KEYWORD,

    //variable name
    IDENTIFIER,
    VARIABLE,
    EXPRESSION,
    //Data Types
    DATA_TYPE,
    NUMERO,
    LETRA,
    TINUOD,
    TIPIK,

    // Arithmetic Operators
    PLUS,       // +
    MINUS,      // -
    MUL,        // *
    DIV,        // /
    MOD,        // %
    LPAREN,     // (
    RPAREN,     // )


    // Relational Operators
    GT,         // >
    LT,         // <
    GE,         // >=
    LE,         // <=
    EQ,         // ==
    NE,         // <>

    // Logical Operators
    UG,         // AND
    O,          // OR
    DILI,       // NOT

    // Boolean Values
    OO,  // TRUE
    DILI_BOOL, // FALSE

    // Symbols
    CONCAT,     // &
    DOLLAR,     // $
    LBRACKET,   // [
    RBRACKET,   // ]
    COMMA,      // ,
    ESCAPE,     // []

    // Assignment Operator
    COMMENT,    // comment
    ASSIGN,     // =

    // End of Input
    EOF
}
