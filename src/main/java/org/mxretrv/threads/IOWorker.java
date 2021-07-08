package org.mxretrv.threads;

import org.json.JSONObject;
import org.mxretrv.App;
import org.mxretrv.utils.IOUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOWorker implements Worker {
    /**
     * Input queue that this class listens on
     */
    private final IOQueue<Map<String, List<String>>> inputQueue;

    private final File inputFile;

    private final File outputFile;

    public IOWorker(IOQueue<Map<String, List<String>>> inputQueue, String inputFileStr, String outputFileStr) {
        if (inputQueue == null)
            throw new IllegalArgumentException("IOWorker " + Thread.currentThread().getName() + " receives io queue that is null");
        this.inputQueue = inputQueue;
        File temp1 = new File(inputFileStr);
        File temp2 = new File(outputFileStr);

        if (!temp1.exists())
            throw new IllegalArgumentException("input file doesn't exist");
        if (!temp1.isFile())
            throw new IllegalArgumentException("input file is not a regular file");
        if (! Paths.get(inputFileStr).isAbsolute())
            throw new IllegalArgumentException("input file is not an absolute path");
        if (!App.isUseStdout() && !Paths.get(outputFileStr).isAbsolute())
            throw new IllegalArgumentException("output file is not an absolute path");
        this.inputFile = temp1;
        this.outputFile = temp2;
    }

    @Override
    public void work() {
        Map<String, List<String>> all = new HashMap<>();
        for (Map<String, List<String>> map: inputQueue) {
            all.putAll(map);
        }
        String json = new JSONObject(all).toString(4);
        if (App.isUseStdout())
            System.out.println("\n\n\n" + json + "\n\n\n");
        else
            IOUtils.writeToFile(json, outputFile);
    }


    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    @Override
    public String toString() {
        return "IOWorker";
    }
}

