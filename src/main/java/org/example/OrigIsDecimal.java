package org.example;

import org.apache.commons.lang.NotImplementedException;

import java.math.BigDecimal;

public class OrigIsDecimal implements IsNumerical {
    private int _precision;
    private int _scale;
    public OrigIsDecimal(int p, int s) {
        _precision = p;
        _scale = s;
    }
    @Override
    public String ToString() {
        return this.getClass().getName();
    }

    @Override
    public boolean isNumerical(String value) {
        int p = _precision;
        int s = _scale;
        if (value == null || p < 0 || s < 0) {
            return false;
        }

        String num = value.toString();
        if (!num.matches("[+-]?\\d*[\\.]?\\d*")) {
            return false;
        }
        try {
            BigDecimal decimal = new BigDecimal(num);
            int scale = decimal.scale();
            int precision = decimal.precision();
            if ((p - s) >= (precision - scale)){
                return true;
            }else{
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
