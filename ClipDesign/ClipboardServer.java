import java.io.*;
import java.net.*;

public class ClipboardServer {
    private static final int PORT = 8889; // 定义服务器端口号
    private static String clipboardContent = ""; // 存储剪贴板内容
    private static String language = "Plain Text"; // 存储剪贴板内容的语言类型
    private static String Ip = "";
    private static int Port;
    private static ClipboardManager localdata = new ClipboardManager();

    public static void main(String[] args) {
        System.out.println("Clipboard Server is running..."); // 输出服务器启动信息
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // 创建服务器套接字
            while (true) {
                Socket clientSocket = serverSocket.accept(); // 接受客户端连接
                new ClientHandler(clientSocket).start(); // 启动新线程处理客户端请求
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace(); // 捕获并打印IO异常
        }
    }
    // 定义客户端处理类
    private static class ClientHandler extends Thread {
        private Socket socket; // 客户端套接字

        public ClientHandler(Socket socket) {
            this.socket = socket; // 初始化客户端套接字
        }

        @Override
        public void run() {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                String command = dis.readUTF(); // 读取客户端发送的命令
                if (command.equals("SET_CLIPBOARD")) { // 设置剪贴板内容
                    synchronized (ClipboardServer.class) {
                        Ip = dis.readUTF();
                        Port = dis.readInt();
                        clipboardContent = dis.readUTF(); // 读取并存储剪贴板内容
                        language = dis.readUTF(); // 读取并存储剪贴板内容的语言类型
                        localdata.putClipboardData(Ip, Port, clipboardContent, language);
                    }
                } else if (command.equals("GET_CLIPBOARD")) { // 获取剪贴板内容
                    synchronized (ClipboardServer.class) {
                        Ip = dis.readUTF();
                        Port = dis.readInt();
                        ClipboardData tempval=localdata.getClipboardData(Ip,Port);
                        clipboardContent=tempval.getClipboardContent();
                        language=tempval.getLanguage();
                        dos.writeUTF(clipboardContent); // 发送剪贴板内容到客户端
                        dos.writeUTF(language); // 发送剪贴板内容的语言类型到客户端
                    }
                } else {
                    dos.writeUTF("ERROR: Unknown command"); // 返回错误信息
                }
            } catch (EOFException e) {
                System.err.println("Client disconnected: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("I/O exception: " + e.getMessage());
                e.printStackTrace(); // 捕获并打印IO异常
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
                e.printStackTrace(); // 捕获并打印其他异常
            } finally {
                try {
                    socket.close(); // 关闭客户端套接字
                } catch (IOException e) {
                    System.err.println("Failed to close socket: " + e.getMessage());
                }
            }
        }
    }
}
