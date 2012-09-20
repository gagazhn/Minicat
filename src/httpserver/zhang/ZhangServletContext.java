/*
 * ZhangServletContext.java
 *
 * Created on 2008.3.20, 5:55
 *
 * a ServletContext
 * there is only one ServletContext in the httpServer
 */

package httpserver.zhang;

import httpserver.MainServer;
import httpserver.zhang.http.ZhangHttpSession;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author zhangyue
 */
public class ZhangServletContext implements ServletContext{
    // HttpContext store the Object
    private HashMap<String, Object> attributes;
    
    // store the relation between the session id and 
    // the session container
    private HashMap<String, ZhangHttpSession> sessionContainer;
    
    // recorde the names of the attributes
//    private ArrayList<String> nameList;
    
    private int majorVersion;
    private int minorVersion;
    
    public ZhangServletContext() {
        attributes = new HashMap<String, Object>();
//        nameList = new ArrayList<String>();
    }

    public ZhangHttpSession getSession(String id){
        return sessionContainer.get(id);
    }
    
    public void pubSession(String id, ZhangHttpSession session){
        sessionContainer.put(id, session);
    }
    
    ///////////////////////////////////////////////////
    // interface
    
    public Enumeration getServlets() {
        return null;
    }

    public Enumeration getServletNames() {
        return null;
    }

    public String getServletContextName() {
        return "ZhangServletContext1.1";
    }

    public String getServerInfo() {
        return "ZhangServletContext1.1";
    }

    public Enumeration getAttributeNames() {
        /////////////////////////////////////////
        return null;
    }

    public Enumeration getInitParameterNames() {
        /////////////////////////////////////////
        return null;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void removeAttribute(String string) {
        attributes.remove(string);
//        nameList.remove(string);
    }

    public void log(String string) {
        //donothing at this time;
    }

    public Servlet getServlet(String string) throws ServletException {
        return null;
    }

    public Set getResourcePaths(String string) {
        //////////////////////////////////////////////////////
        //donothing at this time;
        return null;
    }

    public Object getAttribute(String string) {
        return attributes.get(string);
    }

    public ServletContext getContext(String string) {
        ////////////////////////////////////////////////////////
        //i don't know wether it work.
        return this;
    }

    public String getInitParameter(String string) {
        /////////////////////////////
        return null;
    }

    public String getMimeType(String string) {
        //////////////////////////////
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String string) {
        //////////////////////////////////////////////////
        return null;
    }

    public String getRealPath(String string) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String string) {
        return new ZhangRequestDispatcher(string);
    }

    public URL getResource(String string) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String string) {
        return null;
    }

    public void setAttribute(String string, Object object) {
        attributes.put(string, object);
//        nameList.add(string);
    }

    public void log(String string, Throwable throwable) {
        /////////////////////////////////////////
    }

    public void log(Exception exception, String string) {
        //////////////////////////////////////////
    }

    public String getContextPath() {
        return "/";
    }
    
}
