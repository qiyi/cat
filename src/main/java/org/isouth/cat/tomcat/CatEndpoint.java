package org.isouth.cat.tomcat;

import org.isouth.cat.Cat;
import org.isouth.cat.Subscriber;
import org.isouth.cat.Subscription;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CatEndpoint extends Endpoint {
    private final Cat cat;
    private final ConcurrentMap<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    public CatEndpoint(Cat cat) {
        this.cat = cat;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler(String.class, new CatMessageHandler(this, session));
    }

    public void onClose(Session session, CloseReason closeReason) {
        Subscription subscription = subscriptions.remove(session.getId());
        if (subscription != null) {
            subscription.cancel();
        }
    }

    public void subscribe(Session session, Subscriber<String> subscriber) {
        Subscription subscription = cat.subscribe(subscriber);
        subscriptions.put(session.getId(), subscription);
    }
}
