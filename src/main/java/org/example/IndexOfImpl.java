package org.example;

public class IndexOfImpl implements ISojNvl {
    @Override
    public String getTagValue(String value, String key) {
        if (value == null || key == null || key.length() == 0) {
            return null;
        }
        if (match(value, key) >= 0) {
            return "true";
        }
        return "false";
    }

    public int match(String value, String key) {
        int idx = value.indexOf("&" + key + "=");
        if (idx >= 0) {
            return idx;
        }
        /*
        idx = value.indexOf("&_" + key + "=");
        if (idx >= 0) {
            return idx;
        }
        idx = value.indexOf("!_" + key + "=");
        if (idx >= 0) {
            return idx;
        }
        */
        return -1;
    }

    @Override
    public String ToString() {
        return this.getClass().getName();
    }
}
