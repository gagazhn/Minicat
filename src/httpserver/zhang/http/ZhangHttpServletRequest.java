/*
 * ZhangServletRequire.java
 *
 * Created on 2008.3.2, 1:13
 *
 */

package httpserver.zhang.http;

import httpserver.MainServer;
import httpserver.zhang.*;
import java.io.*;
import java.net.*;
import java.security.Principal;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import httpserver.zhang.ZhangReader;

/**
 *
 * @author zhangyue
 */

// get and analyse the request from the client
public class ZhangHttpServletRequest implements HttpServletRequest{
    public static int NOT_INIT = -1;
    public static int ERROR = 0;
    // a path requst like www.baidu.com/
    public static int PATH = 1;
    //a htm request such as *.html *.htm
    public static int HTML = 2;
    // a jsp request
    public static int JSP = 3;
    // a source request like gif or jpg wav
    public static int SOURCE = 4;
    // a action request like "*.do" or without extends
    public static int ACTION = 5;
    
    //get or put
    private String method;
    
    // the extend of the request header
    private String extend;
    
    //context length
    private int contentLength;
    
    private String queryString;
    private URI requestURI;
    private String connection;
    
    // to store the state of the request
    // ERROR, PATH, ACTION....
    private int requestType;
    
    private String host;
    
    private String accept_Charset;
    
    // to store the id of the session
    private String sessionID;
    
    private ZhangReader reader;
    private Socket socket;
    private HashMap<String, String> parameterMap;
    
