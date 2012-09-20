/*
 * ZhangRequestDispatcher.java
 *
 * Created on 2008.3.21, 12:20
 *
 * the suclass of the RequestDispatcher
 */

package httpserver.zhang;

import httpserver.MainServer;
import httpserver.ServerThread;
import httpserver.zhang.http.ZhangHttpServletRequest;
import httpserver.zhang.http.ZhangHttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.*;

/**
 *
 * @author zhangyue
 */
public class ZhangRequestDispatcher implements RequestDispatcher{
    private String target;
    
    public ZhangRequestDispatcher(String target) {
        this.target = target;
    }

    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        //donothing
    }

    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            if(!target.startsWith("/")){
                //relative path to the current path
                ZhangHttpServletRequest newRequest = new ZhangHttpServletRequest(new URI(((ZhangHttpServletRequest)request).getRequestURI()).getPath() + target);
                ServerThread.toResponse(newRequest, (ZhangHttpServletResponse)response, MainServer.webConfigureReader, MainServer.servletContext);
            }else{
                //relative to the root
                ZhangHttpServletRequest newRequest = new ZhangHttpServletRequest(target);
                ServerThread.toResponse(newRequest, (ZhangHttpServletResponse)response, MainServer.webConfigureReader, MainServer.servletContext);
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
}
