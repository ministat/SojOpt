package org.example;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.apache.commons.lang.StringUtils;
import org.javatuples.Triplet;

public class Re2JNvlImpl extends CachedSojNvlImpl {
    public static final String[] KV_DELIMITER = new String[]{"&", "&_", "&!"};
    public static final String BLANK_STRING = "";
    public Re2JNvlImpl() {
        super(false);
    }

    public Re2JNvlImpl(boolean useCache) {
        super(useCache);
    }

    public String getTagValue(String value, String key) {
        if (!StringUtils.isBlank(value) && !StringUtils.isBlank(key)) {
            String originalValue = value;
            String originalKey = key;
            if (_useCache && _cache.get() != null) {
                Triplet<String, String, String> cache = _cache.get();
                if (cache.getValue0().equals(value) && cache.getValue1().equals(key)) {
                    return cache.getValue2();
                }
            }

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
            String ret = null;
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
                ret = "".equals(kvPair[1]) ? null : kvPair[1];
            } else {
                ret = null;
            }
            if (_useCache) {
                Triplet<String, String, String> triplet = new Triplet<String, String, String>(originalValue, originalKey, ret);
                _cache.set(triplet);
            }
            return ret;
        } else {
            return null;
        }
    }

    public String ToString() {
        return getClass().getName();
    }
}
