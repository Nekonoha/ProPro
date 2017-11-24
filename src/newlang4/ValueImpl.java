package newlang4;

import newlang4.Value;
import newlang4.ValueType;

public class ValueImpl implements Value {
    ValueType valueType;
    String s;
    int i;
    double d;
    boolean b;


    public ValueImpl(String s){
        this.s = s;
        this.valueType = ValueType.STRING;
    }

    public ValueImpl(int i){
        this.i = i;
        this.valueType = ValueType.INTEGER;
    }

    public ValueImpl(double d){
        this.d = d;
        this.valueType = ValueType.DOUBLE;
    }

    public ValueImpl(boolean b){
        this.b = b;
        this.valueType = ValueType.BOOL;
    }

    @Override
    public String getSValue() {
        switch (valueType) {
            case STRING:
                return s;
            case INTEGER:
                return String.valueOf(i);
            case DOUBLE:
                return String.valueOf(d);
            case BOOL:
                return String.valueOf(b);
        }
        return null;
    }

    @Override
    public int getIValue() {
        switch (valueType) {
            case STRING:
                return Integer.parseInt(s);
            case INTEGER:
                return i;
            case DOUBLE:
                return (int)d;
            case BOOL:
            break;
        }
        return -1;
    }

    @Override
    public double getDValue() {
        switch (valueType) {
            case STRING:
                return Double.parseDouble(s);
            case INTEGER:
                return (double)i;
            case DOUBLE:
                return (int)d;
            case BOOL:
                break;
        }
        return -1;
    }

    @Override
    public boolean getBValue() {
        switch (valueType) {
            case STRING:
                return Boolean.parseBoolean(s);
            case INTEGER:
                break;
            case DOUBLE:
                break;
            case BOOL:
                return b;
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return valueType;
    }
}
