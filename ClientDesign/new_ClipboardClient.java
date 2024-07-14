import java.io.*;
import java.net.*;

public class new_ClipboardClient {
    // 服务器的IP地址，替换为实际的服务器IP地址
    private static final String SERVER_IP = "192.168.31.177";  
  
    private static final int SERVER_PORT = 8889;  // 服务器的端口号
    /**
     * 设置剪贴板的内容和语言类型
     * @param content 剪贴板的内容
     * @param language 内容的语言类型
     * @throws IOException 如果发生I/O错误
     */
    public static void setClipboardContent(String content, String language,String Ip,int Port) throws IOException {
        // 创建一个Socket连接到服务器
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             // 创建数据输出流，用于向服务器发送数据
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            // 向服务器发送数据
            dos.writeUTF("SET_CLIPBOARD");
            dos.writeUTF(Ip);
            dos.writeInt(Port);
      
            dos.writeUTF(content);
          
            dos.writeUTF(language);
        }
    }

    /**
     * 获取剪贴板的内容和语言类型
     * @return 包含内容和语言类型的字符串数组
     * @throws IOException 如果发生I/O错误
     */
    public static String[] getClipboardContent(String Ip,int Port) throws IOException {
        // 创建一个Socket连接到服务器
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             // 创建数据输出流，用于向服务器发送数据
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             // 创建数据输入流，用于从服务器接收数据
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            // 向服务器发送操作命令
            dos.writeUTF("GET_CLIPBOARD");
            dos.writeUTF(Ip);
            dos.writeInt(Port);
            
            String content = dis.readUTF();
             
            String language = dis.readUTF();
        
            return new String[]{content, language};
        }
    }
}