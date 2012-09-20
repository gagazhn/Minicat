/*
 * XMLReader.java
 *
 * Created on 2008��3��12��, ����6:18
 *
 * ��ȡjsp��Ŀ��web.xml�ļ�����Ϣ���ṩ����������Եķ���
 * 
 *
 */

package httpserver.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import httpserver.xml.web_XML.*;
import httpserver.zhang.*;
import org.w3c.dom.*;

/**
 *
 * @author �ūh
 */


public class WebConfigureReader {
    //һ��servlet���ServletInformation���hashtable����4��¼servlet��servlet-mapping��jϵ
    private static HashMap<String, ServletInformation> servlet = null; 
    
    //һ��servlet-map����Servlet_mappingInformation���hashtable,��4��¼servlet_mapping��servlet��jϵ
    private static HashMap<String, Servlet_mappingInformation> servletMapping = null;
    
    public final static int HASHTABLE_INIT_CAPACITY = 50;
    
    public WebConfigureReader(String filePath) {
        //init the Hastables
        if(servlet == null){
            servlet = new HashMap<String, ServletInformation>(32);
        }
        if(servletMapping == null)
            servletMapping = new HashMap<String, Servlet_mappingInformation>(32);
        
        try {
            //init
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);
            
            Element root = doc.getDocumentElement();
            
            // get the information of the <servlet>
            NodeList list = root.getElementsByTagName("servlet");
            for(int i = 0; i < list.getLength(); i++){
                Element servletNode = (Element)list.item(i);
                //the element of the <servlet>
                String name = null;
                String cla = null;
                Node servlet_name = servletNode.getElementsByTagName("servlet-name").item(0);
                name = ((Text)servlet_name.getFirstChild()).getData();
                Node servlet_class = servletNode.getElementsByTagName("servlet-class").item(0);
                cla = ((Text)servlet_class.getFirstChild()).getData();
                
                //the ServletInformation class
                ServletInformation servletInformation = new ServletInformation(name, cla);
                servlet.put(servletInformation.getServlet_name(), servletInformation);
            }
            
             // get the information of the <servlet-mapping>
            list = root.getElementsByTagName("servlet-mapping");
            for(int i = 0; i < list.getLength(); i++){
                Element servlet_mappingNode = (Element)list.item(i);
                
                //the element of the <servlet>
                String name = null;
                String url = null;
                Node servlet_name = servlet_mappingNode.getElementsByTagName("servlet-name").item(0);
                name = ((Text)servlet_name.getFirstChild()).getData();
                Node servlet_class = servlet_mappingNode.getElementsByTagName("url-pattern").item(0);
                url = ((Text)servlet_class.getFirstChild()).getData();
                
                //the ServletInformation class
                Servlet_mappingInformation servlet_mapInformation = new Servlet_mappingInformation(url, name);
                servletMapping.put(servlet_mapInformation.getUrl(), servlet_mapInformation);
            }
        }catch (IOException ex){ 
            System.out.println("error: web.xml did'nt exist");
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //analyse
    }
    
    //ͨ��request�еĶԶ�������룬������������·��
    public String getServletPath(String filePath){
        Servlet_mappingInformation sm = (Servlet_mappingInformation)servletMapping.get(filePath);
        ServletInformation s = (ServletInformation)servlet.get(sm.getServlet_name());
        return "./" + httpserver.MainServer.ROOT + s.getClassPath() + ".class";
    }
    
    public String getServletName(String filePath){
    	System.out.println(filePath);
        Servlet_mappingInformation sm = (Servlet_mappingInformation)servletMapping.get(filePath);
        ServletInformation s = (ServletInformation)servlet.get(sm.getServlet_name());
        return s.getServlet_class();
    }
//
//    public static Hashtable getServlet() {
//        return servlet;
//    }
//
//    public static Hashtable getServletMapping() {
//        return servletMapping;
//    }
    
}
