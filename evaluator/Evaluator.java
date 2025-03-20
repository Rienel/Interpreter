package evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.BeginExpression;
import ast.BlockStatement;
import ast.BoolStatement;
import ast.BooleanExpression;
import ast.CharStatement;
import ast.CharacterExpression;
import ast.DisplayExpression;
import ast.Expression;
import ast.ExpressionStatement;
import ast.FloatLiteral;
import ast.FloatStatement;
import ast.HashLiteral;
import ast.HashStatement;
import ast.Identifier;
import ast.IfExpression;
import ast.IndexExpression;
import ast.InfixExpression;
import ast.IntStatement;
import ast.IntegerLiteral;
import ast.Node;
import ast.PrefixExpression;
import ast.Program;
import ast.ReturnStatement;
import ast.Statement;
import ast.StringValue;
import ast.WhileExpression;
import object.BooleanObject;
import object.CharacterObject;
import object.Environment;
import object.Error;
import object.ExtendedEnvironment;
import object.FloatObject;
import object.HashKey;
import object.HashObject;
import object.HashPair;
import object.IntegerObject;
import object.NullObject;
import object.Object;
import object.ObjectType;
import object.ReturnValueObject;
import object.StringObject;
import object.Structure;
import token.Token;
import token.TokenType;

public class Evaluator {
    private static final BooleanObject TRUE = new BooleanObject(true);
    private static final BooleanObject FALSE = new BooleanObject(false);
    private static final NullObject NULL = new NullObject();
    private static int errorCount = 0;
    public static int displayCount = 0;

    

