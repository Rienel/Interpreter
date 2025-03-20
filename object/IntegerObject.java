package object;

public class IntegerObject implements Object{
    private int value;

    public IntegerObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String inspect() {
        return String.format("%d", value);
    }

    @Override
    public ObjectType type() {
       return ObjectType.INTEGER_OBJ;
    }

    

    
}
