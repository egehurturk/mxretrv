package org.mxretrv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mxretrv.threads.ThreadOptimizer;
import org.mxretrv.utils.ArgumentParser;

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


    public static void main( String[] args ) {
        setCommandLineOptions(args);
        logOptions();

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
        if (N <= 30_000)
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
