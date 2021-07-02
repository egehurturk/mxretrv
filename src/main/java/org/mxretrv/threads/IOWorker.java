package org.mxretrv.threads;

import org.mxretrv.events.QEventListener;

public class IOWorker implements Worker, QEventListener {
    private final IOQueue<String> inputQueue;
    private int level = 0;

    public IOWorker(IOQueue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void work() {
        System.out.println(inputQueue.poll());
    }

    @Override
    public void onEvent() {
        work();
    }
}

