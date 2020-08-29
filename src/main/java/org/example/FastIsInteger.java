package org.example;

import com.google.common.primitives.Ints;

public class FastIsInteger implements IsNumerical {
    @Override
    public boolean isNumerical(String value) {
        Integer result = null;
        if (value.charAt(0) != '+') {
            result = Ints.tryParse(value);
        } else {
            result = Ints.tryParse(value.substring(1));
        }
        return (result != null);
    }

    @Override
    public String ToString() {
        return this.getClass().getName();
    }

    public static void main(String args[]) {
        FastIsInteger fi = new FastIsInteger();
        System.out.println(fi.isNumerical("+123"));
        System.out.println(fi.isNumerical("123"));
    }
}
