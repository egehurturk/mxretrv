package org.mxretrv.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mxretrv.dns.MXRecordRetriever;

import javax.naming.NamingException;
import java.util.*;

import static org.mxretrv.App.logInfo;
import static org.mxretrv.App.logWarn;

public class MXWorker implements Worker, Runnable {
    /**
     * Input queue that the worker pushes its job. The worker
     * notifies all event listeners ({@link IOWorker}) so that
     * listeners do their jobs
     */
    private final IOQueue<Map<String, List<String>>> inputQueue;

    /**
     * ArrayList containing domains.
     */
    private final ArrayList<String> domains;

    private final int inputSize;

    private final Logger logger = LogManager.getLogger(MXWorker.class);

    public MXWorker(IOQueue<Map<String, List<String>>> inputQueue, ArrayList<String> domains) {
        if (domains == null || domains.size() == 0)
            throw new IllegalArgumentException("worker " + Thread.currentThread().getName() + " receives input that is null or has 0 size");
        if (inputQueue == null)
            throw new IllegalArgumentException("worker " + Thread.currentThread().getName() + " receives io queue that is null");
        this.inputQueue = inputQueue;
        this.domains = domains;
        this.inputSize = domains.size();
    }

    @Override
    public void work() {
        // Offer the mx records
        Map<String, List<String>> domainMx = new HashMap<>();
        for (String domain: domains) {
            try {
                List<String> records = MXRecordRetriever.mxRecords(domain);
                if (records == null)
                    domainMx.put(domain, Collections.singletonList(""));
                else
                    domainMx.put(domain, records);
            } catch (NamingException e) {
                logWarn(logger, "Naming exception [" + domain + "]: "  + e.getMessage());
                domainMx.put(domain, Collections.singletonList(""));
            }
            logInfo(logger, domain);
        }
        String jsonArrStr = new JSONObject(domainMx).toString(4);
        synchronized (inputQueue) {
            inputQueue.offer(domainMx);
        }

    }

    @Override
    public void run() {
       work();
    }


    public ArrayList<String> getDomains() {
        return domains;
    }

    public int getInputSize() {
        return inputSize;
    }
}


