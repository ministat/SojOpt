package org.example;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;

public class FastIsDecimal implements IsNumerical {
    private int _precision;
    private int _scale;
    public FastIsDecimal(int p, int s) {
        _precision = p;
        _scale = s;
    }
    @Override
    public boolean isNumerical(String value) {
        int p = _precision;
        int s = _scale;
        if (value == null || p < 0 || s < 0) {
            return false;
        }

        if (!NumberUtils.isNumber(value)) {
            return false;
        }
        try {
            BigDecimal decimal = new BigDecimal(value);
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
    @Override
    public String ToString() {
        return this.getClass().getName();
    }
}

