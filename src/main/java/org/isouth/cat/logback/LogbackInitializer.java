package org.isouth.cat.logback;

import ch.qos.logback.classic.LoggerContext;
import org.isouth.cat.Cat;
import org.isouth.cat.CatInitializer;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public class LogbackInitializer implements CatInitializer {
    @Override
    public void initialize(Cat cat) {
        ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
        if (loggerFactory instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) loggerFactory;
            loggerContext.addListener(new CatLoggerListener(cat, loggerContext)
                    .initialize(loggerContext));
        }

    }
}
