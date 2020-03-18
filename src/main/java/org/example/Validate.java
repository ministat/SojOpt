package org.example;

public class Validate extends PerfResource {
    public Validate(String patterns[], String values[]) {
        KEYS = patterns;
        VALUES = values;
    }

    public boolean compareResults() {
        ISojNvl jdkSojNvl = new JdkSojNvlImpl();
        ISojNvl re2jSojNvl = new Re2JNvlImpl();
        ISojNvl fastSojNvl = new FastSojNvlImpl();
        for (int k = 0; k < KEYS.length; k++)
        {
            for (int j = 0 ; j < VALUES.length; j++)
            {
                String ref = jdkSojNvl.getTagValue(VALUES[j], KEYS[k]);
                String res = re2jSojNvl.getTagValue(VALUES[j], KEYS[k]);
                String fst = fastSojNvl.getTagValue(VALUES[j], KEYS[k]);
                if ((ref == null && res != null) ||
                        (ref != null && !ref.equals(res))) {
                    System.out.println("Re2J failed at pat: " + KEYS[k] + " value: " + VALUES[j]);
                    return false;
                }
                if ((ref == null && fst != null) ||
                        (ref != null && !ref.equals(fst))) {
                    System.out.println("FastSoj failed at pat: " + KEYS[k] + " value: " + VALUES[j]);
                    return false;
                }
            }
        }
        return true;
    }
}
