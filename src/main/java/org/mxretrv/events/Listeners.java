package org.mxretrv.events;

import java.util.Collection;
import java.util.HashSet;

public class Listeners {
    private static final Collection<QEventListener> listeners = new HashSet<>();

    public static void register(QEventListener evList) {
        listeners.add(evList);
    }

    /**
     * Broadcast the event
     */
    public static void incomingJob() {
        for (QEventListener listener: listeners)
            listener.onEvent();
    }

}
