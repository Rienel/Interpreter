package parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.SQLOutput;
import java.util.*;


import ast.*;
import lexer.Lexer;
import org.junit.runners.model.TestTimedOutException;
import token.Token;
import token.TokenType;

import org.junit.Assert;


public class Parser {
    private final Lexer lexer;
    private Token curToken;
    private Token peekToken;
    private List<String> errors;
    private final Map<TokenType, PrefixParseFn> prefixParseFns;
    private final Map<TokenType, InfixParseFn> infixParseFns;
    private final Map<TokenType, Integer> infixPrecedences;
    private final Map<TokenType, PrefixParseFn> postfixParseFns;
    private Map<String, Statement> statementsList; 
    private Boolean hasStarted;
    private Boolean variableDeclarationStarted;
    private Boolean executableStarted;
    private int statementsCount;
    private boolean ifStarted;
    private final Program program;
    private List<Statement> tempStatementList;
    private boolean hasEnded;
    private boolean forLoopStarted;

    public enum OperatorType{
        
        LOWEST(1),
        LOGICAL(2),
        EQUALS(3),
        LESSGREATER(4),
        SUM(5),
        PRODUCT(6),
        PREFIX(7),
        INDEX(8);
        
        private final int precedence;
        OperatorType(int precedence){
            this.precedence = precedence;
        }

        public int getPrecedence(){
            return precedence;
        }
    }
    private final List<String> reservedWords;

    public Parser(Lexer lexer){
        tempStatementList = new ArrayList<>();
        program = new Program(new ArrayList<>());
        this.lexer = lexer;
        prefixParseFns = new HashMap<>();
        postfixParseFns = new HashMap<>();
        infixParseFns = new HashMap<>();
        infixPrecedences = new HashMap<>();
        reservedWords = new ArrayList<>();
        statementsList = new HashMap<>();
        hasStarted = false;
        ifStarted = false;
        forLoopStarted = false;
        executableStarted = false;
        variableDeclarationStarted =false;
        hasEnded = false;
        statementsCount = 1;
        initPrecedences();
        initReservedWords();
        errors = new ArrayList<>();
        registerExpressions();
        nextToken();
        nextToken();

        

    }

    private void initPrecedences(){
        infixPrecedences.put(TokenType.EQUAL, OperatorType.EQUALS.getPrecedence());
        infixPrecedences.put(TokenType.NOTEQUAL, OperatorType.EQUALS.getPrecedence());
        infixPrecedences.put(TokenType.LESS, OperatorType.LESSGREATER.getPrecedence());
        infixPrecedences.put(TokenType.GREAT, OperatorType.LESSGREATER.getPrecedence());
        infixPrecedences.put(TokenType. LESSEQ, OperatorType.LESSGREATER.getPrecedence());
        infixPrecedences.put(TokenType.GREATEQ, OperatorType.LESSGREATER.getPrecedence());
        infixPrecedences.put(TokenType.PLUS, OperatorType.SUM.getPrecedence());
        infixPrecedences.put(TokenType.SUBTRACT, OperatorType.SUM.getPrecedence());
        infixPrecedences.put(TokenType.MULTIPLY, OperatorType.PRODUCT.getPrecedence());
        infixPrecedences.put(TokenType.MODULO, OperatorType.PRODUCT.getPrecedence());
        infixPrecedences.put(TokenType.DIVIDE, OperatorType.PRODUCT.getPrecedence());
        infixPrecedences.put(TokenType.AND, OperatorType.LOGICAL.getPrecedence());
        infixPrecedences.put(TokenType.OR, OperatorType.LOGICAL.getPrecedence());
        infixPrecedences.put(TokenType.NOT, OperatorType.LOWEST.getPrecedence());
        infixPrecedences.put(TokenType.INDEXOPEN, OperatorType.INDEX.getPrecedence());
        
    }

    private void initReservedWords(){
        reservedWords.add("MUGNA");
        reservedWords.add("CHAR");
        reservedWords.add("NUMERO");
        reservedWords.add("TINUOD");
        reservedWords.add("FLOAT");
        reservedWords.add("DISPLAY");
        reservedWords.add("SCAN");
        reservedWords.add("SUGOD");
        reservedWords.add("KATAPUSAN");
        reservedWords.add("FUNC");
        reservedWords.add("HASH");
        reservedWords.add("ALANG");
        reservedWords.add("SA");
        reservedWords.add("PUNDOK");
    }

    private void registerExpressions(){

        registerPrefix(TokenType.START, this::parseBeginExpression) ;
        registerPrefix(TokenType.MUGNA, this::parseBeginExpression) ;
        registerPrefix(TokenType.IDENT, this::parseIdentifier);
        registerPrefix(TokenType.LPARA, this::parseGroupedExpression);
        registerPrefix(TokenType.INTEGER, this::parseIntegerLiteral);
        registerPrefix(TokenType.FLOATINGPOINT, this::parseFloatLiteral);
        registerPrefix(TokenType.SUBTRACT, this::parsePrefixExpression);
        registerInfix(TokenType.PLUS, this::parseInfixExpression);
        registerInfix(TokenType.SUBTRACT, this::parseInfixExpression);
        registerInfix(TokenType.MULTIPLY, this::parseInfixExpression);
        registerInfix(TokenType.DIVIDE, this::parseInfixExpression);
        registerInfix(TokenType.MODULO, this::parseInfixExpression);
        registerInfix(TokenType.EQUAL, this::parseInfixExpression);
        registerInfix(TokenType.NOTEQUAL, this::parseInfixExpression);
        registerInfix(TokenType.GREAT, this::parseInfixExpression);
        registerInfix(TokenType.LESS, this::parseInfixExpression);
        registerInfix(TokenType.GREATEQ, this::parseInfixExpression);
        registerInfix(TokenType.LESSEQ, this::parseInfixExpression);
        registerPrefix(TokenType.TRUE, this::parseBoolean);
        registerPrefix(TokenType.FALSE, this::parseBoolean);
        registerPrefix(TokenType.CHARACTER, this::parseCharacter);
        registerPrefix(TokenType.DISPLAY, this::parseDisplayExpression);
        registerPrefix(TokenType.SCAN, this::parseScanExpression);
        registerPrefix(TokenType.IF, this::parseIfExpression);
        registerPrefix(TokenType.WHILE, this::parseWhileExpression);
        registerInfix(TokenType.AND, this::parseInfixExpression);
        registerInfix(TokenType.OR, this::parseInfixExpression);
        registerPrefix(TokenType.NOT, this::parsePrefixExpression);
        registerPrefix(TokenType.STRING, this::parseString);
        registerPrefix(TokenType.LBRACE, this::parseHashLiteral);
        registerInfix(TokenType.INDEXOPEN, this::parseIndexExpression);

        registerPostfix(TokenType.PLUSPLUS, this::parsePostfixExpression);
        registerPostfix(TokenType.MINUSMINUS, this::parsePostfixExpression);
        registerPrefix(TokenType.ALANG, this::parseAlangSaExpression);
    }

