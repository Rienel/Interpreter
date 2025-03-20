package object;

import java.util.Objects;

public class HashKey implements Object{
    ObjectType type;
    long value;

    public HashKey() {
    }
    
    public HashKey(ObjectType type, long value) {
        this.type = type;
        this.value = value;
    }

    public ObjectType getType() {
        return type;
    }
    public void setType(ObjectType type) {
        this.type = type;
    }
    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }

    public static HashKey fromBoolean(boolean value) {
        return new HashKey(ObjectType.BOOLEAN_OBJ, value ? 1L : 0L);
      }
    
      public static HashKey fromInteger(long value) {
        return new HashKey(ObjectType.INTEGER_OBJ, (long) value);
      }
    
      public static HashKey fromChar(char value) {
        return new HashKey(ObjectType.CHARACTER_OBJ, (long) value);
      }
    
      public static HashKey fromFloat(float value) {
       
        return new HashKey(ObjectType.FLOAT_OBJ, Float.floatToIntBits(value));
      }

      @Override
      public String inspect() {
        return null;
      }

      @Override
      public ObjectType type() {
        return type;
      }

      @Override
      public boolean equals(java.lang.Object obj) {
          if(this == obj){
              return true;
          }
          if(obj == null || getClass() != obj.getClass()){
              return false;
          }
          HashKey other = (HashKey)obj;
          return value == other.getValue() && type == other.getType();
      }

      @Override
      public int hashCode() {
        return Objects.hash(type, value);
      }

      
    
}
