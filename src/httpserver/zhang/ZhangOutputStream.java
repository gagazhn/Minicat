/*
 * ZhangOutputStream.java
 *
 * Created on 2008.4.2, 11:47
 *
 * extends the ServletOutputStream
 *
 */

package httpserver.zhang;

import java.io.*;
import java.net.Socket;
import javax.servlet.*;

/**
 *
 * @author zhangyue
 */
public class ZhangOutputStream extends ServletOutputStream{
    protected BufferedOutputStream out;
    
    public ZhangOutputStream(OutputStream outputStream) throws IOException {
        out = new BufferedOutputStream(outputStream);
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void close() throws IOException {
        out.close();
    }

    public void flush() throws IOException {
        out.flush();
    }
    
    
}
