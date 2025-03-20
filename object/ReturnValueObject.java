package object;

public class ReturnValueObject implements Object{
    Object value;

    public ReturnValueObject() {
    }
        
    

    public ReturnValueObject(Object value) {
        this.value = value;
    }



    @Override
    public String inspect() {
        return value.inspect();
    }

    @Override
    public ObjectType type() {
        return ObjectType.RETURN_VALUE_OBJ;
    }



    public Object getValue() {
        return value;
    }



    public void setValue(Object value) {
        this.value = value;
    }

    
    
}
