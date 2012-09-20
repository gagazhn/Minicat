/*
 * Complier3.java
 *
 * Created on April 7, 2008, 8:15 PM
 *
 * the jsp Complier.
 * the final version!
 * maybe~~
 * it should support the taglib of jsp
 */

package httpserver.serverTools;

import httpserver.*;
import httpserver.zhang.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author zhangyue
 */
public class Complier3 {
    public static final int COMPLIE_SUCCESSFUL = 0;
    public static final int FILE_NOT_FOUND = 1;
    public static final int COMPLIE_ERROR = 2;
    
    // to store the state reading a character
    // in the jsp file. for example, the '<', the '"',
    private Stack<Integer> charStack;
    
    private String fileName;
    private String charset;
    private ZhangReader reader;
    
    private LinkedList<StringBuilder> mainText;  // to store the html code by line;
    private LinkedList<String> imports; // to store the imports
    private String declare;          // to store the declare;
    
    //the normale constructor
    public Complier3(String fileName) {
        this(fileName, "UTF-8");
    }
    
    public Complier3(String fileName, String charset) {
        this.fileName = fileName;
        this.charset = charset;
    }
    
    public int Complier() {
        mainText = new LinkedList<StringBuilder>();
        imports = new LinkedList<String>();
        mainText.add(new StringBuilder());
        
        try {
            reader = new ZhangReader(fileName, charset);
        } catch (IOException ex) {
            //file is not exsit
            ex.printStackTrace();
            return FILE_NOT_FOUND;
        }
        
        charStack = new Stack<Integer>();
        
        try {
            
            int c = reader.read();
            while(c != -1) {
                // to do with the charatcer
                if('<' == c) {
                    c = reader.read();
                    if('%' == c) {
                        //the dynamic part
                        c = reader.read();
                        if('@' == c) {
                            // the header of the jsp
                            String name = getHeaderName();
                            if("page".equalsIgnoreCase(name))
                                doPage();
                            else if("taglib".equalsIgnoreCase(name))
                                doTaglib();
                        } else if('!' == c)
                            doDeclare();
                        else if('=' == c)
                            doStatment();
                        else if(-1 == c)
                            // means a error
                            // a unexpected end of the file
                            return COMPLIE_ERROR;
                        else
                            doScript();
                    } else if(-1 == c)
                        // means a error
                        // a unexpected end of the file
                        return COMPLIE_ERROR;
                    else {
                        // means a html code
                        doHtml(c);
                    }
                } else if(-1 == c) {
                    // means a error
                    // a unexpected end of the file
                    return COMPLIE_ERROR;
                } else {
                    // means a html code
                    doHtml(c);
                }
                
                c = reader.read();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return COMPLIE_ERROR;
        }
        
        
        return 0;
    }
    
    private void doHtml(int c) {
        if('\r' == c || '\n' == c) {
            if(mainText.getLast().length() != 0) {
                // avoid out.write("");
                mainText.getLast().insert(0, "out.writer(\"");
                mainText.getLast().append("\");\n");
                mainText.add(new StringBuilder());  // a new line;
            }
        } else {
            // normal code;
            mainText.getLast().appendCodePoint(c);
        }
    }
    
    private void doTaglib() {
        String name = getAttributeName();
        while(name != null) {
            String attribute = getAttribute();
            //donothing at present
            
            name = getAttributeName();
        }
    }
    
    private void doPage() {
        String name = getAttributeName();
        while(name != null) {
            if("import".equalsIgnoreCase(name)) {
                String attribute = getAttribute();
                imports.add(attribute);
            } else if("pageEncoding".equalsIgnoreCase(name)) {
                // donothing;
                String attribute = getAttribute();
            } else
                // jump the attribute;
                getAttribute();
            
            name = getAttributeName();
        }
    }
    
    private void doScript() {
        //to do with the rest html
        if(mainText.getLast().length() != 0)
            mainText.add(new StringBuilder());
        
        int buf = 0;
        
        boolean isInll = false;
        StringBuilder script = mainText.getLast();
        try {
            int c = reader.read();
            while(c != -1) {
                if(isInll) {
                    if('\\' == c) {
                        // next character
                        script.appendCodePoint(reader.read());
                    } else if('"' == c) {
                        script.appendCodePoint(c);
                        isInll = false;
                    } else {
                        // a normal script
                        script.appendCodePoint(c);
                    }
                } else {
                    if('"' == c) {
                        script.appendCodePoint(c);
                        isInll = true;
                    }
                }
                
                c = reader.read();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void doDeclare() {
        ;
    }
    
    private void doStatment() {
        ;
    }
    
    // get the attribute from the line in the '"'
    // igorne the other tag like '<', '>' and so on.
    private String getAttribute() {
        return null;
    }
    
    // get the name of the header like "page", "tablib" ...
    // it stop by a ' ' or a '%'
    private String getHeaderName() {
        return null;
    }
    
    // get the name of the attribute outside the '"'.
    // it will search till get a '%'
    // if this function reachs the end of the tag, it
    // returns null. so by checking the returned value
    // we can find the end.
    private String getAttributeName() {
        return null;
    }
    
    
    
}
