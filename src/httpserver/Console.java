/*
 * Console.java
 *
 * Created on 2008.3.2, 1:16
 *
 * the console of the httpserver
 */

package httpserver;


import java.io.*;
import java.net.*;
import java.util.*;

import httpserver.container.*;
/**
 *
 * @author zhangyue
 */
public class Console extends Thread{
    private MainServer mainServer;
    private Scanner scanner;
    
    public Console(MainServer mainServer) {
        this.mainServer = mainServer;
        scanner = new Scanner(System.in);
    }

    public void run() {
        String command = null;
        while(scanner.hasNextLine()){
            command = scanner.nextLine();
            analyse(command);
        }
    }
    
    // to analyse the command of the console
    private void analyse(String command){
        if("exit".equalsIgnoreCase(command)){
            System.exit(0);
        }else if("clear".equalsIgnoreCase(command)){
            ResourceContainer.clear();
            HtmlContainer.clear();
            httpserver.container.ServletContainer.clear();
            Runtime.getRuntime().gc();
            System.out.println("clear done!");
        }else if("port".equalsIgnoreCase(command)){
            System.out.println("server port is: " + mainServer.getPort());
        }else if("".equalsIgnoreCase(command)){
            //donothing;
        }else if("help".equalsIgnoreCase(command)){
            System.out.println("exit: terminor the server\n" +
                    "clear: clear the servlet object in the server\n" +
                    "port: print the port of the server\n" +
                    "help: get the command help of the server\n" +
                    "\n" +
                    "end of the help");
        }else{
            System.out.println("Wrong command: " + command);
        }
    }
}
