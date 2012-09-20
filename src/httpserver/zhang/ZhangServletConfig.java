/*
 * ZhangServletConfig.java
 *
 * Created on 2008.3.24, 11:38
 *
 * the ZhangServletConfig
 */

package httpserver.zhang;

import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *
 * @author zhangyue
 */
public class ZhangServletConfig implements ServletConfig{
    private ServletContext servletContext;
    
    public ZhangServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    /////////////////////////

    public String getServletName() {
        return "haha";
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Enumeration getInitParameterNames() {
        return null;
    }

    public String getInitParameter(String string) {
        return null;
    }
    
}
