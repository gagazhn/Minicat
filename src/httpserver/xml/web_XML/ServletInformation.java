/*
 * ServletInformation.java
 *
 * Created on 2008.3.15, 11:48
 *
 * symbol a single servlet, and the servlet's information
 * is from the web.xml. it's work together with the class
 * httpserver.XML.web_XML.Servlet_mappingInformation
 *
 */

package httpserver.xml.web_XML;

/**
 *
 * @author zhangyue
 */
public class ServletInformation {
    private String servlet_name;
    private String servlet_class;
    private String classPath;
    
    public ServletInformation(String servlet_name, String servlet_class) {
        this.servlet_name = servlet_name;
        this.servlet_class = servlet_class;
//        System.out.println("ServletInformation(): " + this.servlet_class);
        
        this.classPath = servlet_class.replace(".", "/");
//        System.out.println("ServletInformattion(): " + this.classPath);
    }

    public String getServlet_name() {
        return servlet_name;
    }

    public String getServlet_class() {
        return servlet_class;
    }
    
    public String getClassPath(){
        return "web-inf/classes/" + classPath;
    }
}
