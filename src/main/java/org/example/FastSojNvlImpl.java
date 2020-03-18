package org.example;

public class FastSojNvlImpl implements ISojNvl {
    @Override
    public String getTagValue(String querystring, String param) {
        if (querystring == null || param == null || param.length() == 0) {
            return null;
        }

        String v = null;
        int l = param.length();
        if (querystring.startsWith(param) && querystring.length() > param.length() && querystring.charAt(l) == '=') {
            int pos = querystring.indexOf('&');
            if (pos > 0) {
                v = querystring.substring(l + 1, pos);
            } else {
                v = querystring.substring(l + 1);
            }
        }

        if (v == null) {
            if((querystring.startsWith("_" + param) || querystring.startsWith("!" + param)) && (querystring.length() > param.length() + 1) && querystring.charAt(l + 1) == '=') {
                int pos = querystring.indexOf('&');
                if (pos > 0) {
                    v = querystring.substring(l + 2, pos);
                } else {
                    v = querystring.substring(l + 2);
                }
            }
        }

        if (v == null) {
            int begin = querystring.indexOf("&" + param + "=");
            if (begin >= 0) {
                int begin2 = begin + l + 2;
                int pos = querystring.indexOf('&', begin2);
                if (pos > 0) {
                    v = querystring.substring(begin2, pos);
                } else {
                    v = querystring.substring(begin2);
                }
            }
        }

        if (v == null) {
            int begin_1 = querystring.indexOf("&_" + param + "=");
            int begin_2 = querystring.indexOf("&!" + param + "=");

            int begin = -1;
            if(begin_1 >= 0){
                begin = begin_1;
            } else if(begin_2 >= 0){
                begin = begin_2;
            }

            if (begin >= 0) {
                int begin2 = begin + l + 3;
                int pos = querystring.indexOf('&', begin2);
                if (pos > 0) {
                    v = querystring.substring(begin2, pos);
                } else {
                    v = querystring.substring(begin2);
                }
            }
        }

        if (v != null && v.length() == 0) {
            return null;
        }
        return v;
    }

    @Override
    public String ToString() {
        return this.getClass().getName();
    }
}
