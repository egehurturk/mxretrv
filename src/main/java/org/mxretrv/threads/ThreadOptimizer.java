package org.mxretrv.threads;

public class ThreadOptimizer {
    /** Number of cores present in JVM */
    private static final int numberOfCores = Runtime.getRuntime().availableProcessors() / 2;

    /** CPU utilization coefficient */
    private static final float cpuUtilization = 0.8f;

    /** ratio of wait time to compute time */
    private static final float wc = 50;

    /** number of optimum threads */
    public static long nThreads;

    public static long computeBatchSize(long N) {
        nThreads = (int) (numberOfCores * cpuUtilization * (1 + wc));
        return N / nThreads;
    }
}
