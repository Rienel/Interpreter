package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ast.BoolStatement;
import ast.CharStatement;
import ast.Expression;
import ast.ExpressionStatement;
import ast.Identifier;
import ast.InfixExpression;
import ast.IntegerLiteral;
import ast.Node;
import ast.PrefixExpression;
import ast.Program;
import ast.Statement;
import lexer.Lexer;



public class ParserTest {



    @org.junit.Test
    public static boolean testIndentifier(Expression exp, String value){
        assertTrue(exp instanceof Identifier);
        Identifier ident = (Identifier) exp;

        assertEquals(ident.getValue(), value);

        assertEquals(ident.getTokenLiteral(), value);

        return true;
    }

    @org.junit.Test
    public static boolean testLiteralExpression(Expression exp, Object expected){
        if(expected instanceof Integer){
            return testIndentifier(exp, Integer.toString((int)expected));
        }else if(expected instanceof String){
            return testIndentifier(exp, (String)expected);
        }
        System.err.println(String.format("type of exp not handled. got = %T", exp));
        return false;
    }

    

    @org.junit.Test
    public static boolean testInfixExpression(Expression exp, Object left, String operator, Object right){
        assertTrue(exp instanceof InfixExpression);
        InfixExpression opExp = (InfixExpression) exp;

        if(!testLiteralExpression(opExp.getLeft(), left)){
            return false;
        }

        assertEquals(opExp.getOperator(), operator);

        if(!testLiteralExpression(opExp.getRight(), right)){
            return false;
        }
        return true;
    }



    @org.junit.Test
    public static void testIdentifierExpression() throws Exception{
        String input = "imissher$";
        
        Lexer l = new Lexer(input);
        Parser p = new Parser(l);
        Program program = p.ParseProgram();
        p.checkParserErrors();
        
        assertEquals(1, program.getStatements().size());
        
        Statement stmt = program.getStatements().get(0);
        assertTrue(stmt instanceof ExpressionStatement);
        
        ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
        Expression expression = expressionStatement.getExpression();
        assertTrue(expression instanceof Identifier);
        
        Identifier identifier = (Identifier) expression;
        assertEquals("imissher", identifier.getValue());
        assertEquals("imissher", identifier.getTokenLiteral());
    }

    @org.junit.Test
    public static void testPrecedenceParsing() throws Exception{
        List<PrecedenceTestCase> precedenceTests = new ArrayList<>();
        precedenceTests.add(new PrecedenceTestCase("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"));

        for (PrecedenceTestCase ptc: precedenceTests){
            Lexer lexer = new Lexer(ptc.getInput());
            Parser parser = new Parser(lexer);
            Program program = parser.ParseProgram();
            parser.checkParserErrors();

            String actual = program.string().toString();
            
            assertEquals(ptc.expected, actual);


            System.out.println(String.format("Input Expression: %s", ptc.getInput()));
            System.out.println(String.format("Parsing Result: %s", actual));

        }
    }

    
    @org.junit.Test
    public static void testParsingPrefixExpressions() throws Exception{
        List<PrefixTestCase> prefixTests = new ArrayList<>();
        prefixTests.add(new PrefixTestCase("-15", "-", 15));
        
        for (PrefixTestCase pt : prefixTests){
            Lexer lexer = new Lexer(pt.getInput());
            Parser p = new Parser(lexer);
            Program program = p.ParseProgram();
            p.checkParserErrors();
            
            assertEquals(1, program.getStatements().size());
            
            Statement stmt = program.getStatements().get(0);
            assertTrue(stmt instanceof ExpressionStatement);
            
            ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
            Expression expression = expressionStatement.getExpression();
            assertTrue(expression instanceof PrefixExpression);
            
            PrefixExpression prexp = (PrefixExpression) expression;
            assertEquals(pt.getOperator(), prexp.getOperator());
            
            
        }
        
    }
    
    @org.junit.Test
    public static void testIntegerLiteralExpression() throws Exception{
        String input = "5$";

        Lexer l = new Lexer(input);
        Parser p = new Parser(l);
        Program program = p.ParseProgram();
        p.checkParserErrors();

        assertEquals(1, program.getStatements().size());

        Statement stmt = program.getStatements().get(0);
        assertTrue(stmt instanceof ExpressionStatement);
        
        ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
        Expression expression = expressionStatement.getExpression();
        assertTrue(expression instanceof IntegerLiteral);
        
        IntegerLiteral identifier = (IntegerLiteral) expression;
        assertEquals(5, identifier.getValue());
        assertEquals("5", identifier.getTokenLiteral());
    }
    @org.junit.Test
    public static void testParsingInfixExpressions() throws Exception{
        List<InfixTestCase> infixTests = new ArrayList<>();
        infixTests.add(new InfixTestCase("5 + 5",5, "+", 5));
        infixTests.add(new InfixTestCase("5 - 5",5, "-", 5));
        infixTests.add(new InfixTestCase("5 * 5",5, "*", 5));
        infixTests.add(new InfixTestCase("5 / 5",5, "/", 5));
        infixTests.add(new InfixTestCase("5 % 5",5, "%", 5));
        infixTests.add(new InfixTestCase("5 < 5",5, "<", 5));
        infixTests.add(new InfixTestCase("5 > 5",5, ">", 5));
        infixTests.add(new InfixTestCase("5 <= 5",5, "<=", 5));
        infixTests.add(new InfixTestCase("5 >= 5",5, ">=", 5));
        infixTests.add(new InfixTestCase("5 == 5",5, "==", 5));
        infixTests.add(new InfixTestCase("5 <> 5",5, "<>", 5));
        

        for (InfixTestCase it : infixTests){
            Lexer lexer = new Lexer(it.getInput());
            Parser p = new Parser(lexer);
            Program program = p.ParseProgram();
            p.checkParserErrors();

            assertEquals(1, program.getStatements().size());

            Statement stmt = program.getStatements().get(0);
            assertTrue(stmt instanceof ExpressionStatement);

            ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
            Expression expression = expressionStatement.getExpression();
            assertTrue(expression instanceof InfixExpression);

            InfixExpression exp = (InfixExpression) expression;
            
            if(!testIntegerLiteral(exp.getLeft(), it.getLeftValue())){
                return;
            }

            assertEquals(it.getOperator(), exp.getOperator());

            if(!testIntegerLiteral(exp.getRight(), it.getRightValue())){
                return;
            }

            
        }
        

    }