    public static Object eval(Node node, Environment env){

        if(node instanceof Program){
            Program program = (Program)node;
            return evalStatements(program.getStatements(), env);

        }else if(node instanceof BeginExpression){
            BeginExpression exp = (BeginExpression)node;
            BlockStatement body = exp.getBody();
        
            return eval(body, env);
            

        }else if(node instanceof ExpressionStatement){
            ExpressionStatement exp = (ExpressionStatement)node;
            
            return eval(exp.getExpression(), env);

        }else if(node instanceof IntegerLiteral){
            
            IntegerLiteral inlit = (IntegerLiteral) node;
            return new IntegerObject(inlit.getValue());

        }else if(node instanceof FloatLiteral){
            FloatLiteral inlit = (FloatLiteral) node;
            return new FloatObject(inlit.getValue());

        }else if(node instanceof HashLiteral){
            HashLiteral hash = (HashLiteral)node;
            return evalHashLiteral(hash, env);

        }else if(node instanceof CharacterExpression){
            CharacterExpression ce = (CharacterExpression)node;
            return new CharacterObject(ce.getValue());
        }else if(node instanceof BooleanExpression){
            BooleanExpression be = (BooleanExpression)node;
            return nativeBoolToBooleanObject(be.getValue());
        }else if(node instanceof PrefixExpression){
            PrefixExpression pe = (PrefixExpression)node;
            Object right = eval(pe.getRight(),env);
            if(isError(right)){
                return right;
            }
            return evalPrefixExpression(pe.getOperator(), right);
        }else if(node instanceof IfExpression){
            IfExpression exp = (IfExpression)node;
            return evalIfExpression(exp,env);
        }else if(node instanceof WhileExpression){
            WhileExpression exp = (WhileExpression)node;
            return evalWhileExpression(exp,env);
        }else if(node instanceof InfixExpression){
            InfixExpression ie = (InfixExpression)node;
            Object left = eval(ie.getLeft(), env);
            
            if(isError(left)){
               
                return left;
            }

            Object right = eval(ie.getRight() ,env);
            if(isError(right)){
                return right;
            }

            return evalInfixExpression(ie.getOperator(), left, right);
        }else if(node instanceof HashStatement){
            HashStatement hs = (HashStatement)node;

            if(hs.getValue() == null){
                env.set(hs.getIdent().getValue(), null);
                return NULL;
            }

            Object val = eval(hs.getValue(), env);
            if(isError(val)){
                return val;
            }

            if(!(val instanceof HashObject)){
                return newError("Type Conversion Error, expect = %s, got = %s", ObjectType.INTEGER_OBJ, val.type());
            }
            env.set(hs.getIdent().getValue(), val);

        }else if(node instanceof IntStatement){
            IntStatement is = (IntStatement)node;
          
            if(is.getValue() == null){
                env.set(is.getName().getValue(), null);
                return NULL;
            }
            Object val = eval(is.getValue(), env);
            if(isError(val)){
                return val;
            }
            if(!(val instanceof IntegerObject)){
                return newError("Type Conversion Error, expected = %s, got = %s ", ObjectType.INTEGER_OBJ, val.type());
            }
            // System.out.println(is.getName().getValue());
            env.set(is.getName().getValue(), val);
            
        }else if(node instanceof FloatStatement){
            FloatStatement is = (FloatStatement)node;
            if(is.getValue() == null){
                env.set(is.getName().getValue(), null);
                return NULL;
            }
            Object val = eval(is.getValue(), env);
            if(isError(val)){
                return val;
            }
            if(!(val instanceof FloatObject) && !(val instanceof IntegerObject)){
                return newError("Type Conversion Error, expected = %s, got = %s ", ObjectType.FLOAT_OBJ, val.type());
            }
            env.set(is.getName().getValue(), val);
            
            
        }else if(node instanceof CharStatement){
            CharStatement cs = (CharStatement)node;
            if(cs.getValue() == null){
                env.set(cs.getName().getValue(), null);
                return NULL;
            }
            Object val = eval(cs.getValue(), env);
            if(isError(val)){
                return val;
            }
            if(!(val instanceof CharacterObject)){
                return newError("Type Conversion Error, expected = %s, got = %s ", ObjectType.CHARACTER_OBJ, val.type());
            }
            env.set(cs.getName().getValue(), val);
            

        }else if(node instanceof BoolStatement){
            BoolStatement bs = (BoolStatement)node;
            if(bs.getValue() == null){
                env.set(bs.getName().getValue(), null);
                return NULL;
            }
            Object val = eval(bs.getValue(), env);
            if(isError(val)){
                return val;
            }
            if(!(val instanceof BooleanObject)){
                return newError("Type Conversion Error, expected = %s, got = %s ", ObjectType.CHARACTER_OBJ, val.type());
            }
            env.set(bs.getName().getValue(), val);
            

        }else if(node instanceof ReturnStatement){
            ReturnStatement rs = (ReturnStatement) node;
            Object val = eval(rs.getReturnValue(), env);
            return new ReturnValueObject(val);

        }else if(node instanceof StringValue){
            StringValue sv = (StringValue)node;
            return evalString(sv, env);
        }else if(node instanceof Identifier){
            Identifier id = (Identifier) node;
            return evalIdentifier(id, env);
        }else if(node instanceof BlockStatement){
            BlockStatement bs = (BlockStatement) node;
            return evalStatements(bs.getStatements(), env);
        }else if(node instanceof IndexExpression){
            IndexExpression ie = (IndexExpression)node;
            Object left = eval(ie.getLeft(), env);
            if(isError(left)){
                return left;
            }
            Object index = eval(ie.getIndex(), env);
            if(isError(index)){
                return index;
            }
            return evalIndexExpression(left, index);

        }else if(node instanceof DisplayExpression){
            DisplayExpression de = (DisplayExpression)node;
            displayCount++;
            for(java.lang.Object obj: de.getBody()){
                if(obj instanceof Expression){
                    Expression exp = (Expression)obj;
                    Object object = eval(exp, env);
                    
                    System.out.print(object.inspect());
                }else{
                    Token token = (Token)obj;
                    
                    if(token == null){
                        return newError("Invalid token", null);
                    }
                    if(token.getTokenType() == TokenType.ESCAPE || token.getTokenType().equals(TokenType.STRING)){
                        System.out.print(token.getLiteral());
                    }else if(token.getTokenType() == TokenType.EOL){
                        System.out.print("\n");
                    }
                }
            }
            
            
        }
        return NULL;
    }

    
   
