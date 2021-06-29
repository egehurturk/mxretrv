package org.mxretrv;

import java.util.ArrayList;

public class App {
    /** List of domain names (passed into args) */
    private static ArrayList<String> domains = new ArrayList<>();
    /** input file ('\n' separated file) containing domain names */
    private static String inputFileStr;
    /** output file (',' separated file) containing MX records */
    private static String outputFileStr;
    /** number of domains to be processed in one thread */
    private static int batchSize;

    private static boolean multiThreadingEnabled;
    private static boolean verboseModeEnabled;
    private static boolean useStdout;

    public static final int DEFAULT_BATCH_SIZE = 1;


    public static void main( String[] args ) {
        setCommandLineOptions(args);
    }

    private static void setCommandLineOptions(String[] args) {
        ArgumentParser parser = new ArgumentParser(args);
        inputFileStr = parser.getInputArgument() == null ? "" : parser.getInputArgument() ;
        outputFileStr = parser.getOutputArgument() == null ? "" : parser.getOutputArgument();
        useStdout = outputFileStr.equals("");
        multiThreadingEnabled = parser.getMultiArgument();
        verboseModeEnabled = parser.getVerboseArgument();

        if (parser.getBatchArgument() == null && multiThreadingEnabled)
            // if multi-threading is enabled and batch size argument is missing, set it to default value
            batchSize = DEFAULT_BATCH_SIZE;
        else if (!multiThreadingEnabled || parser.getBatchArgument() == null)
            // if multi-threading is not enabled or batch size argument is missing, ignore it
            batchSize = 0;
        else
            batchSize = Integer.parseInt(parser.getBatchArgument());
    }

    private static void printOptions() {
        System.out.println(inputFileStr);
        System.out.println(outputFileStr);
        System.out.println(batchSize);
        System.out.println(multiThreadingEnabled);
        System.out.println(verboseModeEnabled);
        System.out.println(useStdout);
    }
}

//