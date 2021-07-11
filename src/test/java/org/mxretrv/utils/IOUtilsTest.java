package org.mxretrv.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IOUtilsTest {
    String fileName = "hello.txt";
    Path path;

    @BeforeEach
    void setUp() {
        path = Paths.get(fileName);
    }

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        Thread.sleep(5000);
        Files.delete(path);
    }


    @Test
    void writeToFile() throws IOException {
        String str = "Hello";
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);

        String read = Files.readAllLines(path).get(0);
        assertEquals(str, read);
    }
}