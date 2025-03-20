package object;

public class StringObject implements Object{
    private String value;

    public StringObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String inspect() {
        return String.format("%s", value);
    }

    @Override
    public ObjectType type() {
        return ObjectType.CHARACTER_OBJ;
    }
}
