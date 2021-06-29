package org.mxretrv;

public class App {
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
        printOptions();
    }

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
            nThreads =  N / batchSize;
            remSize = N - batchSize * nThreads;
            remThreads = (int) Math.ceil( ((float) remSize) / batchSize );
        } else {
            batchSize = N;
            nThreads = 1;
            remSize = remThreads = 0;
        }
    }

    private static long computeBatchSize(long N, ArgumentParser parser) {
        long b_t;
        DEFAULT_BATCH_SIZE = ThreadOptimizer.computeBatchSize(N);
        if (N <= 40_000)
            DEFAULT_BATCH_SIZE = 5000;

        if (parser.getBatchArgument() == null && multiThreadingEnabled)
            b_t = DEFAULT_BATCH_SIZE;
        else if (!multiThreadingEnabled || parser.getBatchArgument() == null)
            b_t = 0;
        else
            b_t = Integer.parseInt(parser.getBatchArgument());
        return Math.min(b_t, N);
    }

    private static void printOptions() {
        System.out.println("input file: " + inputFileStr);
        System.out.println("output file: " + outputFileStr);
        System.out.println("batch size: " + batchSize);
        System.out.println("multi? " + multiThreadingEnabled);
        System.out.println("verbose? " + verboseModeEnabled);
        System.out.println("stdout? " + useStdout);
        System.out.println("thread #: " + nThreads);
        System.out.println("remaining size: " + remSize);
        System.out.println("remaining number of threads: " + remThreads);
    }

    public static String getInputFileStr() { return inputFileStr; }

    public static String getOutputFileStr() { return outputFileStr; }

    public static long getBatchSize() { return batchSize; }

    public static boolean isMultiThreadingEnabled() { return multiThreadingEnabled; }

    public static boolean isVerboseModeEnabled() { return verboseModeEnabled; }

    public static boolean isUseStdout() { return useStdout; }

    public static long getDefaultBatchSize() { return DEFAULT_BATCH_SIZE; }

}
