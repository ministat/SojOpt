package org.example;

import org.apache.commons.lang.StringUtils;
import org.neosearch.stringsearch.extensions.StringSearcherExt;
import org.neosearch.stringsearcher.Algorithm;
import org.neosearch.stringsearcher.Emit;
import org.neosearch.stringsearcher.StringSearcher;
import org.neosearch.stringsearcher.StringSearcherBuilder;

import java.util.Arrays;

public class OptClientInfoParser extends CachedSojNvlImpl {
    boolean searchFromIndex = false;
    public void useSearchFromIndex(boolean u) {
        searchFromIndex = u;
    }
    StringSearcher<?> origStringSearcher = StringSearcher.builder()
            .addSearchString("&ForwardedFor=")
            .addSearchString("&RemoteIP=")
            .addSearchString("&Referer=")
            .addSearchString("&ContentLength=")
            .addSearchString("&Script=")
            .addSearchString("&Server=")
            .addSearchString("&Agent=")
            .addSearchString("&Encoding=")
            .addSearchString("&TPool=")
            .addSearchString("&TStamp=")
            .addSearchString("&TType=")
            .addSearchString("&TName=")
            .addSearchString("&TStatus=")
            .addSearchString("&TDuration=")
            .addSearchString("&TPayload=")
            .addSearchString("&TMachine=")
            .build();
    StringSearcherExt<?> stringSearcher = StringSearcherExt.builder()
            .addSearchString("&ForwardedFor=")
            .addSearchString("&RemoteIP=")
            .addSearchString("&Referer=")
            .addSearchString("&ContentLength=")
            .addSearchString("&Script=")
            .addSearchString("&Server=")
            .addSearchString("&Agent=")
            .addSearchString("&Encoding=")
            .addSearchString("&TPool=")
            .addSearchString("&TStamp=")
            .addSearchString("&TType=")
            .addSearchString("&TName=")
            .addSearchString("&TStatus=")
            .addSearchString("&TDuration=")
            .addSearchString("&TPayload=")
            .addSearchString("&TMachine=")
            .build();

    public String origCopy(String clientinfo, String key) {
        if (StringUtils.isBlank(clientinfo)) {
            return null;
        } else if (!StringUtils.isBlank(key) && isValidCIname(key) != 0) {
            int valueStartPos;
            int tmpPos = -1;
            if (clientinfo.startsWith(key + '=')) {
                valueStartPos = (key + '=').length();
                String leftStr = clientinfo.substring(valueStartPos);
                Emit e = origStringSearcher.firstMatch(leftStr);
                if (e == null) {
                    return clientinfo.substring(valueStartPos);
                } else {
                    tmpPos = e.getStart();
                    return clientinfo.substring(valueStartPos, valueStartPos+tmpPos);
                }
            } else if (clientinfo.indexOf("&" + key + '=') < 0) {
                return null;
            } else {
                int p = clientinfo.indexOf("&" + key + '=');
                valueStartPos = p + ("&" + key + '=').length();
                String leftStr = clientinfo.substring(valueStartPos);
                Emit e = origStringSearcher.firstMatch(leftStr);
                if (e == null) {
                    return clientinfo.substring(valueStartPos);
                } else {
                    tmpPos = e.getStart();
                    return clientinfo.substring(valueStartPos, valueStartPos+tmpPos);
                }
            }
        } else {
            return null;
        }
    }

    public String extWoCopy(String clientinfo, String key) {
        if (StringUtils.isBlank(clientinfo)) {
            return null;
        } else if (!StringUtils.isBlank(key) && isValidCIname(key) != 0) {
            int valueStartPos;
            int tmpPos = -1;
            if (clientinfo.startsWith(key + '=')) {
                valueStartPos = (key + '=').length();
                //String leftStr = clientinfo.substring(valueStartPos);
                Emit e = stringSearcher.firstMatch(clientinfo, valueStartPos);
                if (e == null) {
                    return clientinfo.substring(valueStartPos);
                } else {
                    tmpPos = e.getStart();
                    return clientinfo.substring(valueStartPos, tmpPos);
                }
            } else if (clientinfo.indexOf("&" + key + '=') < 0) {
                return null;
            } else {
                int p = clientinfo.indexOf("&" + key + '=');
                valueStartPos = p + ("&" + key + '=').length();
                //String leftStr = clientinfo.substring(valueStartPos);
                Emit e = stringSearcher.firstMatch(clientinfo, valueStartPos);
                if (e == null) {
                    return clientinfo.substring(valueStartPos);
                } else {
                    tmpPos = e.getStart();
                    return clientinfo.substring(valueStartPos, tmpPos);
                }
            }
        } else {
            return null;
        }
    }

