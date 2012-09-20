/*
 * ZhangHttpSession.java
 *
 * Created on April 26, 2008, 8:37 AM
 *
 * implements the javax.servlet.http.HttpSession
 * support the nesscery method.
 *
 */

package httpserver.zhang.http;

import httpserver.MainServer;
//import httpserver.zhang.ZhangServletContext;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author zhangyue
 */
public class ZhangHttpSession implements HttpSession{
    // the creation time
    private long creationTime;
    
    // the session id
    private String id;
    
    // the lastaccessedTIme
    // different from accessedTime
    private long lastAccessedTime;
    
    // the max interval time
    private int maxInactiveInterval;
    
    private boolean isNew;
    
    private HashMap<String, Object> attributes;
    
    public ZhangHttpSession(String id) {
        this(id, 180); // setup time
    }
    
    public ZhangHttpSession(String id, int maxinactiveInterval){
        this.id = id;
        this.maxInactiveInterval = maxinactiveInterval;
        attributes = new HashMap(8);
    }

    ////////////////////////////////////////////////////////////
    // interface
    
    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public ServletContext getServletContext() {
        return MainServer.servletContext;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return null;
    }

    @Override
    public void invalidate() {
        // cancal this session
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        maxInactiveInterval = i;
    }

    @Override
    public void removeValue(String name) {
        // donothin Deprecated
    }

    @Override
    public void removeAttribute(String name) {
        // remove the attribute from the session
        // not from the servercontainer
        attributes.remove(name);
    }

    @Override
    public Object getAttribute(String name) {
        // get the attribute from the session
        // not from the servercontainer
        return attributes.get(name);
    }

    @Override
    public Object getValue(String string) {
        // donothing deprecated
        return null;
    }

    @Override
    public void setAttribute(String name, Object object) {
        // set the attribute from the session
        // not from the servercontainer
        attributes.put(name, object);
    }

    @Override
    public void putValue(String string, Object object) {
        // deprecated
        // donothing
    }
}
