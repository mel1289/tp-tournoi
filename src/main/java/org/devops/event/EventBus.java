package org.devops.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private final Map<String, List<EventListener>> listeners = new HashMap<>();

    public synchronized void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public synchronized void publish(String eventType, String eventData) {
        List<EventListener> eventListeners = listeners.getOrDefault(eventType, List.of());
        for (EventListener listener : eventListeners) {
            listener.onEvent(eventType, eventData);
        }
    }

    public interface EventListener {
        void onEvent(String eventType, String eventData);
    }
}

