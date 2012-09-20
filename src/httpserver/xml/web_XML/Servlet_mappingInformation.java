/*
 * Servlet_mapInformation.java
 *
 * Created on 2008.3.16, 12:23
 *
 * it's from the web.xml. show the relateionship between
 * request and the name of the servlet. it's work together
 * with the httpserver.XML.web_XML.ServletInformation
 */

package httpserver.xml.web_XML;

/**
 *
 * @author zhangyue
 */
public class Servlet_mappingInformation {
    private String url;
    private String servlet_name;
    
    public Servlet_mappingInformation(String url, String servlet_name) {
        this.url = url;
        this.servlet_name = servlet_name;
    }

    public String getUrl() {
        return url;
    }

    public String getServlet_name() {
        return servlet_name;
    }
    
}
