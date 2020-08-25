package org.example;

public class ClientInfoParserValidator extends PerfResource {
    public ClientInfoParserValidator(String patterns[], String values[]) {
        KEYS = patterns;
        VALUES = values;
    }

    public boolean compareResults() {
        ISojNvl jdkSojNvl = new OrigClientInfoParser();
        ISojNvl fastSojNvl = new OptClientInfoParser();
        ISojNvl fastSojNvl2 = new OptClientInfoParser();
        ((OptClientInfoParser)fastSojNvl2).useSearchFromIndex(true);
        for (int k = 0; k < KEYS.length; k++)
        {
            for (int j = 0 ; j < VALUES.length; j++)
            {
                String ref = jdkSojNvl.getTagValue(VALUES[j], KEYS[k]);
                String fst = fastSojNvl.getTagValue(VALUES[j], KEYS[k]);
                String fst2 = fastSojNvl2.getTagValue(VALUES[j], KEYS[k]);
                if ((ref == null && fst != null) ||
                        (ref != null && !ref.equals(fst))) {
                    System.out.println(k + " FastSoj failed at pat: " + KEYS[k] + " value: " + VALUES[j]);
                    return false;
                }

                if ((ref == null && fst2 != null) ||
                        (ref != null && !ref.equals(fst2))) {
                    System.out.println("FastImprSoj failed at pat: " + KEYS[k] + " value: " + VALUES[j]);
                    return false;
                }
            }
        }
        return true;
    }
}
