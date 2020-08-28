package org.example;

public class IsNumericalValidator extends PerfResource {
    private IsNumerical _origin = null;
    private IsNumerical _opt = null;

    public IsNumericalValidator(String values[], int precision, int scale) {
        VALUES = values;
        if (precision > 0 && scale >= 0) {
            _origin = new OrigIsDecimal(precision, scale);
            _opt = new FastIsDecimal(precision, scale);
        }
    }

    public boolean compareResults() {
        for (int j = 0 ; j < VALUES.length; j++) {
            boolean ref = _origin.isNumerical(VALUES[j]);
            boolean fst = _opt.isNumerical(VALUES[j]);
            if (ref != fst) {
                System.out.println(j + " FastIsNumerical failed at value: " + VALUES[j]);
                return false;
            }
        }
        return true;
    }
}
