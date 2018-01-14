package org.isouth.cat.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.AppenderBase;
import org.isouth.cat.Cat;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

import java.util.Map;

public class CatAppender extends AppenderBase<ILoggingEvent> {
    private final Cat cat;
    private PatternLayout layout;

    public CatAppender(Cat cat, LoggerContext context) {
        this.cat = cat;
        this.setContext(context);
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        patternLayout.start();
        this.layout = patternLayout;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (cat.isWritable()) {
            Map<String, String> context;
            MDCAdapter adapter = MDC.getMDCAdapter();
            if (adapter instanceof LogbackMDCAdapter) {
                LogbackMDCAdapter mdc = (LogbackMDCAdapter) adapter;
                context = mdc.getPropertyMap();
            } else {
                context = MDC.getCopyOfContextMap();
            }
            String text = this.layout.doLayout(event);
            cat.write(context, text);
        }
    }
}
