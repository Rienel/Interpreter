package object;

import java.math.BigDecimal;

public class FloatObject implements Object{
    private float value;

    public FloatObject(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String inspect() {
        BigDecimal myDecimal = new BigDecimal(value);
        myDecimal.stripTrailingZeros();
        value = myDecimal.floatValue();
        return Float.toString(value);
    }

    @Override
    public ObjectType type() {
       return ObjectType.FLOAT_OBJ;
    }
}
