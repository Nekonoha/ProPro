package newlang4;

import newlang4.Value;
import newlang4.ValueType;

public class ValueImpl implements Value{
    ValueType valueType;
    String s;

    public ValueImpl(String s) {
        this.s = s;
        this.valueType = ValueType.STRING;
    }

    public ValueImpl(int i) {
        this.s = String.valueOf(i);
        this.valueType = ValueType.INTEGER;
    }

    public ValueImpl(double d){
        this.s = String.valueOf(d);
        this.valueType = ValueType.DOUBLE;
    }

    public ValueImpl(boolean b){
        this.s = String.valueOf(b);
        this.valueType = ValueType.BOOL;
    }

    @Override
    public String getSValue() {
        return s;
    }

    @Override
    public int getIValue() {
        return Integer.parseInt(s);
    }

    @Override
    public double getDValue() {
        return Double.parseDouble(s);
    }

    @Override
    public boolean getBValue() {
        return Boolean.parseBoolean(s);
    }

    @Override
    public ValueType getType() {
        return valueType;
    }
}
