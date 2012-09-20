/*
 * ServletContainer.java
 *
 * Created on 2008.3.31, 10:30
 *
 * the container of the servlet
 *
 */

package httpserver.container;

import java.util.*;
import javax.servlet.*;

/**
 *
 * @author zhangyue
 */
public class ServletContainer {
    private static HashMap<String, Servlet> container = new HashMap<String, Servlet>(32);
    
    
    // clean the container
    public static void clear(){
        container.clear();
    }
    
    // init but donothing now
    public static void init(){
        //dosomething
    }
    
    public static Servlet getServlet(String key){
        return (Servlet)container.get(key);
    }
    
    public static void putServlet(String key, Servlet servlet){
        container.put(key, servlet);
    }
}
