package org.mxretrv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class InputCSVProcessor {
    protected static Logger logger = LogManager.getLogger(InputCSVProcessor.class);
    /** absolute path to input file */
    private String inputFileName;
    /** file representation of {@link #inputFileName}*/
    private File inputFile;
    /** number of lines, also size of input. {@link ThreadOptimizer#computeBatchSize(long)} */

    public InputCSVProcessor(String inputFileName) {
        if (inputFileName == null || inputFileName.equals(""))
            throw new IllegalArgumentException("input file name cannot be null or empty");
        this.inputFileName = inputFileName;
        File temp = new File(inputFileName);
        if (!temp.exists() || temp.isDirectory() || !temp.isAbsolute())
            throw new IllegalArgumentException("input file does not exist, or is a directory, or the path is not an " +
                    "absolute path");
        inputFile = temp;
    }
}
