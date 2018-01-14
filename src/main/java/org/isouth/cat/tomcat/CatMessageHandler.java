package org.isouth.cat.tomcat;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class CatMessageHandler implements MessageHandler.Whole<String> {
    private final CatEndpoint endpoint;
    private final Session session;
    private CatWSSubscriber subscriber;

    public CatMessageHandler(CatEndpoint endpoint, Session session) {
        this.endpoint = endpoint;
        this.session = session;
    }

    private Predicate<Map<String, String>> buildFilter(String message) {
        if (message == null || message.isEmpty()) {
            return (c) -> true;
        }
        Predicate<Map<String, String>> filter = (c) -> false;
        String[] rules = message.split("\n");
        for (String rule : rules) {
            Predicate<Map<String, String>> ruleFilter = (c) -> true;
            String[] matches = rule.split("&");
            for (String match : matches) {
                String[] kv = match.split("=");
                String k = kv[0];
                String v = kv.length > 1 ? kv[1] : null;
                if (v != null && v.contains(",")) {
                    List<String> vv = Arrays.asList(v.split(","));
                    ruleFilter = ruleFilter.and((c) -> vv.contains(c.get(k)));
                } else {
                    ruleFilter = ruleFilter.and((c) -> Objects.equals(c.get(k), v));
                }
            }
            filter = filter.or(ruleFilter);
        }
        return filter;
    }

    @Override
    public void onMessage(String message) {
        Predicate<Map<String, String>> filter = buildFilter(message);
        if (this.subscriber != null) {
            this.subscriber.setFilter(filter);
        } else {
            this.subscriber = new CatWSSubscriber(session.getBasicRemote())
                    .setFilter(filter);
            this.endpoint.subscribe(this.session, this.subscriber);
        }
    }
}
