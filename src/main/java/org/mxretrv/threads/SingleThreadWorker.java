package org.mxretrv.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mxretrv.dns.MXRecordRetriever;
import org.mxretrv.utils.IOUtils;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SingleThreadWorker implements Worker {
    private static final Logger logger = LogManager.getLogger();
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
                domainMx.put(domain, domains);
            } catch (NamingException err) {
                domainMx.put(domain, Collections.singletonList(""));
            }
            
        }
        String json = new JSONObject(domainMx).toString(4);
        if (!IOUtils.writeToFile(json, outputFileStr))
            logger.fatal("cannot write to file");
    }


}