    private static Object evalStatements(List<Statement> stmts, Environment env){
        Object result = null;
        for(Statement stmt: stmts){

            result = eval(stmt, env);
            
            if(result instanceof ReturnValueObject){
                ReturnValueObject rv = (ReturnValueObject) result;
                return rv.getValue();
            }
            if(result !=  null &&result.type().equals(ObjectType.ERROR_OBJ)){
                return result;
            }
        }
        return result;
    }





    private static List<Object> evalExpressions(List<Expression> exps, Environment env ){
        List<Object> result = new ArrayList<>();

        for(Expression exp: exps){
            Object evaluated = eval(exp, env);
            if(isError(evaluated)){
                List<Object> temp = new ArrayList<>();
                temp.add(evaluated);
                return temp;
            }
            result.add(evaluated);
        }
        return result;
    }
    private static Object evalIfExpression(IfExpression expression, Environment env){
        Object condition = eval(expression.getCondition(), env);
        if(expression.getElseConditions().size() != expression.getElseConsequences().size()){
            newError("Invalid IF statement", null);
            return NULL;
        }
        
        if(isTruthy(condition)){
            return eval(expression.getConsequence(), env);
        }else if(expression.getElseConditions().size() > 0){
            for(int i =0; i < expression.getElseConditions().size(); i++){
                Object elseCondition = eval(expression.getElseConditions().get(i), env);
                if(isTruthy(elseCondition)){
                    return eval(expression.getElseConsequences().get(i), env);
                }
            }
        }
        
        return eval(expression.getAlternative(), env);
        

    }
    private static Object evalHashLiteral(HashLiteral node, Environment env){
       Map<HashKey, HashPair> pairs = new HashMap<>();
       
       for(Expression keyNode: node.getPairs().keySet()){
            Object key = eval(keyNode, env);
           
            if(isError(key)){
                return key;
            }
            Object value = eval(node.getPairs().get(keyNode), env);
           
            if(isError(value)){
                return value;
            }
            HashKey hashed = null;
            if(key instanceof IntegerObject){
                IntegerObject io = (IntegerObject)key;
                hashed = HashKey.fromInteger(io.getValue());
                hashed.setType(ObjectType.INTEGER_OBJ);
            }else if(key instanceof BooleanObject){
                BooleanObject bo = (BooleanObject)key;
                hashed = HashKey.fromBoolean(bo.getValue());
                hashed.setType(ObjectType.BOOLEAN_OBJ);
            }else if(key instanceof CharacterObject){
                CharacterObject bo = (CharacterObject)key;
                hashed = HashKey.fromChar(bo.getValue());
                hashed.setType(ObjectType.CHARACTER_OBJ);
            }else if(key instanceof FloatObject){
                FloatObject bo = (FloatObject)key;
                hashed = HashKey.fromFloat(bo.getValue());
                hashed.setType(ObjectType.FLOAT_OBJ);
            }



            pairs.put(hashed, new HashPair(key, value));
            
       }
       
       return new HashObject(pairs);

    }

    private static Object evalIndexExpression(Object left, Object index){
        return evalHashIndexExpression(left, index);
    }

    private static Object evalHashIndexExpression(Object left, Object index){
        HashObject hash = (HashObject)left;
        HashPair pair = null;
        if(index.type().equals(ObjectType.BOOLEAN_OBJ)){
            BooleanObject obj = (BooleanObject)index;
            pair = hash.getPairs().get(HashKey.fromBoolean(obj.getValue()));
        }else if(index.type().equals(ObjectType.INTEGER_OBJ)){
            IntegerObject obj = (IntegerObject)index;
            pair = hash.getPairs().get(HashKey.fromInteger(obj.getValue()));
        }else if(index.type().equals(ObjectType.CHARACTER_OBJ)){
            CharacterObject obj = (CharacterObject)index;
            pair = hash.getPairs().get(HashKey.fromChar(obj.getValue()));
        }else if(index.type().equals(ObjectType.FLOAT_OBJ)){
            FloatObject obj = (FloatObject)index;
            pair = hash.getPairs().get(HashKey.fromFloat(obj.getValue()));
        }
        return pair.getValue();

    }
    private static Object evalWhileExpression(WhileExpression expression, Environment env){
        Object content = null;
        Object condition = eval(expression.getCondition(),env);
        while(isTruthy(condition)){
            content = eval(expression.getContent(), env);
            condition = eval(expression.getCondition(), env);
        }

        return content;
    }