    @org.junit.Test
    public static boolean testIntegerLiteral(Expression ex, int value){
        IntegerLiteral integer = (IntegerLiteral) ex;
        assertTrue(integer instanceof IntegerLiteral);

        assertEquals(integer.getValue(), value);
        assertEquals(integer.getTokenLiteral(), Integer.toString(value));

        return true;
    }


    @org.junit.Test
    public static void testStatements() throws Exception{
        List<StatementsTestCase> statementsTests = new ArrayList<>();{
            new StatementsTestCase("CHAR first = 'x'", "first", 'x');
            new StatementsTestCase("CHAR second = 'y'", "second", 'x');
            new StatementsTestCase("CHAR third = 'z'", "third", 'x');
        }
            
    }


    
    private static boolean testStatement(Statement s, TestCase tc){
        if(!(s.getTokenLiteral().equals("BOOL"))){
            System.err.println("s.TokenLiteral not 'BOOL' . got = " + ( s.getTokenLiteral()));
            return false;
        }else{
            System.out.println(String.format("Token Literal = %s ", s.getTokenLiteral()));
        }

        if(!(s instanceof BoolStatement)){
            System.err.println("S NOT *INTStatement. got = " + s.getClass().getName());
            return false;
        }else {
            System.out.println("enter");
            System.out.println(String.format("%s is an instance of INTStatement ", s.getClass().getName()));
        }

        BoolStatement charStmt = (BoolStatement)s;
        if(!charStmt.getName().getValue().equals(tc.getExpectedIdentifier())){
            System.err.println("intStmt.Value not '" + tc.getExpectedIdentifier() + "'. got = " + charStmt.getValue());
            return false;
        }else  {
            System.out.println(String.format("Value = ", charStmt.getValue()));
        }

        if(!charStmt.getName().getTokenLiteral().equals(tc.expectedIdentifier)){
            System.err.println("intStmt.Value not '" + tc.getExpectedIdentifier() + "'. got = " + charStmt.getTokenLiteral());
            return false;
        }else {
            System.out.println(String.format("Literal = ", charStmt.getTokenLiteral()));
        }
        return true;
    }


    private static class PrefixTestCase{
        private String input;
        private String operator;
        private int integerValue;

        public PrefixTestCase(String input, String operator, int integerValue) {
            this.input = input;
            this.operator = operator;
            this.integerValue = integerValue;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public int getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(int integerValue) {
            this.integerValue = integerValue;
        }

    }
    private static class PrecedenceTestCase{
        private String input;
        private String expected;
        public PrecedenceTestCase(String input, String expected) {
            this.input = input;
            this.expected = expected;
        }
        public String getInput() {
            return input;
        }
        public void setInput(String input) {
            this.input = input;
        }
        public String getExpected() {
            return expected;
        }
        public void setExpected(String expected) {
            this.expected = expected;
        }
        
        
    }
        
    private static class InfixTestCase{
        private String input;
        private int leftValue;
        private String operator;
        private int rightValue;
        
        public InfixTestCase(String input, int leftValue, String operator, int rightValue) {
            this.input = input;
            this.leftValue = leftValue;
            this.operator = operator;
            this.rightValue = rightValue;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public int getLeftValue() {
            return leftValue;
        }

        public void setLeftValue(int leftValue) {
            this.leftValue = leftValue;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public int getRightValue() {
            return rightValue;
        }

        public void setRightValue(int rightValue) {
            this.rightValue = rightValue;
        }

       

        
        
    }


    private static class TestCase {
        private String expectedIdentifier;

        public TestCase(String expectedIdentifier){
            this.expectedIdentifier = expectedIdentifier;
        }

        public String getExpectedIdentifier() {
            return expectedIdentifier;
        }

        public void setExpectedIdentifier(String expectedIdentifier) {
            this.expectedIdentifier = expectedIdentifier;
        }

        
    }
    private static class StatementsTestCase {
        private String input;
        private String expectedIdentifier;
        private Object expectedValue;
        
        public StatementsTestCase(String input, String expectedIdentifier, Object expectedValue) {
            this.input = input;
            this.expectedIdentifier = expectedIdentifier;
            this.expectedValue = expectedValue;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getExpectedIdentifier() {
            return expectedIdentifier;
        }

        public void setExpectedIdentifier(String expectedIdentifier) {
            this.expectedIdentifier = expectedIdentifier;
        }

        public Object getExpectedValue() {
            return expectedValue;
        }

        public void setExpectedValue(Object expectedValue) {
            this.expectedValue = expectedValue;
        }

        

        
    }
}
