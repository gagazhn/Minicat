/*
 * ZhangServletResponse.java
 *
 * Created on 2008��3��6��, ����10:43
 * 
 * the zhang response
 *
 */

package httpserver.zhang.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.net.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import httpserver.zhang.*;

/**
 *
 * @author �ūh
 */
public class ZhangHttpServletResponse implements HttpServletResponse{
    private Socket socket;
    private ZhangHttpServletRequest request;
    
    public ZhangHttpServletResponse(Socket socket, ZhangHttpServletRequest request) {
        this.socket = socket;
        this.request = request;
    }

    //////////////////////////////////////////////////////////
    // the interface methods
    public void addCookie(Cookie cookie) {
        
    }

    public boolean containsHeader(String string) {
        return false;
    }

    public String encodeURL(String string) {
        return null;
    }

    public String encodeRedirectURL(String string) {
        return null;
    }

    public String encodeUrl(String string) {
        return null;
    }

    public String encodeRedirectUrl(String string) {
        return null;
    }

    public void sendError(int i, String string) throws IOException {
    }

    public void sendError(int i) throws IOException {
    }

    public void sendRedirect(String URL) throws IOException {
        PrintWriter out = getWriter();
        if(URL.startsWith("/")){
            //means relative to the root
            out.write("HTTP/1.1 302 Moved Temporarily\n" +
                    "Location: " + request.getScheme() + "://" +
                    request.getServerName() + "/" +
                    URL + "\n\n");
        }else if(URL.startsWith("http://") ||
            URL.startsWith("FTP://")){
            out.write("HTTP/1.1 302 Moved Temporarily\n" +
                    "Location: " + URL + 
                    "\n\n");
        }else{
            out.write("HTTP/1.1 302 Moved Temporarily\n" +
                    "Location: " + request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/") + 1) +
                    URL +
                    "\n\n");
        }
        
        out.close();
    }

    public void setDateHeader(String string, long l) {
    }

    public void addDateHeader(String string, long l) {
    }

    public void setHeader(String string, String string0) {
        
    }

    public void addHeader(String name, String value) {
        try {
            getWriter().println(name + ": " + value);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setIntHeader(String string, int i) {
    }

    public void addIntHeader(String string, int i) {
    }

    public void setStatus(int i) {
    }

    public void setStatus(int i, String string) {
    }

    public String getCharacterEncoding() {
        //////////////////////////////////////
        return "UTF-8";
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        try {
            return new ZhangOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public PrintWriter getWriter() throws IOException{
        try {
            return new ZhangWriter(socket.getOutputStream(), getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setCharacterEncoding(String string) {
    }

    public void setContentLength(int i) {
    }

    public void setContentType(String string) {
    }

    public void setBufferSize(int i) {
    }

    public int getBufferSize() {
        return 256;
    }

    public void flushBuffer() throws IOException {
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return true;
    }

    public void reset() {
    }

    public void setLocale(Locale locale) {
    }

    public Locale getLocale() {
        return null;
    }
    
}
