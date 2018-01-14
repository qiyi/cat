package org.isouth.cat;

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

public interface Subscriber<T> extends Predicate<Map<String, String>> {
    void next(T t) throws IOException;
}
