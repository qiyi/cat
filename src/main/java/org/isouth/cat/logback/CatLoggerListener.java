package org.isouth.cat.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import org.isouth.cat.Cat;

import java.util.Iterator;
import java.util.List;

public class CatLoggerListener implements LoggerContextListener {
    private final Cat cat;
    private CatAppender catAppender;
    private LoggerContext context;

    public CatLoggerListener(Cat cat, LoggerContext context) {
        this.cat = cat;
        this.catAppender = new CatAppender(cat, context);
        this.catAppender.start();
    }

    public CatLoggerListener initialize(LoggerContext context) {
        StatusManager statusManager = context.getStatusManager();
        statusManager.add(new InfoStatus("Register " + this.getClass().getName(), this));
        fly(context);
        return this;
    }

    public CatLoggerListener reinitialize(LoggerContext context) {
        fly(context);
        return this;
    }

    private void fly(LoggerContext context) {
        List<Logger> loggers = context.getLoggerList();
        for (Logger logger : loggers) {
            Iterator<Appender<ILoggingEvent>> iterator = logger.iteratorForAppenders();
            if (iterator.hasNext()) {
                if (!logger.isAttached(catAppender)) {
                    logger.addAppender(catAppender);
                }
            }
        }
    }

    @Override

    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext context) {
    }

    @Override
    public void onReset(LoggerContext context) {
        context.putObject(CatLoggerListener.class.getName(), this);
    }

    @Override
    public void onStop(LoggerContext context) {
        context.putObject(CatLoggerListener.class.getName(), this);
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
