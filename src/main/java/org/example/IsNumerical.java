package org.example;

public interface IsNumerical {
    enum NumericalType {
        START,
        DECIMAL,
        INTEGER,
        BIGINT,
        END
    }
    boolean isNumerical(String value);
    public String ToString();
}