    @Override
    public String getTagValue(String clientinfo, String key) {
        if (searchFromIndex) {
            return extWoCopy(clientinfo, key);
        } else {
            return origCopy(clientinfo, key);
        }
    }

    @Override
    public String ToString() {
        return getClass().getName();
    }

    private static int isValidCIname(String key) {
        String candidates = "ForwardedFor|RemoteIP|Referer|ContentLength|Script|Server|Agent|Encoding|TPool|TStamp|TType|TName|TStatus|TDuration|TPayload|TMachine";
        return candidates.indexOf(key) >= 0 ? 1 : 0;
    }

    public static void test1() {
        //String clientInfo = "TPayload=corr_id_%3D6b64d14d971a85c5%26node_id%3D7a30978f1a0b8f00%26REQUEST_GUID%3D17414f88df5.a0f171e.34650.d9d10b86%26logid%3Dt6wwm53vpd77%253C%253Dosusqn47pse31%2528t1yqm%2Aw%2560ut3530-17414f88e1c-0xdb%26cal_mod%3Dfalse&TPool=r1pulsgwy&TDuration=1&TStatus=0&TType=URL&ContentLength=1032&ForwardedFor=109.181.155.206;184.28.198.133&Script=/trk20svc/TrackingResource/v1&Server=apisd.ebay.com&TMachine=10.69.180.126&TStamp=00:01:07.73&TName=Ginger.CollectionSvc.track&Agent=ebayUserAgent/eBayIOS;6.5.1;iOS;13.6;Apple;iPhone12_5;vodafone UK;414x896;3.0&RemoteIP=109.181.155.206&Encoding=gzip&Referer=null";
        String clientInfo = "TPayload=corr_id_%3D10b9ca021740aca46a4f12d2ffafded3%26node_id%3D1a40fbd5899c044b%26REQUEST_GUID%3D17410b9c-a790-a77d-8e02-63b0ffc8bd81%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31%2528%2560coq1%2Aw%2560ut3542-17410b9ca7d-0x1af%26cal_mod%3Dfalse&TPool=r1lqgsvc&TDuration=3&TStatus=0&TType=URL&ContentLength=2080&ForwardedFor=92.169.117.31;2.17.114.43&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.fr&TMachine=10.119.216.224&TStamp=04:14:05.56&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15&RemoteIP=92.169.117.31&Encoding=gzip&Referer=https://www.ebay.fr/sl/list/v2?draft_id=2234338318023&mode=AddItem&productEnforcement=false";
        OptClientInfoParser optParser = new OptClientInfoParser();
        String keys[] = {"TPayload", "Referer", "Agent", "ForwardedFor", "RemoteIP"};
        for (String k : keys) {
            String v = optParser.getTagValue(clientInfo, k);
            System.out.println(k + " : " + v);
        }
    }
    public static void singleTest() {
        String clientInfo = "TPayload=corr_id_%3D10b9ca021740aca46a4f12d2ffafded3%26node_id%3D1a40fbd5899c044b%26REQUEST_GUID%3D17410b9c-a790-a77d-8e02-63b0ffc8bd81%26logid%3Dt6wwm53vpd77%253C%253Dqkisqn47pse31%2528%2560coq1%2Aw%2560ut3542-17410b9ca7d-0x1af%26cal_mod%3Dfalse&TPool=r1lqgsvc&TDuration=3&TStatus=0&TType=URL&ContentLength=2080&ForwardedFor=92.169.117.31;2.17.114.43&Script=/trk20svc/TrackingResource/v1&Server=www.ebay.fr&TMachine=10.119.216.224&TStamp=04:14:05.56&TName=Ginger.CollectionSvc.track&Agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15&RemoteIP=92.169.117.31&Encoding=gzip&Referer=https://www.ebay.fr/sl/list/v2?draft_id=2234338318023&mode=AddItem&productEnforcement=false";
        OptClientInfoParser optParser = new OptClientInfoParser();
        String k = "TPayload";
        String v = optParser.getTagValue(clientInfo, k);
        System.out.println(k + " : " + v);
    }
    public static void main(String args[]) {
        //singleTest();
        test1();
    }
}