    public void nextToken(){
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    public Program ParseProgram() throws Exception{
        
    
        while(curToken.getTokenType() != TokenType.EOF){
            List<Statement> stmt = parseStatement();
            if(stmt != null){
                for(Statement statement: stmt){
                    program.addStatement(statement);
                }
            }
            nextToken();
        }

        return program;
        

    }

    public Expression parseString(){
        String str = curToken.getLiteral();
        return new StringValue(curToken, str);
    }

    public Expression parseIdentifier(){
        String ident = curToken.getLiteral();
        return new Identifier(curToken, ident);
    }

    public Expression parseBoolean(){
        return new BooleanExpression(curToken, curTokenIs(TokenType.TRUE));
    }

    public Expression parseCharacter(){
        return new CharacterExpression(curToken, curToken.getLiteral().charAt(0));
    }
    
    public Expression parseHashLiteral(){
        HashLiteral hash = new HashLiteral();
        hash.setToken(curToken);
        Map<Expression, Expression> tempMap = new HashMap<>();
        
        while(!peekTokenIs(TokenType.RBRACE)){
            nextToken();
            try {
                Expression key =  parseExpression(OperatorType.LOWEST.getPrecedence());
                if(!expectPeek(TokenType.COLON)){
                    return null;
                }
                nextToken();
                Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                
                tempMap.put(key, value);

                if(!peekTokenIs(TokenType.RBRACE) && !expectPeek(TokenType.COMMA)){
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(!expectPeek(TokenType.RBRACE)){
            return null;
        }

        hash.setPairs(tempMap);

        return hash;


    }

    public Expression parseIndexExpression(Expression left){
        IndexExpression indexExpression = new IndexExpression();
        indexExpression.setToken(curToken);
        indexExpression.setLeft(left);
        nextToken();

        try {
            indexExpression.setIndex(parseExpression(OperatorType.LOWEST.getPrecedence()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!expectPeek(TokenType.INDEXCLOSE)){
            return null;
        }

        return indexExpression;
    }

    public List<Statement> parseStatement() throws Exception {
        if(hasEnded){
            errors.add(String.format("Error: token %s nakit-an human sa END CODE : linya %d", curToken.getLiteral(), Lexer.getLine()));
            return null;
        }

        tempStatementList = new ArrayList<>();

        switch (curToken.getTokenType()){
            case RETURN:
                parseReturnStatement();
                return tempStatementList;

            case CHAR:
                if(executableStarted){
                    errors.add("ERROR: Dili pwede ang Variable human sa  Executable code : linya " + Lexer.getLine());
                    return null;
                }
                variableDeclarationStarted = true;
                parseCharStatement();
                return tempStatementList;

            case INT:
                if(executableStarted){
                    errors.add("ERROR: Dili pwede ang Variable human sa  Executable code : linya " + Lexer.getLine());
                    return null;
                }
                variableDeclarationStarted = true;
                parseIntStatement();
                return tempStatementList;

            case BOOL:
                if(executableStarted){
                    errors.add("ERROR: Dili pwede ang Variable human sa  Executable code : linya " + Lexer.getLine());
                    return null;
                }
                parseBoolStatement();
                variableDeclarationStarted = true;
                return tempStatementList;

            case FLOAT:
                if(executableStarted){
                    errors.add("ERROR: Cannot Declare Variable after Executable code : line " + Lexer.getLine());
                    return null;
                }
                variableDeclarationStarted = true;
                parseFloatStatement();
                return tempStatementList;
            case HASH:
                if(executableStarted){
                    errors.add("ERROR: Dili pwede ang Variable human sa  Executable code : linya " + Lexer.getLine());
                    return null;
                }
                variableDeclarationStarted = true;
                parseHashStatement();
                return tempStatementList;


            case ILLEGAL:
                errors.add(String.format("Illegal na token %s : linya %d", curToken.getLiteral(), Lexer.getLine()));
                return null;

            case ALANG:
                // Handle the "ALANG" keyword (For loop initialization)
                if (peekTokenIs(TokenType.SA)) {
                    tempStatementList.add(parseAlangSaExpression()); // Call the parseForExpression method
                    return tempStatementList;
                }
            case PUNDOK:
                if (peekTokenIs(TokenType.LBRACE)) {
                    tempStatementList.add(parsePundokExpression());
                    return tempStatementList;
                }
            default:
                if(curTokenIs(TokenType.IDENT) && peekTokenIs(TokenType.ASSIGN)){
                    parseReassignment();
                    return tempStatementList;
                }
                List<Statement> tempExp = new ArrayList<>();
                tempExp.add(parseExpressionStatement());
                return tempExp;
        }
    }

    private void parseReassignment(){
        String ident = curToken.getLiteral();
        executableStarted = true;
        if(statementsList.containsKey(curToken.getLiteral())){
            
            if(statementsList.get(ident) instanceof IntStatement){
                List<IntStatement> temp = new ArrayList<>();
                IntStatement newStmt = new IntStatement();
                Token token = new Token(TokenType.INT, "INT");
                newStmt.setToken(token);
                newStmt.setName(new Identifier(token, ident));
                nextToken();
                nextToken();
                if(peekTokenIs(TokenType.ASSIGN)){
                    temp.add(newStmt);
                    while(peekTokenIs(TokenType.ASSIGN)){
                        IntStatement is = new IntStatement();
                        is.setName(new Identifier(curToken, curToken.getLiteral()));
                        is.setToken(curToken);
                        temp.add(is);
                        nextToken();
                        nextToken();
                    }
                    try{
                        
                        Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                        for(IntStatement x: temp){
                            x.setValue(value);
                            tempStatementList.add(x);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                            
                try {
                    newStmt.setValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
                } catch (Exception e) {
                    e.printStackTrace();
                        
                }
                tempStatementList.add(newStmt);
                
            }else if(statementsList.get(ident) instanceof FloatStatement){
                    List<FloatStatement> temp = new ArrayList<>();
                    FloatStatement newStmt = new FloatStatement();
                    Token token = new Token(TokenType.FLOAT, "FLOAT");
                    newStmt.setToken(token);
                    newStmt.setName(new Identifier(token, ident));
                   
                    nextToken();
                    nextToken();
                    if(peekTokenIs(TokenType.ASSIGN)){
                        temp.add(newStmt);
                        while(peekTokenIs(TokenType.ASSIGN)){
                            FloatStatement is = new FloatStatement();
                            is.setName(new Identifier(curToken, curToken.getLiteral()));
                            is.setToken(curToken);
                            temp.add(is);
                            nextToken();
                            nextToken();
                        }
                        try{
                            
                            Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                            for(FloatStatement x: temp){
                                x.setValue(value);
                                tempStatementList.add(x);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return;
                    }
                                
                    try {
                        newStmt.setValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
                    } catch (Exception e) {
                        e.printStackTrace();
                            
                    }
                    tempStatementList.add(newStmt);
                   
                
            }else if(statementsList.get(ident) instanceof BoolStatement){
                    List<BoolStatement> temp = new ArrayList<>();
                    BoolStatement newStmt = new BoolStatement();
                    Token token = new Token(TokenType.BOOL, "BOOL");
                    newStmt.setToken(token);
                    newStmt.setName(new Identifier(token, ident));
                    
                    nextToken();
                    nextToken();

                    if(peekTokenIs(TokenType.ASSIGN)){
                        temp.add(newStmt);
                        while(peekTokenIs(TokenType.ASSIGN)){
                            BoolStatement is = new BoolStatement();
                            is.setName(new Identifier(curToken, curToken.getLiteral()));
                            is.setToken(curToken);
                            temp.add(is);
                            nextToken();
                            nextToken();
                        }
                        try{
                            
                            Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                            for(BoolStatement x: temp){
                                x.setValue(value);
                                tempStatementList.add(x);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return;
                    }
                                
                    try {
                        newStmt.setValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
                    } catch (Exception e) {
                        e.printStackTrace();
                            
                    }
                    tempStatementList.add(newStmt);
                    
                
                
            }else if(statementsList.get(ident) instanceof CharStatement){
                    List<CharStatement> temp = new ArrayList<>();
                    CharStatement newStmt = new CharStatement();
                    Token token = new Token(TokenType.CHAR, "CHAR");
                    newStmt.setToken(token);
                    newStmt.setName(new Identifier(token, ident));
                    nextToken();
                    nextToken();

                    if(peekTokenIs(TokenType.ASSIGN)){
                        temp.add(newStmt);
                        while(peekTokenIs(TokenType.ASSIGN)){
                            CharStatement is = new CharStatement();
                            is.setName(new Identifier(curToken, curToken.getLiteral()));
                            is.setToken(curToken);
                            temp.add(is);
                            nextToken();
                            nextToken();
                        }
                        try{
                            
                            Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                            for(CharStatement x: temp){
                                x.setValue(value);
                                tempStatementList.add(x);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return;
                    }
                                
                    try {
                        newStmt.setValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
                    } catch (Exception e) {
                        e.printStackTrace();
                            
                    }
                    tempStatementList.add(newStmt);
                    
                }
            }
        }

    private WhileExpression parseWhileExpression(){
        executableStarted = true;
        forLoopStarted = true;
        
        WhileExpression exp = new WhileExpression();
        exp.setToken(curToken);

        if(!expectPeek(TokenType.LPARA)){
            return null;
        }
        nextToken();
        try{
            exp.setCondition(parseExpression(OperatorType.LOWEST.getPrecedence()));
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!expectPeek(TokenType.RPARA)){
            return null;
        }
        statementsCount++;

        if(!expectPeek(TokenType.START)){
            return null;
        }
       
        nextToken();
        if(!curTokenIs(TokenType.WHILE)){
            errors.add("Sayup imong LOOP Mam/Ser");
            return null;
        }
        statementsCount++;

        try{
            exp.setContent(parseBlockStatement(curToken.getLiteral()));
        } catch(Exception e){
            e.printStackTrace();
        }
        forLoopStarted = false;
        return exp;
    }

    private IfExpression parseIfExpression(){
        executableStarted = true;
        ifStarted = true;
        
        IfExpression exp = new IfExpression();
        exp.setToken(curToken);


        if(!expectPeek(TokenType.LPARA)){
            return null;
        }
        nextToken();
        try {
            exp.setCondition(parseExpression(OperatorType.LOWEST.getPrecedence()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!expectPeek(TokenType.RPARA)){
            return null;
        }
        statementsCount++;
        if(!expectPeek(TokenType.START)){
            return null;
        }
       
        nextToken();
        if(!curTokenIs(TokenType.IF)){
            errors.add("Invalid na token : linya " + Lexer.getLine());
            return null;
        }
        statementsCount++;

        try {
            exp.setConsequence(parseBlockStatement(curToken.getLiteral()));
            
        } catch (Exception e) {
           e.printStackTrace();
        }

        if(peekTokenIs(TokenType.ELSE)){
            nextToken();
            statementsCount++;
            while(peekTokenIs(TokenType.IF)){
                nextToken();
                if(!expectPeek(TokenType.LPARA)){
                    return null;
                }
                nextToken();
                try {
                    exp.addElseCondition(parseExpression(OperatorType.LOWEST.getPrecedence()));
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!expectPeek(TokenType.RPARA)){
                    return null;
                }
                statementsCount++;
                if(!expectPeek(TokenType.START)){
                    return null;
                }

                nextToken();
                if(!curTokenIs(TokenType.IF)){
                    errors.add("Invalid na token, linya " + Lexer.getLine());
                    return null;
                }
                statementsCount++;
                try {
                    exp.addElseConsequene(parseBlockStatement(curToken.getLiteral()));
                    statementsCount++;
                    
                } catch (Exception e) {
                   e.printStackTrace();
                }
                if(!peekTokenIs(TokenType.ELSE)){
                    return exp;
                }
                nextToken();

            }

            if(!expectPeek(TokenType.START)){
                return null;
            }
            nextToken();
            if(!curTokenIs(TokenType.IF)){
                errors.add("Invalid na token : linya " + Lexer.getLine());
                return null;
            }
            statementsCount++;
            
            try {
                exp.setAlternative(parseBlockStatement(curToken.getLiteral()));
                statementsCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executableStarted = true;
        ifStarted = false;
       return exp;

    }

    private BeginExpression parseBeginExpression(){
        if(ifStarted || forLoopStarted){
            errors.add("Dili pwede imo sugod");
            return null;
        }
        executableStarted = false;
        variableDeclarationStarted = false;
        statementsList = new HashMap<>();
        BeginExpression exp = new BeginExpression();
        exp.setToken(curToken);

        if(!curToken.getLiteral().equals("SUGOD")){
            return null;
        }
        if(hasStarted){
            errors.add("ERROR: Naay code human sa END CODE");
            return null;
        }
        hasStarted = true;

        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        try {
            exp.setBody(parseBlockStatement(ident.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        statementsCount++;

        return exp;
    }

    private ReturnStatement parseReturnStatement(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Sobra sa usa nga statement per line kay dili pwede : linya " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Magsugod dapat ug %s, pero = %s : linya %d", curToken.getLiteral(), Lexer.getLine()));
            return null;
        }
        ReturnStatement stmt = new ReturnStatement();
        stmt.setToken(curToken);
        nextToken();

        try{
            stmt.setReturnValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
        }catch(Exception e){
            e.printStackTrace();
        }
        tempStatementList.add(stmt);
        return stmt;
    }

    private DisplayExpression parseDisplayExpression(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Sobra sa usa nga statement per line kay dili pwede : linya " + Lexer.getLine());
            return null;
        }
        DisplayExpression exp = new DisplayExpression();
        if(!variableDeclarationStarted){
            errors.add("Executable code before variable declaration kay dili pwede : linya " + Lexer.getLine());
            return null;
        }
        executableStarted = true;
        exp.setToken(curToken);
        if(!expectPeek(TokenType.COLON)){
            return null;
        }
        nextToken();
        List<Object> all = new ArrayList<>();
        if(curTokenIs(TokenType.ESCAPE) || curTokenIs(TokenType.EOL)){
            all.add(curToken);
        }else{
            try {
                all.add(parseExpression(OperatorType.LOWEST.getPrecedence()));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        while(peekTokenIs(TokenType.CONCAT)){
            nextToken();
            all.add(curToken);
            nextToken();
            if(curTokenIs(TokenType.ESCAPE) || curTokenIs(TokenType.EOL) || curTokenIs(TokenType.STRING)){
                all.add(curToken);
                continue;
            }

            try {
                all.add(parseExpression(OperatorType.LOWEST.getPrecedence()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        exp.setBody(all);

        if(exp.getBody().isEmpty()){
            System.out.println("Walay sulod");
        }else {
            System.out.println(exp.getTokenLiteral());
        }
        statementsCount++;
        return exp;
        
    }

    private ScanExpression parseScanExpression(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Sobra sa usa nga statement per line kay dili pwede : linya  " + Lexer.getLine());
            return null;
        }
        if(!variableDeclarationStarted){
            errors.add("Executable code before variable declaration kay dili pwede : linya" + Lexer.getLine());
            return null;
        }
        executableStarted = true;
        ScanExpression exp = new ScanExpression();
        List<String> idents = new ArrayList<>();
        exp.setToken(curToken);
        if(!expectPeek(TokenType.COLON)){
            return null;
        }
        if(!expectPeek(TokenType.IDENT)){
            return null;
        }

        if(statementsList.containsKey(curToken.getLiteral())){
            idents.add(curToken.getLiteral());
        }else {
            errors.add(String.format("Identifier %s wala nabuhi", curToken.getLiteral()));
            return null;
        }

        while(peekTokenIs(TokenType.COMMA)){
            nextToken();
            if(!expectPeek(TokenType.IDENT)){
                return null;
            }
    
            if(statementsList.containsKey(curToken.getLiteral())){
                idents.add(curToken.getLiteral());
            }else {
                errors.add(String.format("Identifier %s wala nabuhi", curToken.getLiteral()));
                return null;
            }
        }
        List<Expression> expressions = startScanning();
        if(idents.size() != expressions.size()){
            errors.add("Kuwang imo ge butang para sa scan : linya " + Lexer.getLine());
            return null;
        }
        assignScan(idents, expressions);
        return exp;

    }

    private void assignScan(List<String> idents, List<Expression> expressions){
        for(int i = 0; i < idents.size(); i++){
            if(statementsList.containsKey(idents.get(i))){
                if(statementsList.get(idents.get(i)) instanceof IntStatement is){
                    is.setValue(expressions.get(i));
                }else if(statementsList.get(idents.get(i)) instanceof CharStatement is){
                    is.setValue(expressions.get(i));
                }else if(statementsList.get(idents.get(i)) instanceof BoolStatement is){
                    is.setValue(expressions.get(i));
                }else if(statementsList.get(idents.get(i)) instanceof FloatStatement is){
                    is.setValue(expressions.get(i));
                }
            }
        }
    }

    private List<Expression> startScanning(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        List<Expression> expressions = new ArrayList<>();
        String line = "";
        
        System.out.println("Input: ");
        System.out.print(" ");
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        
        
    
        Lexer l = new Lexer(line);
        Parser p = new Parser(l);

        try {
            expressions.add(p.parseExpression(OperatorType.LOWEST.getPrecedence()));
            while(p.peekTokenIs(TokenType.COMMA)){
                p.nextToken();
                p.nextToken();
                expressions.add(p.parseExpression(OperatorType.LOWEST.getPrecedence()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expressions;
    }

    private BlockStatement parseBlockStatement(String type) throws Exception{
        BlockStatement bs = new BlockStatement();
        bs.setToken(curToken);

        nextToken();
        
        while(!curTokenIs(TokenType.END)){
            if(curTokenIs(TokenType.EOF)){
                endCodeError();
                //return null;
            }
            List<Statement> stmt = parseStatement();
            if(stmt != null){
                for(Statement statement: stmt){
                    bs.addStatement(statement);
                }
            }
            
            nextToken();
        }
        
        nextToken();
        if(type.equals("KATAPUSAN")){

            hasEnded = true;
        }else if(type.equals("IF")){
            ifStarted = false;
        }

        return bs;
    }

    public Expression parseIntegerLiteral(){


        IntegerLiteral literal = new IntegerLiteral();

        literal.setToken(curToken);
        int value;
        try{
            value = Integer.parseInt(curToken.getLiteral(), 10);
        }catch(NumberFormatException e){
            String msg = String.format("Di pwede  e-parse %s nga integer", curToken.getLiteral());
            System.err.println(msg);
            errors.add(msg);
            return null;
        }

        literal.setValue(value);
        
        return literal;

    }

    public Expression parseFloatLiteral(){
        
        FloatLiteral literal = new FloatLiteral();
        literal.setToken(curToken);
        float value;
        try{
            value = Float.parseFloat(curToken.getLiteral());
        }catch(NumberFormatException e){
            String msg = String.format("Di pwede  e-parse %s nga float : line %d", curToken.getLiteral(), Lexer.getLine());
            System.err.println(msg);
            errors.add(msg);
            return null;
        }

        literal.setValue(value);
        
        return literal;

    }

    public Expression parseGroupedExpression(){
        nextToken();
        Expression exp;
        try {
            exp = parseExpression(OperatorType.LOWEST.getPrecedence());

            if(!expectPeek(TokenType.RPARA)){
                return null;
            }
    
            return exp;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
        
    }

    public ExpressionStatement parseExpressionStatement() throws Exception{
        ExpressionStatement stmt = new ExpressionStatement();
        stmt.setToken(curToken);

        stmt.setExpression(parseExpression(OperatorType.LOWEST.getPrecedence()));

        if(peekTokenIs(TokenType.EOL)){
            nextToken();
        }
       
        return stmt;
    }
   
    public Expression parseExpression(int precedence) throws Exception{
        PrefixParseFn prefix = prefixParseFns.get(curToken.getTokenType());
        if(prefix == null){
            noPrefixParseFNError(curToken.getTokenType());
            return null;
        }
        Expression leftExp = prefix.apply();

        while(!peekTokenIs(TokenType.EOL) && precedence < peekPrecedence() && !peekTokenIs(TokenType.CONCAT) && !peekTokenIs(TokenType.ESCAPE) && !peekTokenIs((TokenType.COMMA))){
            InfixParseFn infix = infixParseFns.get(peekToken.getTokenType());
            if(infix == null){

                return leftExp;
            }
            nextToken();
            leftExp = infix.apply(leftExp);
        }
        return leftExp;
    }

//    public Expression parseExpression(int precedence) throws Exception {
//        // Parse prefix expression
//        PrefixParseFn prefix = prefixParseFns.get(curToken.getTokenType());
//        if (prefix == null) {
//            noPrefixParseFNError(curToken.getTokenType());
//            return null;
//        }
//
//        Expression leftExp = prefix.apply();
//
//        // Parse infix or postfix expressions based on precedence
//        while (!peekTokenIs(TokenType.EOL)
//                && precedence < peekPrecedence()
//                && !peekTokenIs(TokenType.CONCAT)
//                && !peekTokenIs(TokenType.ESCAPE)
//                && !peekTokenIs(TokenType.COMMA)) {
//
//            InfixParseFn infix = infixParseFns.get(peekToken.getTokenType());
//            if (infix == null) {
//                return leftExp;
//            }
//
//            nextToken();
//            leftExp = infix.apply(leftExp);
//        }
//
//        return leftExp;
//    }


    public CharStatement parseCharStatement(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Dili pde daghan: linya " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Magsugod dapat ug %s, nakuha = %s : linya %d", curToken.getLiteral(), Lexer.getLine()));
            return null;
        }
        CharStatement stmt = new CharStatement();
        stmt.setToken(curToken);

        if (!expectPeek(TokenType.IDENT)){
            return null;
        }

        if(isReservedWord(curToken.getLiteral())){
            reservedWordsError(curToken.getLiteral());
            return null;
        }
        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        if(statementsList.containsKey(ident.getValue())){
            errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
            return null;
        }
        stmt.setName(ident);

        if(peekTokenIs(TokenType.ASSIGN)){
            nextToken();
            nextToken();
            if(curTokenIs(TokenType.IDENT) && peekTokenIs(TokenType.ASSIGN)){
                List<CharStatement> temp = new ArrayList<>();
                temp.add(stmt);
                while(peekTokenIs(TokenType.ASSIGN)){
                    CharStatement is = new CharStatement();
                    is.setToken(curToken);
                    Identifier tempIdent = new Identifier(curToken, curToken.getLiteral());
                    if(statementsList.containsKey(ident.getValue())){
                        errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
                        return null;
                    }
                    is.setName(tempIdent);
                    temp.add(is);
                    nextToken();
                    nextToken();
                }
                try{

                    Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                    for(CharStatement x: temp){
                        x.setValue(value);
                        tempStatementList.add(x);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            try {
                Expression result = parseExpression(OperatorType.LOWEST.getPrecedence());
                stmt.setValue(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseCharStatement();
        }


        statementsList.put(ident.getValue(), stmt);
        statementsCount++;
        if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseCharStatement();
        }
        tempStatementList.add(stmt);
        return stmt;
    }

    private HashStatement parseHashStatement(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Dili pde daghan : Linya " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Mag sugod dapat ug %s, got = %s : Linya ", curToken.getLiteral()));
            return null;
        }
        HashStatement stmt = new HashStatement();
        stmt.setToken(curToken);

        if(!expectPeek(TokenType.IDENT)){
            return null;
        }

        if(isReservedWord(curToken.getLiteral())){
            reservedWordsError(curToken.getLiteral());
            return null;
        }

        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        if(statementsList.containsKey(ident.getValue())){
            errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
            return null;
        }

        stmt.setIdent(ident);

        if(peekTokenIs(TokenType.ASSIGN)){
            nextToken();
            nextToken();
            try {
                stmt.setValue(parseExpression(OperatorType.LOWEST.getPrecedence()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        tempStatementList.add(stmt);
        statementsList.put(ident.getValue(), stmt);
        statementsCount++;
        return stmt;


    }

    public IntStatement parseIntStatement(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("DI pwede daghan : line " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Magsugod dapat %s, got = %s : linya %d", curToken.getLiteral(), Lexer.getLine()));
            return null;
        }
        IntStatement stmt = new IntStatement();
        stmt.setToken(curToken);

        if (!expectPeek(TokenType.IDENT)){
            return null;
        }
            
        if(isReservedWord(curToken.getLiteral())){
            reservedWordsError(curToken.getLiteral());
            return null;
        }
        
        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        if(statementsList.containsKey(ident.getValue())){
            errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
            return null;
        }

        stmt.setName(ident);

        if(peekTokenIs(TokenType.ASSIGN)){
            nextToken();
            nextToken();
            if(curTokenIs(TokenType.IDENT) && peekTokenIs(TokenType.ASSIGN)){
                List<IntStatement> temp = new ArrayList<>();
                temp.add(stmt);
                while(peekTokenIs(TokenType.ASSIGN)){
                    IntStatement is = new IntStatement();
                    is.setToken(curToken);
                    
                    Identifier tempIdent = new Identifier(curToken, curToken.getLiteral());
                    if(statementsList.containsKey(ident.getValue())){
                        errors.add(String.format("Identifier %s kay gigamit na", tempIdent.getValue()));
                        return null;
                    }
                    is.setName(tempIdent);
                    
                    temp.add(is);
                    nextToken();
                    nextToken();
                }
                try{

                    Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                    for(IntStatement x: temp){
                        x.setValue(value);
                        tempStatementList.add(x);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            
            try {
                Expression result = parseExpression(OperatorType.LOWEST.getPrecedence());
                
                stmt.setValue(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
           
        }else if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseIntStatement();
        }
        

        statementsList.put(ident.getValue(), stmt);
        statementsCount++;
        tempStatementList.add(stmt);

        if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseIntStatement();
        }
        tempStatementList.add(stmt);
        

       return stmt;
    }

    public FloatStatement parseFloatStatement(){
        if(Lexer.getLine() -1 < statementsCount){
            errors.add("Di pwede daghan : line " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Mag sugod dapat %s, got = %s : linya %d", curToken.getLiteral(), Lexer.getLine()));
            return null;
        }
        FloatStatement stmt = new FloatStatement();
        stmt.setToken(curToken);

        if (!expectPeek(TokenType.IDENT)){
            return null;
        }
            
        if(isReservedWord(curToken.getLiteral())){
            reservedWordsError(curToken.getLiteral());
            return null;
        }
        
        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        if(statementsList.containsKey(ident.getValue())){
            errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
            return null;
        }
        stmt.setName(ident);

        if(peekTokenIs(TokenType.ASSIGN)){
            nextToken();
            nextToken();
            if(curTokenIs(TokenType.IDENT) && peekTokenIs(TokenType.ASSIGN)){
                List<FloatStatement> temp = new ArrayList<>();
                temp.add(stmt);
                while(peekTokenIs(TokenType.ASSIGN)){
                    FloatStatement is = new FloatStatement();
                    is.setToken(curToken);
                    Identifier tempIdent = new Identifier(curToken, curToken.getLiteral());
                    if(statementsList.containsKey(ident.getValue())){
                        errors.add(String.format("Identifier %s kay gigamit na", tempIdent.getValue()));
                        return null;
                    }
                    temp.add(is);
                    nextToken();
                    nextToken();
                }
                try{

                    Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                    for(FloatStatement x: temp){
                        x.setValue(value);
                        tempStatementList.add(x);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            
            try {
                Expression result = parseExpression(OperatorType.LOWEST.getPrecedence());
                stmt.setValue(result);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseFloatStatement();

        }
        
        statementsList.put(ident.getValue(), stmt);
        statementsCount++;
        if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseFloatStatement();
        }
        tempStatementList.add(stmt);

        return stmt;

    }
    
    public BoolStatement parseBoolStatement(){
        if(Lexer.getLine() - 1 < statementsCount){
            errors.add("Di pwede daghan. Linya: " + Lexer.getLine());
            return null;
        }
        if(!hasStarted){
            errors.add(String.format("Magsugod dapat ug %s"));
            return null;
        }
        BoolStatement stmt = new BoolStatement();
        stmt.setToken(curToken);

        if (!expectPeek(TokenType.IDENT)){
            return null;
        }

        if(isReservedWord(curToken.getLiteral())){
            reservedWordsError(curToken.getLiteral());
            return null;
        }
        Identifier ident = new Identifier(curToken, curToken.getLiteral());
        if(statementsList.containsKey(ident.getValue())){
            errors.add(String.format("Identifier %s kay gigamit na", ident.getValue()));
            return null;
        }
        stmt.setName(ident);

        if(peekTokenIs(TokenType.ASSIGN)){
            nextToken();
            nextToken();
            if(curTokenIs(TokenType.IDENT) && peekTokenIs(TokenType.ASSIGN)){
                List<BoolStatement> temp = new ArrayList<>();
                temp.add(stmt);
                while(peekTokenIs(TokenType.ASSIGN)){
                    BoolStatement is = new BoolStatement();
                    is.setToken(curToken);
                    Identifier tempIdent = new Identifier(curToken, curToken.getLiteral());
                    if(statementsList.containsKey(ident.getValue())){
                        errors.add(String.format("Identifier %s kay gigamit na", tempIdent.getValue()));
                        return null;
                    }
                    temp.add(is);
                    nextToken();
                    nextToken();
                }
                try{

                    Expression value = parseExpression(OperatorType.LOWEST.getPrecedence());
                    for(BoolStatement x: temp){
                        x.setValue(value);
                        tempStatementList.add(x);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        
            try {
                Expression result = parseExpression(OperatorType.LOWEST.getPrecedence());
                
                stmt.setValue(result);
            } catch (Exception e) {
                e.printStackTrace();
                }
        }else if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseBoolStatement();
        }
        

        statementsList.put(ident.getValue(), stmt);
        statementsCount++;

        if(peekTokenIs(TokenType.COMMA)){
            nextToken();
            statementsCount--;
            parseBoolStatement();
        }

        tempStatementList.add(stmt);
        return stmt;
    }

    public Expression parsePrefixExpression(){
       
        PrefixExpression expression = new PrefixExpression();
        expression.setToken(curToken);
        expression.setOperator(curToken.getLiteral());

        nextToken();
        
        try {
            expression.setRight(parseExpression(OperatorType.PREFIX.getPrecedence()));
        } catch (Exception e) {
            e.printStackTrace();
        }
       

        return expression;
    }

    public Expression parsePostfixExpression() throws Exception {
        // Start by parsing the left operand (e.g., `ctr`)
        Expression left = parseExpression(OperatorType.LOWEST.getPrecedence());

        // Create the PostfixExpression object
        PostfixExpression expression = new PostfixExpression();
        expression.setToken(curToken);  // Set the current token (e.g., `++`)
        expression.setLeft(left);  // Set the left operand (e.g., `ctr`)

        // Move to the next token (to process any following operator)
        nextToken();

        // Return the PostfixExpression
        return expression;
    }

    public Expression parseInfixExpression(Expression left){
        InfixExpression expression = new InfixExpression();
        expression.setToken(curToken);
        expression.setOperator(curToken.getLiteral());
        expression.setLeft(left);
        
        int precedence = curPrecedence();
        nextToken();
        try {
            expression.setRight(parseExpression(precedence));
        } catch (Exception ignored) {
            
        }
        return expression;
    }

    private AlangSaExpression parseAlangSaExpression() throws Exception {
        executableStarted = true;
        forLoopStarted = true;
        String str = "";
        int start,end;
        AlangSaExpression alangSaExpr = new AlangSaExpression();
        alangSaExpr.setToken(curToken);

        if (!expectPeek(TokenType.SA)) {
            return null;
        }

        if (!expectPeek(TokenType.LPARA)) {
            return null;
        }
        nextToken(); // Move inside the parentheses

        // Parse initialization
        List<Statement> initializationList = parseStatement();
        if (initializationList != null && !initializationList.isEmpty()) {
            alangSaExpr.setInitialization(initializationList.getFirst()); // fix here
        }
        //start
        start = Integer.parseInt(curToken.getLiteral());
        if (!expectPeek(TokenType.COMMA)) {
            System.out.println(curToken.getLiteral());
            return null;
        }
        nextToken(); // move to condition
        if(peekTokenIs(TokenType.LESS)){
            str = "<";
        }
        else if(peekTokenIs(TokenType.LESSEQ)){
            str = "<=";
        }
        else if(peekTokenIs(TokenType.GREAT)){
            str = ">";
        }
        else if(peekTokenIs(TokenType.GREATEQ)){
            str = ">=";
        }
        else if(peekTokenIs(TokenType.EQUAL)){
            str = "==";
        }
        else if(peekTokenIs(TokenType.NOTEQUAL)){
            str = "<>";
        }


        Expression condition = parseExpression(0);
        alangSaExpr.setCondition(condition);

        System.out.println("->" + curToken.getLiteral());
        end = Integer.parseInt(curToken.getLiteral());
        if (!expectPeek(TokenType.COMMA)) {
            return null;
        }

        nextToken(); // move to update
        List<Statement> updateList = parseStatement();
        System.out.println("->" + curToken.getLiteral());
        if (updateList != null && !updateList.isEmpty()) {
            alangSaExpr.setUpdate(updateList.getFirst()); // fix here
            nextToken();
        }
        System.out.println("->" + curToken.getLiteral());

        if (!expectPeek(TokenType.RPARA)) {
            return null;
        }

        if (!expectPeek(TokenType.PUNDOK)) {
            return null;
        }

        nextToken(); // Move into PUNDOK block
        BlockStatement content = parsePundokExpression();
        alangSaExpr.setContent(content);


        return alangSaExpr;

    }



    private BlockStatement parsePundokExpression() {
        BlockStatement block = new BlockStatement();
        block.setToken(curToken); // '{' token

        List<Statement> stmt = new ArrayList<>();
        nextToken(); // move to the first token inside block

        while (!curTokenIs(TokenType.RBRACE) && !curTokenIs(TokenType.END) && !curTokenIs(TokenType.EOF)) {
            Statement statement = null;

            // Parse Display (IPAKITA)
            if (curTokenIs(TokenType.DISPLAY)) {
                System.out.println(">>>>" + curToken);
            }
            else if (curTokenIs(TokenType.COLON)) {
                System.out.println(">>>>" + curToken);
            }
            else if (curTokenIs(TokenType.IDENT)) {
                System.out.println(">>>>" + curToken);
            }
            else if (curTokenIs(TokenType.INTEGER)) {
                System.out.println(">>>>" + curToken);
            }
            else {
                nextToken(); // Move to the next token
                continue; // Skip the current token and move forward
            }
            nextToken(); // Move to the next token after processing the current one
        }
        block.setStatements(stmt); // Set the list of statements to the block
        return block;
    }


    private boolean curTokenIs(TokenType t){
        return curToken.getTokenType() == t;
    }
    
    private boolean peekTokenIs(TokenType t){
        return peekToken.getTokenType() == t;
    }

    private boolean expectPeek(TokenType t){
        if(peekTokenIs(t)){
            nextToken();
            return true;
        }else {
            peekError(t);
            return false;
        }
    }

    private int curPrecedence(){
        Integer p = infixPrecedences.get(curToken.getTokenType());
        if(p != null){
            return p;
        }

        return OperatorType.LOWEST.getPrecedence();
    }

    private int peekPrecedence(){
        Integer p = infixPrecedences.get(peekToken.getTokenType());
        if(p != null){
            return p;
        }
        return OperatorType.LOWEST.getPrecedence();

    }
    
    public List<String> getErrors(){
        return errors;
    }

    public void checkParserErrors(){
        errors = getErrors();
        if(errors.isEmpty()){
            return;
        }
        
        StringBuilder message = new StringBuilder(String.format("Parser kay naay %d errors:\n", errors.size()));
        for(String msg : errors){
            message.append("parser error: ").append(msg).append("\n");
        }

        Assert.fail(message.toString());
        

    }

    private boolean isReservedWord(String ident){
        return reservedWords.contains(ident);
    }

    public void noPrefixParseFNError(TokenType ignoredT){
        String msg = String.format("Dili pwede na  token  %s sa linya: %d noPrefixParseError", curToken.getLiteral(),Lexer.getLine());
        errors.add(msg);
    }

    private void peekError(TokenType ignoredT){
        String msg = String.format("%d: nag expected sya nga %s peekError" + curToken,Lexer.getLine(), peekToken.getTokenType());
        errors.add(msg);
    }

    private void identifierMismatchError(String expected, String ignoredGot){
        String msg = String.format("Identifer mismatch: %s",expected);
        errors.add(msg);
        
    }   

    private void reservedWordsError(String ident){
        String msg = String.format("dili pwede kay identifier, %s reservedWordsError", ident);
        errors.add(msg);
    }

    private void endCodeError(){
        String msg = String.format("nag expected sya nga ug token %s %s endCodeError" +curToken, TokenType.END, curToken.getTokenType());
        errors.add(msg);
    }

    private void typeConversionError(TokenType t){
        String msg = String.format("Type conversion error %s %s", t, curToken.getTokenType() );
        errors.add(msg);
    }

    private void typeConversionError(String t){
        String msg = String.format("Type conversion error %s %s", t, curToken.getTokenType() );
        errors.add(msg);
    }

    public void noPostfixParseFNError(TokenType ignoredT){
        String msg = String.format("Invalid token  %s at line %d noPostfixParseError", curToken.getLiteral(),Lexer.getLine());
        errors.add(msg);
    }

    public void registerPostfix(TokenType tokenType, PrefixParseFn fn){
        postfixParseFns.put(tokenType, fn);
    }

    public void registerPrefix(TokenType tokenType, PrefixParseFn fn){
        prefixParseFns.put(tokenType, fn);
    }

    public void registerInfix(TokenType tokenType, InfixParseFn fn){
        infixParseFns.put(tokenType, fn);
    }
    
    public int getStatementsCount(){
        return statementsCount;
    }
}
