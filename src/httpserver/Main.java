/*
 * Main.java
 *
 * Created on 2008.2.12, 3:02
 * A http server
 * @author 张月
 *
 */

package httpserver;

public class Main {
    // 这是在LINUX环境下输入的文字
    // 不知在WINDOWS下能否被看见
    public static void main(String[] args) {
        new MainServer().serverRun();
    }
}