package evaluator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import ast.Program;
import lexer.Lexer;
import object.Environment;
import object.IntegerObject;
import object.Object;
import parser.Parser;

public class EvaluatorTest {
    
    public static void TestEvalIntegerExpression(){
        List<IntegerTestCase> integerTests = new ArrayList<>();
        integerTests.add(new IntegerTestCase("5", 5));
        integerTests.add(new IntegerTestCase("10", 10));


        for(IntegerTestCase it: integerTests){
            Object evaluated = testEval(it.getInput());
            testIntegerObject(evaluated, it.getExpected());
           
        }
        
    }

    private static Object testEval(String input){
        Lexer l = new Lexer(input);
        Parser p = new Parser(l);
        Program program = new Program();
        Environment env = new Environment();
        try {
            program = p.ParseProgram();
        } catch (Exception e) {
        
            e.printStackTrace();
        }
        
        
        return Evaluator.eval(program, env);
    }

    private static boolean testIntegerObject(Object obj, int expected){
        assertTrue(obj instanceof IntegerObject);
        IntegerObject result = (IntegerObject)obj;

        assertEquals(result.getValue(), expected);

        return true;
    }

    private static class IntegerTestCase{
        private String input;
        private int expected;

        public IntegerTestCase(String input, int expected) {
            this.input = input;
            this.expected = expected;
        }

        public String getInput() {
            return input;
        }

        public int getExpected() {
            return expected;
        }

        
        
    }
       
 }

