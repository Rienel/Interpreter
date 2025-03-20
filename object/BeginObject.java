package object;

public class BeginObject implements Object{

    @Override
    public String inspect() {
        return "SUGOD";
    }

    @Override
    public ObjectType type() {
        return ObjectType.BEGIN_OBJ;
    }
    
    
}
