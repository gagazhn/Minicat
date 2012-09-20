/*
 * to store the header information sent to the cilent
 * the class used to support the information to the function:
 * ServerThread.sendHeader();
 */

package httpserver;

/**
 *
 * @author zhangyue
 */
public class ResponseHeader {
    private String protocol;
    private int stateCode;
    private String location;
    private boolean hasCookies;
    
    public ResponseHeader(String protocol, int stateCode, String location, boolean hasCookies){
        this.protocol = protocol;
        this.stateCode = stateCode;
        this.location = location;
        this.hasCookies = hasCookies;
    }
    
    public void setCookies(){
        if(hasCookies){
            
        }
    }
    
    public static ResponseHeader Builder(){
        return new ResponseHeader("a", 200, "/index.jsp", true);
    }
}
