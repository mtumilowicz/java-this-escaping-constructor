import java.awt.*;
import java.util.EventListener;

class EventListener2 {
    EventListener2(EventSource eventSource) {

        eventSource.registerListener(
                new EventListener() {
                    public void onEvent(Event e) {
                        eventReceived(e);
                    }
                });
    }

    void eventReceived(Event e) {
    }
}

class EventSource {
    void registerListener(EventListener listener) {

    }
}