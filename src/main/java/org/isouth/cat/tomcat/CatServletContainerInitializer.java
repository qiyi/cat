package org.isouth.cat.tomcat;

import org.isouth.cat.Cat;

import javax.servlet.*;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.util.EnumSet;
import java.util.Set;

public class CatServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic filter = servletContext.addFilter("logcat", CatFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        Cat cat = new Cat().init();
        ServerContainer serverContainer = (ServerContainer) servletContext.getAttribute(ServerContainer.class.getName());
        try {
            serverContainer.addEndpoint(ServerEndpointConfig.Builder
                    .create(CatEndpoint.class, "/logcat")
                    .configurator(new CatConfigurator(cat)).build());
        } catch (DeploymentException e) {
            throw new ServletException(e);
        }
    }
}
