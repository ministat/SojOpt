package org.example;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.apache.commons.lang.StringUtils;

public class Re2JNvlImpl implements ISojNvl {
    public static final String[] KV_DELIMITER = new String[]{"&", "&_", "&!"};
    public static final String BLANK_STRING = "";
    public Re2JNvlImpl() {

    }

    public String getTagValue(String value, String key) {
        if (!StringUtils.isBlank(value) && !StringUtils.isBlank(key)) {
            value = "&" + value;
            String kvSet = "";
            String keySet = "";

            int startpos;
            for(startpos = 0; startpos < KV_DELIMITER.length; ++startpos) {
                keySet = keySet + KV_DELIMITER[startpos] + key + "=|";
                kvSet = kvSet + KV_DELIMITER[startpos] + "|";
            }

            keySet = keySet.substring(0, keySet.length() - 1);
            kvSet = kvSet.substring(0, kvSet.length() - 1);
            Pattern p1 = Pattern.compile(keySet);
            Pattern p2 = Pattern.compile(kvSet);
            Matcher m1 = p1.matcher(value);
            Matcher m2 = p2.matcher(value);
            if (m1.find()) {
                startpos = m1.start();
                int tmppos = m1.end();
                int endpos;
                if (m2.find(tmppos)) {
                    endpos = m2.start();
                } else {
                    endpos = value.length();
                }

                if (endpos < 0) {
                    endpos = value.length() - 1;
                }

                String[] kvPair = value.substring(startpos, endpos).split("=", 2);
                return "".equals(kvPair[1]) ? null : kvPair[1];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String ToString() {
        return getClass().getName();
    }
}
