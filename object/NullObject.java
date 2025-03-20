package object;

public class NullObject implements Object{

    @Override
    public String inspect() {
        return null;
    }

    @Override
    public ObjectType type() {
        return ObjectType.NULL_OBJ;
    }

    
    
}