    public ZhangHttpServletRequest(Socket socket) throws ZhangServletRequestException{
        this.socket = socket;
        try {
            reader = new ZhangReader(socket.getInputStream());
            parameterMap = new HashMap<String, String>(8);
            
            //analyse the request////////////////////////////////////////////////////////////////
            
            //method
            String line = reader.readNext();
            if("GET".equalsIgnoreCase(line))
                method = "GET";
            else if("POST".equalsIgnoreCase(line))
                method = "POST";
            else
                throw new ZhangServletRequestException("error in get the request method.");
            
            // the request
            String request = reader.readNext();
            System.out.println("ZhangHttpServletRequest:78 request:" + request);
            try {
                requestURI = new URI(request);
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
            
            // get the Extend
            extend = getExtend(requestURI.getPath());
            
            //requestType
            if("jsp".equalsIgnoreCase(extend))
                requestType = JSP;
            else if("htm".equalsIgnoreCase(extend) ||
                    "html".equalsIgnoreCase(extend))
                requestType = HTML;
            else if("gif".equalsIgnoreCase(extend) ||
                    "jpg".equalsIgnoreCase(extend) ||
                    "bmp".equalsIgnoreCase(extend) ||
                    "ico".equalsIgnoreCase(extend) ||
                    "exe".equalsIgnoreCase(extend) ||
                    "css".equalsIgnoreCase(extend))
                requestType = SOURCE;
            else if("do".equalsIgnoreCase(extend) ||
                    "".equalsIgnoreCase(extend))
                requestType = ACTION;
            else
                requestType = ERROR;
            
            //开始分析request中的其它信息,包括
            //HOST:
            //...
            
            // 如巢是GET请求,则此时request的参数已经送达
            // 可以进行分析. 如果是POST请求,则参数在分析完
            // request的其它各顶属性后才能得到.
            
            // 根据请求方法来决定分析顺序
            if("GET".equalsIgnoreCase(getMethod())){
                //GET 请求:
                
                // request 参数
                queryString = requestURI.getRawQuery();
                
                
                // the rest part of the request header
                line = reader.readNext();
                while(line != null){
//                    System.out.println("zhanghttpservletrequest129 line:" + line);
                    if(line.equalsIgnoreCase("Connection:"))
                        break;
                    else if("Host:".equalsIgnoreCase(line))
                        //Host
                        host = reader.readNext();
//                    else if("Accept-Charset:".equalsIgnoreCase(line))
//                        // charset;
//                        accept_Charset = reader.readNext();
                    
                    System.out.println(line);
                    line = reader.readNext();
                }
            }else if("POST".equalsIgnoreCase(getMethod())){
                // POST
                line = reader.readNext();
                while(line != null){
                    if("Content-Length:".equalsIgnoreCase(line)){
                        line = reader.readNext();
                        
                        System.out.println("contenet-length: " + line);
                        contentLength = Integer.parseInt(line);
//                    }else if("")
                    }else if("".equalsIgnoreCase(line)){
                        queryString = reader.readByLength(contentLength);
                        break;
                    }
                    
                    line = reader.readNext();
                }
            }else{
                //donothing
            }
            
            //analyse the parameters
            
            if(queryString != null){
                StringBuilder parameter = new StringBuilder();
                System.out.println("paramter: " + queryString);
//                queryString = new String(queryString.getBytes("UTF-8"));
//                System.out.println("paramter: " + queryString);
                String name = null, value = null;
                for(int i = 0; i < queryString.length(); i++){
                    // analyse the query
                    // get each param
                    int cp = queryString.codePointAt(i);
                    
                    if('&' == cp){
                        value = parameter.toString();
                        parameter.delete(0, parameter.length());
                        parameterMap.put(name, value);
                    }else if('=' == cp){
                        name = parameter.toString();
                        parameter.delete(0, parameter.length());
                    }else{
                        // 转义字符
                        if('+' == cp)
                            parameter.appendCodePoint(' ');
                        else if('%' == cp){
                            // 用来储存转义字符的16进制数
                            StringBuilder temp = new StringBuilder(2);
                            temp.appendCodePoint(queryString.codePointAt(i + 1));
                            temp.appendCodePoint(queryString.codePointAt(i + 2));
                            i += 2;
                            
                            byte b = (byte)Integer.parseInt(temp.toString(), 16);
                            if(b >= 0){
                                // ASCII code
                                parameter.appendCodePoint(b);
                            }else{
                                // 这个字符由多个byte组成(3)
                                
                                temp = new StringBuilder(2);
                                temp.appendCodePoint(queryString.codePointAt(i + 2));
                                temp.appendCodePoint(queryString.codePointAt(i + 3));
                                i += 3;
                                byte b2 = (byte)Integer.parseInt(temp.toString(), 16);
                                
                                temp = new StringBuilder(2);
                                temp.appendCodePoint(queryString.codePointAt(i + 2));
                                temp.appendCodePoint(queryString.codePointAt(i + 3));
                                i += 3;
                                byte b3 = (byte)Integer.parseInt(temp.toString(), 16);
                                
                                byte[] bn = {b, b2, b3};
                                parameter.append(new String(bn, "UTF-8"));
                            }
                        }else{
                            parameter.appendCodePoint(cp);
                        }
                    }
                }
                
                //the last value
                value = parameter.toString();
                parameter.delete(0, parameter.length());
                parameterMap.put(name, value);
            }
            
            // half close the socket
            socket.shutdownInput();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // this constructor is used by the ZhangRequestDispatcher
    // to create a HttpRequest in a simply way
    // only support the GET request method.
    public ZhangHttpServletRequest(String URI){
        
        // parameters
        parameterMap = new HashMap<String, String>(8);
        
        // int this construct the method must be GET
        method = "GET";
        
        // get the the request
        String request = URI.trim();
        try {
            // to a URI because it will call the function of URI
            requestURI = new URI(request);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        
        // the extend
        extend = getExtend(requestURI.getPath());
        
        // requestType
        if("jsp".equalsIgnoreCase(extend))
            requestType = JSP;
        else if("htm".equalsIgnoreCase(extend) ||
                "html".equalsIgnoreCase(extend))
            requestType = HTML;
        else if("gif".equalsIgnoreCase(extend) ||
                "jpg".equalsIgnoreCase(extend) ||
                "bmp".equalsIgnoreCase(extend) ||
                "ico".equalsIgnoreCase(extend) ||
                "exe".equalsIgnoreCase(extend) ||
                "css".equalsIgnoreCase(extend))
            requestType = SOURCE;
        else if("do".equalsIgnoreCase(extend) ||
                "".equalsIgnoreCase(extend))
            requestType = ACTION;
        else
            requestType = ERROR;
        
        // get and analyse the query
        queryString = requestURI.getQuery();
        
        
       if(queryString != null){
                StringBuilder parameter = new StringBuilder();
                System.out.println("paramter: " + queryString);
//                queryString = new String(queryString.getBytes("UTF-8"));
//                System.out.println("paramter: " + queryString);
                String name = null, value = null;
                for(int i = 0; i < queryString.length(); i++){
                    // analyse the query
                    // get each param
                    int cp = queryString.codePointAt(i);
                    
                    if('&' == cp){
                        value = parameter.toString();
                        parameter.delete(0, parameter.length());
                        parameterMap.put(name, value);
                    }else if('=' == cp){
                        name = parameter.toString();
                        parameter.delete(0, parameter.length());
                    }else{
                        // 转义字符
                        if('+' == cp)
                            parameter.appendCodePoint(' ');
                        else if('%' == cp){
                            // 用来储存转义字符的16进制数
                            StringBuilder temp = new StringBuilder(2);
                            temp.appendCodePoint(queryString.codePointAt(i + 1));
                            temp.appendCodePoint(queryString.codePointAt(i + 2));
                            i += 2;
                            
                            byte b = (byte)Integer.parseInt(temp.toString(), 16);
                            if(b >= 0){
                                // ASCII code
                                parameter.appendCodePoint(b);
                            }else{
                                // 这个字符由多个byte组成(3)
                                
                                temp = new StringBuilder(2);
                                temp.appendCodePoint(queryString.codePointAt(i + 2));
                                temp.appendCodePoint(queryString.codePointAt(i + 3));
                                i += 3;
                                byte b2 = (byte)Integer.parseInt(temp.toString(), 16);
                                
                                temp = new StringBuilder(2);
                                temp.appendCodePoint(queryString.codePointAt(i + 2));
                                temp.appendCodePoint(queryString.codePointAt(i + 3));
                                i += 3;
                                byte b3 = (byte)Integer.parseInt(temp.toString(), 16);
                                
                                byte[] bn = {b, b2, b3};
                                    try {
                                        parameter.append(new String(bn, "UTF-8"));
                                    } catch (UnsupportedEncodingException ex) {
                                        ex.printStackTrace();
                                    }
                            }
                        }else{
                            parameter.appendCodePoint(cp);
                        }
                    }
                }
                
                //the last value
                value = parameter.toString();
                parameter.delete(0, parameter.length());
                parameterMap.put(name, value);
            }
    }
    
    private String getExtend(String name){
        if(name != null){
            int i = name.lastIndexOf('.');
            if(i != -1)
                return name.substring(i + 1, name.length());
            else
                return "";
        }
        return null;
    }
    
    public int getRequestType(){
        return requestType;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    //the interface methods
    @Override
    public Object getAttribute(String string) {
        return null;
    }
    
    @Override
    public Enumeration getAttributeNames() {
        return null;
    }
    
    @Override
    public String getCharacterEncoding() {
        return accept_Charset;
    }
    
    @Override
    public void setCharacterEncoding(String string) throws UnsupportedEncodingException {
    }
    
    @Override
    public int getContentLength() {
        return 10;
    }
    
    @Override
    public String getContentType() {
        return null;
    }
    
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }
    
    public String getParameter(String string) {
        String s = parameterMap.get(string);
        System.out.println("zhangrequest: 227: parameter: " + parameterMap.get(string));
        return s;
    }
    
    public Enumeration getParameterNames() {
        return null;
    }
    
    public String[] getParameterValues(String string) {
        return null;
    }
    
    public Map getParameterMap() {
        return parameterMap;
    }
    
    @Override
    public String getProtocol() {
        return null;
    }
    
    @Override
    public String getScheme() {
        return "http";
    }
    
    public String getServerName() {
        return host;
    }
    
    public int getServerPort() {
        return 80;
    }
    
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public String getRemoteAddr() {
        return null;
    }
    
    public String getRemoteHost() {
        return null;
    }
    
    public void setAttribute(String string, Object object) {
        ///////////////////////////////////////////////////////
    }
    
    public void removeAttribute(String string) {
    }
    
    public Locale getLocale() {
        return null;
    }
    
    public Enumeration getLocales() {
        return null;
    }
    
    public boolean isSecure() {
        return true;
    }
    
    public RequestDispatcher getRequestDispatcher(String string) {
        return new ZhangRequestDispatcher(string);
    }
    
    public int getRemotePort() {
        return 8080;
    }
    
    public String getLocalName() {
        return null;
    }
    
    public String getLocalAddr() {
        return null;
    }
    
    public int getLocalPort() {
        return 80;
    }
    
    public String getRealPath(String string) {
        return null;
    }
    
    public String getAuthType() {
        return null;
    }
    
    public Cookie[] getCookies() {
        return null;
    }
    
    public long getDateHeader(String string) {
        return 0;
    }
    
    public String getHeader(String string) {
        return null;
    }
    
    public Enumeration getHeaders(String string) {
        return null;
    }
    
    public Enumeration getHeaderNames() {
        return null;
    }
    
    public int getIntHeader(String string) {
        return 0;
    }
    
    public String getMethod() {
        return method;
    }
    
    public String getPathInfo() {
        return null;
    }
    
    public String getPathTranslated() {
        return null;
    }
    
    public String getContextPath() {
        return null;
    }
    
    public String getQueryString() {
        return queryString;
    }
    
    public String getRemoteUser() {
        return null;
    }
    
    public boolean isUserInRole(String string) {
        return true;
    }
    
    public Principal getUserPrincipal() {
        return null;
    }
    
    public String getRequestedSessionId() {
        return sessionID;
    }
    
    public String getRequestURI() {
        return requestURI.getPath();
    }
    
    public StringBuffer getRequestURL() {
        return null;
    }
    
    public String getServletPath() {
        return null;
    }
    
    @Override
    public HttpSession getSession(boolean b) {
        if(sessionID == null) // the sessionID is null
            return null;
        
        ZhangHttpSession session = MainServer.servletContext.getSession(sessionID);
        
        if(session != null){
            // has a session
            return session;
        }else if(session != null && b){
            // has no session
            session = new ZhangHttpSession(sessionID);
            MainServer.servletContext.pubSession(sessionID, session);
            return session;
        }else{
            // never here
            return null;
        }
    }
    
    @Override
    public HttpSession getSession() {
        return getSession(true);
    }
    
    // it only can check the exist of the sessionID
    @Override
    public boolean isRequestedSessionIdValid() {
        if(sessionID != null)
            return true;
        else 
            return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }
    
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        // Deprecated
        return true;
    }
}