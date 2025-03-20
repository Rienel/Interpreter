package object;

public class HashPair  implements Object{
    Object key;
    Object value;
    public HashPair() {
    }
    
    public HashPair(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }
    public Object getValue() {
        return value;
    }
    public void setKey(Object key) {
        this.key = key;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    @Override
    public String inspect() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ObjectType type() {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}
