package org.mxretrv.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class IOUtils {
    private static final Logger logger = LogManager.getLogger(IOUtils.class);
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
        try {
            Files.createFile(p);
        } catch (IOException err) {
            logger.warn("IO error occurred. Cannot create " + path);
            return false;
        }
        try {
            Files.write(p, content.getBytes());
        } catch (IOException e) {
            logger.warn("IO error occurred. Cannot write to " + path);
            return false;
        }
        return true;
    }

}
