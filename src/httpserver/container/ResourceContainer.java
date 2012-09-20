/*
 * ResourceContainer.java
 *
 * Created on 2008.3.31, 11:40
 *
 * the resouce container
 *
 */

package httpserver.container;

import java.nio.MappedByteBuffer;
import java.util.*;

/**
 *
 * @author 张月
 */
public class ResourceContainer {
    ////////////
    private static HashMap<String, MappedByteBuffer> container = new HashMap<String, MappedByteBuffer>(64);
    
    public static void init(){
        
    }
    
    public static void clear(){
        container.clear();
    }
    
    public static MappedByteBuffer getResource(String key){
        return container.get(key);
    }
    
    public static void putResource(String key, MappedByteBuffer value){
        container.put(key, value);
    }
}
