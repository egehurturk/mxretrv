package org.mxretrv.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final List<String> domains;

    private final int inputSize;

    private final Logger logger = LogManager.getLogger(MXWorker.class);

    public MXWorker(IOQueue<Map<String, List<String>>> inputQueue, List<String> domains) {
        Objects.requireNonNull(domains);
        Objects.requireNonNull(inputQueue);

        if (domains.isEmpty())
            throw new IllegalArgumentException("worker " + Thread.currentThread().getName() + " receives input that is null or has 0 size");
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
                domainMx.put(domain, records);
            } catch (NamingException e) {
                logWarn(logger, "Naming exception [" + domain + "]: "  + e.getMessage());
                domainMx.put(domain, Collections.singletonList(""));
            }
            logInfo(logger, domain);
        }
        synchronized (inputQueue) {
            inputQueue.offer(domainMx);
        }

    }

    @Override
    public void run() {
       work();
    }


    public List<String> getDomains() {
        return domains;
    }

    public int getInputSize() {
        return inputSize;
    }
}


