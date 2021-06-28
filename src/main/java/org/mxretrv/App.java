package org.mxretrv;


public class App {
    /** List of domain names (passed into args) */
    private static String[] domainNames;
    /** input file ('\n' separated file) containing domain names */
    private static String inputFileStr;
    /** output file (',' separated file) containing MX records */
    private static String outputFileStr;
    /** number of domains to be processed in one thread */
    private static int batchSize;

    private static boolean readFromStdin;
    private static boolean multiThreadingEnabled;
    private static boolean verboseModeEnabled;


    public static void main( String[] args ) {
        if (args.length == 1) { // read from stdin
            readFromStdin = true;
        }


    }

    private static void getOptions() {


    }
}
