package org.mxretrv.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public final class IOUtils {

    private IOUtils() { throw new IllegalStateException("Utility Class"); }

    private static final Logger logger = LogManager.getLogger(IOUtils.class);
    private static int ntimes = 0;
    private static int nftimes = 0;
    /**
     * Write the given content into the file.
     * @param content string content
     * @param path absolute path of the file
     * @return boolean value based on success
     */
    public static boolean writeToFile(String content, String path) {
        Path p = Paths.get(path);
        if (!p.isAbsolute()) {
            logger.error("output file path is not absolute");
            return false;
        }
        if (!Files.exists(p)) {
            try {
                Files.createFile(p);
                ntimes++;
            } catch (IOException err) {
                logger.warn("IO error occurred. Cannot create " + path);
                err.printStackTrace();
                return false;
            }
        } else if (ntimes < 1){
            try {
                PrintWriter writer = new PrintWriter(new File(path));
                writer.print("");
                writer.close();
                nftimes++;
            } catch (IOException e) {
                logger.warn("IO error occurred. Cannot delete " + path);
                e.printStackTrace();
            }
        }

        try {
            Files.write(p, content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.warn("IO error occurred. Cannot write to " + path);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeToFile(String content, File out) {
        Path p = Paths.get(out.getAbsolutePath());
        if (!Files.exists(p)) {
            try {
                Files.createFile(p);
                nftimes++;
            } catch (IOException err) {
                logger.warn("IO error occurred. Cannot create " + out.getName());
                err.printStackTrace();
                return false;
            }
        } else if (nftimes < 1) {
            try {
                PrintWriter writer = new PrintWriter(out);
                writer.print("");
                writer.close();
                nftimes++;
            } catch (IOException e) {
                logger.warn("IO error occurred. Cannot delete " + p);
                e.printStackTrace();
            }
        }
        try {
            Files.write(p, content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.warn("IO error occurred. Cannot write to " + out.getName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<String> readFromCsv(String inputFileStr) {
        ArrayList<String> data = new ArrayList<>();

        // Read data into arraylist
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileStr)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line);
                }
            }
        } catch (IOException err) {
            logger.warn("Cannot store CSV file. Stopping execution:");
            logger.info(err.getMessage());
        }

        return data;
    }

    public static ArrayList<String> readFromCsv(String inputFileStr, int nSamples) {
        ArrayList<String> data = new ArrayList<>();
        int n = 0;
        // Read data into arraylist
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(new File(inputFileStr)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (n >= nSamples)
                        break;
                    data.add(line);
                    n++;
                }
            }
        } catch (IOException err) {
            logger.warn("Cannot store CSV file. Stopping execution:");
            logger.info(err.getMessage());
        }

        return data;
    }



}
