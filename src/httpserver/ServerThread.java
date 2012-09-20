/*
 * ServerThread.java
 *
 * Created on 2008��2��12��, ����3:14
 *
 * @author ����
 *
 * a Server Thread, which support imformation to browser, get information from it
 */

package httpserver;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import javax.servlet.http.*;
import javax.servlet.*;

import httpserver.serverTools.*;
import httpserver.zhang.*;
import httpserver.xml.*;
import httpserver.remoteClass.*;
import httpserver.container.*;
import httpserver.zhang.http.*;

public class ServerThread extends Thread{
    private Socket socket;
    private WebConfigureReader webConfigureReader;
    private ZhangServletContext servletContext;
    private ZhangHttpServletRequest request;
    private ZhangHttpServletResponse response;
    
    public ServerThread(Socket socket, WebConfigureReader webConfigureReader, ZhangServletContext servletContext) {
        this.socket = socket;
        this.webConfigureReader = webConfigureReader;
        this.servletContext = servletContext;
    }
    
    public void run() {
        try {
            // get the information of client's request
            request = new ZhangHttpServletRequest(socket);
            // the response;
            response = new ZhangHttpServletResponse(socket, request);
            
            toResponse(request, response, webConfigureReader, servletContext);
            
            // close the socket
            if(!socket.isClosed())
                socket.close();
        } catch (ZhangServletRequestException ex) {
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static void toResponse(ZhangHttpServletRequest request, ZhangHttpServletResponse response,
            WebConfigureReader webConfigureReader, ZhangServletContext servletContext){
        try{
            //analyse
            // a jsp or servlet request
            if(request.getRequestType() == ZhangHttpServletRequest.JSP)
                sendJSP(request, response, servletContext);
            else if(request.getRequestType() == ZhangHttpServletRequest.ACTION)
                sendAction(request, response, webConfigureReader, servletContext);
            
            // a static request like htm or html or soruce
            else if(request.getRequestType() == ZhangHttpServletRequest.HTML)
                sendHtml(request, response);
            else if(request.getRequestType() == ZhangHttpServletRequest.SOURCE)
                sendResource(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // send the error message to the cilent
    // state 400 supported only at present
    public static void sendError(ZhangHttpServletResponse response, String note){
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.write("HTTP/1.1 400 Bad Request\n\n" +
                    note +
                    "\n\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println("Exception in sendError.");
        }
    }
    
    // 一个Servlet类,包括一个*.do请求.
    public static void sendAction(ZhangHttpServletRequest request,
            ZhangHttpServletResponse response,
            WebConfigureReader webConfigureReader,
            ZhangServletContext servletContext){
        // is the servlet in the servletContainer?
        Servlet servlet = ServletContainer.getServlet(request.getRequestURI());
        if(servlet == null){
            //the action is not in the ServletContainer
            //load ths remote class
            RemoteClassLoader loader = new RemoteClassLoader(webConfigureReader.getServletName(request.getRequestURI()));
            try {
                servlet = (HttpServlet)loader.findClass().newInstance();
                
                // init the servlet only once
                servlet.init(new ZhangServletConfig(servletContext));
                
                // save the Servlet
                ServletContainer.putServlet(request.getRequestURI(), servlet);
            } catch (Exception ex) {
                sendError(response, "A error occur in the runtime!");
                ex.printStackTrace();
            }
        }
        
        try {
            // in the SerlvetContainer
            servlet.service(request, response);
            
        } catch (Exception ex) {
            sendError(response, "A error occur in the runtime!");
            ex.printStackTrace();
        }
    }
    
    // send the jsp in a dynamic
    public static void sendJSP(ZhangHttpServletRequest request,
            ZhangHttpServletResponse response,
            ZhangServletContext servletContext){
        //Search the jsp in the memory
        Servlet servlet = ServletContainer.getServlet(request.getRequestURI());
        if(servlet == null){
            try {
                // the jsp is inot in the container
                // so load it form the file
                
                //complie the jsp into a servlet
                Complier2 jsp = new Complier2(request.getRequestURI());
                if(jsp.complie() != 0){
                    sendError(response, "compling error");
                    return;
                }
                RemoteClassLoader loader = new RemoteClassLoader(jsp.getClassName());
                servlet = (ZhangHttpServlet) loader.findClass().newInstance();
                servlet.init(new ZhangServletConfig(servletContext));
                
                // init the servlet just only once
                ((ZhangHttpServlet)servlet).jspInit();
                
                // save the servlet
                ServletContainer.putServlet(request.getRequestURI(), servlet);
            } catch (Exception ex) {
                ex.printStackTrace();
                sendError(response, ex.getMessage());
            }
        }
        
        // do the service;
        // send the response
        try {
            sendHeader(request, response);
            servlet.service(request, response);
        } catch (Exception ex) {
            sendError(response, "A error occur in the runtime!");
            ex.printStackTrace();
        }
    }
    
    // send the htm to the client
    public static void sendHtml(HttpServletRequest request, ZhangHttpServletResponse response){
        ArrayList<String> html = HtmlContainer.getHtml(request.getRequestURI());
        if(html == null){
            // there is not the html in the memory
            try {
                html = new ArrayList<String>(32);
                ZhangReader reader = new ZhangReader("./" + httpserver.MainServer.ROOT + request.getRequestURI());
                
                String line = reader.readLine();
                while(line != null){
                    html.add(line);
                    line = reader.readLine();
                }
                
                reader.close();
                
            } catch (IOException ex) {
                sendError(response, ex.getMessage());
                return;
            }
        }
        
        try {
            ZhangWriter writer = (ZhangWriter)response.getWriter();
            for(String line: html){
                writer.write(line + "\n");
            }
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static void sendResource(ZhangHttpServletRequest request, ZhangHttpServletResponse response){
        // send the resource to the cilent
        // such as the picture, file, and so on
        MappedByteBuffer resourceMap = ResourceContainer.getResource(request.getRequestURI());
        if(resourceMap == null){
            // the resource is not in the container
            FileInputStream in = null;
            
            try {
//
//            String GIFHead = "HTTP/1.1 200 OK" +
//                    "Server: Apache-Coyote/1.1" +
//                    "ETag: W/\"1777-1200622693062\"" +
//                    "Last-Modified: Fri, 18 Jan 2008 02:18:13 GMT" +
//                    "Content-Length: 1777" +
//                    "Date: Tue, 19 Feb 2008 01:48:54 GMT\n\n";
                ////////////////////////////
                
                in = new FileInputStream("./" + httpserver.MainServer.ROOT + request.getRequestURI());
                FileChannel channel = in.getChannel();
                long length = channel.size();
                resourceMap = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
//                resourceMap.load();
                
                // save the resource to the container
                ResourceContainer.putResource(request.getRequestURI(), resourceMap);
                
                // close the inputstream
                in.close();
                
            }catch (Exception ex){
                if(ex instanceof IOException){
                    sendError(response, ex.getMessage());
                    return;
                }else{
                    ex.printStackTrace();
                }
            }
        }
        
        try {
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            for(int i = 0; i < resourceMap.limit(); i++){
                out.write(resourceMap.get(i));
            }
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void sendHeader(ZhangHttpServletRequest request, ZhangHttpServletResponse response){
        ;
    }
}
