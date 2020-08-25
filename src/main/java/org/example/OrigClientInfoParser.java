package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class OrigClientInfoParser extends CachedSojNvlImpl {

    public static final String keyWords = "ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding|TPool|TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine";
    public static final char kvpDelimiter = '&';
    public static final char kvDelimiter = '=';
    public static final String tSearchWords = "&" + "ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding|TPool|TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine".replace("|", "=|&") + "=";
    static Pattern p1 = Pattern.compile("ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding|TPool|TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine");
    static Pattern p2;

    private static int isValidCIname(String key) {
        Matcher m1 = p1.matcher(key);
        return m1.find() ? 1 : 0;
    }

    static {
        p2 = Pattern.compile(tSearchWords);
    }

    @Override
    public String getTagValue(String clientinfo, String key) {
        if (StringUtils.isBlank(clientinfo)) {
            return null;
        } else if (!StringUtils.isBlank(key) && isValidCIname(key) != 0) {
            Matcher m1 = p2.matcher(clientinfo);
            int delStartPos = -1;
            int valueStartPos;
            int matchStartPos;
            if (clientinfo.startsWith(key + '=')) {
                valueStartPos = (key + '=').length();
                matchStartPos = valueStartPos;

                while(m1.find(matchStartPos) && delStartPos == -1) {
                    delStartPos = m1.start();
                    if (clientinfo.charAt(delStartPos - 1) == '&') {
                        matchStartPos = delStartPos + 1;
                        delStartPos = -1;
                    }
                }

                if (delStartPos == -1) {
                    delStartPos = clientinfo.length();
                }

                return clientinfo.substring(valueStartPos, delStartPos);
            } else if (clientinfo.indexOf("&" + key + '=') < 0) {
                return null;
            } else {
                int p = clientinfo.indexOf("&" + key + '=');
                valueStartPos = p + ("&" + key + '=').length();
                matchStartPos = valueStartPos;

                while(m1.find(matchStartPos) && delStartPos == -1) {
                    delStartPos = m1.start();
                    if (clientinfo.charAt(delStartPos - 1) == '&') {
                        matchStartPos = delStartPos + 1;
                        delStartPos = -1;
                    }
                }

                if (delStartPos == -1) {
                    delStartPos = clientinfo.length();
                }

                return clientinfo.substring(valueStartPos, delStartPos);
            }
        } else {
            return null;
        }
    }

    @Override
    public String ToString() {
        return getClass().getName();
    }

    public static void test1() {
        //String clientInfo = "TPayload=corr_id_%3D6b64d14d971a85c5%26node_id%3D7a30978f1a0b8f00%26REQUEST_GUID%3D17414f88df5.a0f171e.34650.d9d10b86%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%2528t1yqm%2Aw%2560ut3530-17414f88e1c-0xdb%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=1&TStatus=0&TType=URL&ContentLength=1032&ForwardedFor=109.181.155.206;184.28.198.133&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.69.180.126&TStamp=00:01:07.73&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayIOS;6.5.1;iOS;13.6;Apple;iPhone12_5;vodafone UK;414x896;3.0&RemoteIP=109.181.155.206&Encoding=gzip&Referer=null";
        String clientInfo = "TPayload=corr_id_%3D10b9ca021740aca46a4f12d2ffafded3%26node_id%3D1a40fbd5899c044b%26REQUEST_GUID%3D17410b9c-a790-a77d-8e02-63b0ffc8bd81%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31%2528%2560coq1%2Aw%2560ut3542-17410b9ca7d-0x1af%26cal_mod%3Dfalse&TPool=r1lqgsvc&TDuration=3&TStatus=0&TType=URL&ContentLength=2080&ForwardedFor=92.169.117.31;2.17.114.43&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.fr&TMachine=10.119.216.224&TStamp=04:14:05.56&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15&RemoteIP=92.169.117.31&Encoding=gzip&Referer=https://www.ebay.fr/sl/list/v2?draft_id=2234338318023&mode=AddItem&productEnforcement=false";
        OrigClientInfoParser optParser = new OrigClientInfoParser();
        String keys[] = {"TPayload", "Referer", "Agent", "ForwardedFor", "RemoteIP"};
        for (String k : keys) {
            String v = optParser.getTagValue(clientInfo, k);
            System.out.println(k + " : " + v);
        }
    }

    public static void genReference() {
        String clientInfo = "TPayload=corr_id_%3D10b9ca021740aca46a4f12d2ffafded3%26node_id%3D1a40fbd5899c044b%26REQUEST_GUID%3D17410b9c-a790-a77d-8e02-63b0ffc8bd81%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31%2528%2560coq1%2Aw%2560ut3542-17410b9ca7d-0x1af%26cal_mod%3Dfalse&TPool=r1lqgsvc&TDuration=3&TStatus=0&TType=URL&ContentLength=2080&ForwardedFor=92.169.117.31;2.17.114.43&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.fr&TMachine=10.119.216.224&TStamp=04:14:05.56&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15&RemoteIP=92.169.117.31&Encoding=gzip&Referer=https://www.ebay.fr/sl/list/v2?draft_id=2234338318023&mode=AddItem&productEnforcement=false";
        OrigClientInfoParser optParser = new OrigClientInfoParser();
        String keys[] = {"ForwardedFor", "RemoteIP", "Referer", "ContentLength", "Script", "Server", "Agent", "Encoding", "TPool", "TStamp", "TType", "TName", "TStatus", "TDuration", "TPayload", "TMachine"};
        for (String k : keys) {
            String v = optParser.getTagValue(clientInfo, k);
            System.out.println(k + ":" + v);
        }
    }
    public static void singleTest() {
        String clientInfo = "TPayload=corr_id_%3D6b64d14d971a85c5%26node_id%3D7a30978f1a0b8f00%26REQUEST_GUID%3D17414f88df5.a0f171e.34650.d9d10b86%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%2528t1yqm%2Aw%2560ut3530-17414f88e1c-0xdb%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=1&TStatus=0&TType=URL&ContentLength=1032&ForwardedFor=109.181.155.206;184.28.198.133&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.69.180.126&TStamp=00:01:07.73&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayIOS;6.5.1;iOS;13.6;Apple;iPhone12_5;vodafone UK;414x896;3.0&RemoteIP=109.181.155.206&Encoding=gzip&Referer=null";
        OrigClientInfoParser optParser = new OrigClientInfoParser();
        String v = optParser.getTagValue(clientInfo, "Agent");
        System.out.println("Agent : " + v);
    }
    public static void main(String args[]) {
        //test1();
        genReference();
    }
}
