package ast;

public interface Statement extends Node, Expression {
    public abstract void statementNode();

    @Override
    default void expressionNode() {

    }

    @Override
    default String getTokenLiteral() {
        return "";
    }

    @Override
    default String string() {
        return "";
    }
}