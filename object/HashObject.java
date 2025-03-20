package object;

import java.util.HashMap;
import java.util.Map;

public class HashObject implements Object{
    Map<HashKey, HashPair> pairs;

    public HashObject() {
        pairs = new HashMap<>();
    }
    

    public HashObject(Map<HashKey, HashPair> pairs) {
        this.pairs = pairs;
    }


    public Map<HashKey, HashPair> getPairs() {
        return pairs;
    }

    public void setPairs(Map<HashKey, HashPair> pairs) {
        this.pairs = pairs;
    }

    @Override
    public String inspect() {
        
        return null;
    }

    @Override
    public ObjectType type() {
        return ObjectType.HASH_OBJ;
    }


   

    

    
}
