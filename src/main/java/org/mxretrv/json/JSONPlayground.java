package org.mxretrv.json;

import org.json.JSONObject;
import org.mxretrv.dns.MXRecordRetriever;
import org.mxretrv.utils.IOUtils;

import javax.naming.NamingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONPlayground {
    public static void main(String[] args) throws NamingException {
        Map<String, List<String>> domainMx = new HashMap<>();
        domainMx.put("octeth.com", MXRecordRetriever.mxRecords("octeth.com"));
        domainMx.put("amazon.com", MXRecordRetriever.mxRecords("amazon.com"));
        domainMx.put("google.com", MXRecordRetriever.mxRecords("google.com"));
        String json = new JSONObject(domainMx).toString(4);
        if (!IOUtils.writeToFile(json, "/Users/egehurturk/Development/Mxretrv/src/main/java/org/mxretrv/json/out.json"))
            System.err.println("cannot write to file");
    }
}
