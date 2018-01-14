package org.isouth.cat.tomcat;

import org.isouth.cat.Cat;

import javax.websocket.server.ServerEndpointConfig;

public class CatConfigurator extends ServerEndpointConfig.Configurator {
    private final CatEndpoint endpoint;

    public CatConfigurator(Cat cat) {
        this.endpoint = new CatEndpoint(cat);
    }

    public <T extends Object> T getEndpointInstance(Class<T> clazz)
            throws InstantiationException {
        //noinspection unchecked
        return (T) this.endpoint;
    }
}
