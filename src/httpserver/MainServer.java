/*
 * NewClass.java
 *
 * Created on 2008.2.12, 3:04
 *
 * @author zhangyue
 */

package httpserver;

import httpserver.container.ServletContainer;
import java.io.IOException;
import java.net.*;
import httpserver.zhang.*;
import httpserver.xml.WebConfigureReader;

public class MainServer {
    public static String ROOT = "root/";
    
    // init the web.xml
    public static WebConfigureReader webConfigureReader = new WebConfigureReader(ROOT + "web-inf/web.xml");
    
    // init the ServletContext
    public static ZhangServletContext servletContext = new ZhangServletContext();
    
    private ServerSocket server;

    private int port;
    
    public MainServer() {
        port = 8080;
    }
    
    public void serverRun(){
        try {
            // the socket
            server = new ServerSocket(port);
            
            // the Console
            Console console = new Console(this);
            console.start();
            
            // init the ServletContainer
            ServletContainer.init();
            
            // Threads started here
            System.out.println("Server started!:");
            while(true){
                Socket socket = server.accept();
                new ServerThread(socket, webConfigureReader, servletContext).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getPort(){
        return port;
    }
}
