/*
 * Loader.java
 *
 * Created on 2008.3.5, 7:38
 *
 * to load a complied jsp page, that meant to load a dynamic class.
 */

package httpserver.remoteClass;


import java.util.*;
import java.io.*;

//import httpserver.zhang.*;

/**
 * 目前还是只能支持单布置，即固定了class类的位置
 *
 * @author zhangyue
 */
public class RemoteClassLoader extends ClassLoader {
    private String[] classPath = {"root/web-inf/classes/", "classes/"};
    private String name;
    
    //name is the bin name of a class, filePath is a way of the file;
    public RemoteClassLoader(String name) {
        this.name = name;
    }
    
    public Class<?> findClass(){
        return findClass(name);
    }
    
    protected Class<?> findClass(String name){
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }
    
    private byte[] loadClassData(String className) {
    	//!!
    	System.out.println("FIND::::" + className);
    	for (int i = 0; i < classPath.length; i++) {
    		File classFile = new File(classNameToPath(classPath[i], className));
    		if (!classFile.isFile()) {
    			continue;
    		}
    		try { 
    			InputStream ins = new FileInputStream(classFile); 
    			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    			int bufferSize = 4096; 
    			byte[] buffer = new byte[bufferSize]; 
    			int bytesNumRead = 0; 
    			while ((bytesNumRead = ins.read(buffer)) != -1) { 
    				baos.write(buffer, 0, bytesNumRead); 
    			} 
    			return baos.toByteArray(); 
    		} catch (IOException e) { 
    			e.printStackTrace(); 
    		} 
    	}
    	
    	return null; 
    } 

    
    private String classNameToPath(String dir, String className) { 
        return dir + File.separatorChar 
                + className.replace('.', File.separatorChar) + ".class"; 
    } 
    
    /*private byte[] loadClassData(){
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
            ArrayList<Byte> list = new ArrayList<Byte>();
            byte[] b = new byte[256];
            
            int i = in.read(b);
            while(i > -1){
                for(int j = 0; j < i; j++){
                    list.add(b[j]);
                }
                i = in.read(b);
            }
            
            byte[] classByte = new byte[list.size()];
            
            for(int j = 0; j < classByte.length; j++){
                classByte[j] = list.get(j);
            }
            
            return classByte;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/
//    public static void main(String[] argc){
//        try {
//            ZhangHttpServlet s = (ZhangHttpServlet)new  RemoteClassLoader("index_jsp", "classes/index_jsp.class").findClass().newInstance();
//            System.out.println("saaa");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
