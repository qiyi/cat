package org.isouth.cat.tomcat;

import org.isouth.cat.Subscriber;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

public class CatWSSubscriber implements Subscriber<String> {

    private final RemoteEndpoint.Basic basicRemote;
    private Predicate<Map<String, String>> filter;

    public CatWSSubscriber(RemoteEndpoint.Basic basicRemote) {
        this.basicRemote = basicRemote;
    }

    public CatWSSubscriber setFilter(Predicate<Map<String, String>> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public void next(String s) throws IOException {
        this.basicRemote.sendText(s, true);
    }

    @Override
    public boolean test(Map<String, String> context) {
        return filter.test(context);
    }
}
