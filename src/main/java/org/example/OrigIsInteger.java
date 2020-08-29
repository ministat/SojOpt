package org.example;

public class OrigIsInteger implements IsNumerical {
    @Override
    public boolean isNumerical(String value) {
        if (value == null) {
            return false;
        }
        try {
            Integer.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String ToString() {
        return this.getClass().getName();
    }
}
