package org.mxretrv.threads;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;

public class IOQueue<T> extends AbstractQueue<T> {
    /** queue data */
    private LinkedList<T> elements;

    public IOQueue() {
        this.elements = new LinkedList<>();
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
        if(t == null) return false;
        elements.add(t);
        return true;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T poll() {
        Iterator<T> iter = elements.iterator();
        T t = iter.next();
        if(t != null){
            iter.remove();
            return t;
        }
        return null;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        return elements.getFirst();
    }
}
