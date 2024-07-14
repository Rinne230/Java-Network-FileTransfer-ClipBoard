import java.io.*;
import java.net.*;
import java.util.*;

public class FileTransferClient {
    private static final String SERVER_IP = "192.168.31.177";  // 服务器IP地址
    private static final int SERVER_PORT = 8888;

    /**
     * 注册客户端到中央服务器
     * @param localIp 客户端本地 IP 地址
     * @param port 客户端端口号
     * @param alias 客户端别名
     * @throws IOException 如果注册过程中发生 IO 异常
     */
    public static void register(String localIp, int port, String alias) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT); // 与中央服务器套接字连接
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // 输出流，向服务器
             DataInputStream dis = new DataInputStream(socket.getInputStream())) { // 输入流，接受服务器

            dos.writeUTF("REGISTER"); // 注册命令
            dos.writeUTF(localIp); // 发送ip
            dos.writeUTF(Integer.toString(port)); // 发送端口
            dos.writeUTF(alias); // 别名

            String response = dis.readUTF(); 
            if (!response.equals("REGISTERED")) { // 检验是否注册成功
                throw new IOException("Failed to register");
            }
        }
    }

    /**
     * 注销客户端从中央服务器
     * @param localIp 客户端本地 IP 地址
     * @param port 客户端端口号
     * @throws IOException 如果注销过程中发生 IO 异常
     */
    public static void unregister(String localIp, int port) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeUTF("UNREGISTER");
            dos.writeUTF(localIp);
            dos.writeUTF(Integer.toString(port));

            String response = dis.readUTF();
            if (!response.equals("UNREGISTERED")) {
                throw new IOException("Failed to unregister"); // 和注册过程一样，检查是否注销成功
            }
        }
    }

    /**
     * 获取所有已注册的客户端列表
     * @return 包含所有客户端信息的映射，键为 IP:端口，值为别名
     * @throws IOException 如果获取客户端列表过程中发生 IO 异常
     */
    public static Map<String, String> getClients() throws IOException {
        Map<String, String> clients = new LinkedHashMap<>(); // 保证顺序
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeUTF("GET_CLIENTS"); // 获取客户端列表命令

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String[] clientInfo = dis.readUTF().split(",");
                clients.put(clientInfo[0], clientInfo[1]); // 存储到Map中，键为 IP+端口，值为别名
            }
        }
        return clients; 
    }
}
