package org.isouth.cat;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cat {
    private List<Subscriber<String>> subscribers = new CopyOnWriteArrayList<>();

    public Cat init() {
        ServiceLoader<CatInitializer> loader = ServiceLoader.load(CatInitializer.class);
        Iterator<CatInitializer> iterator = loader.iterator();
        while (iterator.hasNext()) {
            try {
                CatInitializer initializer = iterator.next();
                initializer.initialize(this);
            } catch (RuntimeException e) {
                System.out.println("CatInitializer initialize failed." + e);
            }

        }
        return this;
    }

    public boolean isWritable() {
        return !subscribers.isEmpty();
    }

    public void write(Map<String, String> context, String log) {
        for (Subscriber<String> subscriber : subscribers) {
            if (subscriber.test(context)) {
                try {
                    subscriber.next(log);
                } catch (IOException e) {
                    System.err.println(e);
                }

            }
        }
    }

    public Subscription subscribe(Subscriber<String> subscriber) {
        subscribers.add(subscriber);
        return () -> subscribers.remove(subscriber);
    }
}
