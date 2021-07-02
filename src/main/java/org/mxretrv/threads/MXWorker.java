package org.mxretrv.threads;

import org.mxretrv.events.Listeners;

public class MXWorker implements Worker, Runnable {
    private final IOQueue<String> inputQueue;
    String thing;

    public MXWorker(IOQueue<String> inputQueue, String t) {
        this.inputQueue = inputQueue;
        thing = t;
    }

    @Override
    public void work() {
        inputQueue.offer(thing);
        Listeners.incomingJob();
    }

    @Override
    public void run() {
       work();
    }


}
