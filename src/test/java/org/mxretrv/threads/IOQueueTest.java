package org.mxretrv.threads;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("I/O Queue Tests")
public class IOQueueTest {
    private IOQueue<Integer> ioQueue;

    @BeforeEach
    public void setUp() {
        ioQueue = new IOQueue<>();
    }

    @Test
    @DisplayName("test addition")
    void testQueueAdd() {
        for (int i = 0; i < 11; i++)
            Assertions.assertTrue(ioQueue.offer(i));
    }

    @Test
    @DisplayName("test size")
    void testQueueSize() {
        for (int i = 0; i < 11; i++)
            Assertions.assertTrue(ioQueue.offer(i));
        Assertions.assertEquals(ioQueue.size(), 11);
    }

    @Test
    @DisplayName("test poll")
    void testQueuePoll() {
        for (int i = 0; i < 11; i++)
            Assertions.assertTrue(ioQueue.offer(i));
        int first = ioQueue.poll();
        int last = ioQueue.poll();
        Assertions.assertEquals(first, 0);
        Assertions.assertEquals(last, 1);
        Assertions.assertDoesNotThrow(() -> ioQueue.poll());
        Assertions.assertDoesNotThrow(() -> ioQueue.poll());
    }

    @Test
    @DisplayName("test peek")
    void testQueuePeek() {
        for (int i = 0; i < 11; i++)
            Assertions.assertTrue(ioQueue.offer(i));
        int first = ioQueue.peek();
        Assertions.assertEquals(11, ioQueue.size());
        Assertions.assertEquals(first, 0);
    }


}
