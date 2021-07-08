package org.mxretrv.threads;

import org.json.JSONObject;
import org.mxretrv.dns.MXRecordRetriever;
import org.mxretrv.utils.IOUtils;

import javax.naming.NamingException;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleThreadWorker implements Worker {
    private String inputFileStr;
    private String outputFileStr;

    public SingleThreadWorker(String inputFileStr, String outputFileStr) {
        this.inputFileStr = inputFileStr;
        this.outputFileStr = outputFileStr;
    }

    public String getInputFileStr() {
        return inputFileStr;
    }

    public void setInputFileStr(String inputFileStr) {
        this.inputFileStr = inputFileStr;
    }

    public String getOutputFileStr() {
        return outputFileStr;
    }

    public void setOutputFileStr(String outputFileStr) {
        this.outputFileStr = outputFileStr;
    }

    @Override
    public void work() throws IOException {
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileStr)))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        }
        Map<String, List<String>> domainMx = new HashMap<>();
        for (String domain: data) {
            try {
                List<String> domains = MXRecordRetriever.mxRecords(domain);
                if (domains == null)
                    domainMx.put(domain, Collections.singletonList(""));    
                else
                    domainMx.put(domain, domains);    
            } catch (NamingException err) {
                domainMx.put(domain, Collections.singletonList(""));
            }
            
        }
        String json = new JSONObject(domainMx).toString(4);
        if (!IOUtils.writeToFile(json, outputFileStr))
            System.err.println("cannot write to file");
    }


}