    private static boolean isTruthy(Object obj){
        if(obj.equals(NULL)){
            return false;
        }else if(obj.equals(TRUE)){
            return true;
        }else if(obj.equals(FALSE)){
            return false;
        }
        return true;
    }
    private static Object evalString(StringValue sv, Environment env){
        return new StringObject(sv.getValue());
    }

    private static Object evalIdentifier(Identifier node, Environment env){
        Object value;
        if(env.has(node.getValue())){
            
            value = env.get(node.getValue());
            
        }else{
            return newError("identifier not found: " + node.getValue());
        }
        
        return value;

    }

    private static BooleanObject nativeBoolToBooleanObject(boolean input){
        if(input){
            return TRUE;
        }
        return FALSE;

    }

    private static Object evalInfixExpression(String operator, Object left, Object right){
        if(left == null || right == null){
            return newError("Incompatible: can't use operator %s on %s and %s", operator, left, right);
        }
        if(left.type().equals(ObjectType.INTEGER_OBJ) && right.type().equals(ObjectType.INTEGER_OBJ)){
            return evalIntegerInfixExpression(operator, left, right);
        }else if(left.type().equals(ObjectType.CHARACTER_OBJ) && right.type().equals(ObjectType.CHARACTER_OBJ)){
            return evalCharacterInfixExpression(operator, left, right);
        }else if(left.type().equals(ObjectType.BOOLEAN_OBJ) && right.type().equals(ObjectType.BOOLEAN_OBJ)){
            return evalBooleanInfixExpression(operator, left, right);
        }else if((left.type().equals(ObjectType.FLOAT_OBJ) && right.type().equals(ObjectType.FLOAT_OBJ)) || (left.type().equals(ObjectType.FLOAT_OBJ) && right.type().equals(ObjectType.INTEGER_OBJ)) || left.type().equals(ObjectType.INTEGER_OBJ) && right.type().equals(ObjectType.FLOAT_OBJ)){
            return evalFloatInfixExpression(operator, left, right);
        }else if(!(left.type().equals(right.type()))){
            return newError("type mismatch: %s %s %s", left.type(), operator, right.type());
        }
        return newError("unkown operator: %s %s %s", left.type(), operator, right.type());
    }

    private static Object evalIntegerInfixExpression(String operator, Object left, Object right){
        IntegerObject leftObj = (IntegerObject)left;
        IntegerObject rightObj = (IntegerObject)right;

        int leftVal = leftObj.getValue();
        int rightVal = rightObj.getValue();

        switch (operator){
            case "+":
                return new IntegerObject(leftVal + rightVal);
            case "-":
                return new IntegerObject(leftVal - rightVal);
            case "*":
                return new IntegerObject(leftVal * rightVal);
            case "/":
                return new IntegerObject(leftVal / rightVal);
            case "%":
                return new IntegerObject(leftVal % rightVal);
            case ">":
                return nativeBoolToBooleanObject(leftVal > rightVal);
            case "<":
                return nativeBoolToBooleanObject(leftVal < rightVal);
            case ">=":
                return nativeBoolToBooleanObject(leftVal >= rightVal);
            case "<=":
                return nativeBoolToBooleanObject(leftVal <= rightVal);
            case "==":
                return nativeBoolToBooleanObject(leftVal == rightVal);
            case "<>":
                return nativeBoolToBooleanObject(leftVal != rightVal);
            default:
            return NULL;
        }
    }
    private static Object evalFloatInfixExpression(String operator, Object left, Object right){
        float leftVal;
        float rightVal;
        if(left instanceof FloatObject && right instanceof FloatObject){
            FloatObject leftObj = (FloatObject)left;
            FloatObject rightObj = (FloatObject)right;
            leftVal = leftObj.getValue();
            rightVal = rightObj.getValue();
        }else if(left instanceof FloatObject && right instanceof IntegerObject){
            FloatObject leftObj = (FloatObject)left;
            IntegerObject rightObj = (IntegerObject)right;
            leftVal = leftObj.getValue();
            rightVal = rightObj.getValue();
        }else{
            IntegerObject leftObj = (IntegerObject)left;
            FloatObject rightObj = (FloatObject)right;
            leftVal = leftObj.getValue();
            rightVal = rightObj.getValue();
        }
        

        switch (operator){
            case "+":
                return new FloatObject(leftVal + rightVal);
            case "-":
                return new FloatObject(leftVal - rightVal);
            case "*":
                return new FloatObject(leftVal * rightVal);
            case "/":
                return new FloatObject(leftVal / rightVal);
            case "%":
                return new FloatObject(leftVal % rightVal);
            case ">":
                return nativeBoolToBooleanObject(leftVal > rightVal);
            case "<":
                return nativeBoolToBooleanObject(leftVal < rightVal);
            case ">=":
                return nativeBoolToBooleanObject(leftVal >= rightVal);
            case "<=":
                return nativeBoolToBooleanObject(leftVal <= rightVal);
            case "==":
                return nativeBoolToBooleanObject(leftVal == rightVal);
            case "<>":
                return nativeBoolToBooleanObject(leftVal != rightVal);
            default:
            return NULL;
        }
    }

