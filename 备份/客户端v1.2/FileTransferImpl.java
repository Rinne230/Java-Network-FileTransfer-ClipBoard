import java.io.*;
import java.net.*;

public class FileTransferImpl implements FileTransfer { // 实现文件传输接口

    @Override
    public void sendFile(File file, String destinationIp, int port) throws IOException {
        // 创建一个 Socket 连接到目标 IP 和端口
        try (Socket socket = new Socket(destinationIp, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // 输出流，向目标发送文件信息   
             FileInputStream fis = new FileInputStream(file); // 输入流，读取要发送的文件
             BufferedOutputStream bos = new BufferedOutputStream(dos)) { // 缓冲输出流

            dos.writeUTF(file.getName()); // 发送文件名
            byte[] buffer = new byte[4096]; // 缓冲区
            int bytesRead;
            // 读取文件内容并发送 （利用的是socket）
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead); // 缓冲区内容写到输出流
            }
        } catch (IOException e) {
            System.err.println("Error while sending file: " + e.getMessage()); // 发生异常时打印错误信息
            throw e; 
        }
    }

    @Override
    public void receiveFile(String savePath, int port) throws IOException { // 接收文件方法
        ServerSocket serverSocket = null;
        // 尝试绑定到指定端口
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (BindException e) {
                port++; // 如果端口被占用，尝试下一个端口
            }
        }
        // 等待客户端连接
        try (Socket socket = serverSocket.accept();
             DataInputStream dis = new DataInputStream(socket.getInputStream()); // 创建数据输入流，接收文件名和文件内容
             BufferedInputStream bis = new BufferedInputStream(dis)) { // 创建缓冲输入流，提高效率

            String fileName = dis.readUTF(); // 接收文件名
            File file = new File(savePath, fileName); // 创建文件对象，保存接收到的内容 
            try (FileOutputStream fos = new FileOutputStream(file)) { // 创建文件输出流，写入文件内容
                byte[] buffer = new byte[4096]; // 缓冲区
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
