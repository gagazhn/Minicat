/*
 * ZhangServlet.java
 *
 * Created on 2008.3.5, 10:57
 *
 * a abstract Servlet that suppert some necessary methods to call;
 *
 */

package httpserver.zhang.http;

import httpserver.zhang.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhangyue
 */
public abstract class ZhangHttpServlet extends HttpServlet{
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException;
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void init() throws ServletException {
        super.init();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public void jspInit(){
        ;
    }
}
