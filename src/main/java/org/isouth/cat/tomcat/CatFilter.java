package org.isouth.cat.tomcat;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class CatFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String clientIP = httpServletRequest.getHeader("X-Forwarded-For");
            if (clientIP == null) {
                clientIP = httpServletRequest.getRemoteAddr();
            }
            MDC.put("req.clientIP", clientIP);
        }
        Map<String, String[]> parameters = servletRequest.getParameterMap();
        for (Map.Entry<String, String[]> parameter : parameters.entrySet()) {
            String[] values = parameter.getValue();
            if (values != null && values.length > 1) {
                MDC.put(parameter.getKey(), String.join(",", values));
            } else {
                MDC.put(parameter.getKey(), values == null ? "null" : values[0]);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {

    }
}
