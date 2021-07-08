package org.mxretrv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mxretrv.threads.IOQueue;
import org.mxretrv.threads.IOWorker;
import org.mxretrv.threads.MXWorker;
import org.mxretrv.threads.SingleThreadWorker;
import org.mxretrv.utils.ArgumentParser;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    protected static Logger logger = LogManager.getLogger(App.class);
    /** input file ('\n' separated file) containing domain names */
    private static String inputFileStr;
    /** output file (',' separated file) containing MX records */
    private static String outputFileStr;
    /** number of domains to be processed in one thread */
    private static int batchSize;
    private static long nThreads;
    public static long remSize;
    public static long remThreads;

    private static boolean multiThreadingEnabled;
    private static boolean verboseModeEnabled;
    private static boolean useStdout;

    /**
     * Nthreads = Ncpu * Ucpu * (1 + W/C)
     * Bsize = Ndomains / Nthreads
     */
    private static int DEFAULT_BATCH_SIZE;


    public static void main( String[] args ) throws IOException, NamingException, InterruptedException {
        setCommandLineOptions(args);
        logOptions();

        if (!multiThreadingEnabled) {
            System.out.println("using single thread");
            long startTime = System.currentTimeMillis();
            SingleThreadWorker s = new SingleThreadWorker(inputFileStr, outputFileStr);
            s.work();
            long endTime = System.currentTimeMillis();
            System.out.println("That took " + (endTime - startTime) + " milliseconds, " + (endTime - startTime) / 1000 + " seconds");
        }

        IOQueue<Map<String, List<String>>> queue = new IOQueue<>();

        ArrayList<String> data = new ArrayList<>();

        int nSamples = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileStr)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (nSamples >= 40)
                    break;
                data.add(line);
                nSamples++;
            }
        }

        logInfo(logger, "data size: " + data.size());

        List<Runnable> tasks = new ArrayList<>();

        int TOTAL_SIZE = data.size();
        batchSize = Math.min(TOTAL_SIZE, batchSize);
        int THREAD_N   = TOTAL_SIZE / batchSize;
        logInfo(logger, "threads: " + THREAD_N);
        logInfo(logger, "batch size: " + batchSize);

        // test with 100 threads --> each thread with 507,000 / 100 = 5070 size
        for (int i = 0; i < THREAD_N; i++) {
            ArrayList<String> batch = new ArrayList<>(data.subList(i * batchSize, (i+1) * batchSize));
            tasks.add(new MXWorker(queue, batch));
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_N);
        long startTime = System.currentTimeMillis();
        logInfo(logger, "task size: " + tasks.size());
        for (Runnable r: tasks)
            pool.execute(r);
        pool.shutdown();

        boolean finished = pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        if (!finished) {
            pool.shutdownNow();
            if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("Pool did not terminate");
        }

        IOWorker ioWorker = new IOWorker(queue, inputFileStr, outputFileStr);
        ioWorker.work();
        long endTime = System.currentTimeMillis();

        if (!useStdout) {
            if (check(Files.readString(Paths.get(outputFileStr), StandardCharsets.US_ASCII), TOTAL_SIZE))
                System.out.println("✅");
            else
                System.out.println("❌");
        }

        System.out.println("That took " + (endTime - startTime) + " milliseconds, " + (endTime - startTime) / 1000 + " seconds");
    }

    private static boolean check(String contents, int dataSize) {
        JSONObject jsonObject = new JSONObject(contents.trim());

        Iterator<String> keys = jsonObject.keys();
        int count = 0;
        while(keys.hasNext()) {
            keys.next();
            count++;
        }
        logInfo(logger, count);
        return count == dataSize;

    }

    /**
     * Set parameters from command line.
     * @param args argument vector
     */
    private static void setCommandLineOptions(String[] args) {
        ArgumentParser parser = new ArgumentParser(args);
        inputFileStr = parser.getInputArgument() ;

        outputFileStr = parser.getOutputArgument() == null ? "" : parser.getOutputArgument();
        useStdout = outputFileStr.equals("");
        multiThreadingEnabled = parser.getMultiArgument();
        verboseModeEnabled = parser.getVerboseArgument();
        batchSize = computeBatchSize(parser);
    }

    /**
     * Compute the ideal batch size, given the size of the input
     * @param parser argument parser to check existance of batch option
     * @return value of batch size
     */
    private static int computeBatchSize(ArgumentParser parser) {
        int b_t;
        DEFAULT_BATCH_SIZE = 1000;

        if (parser.getBatchArgument() == null && multiThreadingEnabled)
            b_t = DEFAULT_BATCH_SIZE;
        else if (!multiThreadingEnabled || parser.getBatchArgument() == null)
            b_t = 0;
        else
            b_t = Integer.parseInt(parser.getBatchArgument());
        return b_t;
    }

    /**
     * Log options if verbose mode enabled
     */
    private static void logOptions() {
        if (!verboseModeEnabled)
            return;
       logInfo(logger, "input file: " + inputFileStr);
       logInfo(logger, "output file: " + outputFileStr);
       logInfo(logger, "batch size: " + batchSize);
       logInfo(logger, "using stdout " + useStdout);
       logInfo(logger, "thread number: " + nThreads);
       logInfo(logger, "remaining work: " + remSize);
       logInfo(logger, "remaining number of threads: " + remThreads);
    }

    public static void logInfo(Logger logger, String msg) {
        if (verboseModeEnabled)
            logger.info(msg);
    }

    public static void logWarn(Logger logger, String msg) {
        if (verboseModeEnabled)
            logger.warn(msg);
    }

    private static void logInfo(Logger logger, int msg) {
        if (verboseModeEnabled)
            logger.info(msg);
    }

    private static void logWarn(Logger logger, int msg) {
        if (verboseModeEnabled)
            logger.warn(msg);
    }

    private static void logInfo(Logger logger, double msg) {
        if (verboseModeEnabled)
            logger.info(msg);
    }

    private static void logWarn(Logger logger, double msg) {
        if (verboseModeEnabled)
            logger.warn(msg);
    }

    // Getters, setters
    public static String getInputFileStr() { return inputFileStr; }

    public static String getOutputFileStr() { return outputFileStr; }

    public static long getBatchSize() { return batchSize; }

    public static boolean isMultiThreadingEnabled() { return multiThreadingEnabled; }

    public static boolean isVerboseModeEnabled() { return verboseModeEnabled; }

    public static boolean isUseStdout() { return useStdout; }

    public static int getDefaultBatchSize() { return DEFAULT_BATCH_SIZE; }

}

/*

TODO:
    - [] Refactor thread - batch division
        - Default batch size is 100
    - [] Test on inputs
 */