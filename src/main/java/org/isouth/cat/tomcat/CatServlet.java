package org.isouth.cat.tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class CatServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        URL requestURL = new URL(req.getRequestURL().toString());
        String wsURL = new StringBuilder()
                .append("ws://")
                .append(requestURL.getAuthority())
                .append(requestURL.getPath())
                .toString();
        String queryString = req.getQueryString();
        String data = queryString == null ? "" : queryString;
        resp.getWriter().write("<html><meta charset=\"UTF-8\" /><script>var lines = 0;" +
                "var client = new WebSocket(\"" + wsURL + "\");" +
                "client.onopen = function() {client.send(\"" + data + "\")};" +
                "client.onmessage = function(msg) {" +
                "  if (lines > 1000) {lines=0;document.body.innerHTML=\"\";}" +
                "  lines=lines+1; document.body.innerHTML = document.body.innerHTML + msg.data + \"<br />\";" +
                "}" +
                "</script><body></body></html>");
        resp.getWriter().flush();
    }
}