    private static Object evalCharacterInfixExpression(String operator, Object left, Object right){
        CharacterObject leftObj = (CharacterObject)left;
        CharacterObject rightObj = (CharacterObject)right;

        char leftVal = leftObj.getValue();
        char rightVal = rightObj.getValue();

        switch (operator){
            case "==":
                return nativeBoolToBooleanObject(leftVal == rightVal);
            case "<>":
                return nativeBoolToBooleanObject(leftVal != rightVal);
            default:
            return NULL;
        }
    }
    private static Object evalBooleanInfixExpression(String operator, Object left, Object right){
        BooleanObject leftObj = (BooleanObject)left;
        BooleanObject rightObj = (BooleanObject)right;

        boolean leftVal = leftObj.getValue();
        boolean rightVal = rightObj.getValue();

        switch (operator){
            case "UG":
                return nativeBoolToBooleanObject(leftVal == rightVal);
            case "O":
                return nativeBoolToBooleanObject(leftVal || rightVal);
            default:
            return NULL;
        }
    }
    
    private static Object evalPrefixExpression(String operator, Object right){
        switch (operator){
            case "-":
                
                return evalMinusPrefixOperatorExpression(right);

            case "DILI":
                return evalNotPrefixOperatorExpression(right);
            default:
                return newError("unknown operator: %s%s", operator, right.type());
        }
    }

    private static Object evalMinusPrefixOperatorExpression(Object right){
        if(!(right.type().equals(ObjectType.INTEGER_OBJ)) && !(right.type().equals(ObjectType.FLOAT_OBJ))){
            return NULL;
        }
        if(right.type().equals(ObjectType.INTEGER_OBJ)){

            IntegerObject obj = (IntegerObject)right;
            int value = obj.getValue();
            return new IntegerObject(value * -1);
        }else{
            FloatObject obj = (FloatObject)right;
            float value = obj.getValue();
            return new FloatObject(value * -1);
        }
    }

    private static Object evalNotPrefixOperatorExpression(Object right){
        if(!(right.type().equals(ObjectType.BOOLEAN_OBJ))){
            newError("Error: Expected %s, got %s instead", ObjectType.BOOLEAN_OBJ, right.type());
            return NULL;
        }

            BooleanObject obj = (BooleanObject)right;
            boolean value = obj.getValue();
            return new BooleanObject(!value);
        
    }

    private static Error newError(String format, java.lang.Object... a){
        errorCount++;
        return new Error(String.format(format, a));
    }

    private static boolean isError(Object obj){
        if(obj != null){
            return obj.type().equals(ObjectType.ERROR_OBJ);
        }
        return false;   
    }
}
