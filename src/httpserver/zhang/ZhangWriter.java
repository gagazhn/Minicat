/*
 * ZhangWriter.java
 *
 * Created on 2008��3��18��, ����12:08
 *
 * Ϊ�˸�HttpServletResponse�ṩ�������󣬴���̳�PrintWriter�Ҷ��ַ����
 * ��װ��BufferedWriter, �����ZhangWriter��Ч��
 *
 */

package httpserver.zhang;

import java.io.*;
import java.util.Locale;

/**
 *
 * @author zhangyue
 */
public class ZhangWriter extends PrintWriter{
    private OutputStream outputStream;
    private String charset;
    protected OutputStreamWriter outputStreamWriter;
    
    //the symbol weather the Writer is buffered;
    private boolean isBuffered;
    
//    protected BufferedWriter bufferedWriter;
//    
//    public ZhangWriter(OutputStream outputStream, String charset) throws UnsupportedEncodingException{
//        super(new BufferedWriter(
//                new OutputStreamWriter(outputStream, charset)), true);
//        this.charset = charset;
//        bufferedWriter = (BufferedWriter)out;
//    }
//    ��BufferedWriter4��װ������.��ʵ����,��Wwrite(ZhangHttpServlet.getServletContext().toString())ʱʧЧ!!!!!
    public ZhangWriter(OutputStream outputStream, String charset) throws UnsupportedEncodingException{
        this(outputStream, charset, false);
    }
    
    public ZhangWriter(OutputStream outputStream, String charset, boolean isBuffered) throws UnsupportedEncodingException{
        super(new OutputStreamWriter(outputStream, charset), isBuffered);
        this.charset = charset;
        this.outputStreamWriter = (OutputStreamWriter)out;
        this.isBuffered = isBuffered;
    }
    
    public String getCharset(){
        return charset;
    }
    
    ////////////////////////////////////////////////////////////
    public void print(char[] s) {
        try {
            outputStreamWriter.write(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void write(char[] buf){
        try {
            outputStreamWriter.write(buf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void println() {
        super.println();
    }
    
    public void println(char[] x) {
        print(x);
        println();
    }
    
    public PrintWriter append(char c) {
        try {
            outputStreamWriter.append(c);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public void print(char c) {
        try {
            outputStreamWriter.write(c);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void println(char x) {
        try {
            outputStreamWriter.write(x);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void println(long x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void print(long l) {
        try {
            outputStreamWriter.write(String.valueOf(l));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void print(float f) {
        try {
            outputStreamWriter.write(String.valueOf(f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public PrintWriter format(String format, Object... args) {
        try {
            throw new IOException("This method is not supported in ZhangWriter!\nI'm sorry for that.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public PrintWriter printf(String format, Object... args) {
        try {
            throw new IOException("This method is not supported in ZhangWriter!\nI'm sorry for that.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public void println(float x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void write(String s, int off, int len) {
        try {
            outputStreamWriter.write(s, off, len);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public PrintWriter append(CharSequence csq) {
        try {
            outputStreamWriter.append(csq);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public void print(boolean b) {
        try {
            outputStreamWriter.write(String.valueOf(b));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void println(boolean x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void println(Object x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void print(Object obj) {
        try {
            outputStreamWriter.write(String.valueOf(obj));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void print(int i) {
        try {
            outputStreamWriter.write(String.valueOf(i));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void write(char[] buf, int off, int len) {
        try {
            outputStreamWriter.write(buf, off, len);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void write(int c) {
        try {
            outputStreamWriter.write(c);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void println(int x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void println(String x) {
        try {
            if(x != null)
                outputStreamWriter.write(x);
            else
                outputStreamWriter.write("null");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        println();
    }
    
    public void write(String s) {
        try {
            if(s == null)
                outputStreamWriter.write("null");
            else
                outputStreamWriter.write(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void print(String s) {
        super.print(s);
    }
    
    public void print(double d) {
        try {
            outputStreamWriter.write(String.valueOf(d));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void println(double x) {
        try {
            outputStreamWriter.write(String.valueOf(x));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public PrintWriter format(Locale l, String format, Object... args) {
        try {
            throw new IOException("This method is not supported in ZhangWriter!\nI'm sorry for that.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public PrintWriter printf(Locale l, String format, Object... args) {
        try {
            throw new IOException("This method is not supported in ZhangWriter!\nI'm sorry for that.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return this;
    }
    
    public PrintWriter append(CharSequence csq, int start, int end) {
        try {
            outputStreamWriter.append(csq, start, end);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }
}
