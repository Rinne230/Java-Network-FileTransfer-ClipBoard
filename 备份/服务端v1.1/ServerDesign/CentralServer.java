    import java.io.*;
    import java.net.*;
    import java.util.*;

    public class CentralServer { // 中央服务器类
        private static final int PORT = 8888; //服务器持续监听的端口号
        private static Map<String, String> clients = Collections.synchronizedMap(new HashMap<>()); //客户端信息映射

        public static void main(String[] args) {
            System.out.println("Central Server is running...");
            try (ServerSocket serverSocket = new ServerSocket(PORT)) { // 创建服务器套接字并绑定到指定端口
                while (true) { // 持续监听客户端 
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start(); // 启动客户端处理线程（多线程处理客户端请求）
                }
            } catch (IOException e) { 
                e.printStackTrace(); // 异常堆栈打印
            }
        }

        private static class ClientHandler extends Thread { // 客户端线程处理类
            private Socket socket; // 客户端套接字

            public ClientHandler(Socket socket) {
                this.socket = socket; // 构造方法，客户端套接字
            }

            @Override
            public void run() {
                try (DataInputStream dis = new DataInputStream(socket.getInputStream()); // 输入流，读取客户端发送的信息
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) { //输出流，向客户端发送信息

                    String command = dis.readUTF();
                    if (command.equals("REGISTER")) { // 处理注册命令
                        String ip = dis.readUTF(); // ip
                        String port = dis.readUTF(); // 端口号
                        String alias = dis.readUTF(); // 别名
                        String id = ip + ":" + port; // 唯一的客户端表示符
                        clients.put(id, alias);
                        dos.writeUTF("REGISTERED");
                    } else if (command.equals("UNREGISTER")) { // 处理注销命令
                        String ip = dis.readUTF();
                        String port = dis.readUTF();
                        String id = ip + ":" + port;
                        clients.remove(id);
                        dos.writeUTF("UNREGISTERED");
                    } else if (command.equals("GET_CLIENTS")) { // 处理获取在线的客户端
                        dos.writeInt(clients.size()); // 在线的客户端数量
                        for (Map.Entry<String, String> entry : clients.entrySet()) {
                            dos.writeUTF(entry.getKey() + "," + entry.getValue()); // 遍历传递每个客户端信息
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // 异常堆栈打印
                }
            }
        }
    }
