package org.mxretrv.threads;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class IOQueue<T> extends AbstractQueue<T> {
    /** queue data */
    private final LinkedBlockingQueue<T> elements;

    public IOQueue() {
        this.elements = new LinkedBlockingQueue<>();
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    /**
     * Inserts the specified element into this queue
     *
     * @param t the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     */
    @Override
    public boolean offer(T t) {
        return elements.offer(t);
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T poll() {
        return elements.poll();
    }

    public T poll(long timeout, TimeUnit unit) {
        try {
            return elements.poll(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        return elements.peek();
    }
}
