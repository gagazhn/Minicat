/*
 * ZhangReader.java
 *
 * Created on 2008��3��10��, ����11:45
 *
 * �̳�InputStreamReader�������������
 * ����.��UTF-8��ʽ
 *
 */

package httpserver.zhang;

import java.io.*;
import java.util.*;

/**
 *
 * @author �ūh
 */
public class ZhangReader extends InputStreamReader{
    //��¼�Ƿ�֮ǰ������һ��'\r'�ַ�
    private boolean hasR = false;
    
    // read the byte from the cilent, so the charset if UTF-8
    // because of the UTF-8 if the standard format in the 
    // Http transport
    public ZhangReader(InputStream in) throws IOException{
        super(in, "UTF-8");
    }
    
    public ZhangReader(InputStream in, String charset) throws IOException{
        super(in, charset);
    }
    
    public ZhangReader(String filePath) throws IOException{
        super(new FileInputStream(filePath), "UTF-8");
    }
    
    public ZhangReader(String filePath, String charset) throws IOException{
        super(new FileInputStream(filePath), charset);
    }
    
    // readLine()
    // if read a empty line return ""
    // if the reader can't read any more(read() == -1)
    // it will return null;
    public final String readLine(){
        StringBuilder line = new StringBuilder(64);
        int c = 0;
        
        try {
            c = super.read();
            while(c != -1){
                if(c == -1){
//                    System.out.println(line.toString());
                    return line.toString();
                }else if(c == '\n'){
                    if(hasR){
                        //means reading a '\n' after a '\r'
                        //so do nothing.
                        hasR = false;
                    }else{
                        //a normal '\n'
                        return line.toString();
                    }
                }else if(c == '\r'){
                    // always mean a end of a line;
                    hasR = true;
                    return line.toString();
                }else{
                    //����Ѿ�������һ��ǻ��з�
                    hasR = false;
                    line.appendCodePoint(c);
                }
                
                c = super.read();
            }
        } catch (IOException ex) {
            System.out.println("Connection reset!");
            if(line.equals(""))
                return null;
            else
                return line.toString();
        }
        return null;
//        return new String(line, 0, pointAt);
    }
    
    // ��ȡһ���ַ�, ���' ', '\n', '\r'�ַ��򷵻�,�򷵻�null
    // ֻ���ڶ�ȡ����requestʱ������
    public final String readNext(){
        StringBuilder line = new StringBuilder(64);
        
        try {
            int c = super.read();
            while(c != -1){
                if(c == -1){
//                    System.out.println(line.toString());
                    hasR = false;
                    return line.toString();
                }else if(c == '\n'){
                    if(hasR){
                        //means reading a '\n' after a '\r'
                        //so do nothing.
                        hasR = false;
                    }else{
                        //a normal '\n'
                        hasR = false;
                        return line.toString();
                    }
                }else if(c == '\r'){
                    // always mean a end of a line;
                    hasR = true;
                    return line.toString();
                }else if(c == ' '){
                    if(line.length() != 0){
                        hasR = false;
                        return line.toString();
                    }
                }else{
                    //����Ѿ�������һ��ǻ��з�
                    hasR = false;
                    line.appendCodePoint(c);
                }
                
                c = super.read();
            }
        } catch (IOException ex) {
            System.out.println("Connection reset!");
            if(line.equals(""))
                return null;
            else
                return line.toString();
        }
        return null;
    }
    
    public String readByLength(int codeCount){
        StringBuilder line = new StringBuilder(64);
        int c = 0;
        
        try {
            c = super.read();
            for(int i = 0; i < codeCount && c != -1; i++){
                line.appendCodePoint(c);
                
                c = super.read();
            }
            
            // the last character
            if(c != -1)
                line.appendCodePoint(c);
            
            
            return line.toString().trim();
//            for(int i = 0; i < codeCount; i++){
//                c = super.read();
//                if(c == -1){
//                    return line.toString();
//                }else if(c == '\n' || c == '\r' || c == -1){
//                    //���lineΪ��ʱ�����κ���
//                    if(line.length() != 0) {
////                        System.out.println("ZhangReader.readLine():" + line.toString());
//                        return line.toString().trim();
//                    }
//                }else{
//                    line.appendCodePoint(c);
//                }
//            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    // 读入流，直到读入ch或到流的末尾，返回读入的String
    // if a Exception occur, return null;
    public String till(int ch){
        StringBuilder line = new StringBuilder();
        int c = 0;
        
        try {
            c = super.read();
            while(c != -1 && c != ch){
                line.appendCodePoint(c);
                
                c = super.read();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return line.toString();
    }
}
