package object;

import ast.BlockStatement;

public class Structure implements Object {
    BlockStatement body;
    Environment env;

    public Structure(BlockStatement body, Environment env) {
        this.body = body;
        this.env = env;
    }

    

    @Override
    public String inspect() {
        StringBuilder out = new StringBuilder();

        out.append("SUGOD");
        out.append("\n");
        out.append(body.string());
        out.append("\n");
        out.append("KATAPUSAN");

        return out.toString();
    }

    @Override
    public ObjectType type() {
        return ObjectType.STRUCTURE_OBJ;
    }



    public BlockStatement getBody() {
        return body;
    }



    public void setBody(BlockStatement body) {
        this.body = body;
    }



    public Environment getEnv() {
        return env;
    }



    public void setEnv(Environment env) {
        this.env = env;
    }

    
}
