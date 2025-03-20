package object;

public class Error implements Object{
    String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String inspect() {
        return "ERROR: " + message;
    }

    @Override
    public ObjectType type() {
        return ObjectType.ERROR_OBJ;
    }


    
}
