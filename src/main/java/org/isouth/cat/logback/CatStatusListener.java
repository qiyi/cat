package org.isouth.cat.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;

import java.util.List;

public class CatStatusListener implements StatusListener {
    private CatLoggerListener listener;

    @Override
    public void addStatusEvent(Status status) {
        Object origin = status.getOrigin();
        if (origin instanceof CatLoggerListener) {
            // Cat 应用加载事件
            this.listener = ((CatLoggerListener) origin);
        } else if (origin instanceof JoranConfigurator) {
            // 配置加载事件
            JoranConfigurator configurator = (JoranConfigurator) origin;
            LoggerContext context = (LoggerContext) configurator.getContext();
            CatLoggerListener listener = (CatLoggerListener) context.getObject(CatLoggerListener.class.getName());
            if (listener != null && this.listener == null) {
                this.listener = listener;
                List<LoggerContextListener> copyOfListenerList = context.getCopyOfListenerList();
                if (!copyOfListenerList.contains(this.listener)) {
                    this.listener.reinitialize(context);
                    context.addListener(this.listener);
                }
            }
        }
    }
}
