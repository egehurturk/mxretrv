package org.mxretrv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mxretrv.threads.IOQueue;
import org.mxretrv.threads.IOWorker;
import org.mxretrv.threads.MXWorker;
import org.mxretrv.threads.ThreadOptimizer;
import org.mxretrv.threads.SingleThreadWorker;
import org.mxretrv.utils.ArgumentParser;

import javax.naming.NamingException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    protected static Logger logger = LogManager.getLogger(App.class);
    /** input file ('\n' separated file) containing domain names */
    private static String inputFileStr;
    private static long N;
    /** output file (',' separated file) containing MX records */
    private static String outputFileStr;
    /** number of domains to be processed in one thread */
    private static long batchSize;
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
    private static long DEFAULT_BATCH_SIZE;


    public static void main( String[] args ) throws IOException, NamingException, InterruptedException {
        setCommandLineOptions(args);
        logOptions();

        if (!multiThreadingEnabled) {
            long startTime = System.currentTimeMillis();
            SingleThreadWorker s = new SingleThreadWorker(inputFileStr, outputFileStr);
            s.work();
            long endTime = System.currentTimeMillis();
            System.out.println("That took " + (endTime - startTime) + " milliseconds, " + (endTime - startTime) / 100 + " seconds");
        }

        IOQueue<Map<String, List<String>>> queue = new IOQueue<>();

        ArrayList<String> data = new ArrayList<>();

        int nSamples = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileStr)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (nSamples >= 1000)
                    break;
                data.add(line);
                nSamples++;
            }
        }

        logger.info("data size: " + data.size());

        List<Runnable> tasks = new ArrayList<>();

        int TOTAL_SIZE = data.size();
        int THREAD_N   = 100;
        int BATCH_SIZE = TOTAL_SIZE / THREAD_N;

        logger.info("threads: " + THREAD_N);
        logger.info("batch size: " + BATCH_SIZE);

        // test with 100 threads --> each thread with 507,000 / 100 = 5070 size
        for (int i = 0; i < THREAD_N; i++) {
            ArrayList<String> batch = new ArrayList<>(data.subList(i * BATCH_SIZE, (i+1) * BATCH_SIZE));
            tasks.add(new MXWorker(queue, batch));
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_N);
        long startTime = System.currentTimeMillis();
        logger.info("task size: " + tasks.size());
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

        if (check(Files.readString(Paths.get(outputFileStr), StandardCharsets.US_ASCII), TOTAL_SIZE))
            System.out.println("nice work!");
        else
            System.out.println("noooo!");

        System.out.println("That took " + (endTime - startTime) + " milliseconds, " + (endTime - startTime) / 100 + " seconds");
    }

    private static boolean check(String contents, int dataSize) {
        JSONObject jsonObject = new JSONObject(contents.trim());

        Iterator<String> keys = jsonObject.keys();
        int count = 0;
        while(keys.hasNext()) {
            keys.next();
            count++;
        }
        logger.info(count);
        return count == dataSize;

    }

    /**
     * Set parameters from command line.
     * @param args argument vector
     */
    private static void setCommandLineOptions(String[] args) {
        ArgumentParser parser = new ArgumentParser(args);
        inputFileStr = parser.getInputArgument() ;
        N = Long.parseLong(parser.getN());

        outputFileStr = parser.getOutputArgument() == null ? "" : parser.getOutputArgument();
        useStdout = outputFileStr.equals("");
        multiThreadingEnabled = parser.getMultiArgument();
        verboseModeEnabled = parser.getVerboseArgument();

        if (multiThreadingEnabled) {
            batchSize = computeBatchSize(N, parser);
            // remSize = n mod batchSize
            // a = nq + r, n = nThreads, q = batchSize, r = remainder
            nThreads =  N / batchSize;
            remSize = N - batchSize * nThreads;
            remThreads = (int) Math.ceil( ((float) remSize) / batchSize ); // always = 1
        } else {
            batchSize = N;
            nThreads = 1;
            remSize = remThreads = 0;
        }
    }

    /**
     * Compute the ideal batch size, given the size of the input
     * @param N size of input
     * @param parser argument parser to check existance of batch option
     * @return value of batch size
     */
    private static long computeBatchSize(long N, ArgumentParser parser) {
        long b_t;
        DEFAULT_BATCH_SIZE = ThreadOptimizer.computeBatchSize(N);
        if (N <= 20_000)
            DEFAULT_BATCH_SIZE = 5000;

        if (parser.getBatchArgument() == null && multiThreadingEnabled)
            b_t = DEFAULT_BATCH_SIZE;
        else if (!multiThreadingEnabled || parser.getBatchArgument() == null)
            b_t = 0;
        else
            b_t = Integer.parseInt(parser.getBatchArgument());
        return Math.min(b_t, N);
    }

    /**
     * Log options if verbose mode enabled
     */
    private static void logOptions() {
        if (!verboseModeEnabled)
            return;
       logger.info("input file: " + inputFileStr);
       logger.info("output file: " + outputFileStr);
       logger.info("batch size: " + batchSize);
       logger.info("using stdout " + useStdout);
       logger.info("thread number: " + nThreads);
       logger.info("remaining work: " + remSize);
       logger.info("remaining number of threads: " + remThreads);
    }

    // Getters, setters
    public static String getInputFileStr() { return inputFileStr; }

    public static String getOutputFileStr() { return outputFileStr; }

    public static long getBatchSize() { return batchSize; }

    public static boolean isMultiThreadingEnabled() { return multiThreadingEnabled; }

    public static boolean isVerboseModeEnabled() { return verboseModeEnabled; }

    public static boolean isUseStdout() { return useStdout; }

    public static long getDefaultBatchSize() { return DEFAULT_BATCH_SIZE; }

}
