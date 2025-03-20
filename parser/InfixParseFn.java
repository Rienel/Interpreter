package parser;

import ast.Expression;

public interface InfixParseFn {
    Expression apply(Expression left);

}
