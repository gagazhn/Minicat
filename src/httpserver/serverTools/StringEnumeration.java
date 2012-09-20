/*
 * StringEnumeration.java
 *
 * Created on 2008.3.28, 12:58
 *
 */

package httpserver.serverTools;

import java.util.*;

/**
 *
 * @authorzhangyue
 */
public class StringEnumeration implements Enumeration{
    private HashMap map;
    public StringEnumeration(HashMap map) {
        this.map = map;
    }

    public boolean hasMoreElements() {
        return false;
    }

    public Object nextElement() {
        return null;
    }
    
}
