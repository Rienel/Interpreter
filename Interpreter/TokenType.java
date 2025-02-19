public enum TokenType {
    // Control Keywords
    SUGOD,
    KATAPUSAN,

    // Variable Declarations
    MUGNA,
    KEYWORD,

    //variable name
    IDENTIFIER,
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
    AMPERSAND,  // &
    DOLLAR,     // $
    LBRACKET,   // [
    RBRACKET,   // ]
    COMMA,      // ,

    // Assignment Operator
    ASSIGN,     // =

    // End of Input
    EOF
}
