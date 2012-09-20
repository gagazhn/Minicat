/*
 * HtmlContainer.java
 *
 * Created on 2008.3.31, 11:39
 * 
 * a Container for static html page 
 *
 */

package httpserver.container;

import java.util.*;

/**
 *
 * @author  zhangyue
 */
public class HtmlContainer {
    private static HashMap<String, ArrayList<String>> container = new HashMap<String, ArrayList<String>>(32);
    
    public static void init(){
        ;
    }
    
    public static void clear(){
        container.clear();
    }
    
    public static ArrayList<String> getHtml(String key){
        return container.get(key);
    }
    
    public static void putHtml(String key, ArrayList<String> html){
        container.put(key, html);
    }
}
