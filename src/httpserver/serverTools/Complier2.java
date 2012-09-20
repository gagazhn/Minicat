/*
 * HtmlScanner.java
 *
 * Created on 2008.2.25, 11:29
 *
 * complier the jsp file to java
 *
 */

package httpserver.serverTools;

import httpserver.ServerThread;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import httpserver.zhang.*;
/**
 *
 * @author zhangyue
 */
public class Complier2 {
    private final String classpath = "classes";
    private String requestURI;
    private String filePath;
    private File file;
    private String className;
    
    public static final int COMPLIED_UNSUCCESSFUL = 1;
    public static final int COMPLIED_SUCCESSFUL = 0;
    
    public Complier2(String requestURI) throws IOException{
        // store the imports target@tablib
        ArrayList<String> imports = new ArrayList<String>(16);
        
        // store the declares
        ArrayList<String> declares = new ArrayList<String>(16);
        
        // main text
        ArrayList<String> mainText = new ArrayList<String>(64);
        
        
        try {
            //reader reads message from jspfile;
            ZhangReader reader = new ZhangReader("./" + httpserver.MainServer.ROOT + requestURI);
            
            this.requestURI = requestURI;
            filePath = getClassName(requestURI);
            file = new File("./" + classpath  + filePath + ".java");
            file.getParentFile().mkdirs();
            file.createNewFile();
            
            //the file writer
            FileWriter writer = new FileWriter(file, false);
            
            StringBuilder htmlCode = new StringBuilder();
            int c = reader.read();
            while(c != -1){
                if('<' == c){
                    //һ��ͷ
                    int d = reader.read();
                    if('%' == d){
                        //means the script of java
                        //to do with the htmlCode first
                        mainText.add("out.print(\"" + htmlCode.toString() + "\");\n");
                        htmlCode.delete(0, htmlCode.length());
                        
                        int e = reader.read();
                        if('!' == e){
                            //declaring
                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            // get the main of declaring
                            StringBuilder builder = new StringBuilder();
                            while(reader.ready()){
                                int f = reader.read();
                                if('%' != f){
                                    //���������ĩβ
                                    builder.appendCodePoint(f);
                                }else{
                                    //����Ľ���
                                    declares.add(builder.toString());
                                    //�Թ�'>'
                                    reader.read();
                                    break;
                                }
                            }
                        }else if('@' == e){
                            //head
                            StringBuilder flag = new StringBuilder();
                            int f = reader.read();
                            
                            //get the flag such as page | taglib
                            while(' ' != f){
                                flag.appendCodePoint(f);
                                f = reader.read();
                            }
                            
                            if("page".equalsIgnoreCase(flag.toString())){
                                //���������
                                boolean isExit =false;
                                while(!isExit){
                                    f = reader.read();
                                    if('%' == f){
                                        reader.read();
                                        break;
                                    }else if(' ' != f){
                                        //get the name of the attribute
                                        StringBuilder attribute = new StringBuilder();
                                        while(f != -1 && f != '=' && f != '%'){
                                            attribute.appendCodePoint(f);
                                            f = reader.read();
                                        }
                                        //analyse the attribute
                                        if("import".equalsIgnoreCase(attribute.toString())){
                                            //import =
                                            // get the value of the attribute
                                            reader.till('\"');
                                            StringBuilder value = new StringBuilder(reader.till('\"'));
                                            int i = value.toString().indexOf(',');
                                            if(i != -1){
                                                while(i != -1){
                                                    imports.add("import " + value.substring(0, i) + ";\n");
                                                    value.delete(0, i + 1);
                                                    i = value.toString().indexOf(',');
                                                }
                                            }else
                                                imports.add("import " + value.toString() + ";\n");
                                        }else{
                                            reader.till('\"');
                                            reader.till('\"');
                                        }
                                    }
                                }
                                
//                                boolean isExit = false;
//                                //���������
//                                while(!isExit){
//                                    //each attribute
//                                    int k = reader.read();
//                                    StringBuilder impt = new StringBuilder();
//                                    while(' ' != k && '%' != k){
//                                        impt.appendCodePoint(k);
//                                        k = reader.read();
//                                    }
//                                    //��importд��imports��,ͬʱ��'='����' '
//                                    imports.add(impt.toString().trim().replace('=', ' '));
//                                    if('%' == k)
//                                        //means the end of the header
//                                        //ignore the '>'
//                                        reader.read();
//                                    break;
//                                }
                            }else if("taglib".equalsIgnoreCase(flag.toString())){
                                int k = reader.read();
                                while('%' != k){
                                    //donothing now
                                    k = reader.read();
                                }
                                //ignore ths '>'
                                reader.read();
                            }else if("include".equalsIgnoreCase(flag.toString())){
                                int k = reader.read();
                                while('%' != k){
                                    //donothing now
                                    k = reader.read();
                                }
                                //ignore ths '>'
                                reader.read();
                            }
                        }
//                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                        }
                        else if('=' == e){
                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            StringBuilder script = new StringBuilder();
                            int f = reader.read();
                            while('%' != f){
                                if('\n' != f && '\r' != f)
                                    script.appendCodePoint(f);
                                f = reader.read();
                            }
                            reader.read();
                            mainText.add("out.print(" + script +");\n");
                            script.delete(0, script.length());
                        }else{
                            //the script
                            /////////////////////////////////////////////////
                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            StringBuilder script = new StringBuilder();
                            if(' ' != e)
                                script.appendCodePoint(e);
                            int f = reader.read();
                            while('%' != f){
                                script.appendCodePoint(f);
                                f = reader.read();
                            }
                            reader.read();      //���'>
                            mainText.add(script.toString());
                            script.delete(0, script.length());
                        }
                    }else{
                        htmlCode.appendCodePoint(c);
                        
                        if('\\' == d || '"' == d){
                            htmlCode.appendCodePoint('\\');
                        }
                        htmlCode.appendCodePoint(d);
                    }
                }else{
                    if('\\' == c || '"' == c){
                        htmlCode.appendCodePoint('\\');
                        htmlCode.appendCodePoint(c);
                    }else if('\n' == c || '\r' == c){
                        mainText.add("out.print(\"" + htmlCode.toString() + "\");\n");
                        htmlCode.delete(0, htmlCode.length());
                    }else{
                        htmlCode.appendCodePoint(c);
                    }
                }
                
                c = reader.read();
            }
            mainText.add("out.print(\"" + htmlCode.toString() + "\");");
            
            writer.write("//httpServer.serverTools.Complier by Zhang\n\n");
            
            if (!getPackageName().equals("")) {
            	writer.write("package " + getPackageName() + ";");
            }
            
            writer.write("import java.io.*;\n");
            writer.write("import java.net.*;\n\n");
            
            writer.write("import javax.servlet.*;\n");
            writer.write("import javax.servlet.http.*;\n\n");
            
            writer.write("import httpserver.zhang.*;\n\n");
            writer.write("import httpserver.zhang.http.*;\n\n");
            
            //imports
            for(String each: imports){
                writer.write(each + ";\n");
            }
            
            //get the className
            className = file.getName().substring(0, file.getName().indexOf('.'));
//            System.out.println("Complier2_182: className: " + className);
            
            writer.write("public class " + className + " extends ZhangHttpServlet {\n");
            
            for(String each: declares){
                writer.write(each);
            }
            
            writer.write("    protected void processRequest(HttpServletRequest request, HttpServletResponse response)\n");
            writer.write("    throws ServletException, IOException {\n");
//            writer.write("        PageContext pageContext = null;");
            writer.write("        HttpSession session = null;\n");
            writer.write("        ServletContext application = getServletContext();\n");
            writer.write("        ServletConfig config = getServletConfig();\n");
            writer.write("        PrintWriter out = response.getWriter();\n\n");
            
            //mainText
            for(String each: mainText){
                writer.write(each);
            }
            
            //the end of the jsp
            writer.write("        out.close();\n}\n");
            writer.write("}\n");
            
            writer.close();
//            System.out.println("cccccccccccccccccccccccccccccccccccc");
//            Process javac = Runtime.getRuntime().exec("javac "
//                    + "-cp .;src/;lib/servlet-api.jar;WEB-INF/classes/; "
//                    + classpath + filePath + ".java");
//            ZhangReader in = new ZhangReader(javac.getInputStream());
//            String line = in.readLine();
//            while(line != null){
//                System.out.println(line);
//            }
//            javac.waitFor();
//            System.out.println(javac.exitValue());
//            System.out.println("asdfasdfasdfasdf");
//            String aa = a.readLine();
//            while(aa != null){
//                System.out.println(aa);
//                aa = a.readLine();
            
            
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }
    
    public int complie(){
        String OS =System.getProperty("os.name");
        if("linux".equalsIgnoreCase(OS)){
            System.out.println("Complier2: Start to compile for linux:");
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            Iterable compilationUnits = fileManager.getJavaFileObjects(classpath + filePath + ".java");
            Iterable<String> options = Arrays.asList("-cp", ".:./bin:lib/servlet-api.jar:./root/web-inf/classes/:");
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
            
            boolean result = task.call();
//            int i = com.sun.tools.javac.Main.compile(new String[]{ "-cp"
//                    ,".:./build/classes/:lib/servlet-api.jar:" + "./" + httpserver.MainServer.ROOT + "web-inf/classes/: "
//                    , classpath + filePath + ".java"});
            System.out.println("Compiler2: end." + result);
            if(!result){
                // means the server can't complie the file
                // so send the error messages to the cilent
                return COMPLIED_UNSUCCESSFUL;
            }else{
                // complie sucess
                return COMPLIED_SUCCESSFUL;
            }
        }else if("unix".equalsIgnoreCase(OS)){
            return COMPLIED_UNSUCCESSFUL;
        }else{
            //windows
        	System.out.println("Complier2: Start to compile for win:");
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            Iterable compilationUnits = fileManager.getJavaFileObjects(classpath + filePath + ".java");
            Iterable<String> options = Arrays.asList("-cp", ".:./bin:lib/servlet-api.jar:./root/web-inf/classes/:");
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
            
            int result = compiler.run(null, null, null, "-cp .:./bin:lib/servlet-api.jar:./" + 
            httpserver.MainServer.ROOT + "web-inf/classes/: " + classpath + filePath + ".java");
//            int i = com.sun.tools.javac.Main.compile(new String[]{ "-cp"
//                    ,".:./build/classes/:lib/servlet-api.jar:" + "./" + httpserver.MainServer.ROOT + "web-inf/classes/: "
//                    , classpath + filePath + ".java"});
            System.out.println("Compiler2: end." + result);
            if(result != 0){
                // means the server can't complie the file
                // so send the error messages to the cilent
                return COMPLIED_UNSUCCESSFUL;
            }else{
                // complie sucess
                return COMPLIED_SUCCESSFUL;
            }
        }
        
    }
    
    private String getClassName(String requestURI){
        return requestURI.replace('.', '_');
    }
    
    private String getPackageName() {
    	String p = getClassName();
    	if (p.lastIndexOf(".") == -1) {
    		return "";
    	}
    	p = p.substring(0, p.lastIndexOf("."));
    	return p;
    }
    
    public String getClassName(){
    	//!!
    	System.out.println(requestURI);
    	System.out.println(requestURI.replace(".", "_").replace("/", "."));
        String className = requestURI.replace(".", "_").replace("/", ".");
        return className.substring(1);
    }
    
    public String getJspClass(){
        return file.getPath().substring(0, file.toString().lastIndexOf('.')) + ".class";
    }
//
//    public String getClassName(){
//        return jspClass;
//    }
}
