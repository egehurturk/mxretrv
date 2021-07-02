package org.mxretrv.threads;

import org.mxretrv.events.Listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dummy {
    public static void main(String[] args) { ;
        IOQueue<String> ioQueue = new IOQueue<>();
        Listeners.register(new IOWorker(ioQueue));
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(new MXWorker(ioQueue, "thread-1"));
        executorService.execute(new MXWorker(ioQueue, "thread-2"));
        executorService.execute(new MXWorker(ioQueue, "thread-3"));
        executorService.shutdown();
    }
}


