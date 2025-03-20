package object;

public class CharacterObject implements Object{
    private char value;

    public CharacterObject(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public String inspect() {
        return String.format("%c", value);
    }

    @Override
    public ObjectType type() {
        return ObjectType.CHARACTER_OBJ;
    }

    
}
