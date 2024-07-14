import java.io.File;
import java.io.IOException;

public interface FileTransfer {
    /**
     * 发送文件方法
     * @param file 要发送的文件对象
     * @param destinationIp 目标 IP 地址
     * @param port 目标端口号
     * @throws IOException 如果发送过程中发生 IO 异常
     */
    void sendFile(File file, String destinationIp, int port) throws IOException;
    /**
     * 接收文件方法
     * @param savePath 文件保存路径
     * @param port 监听的端口号
     * @throws IOException 如果接收过程中发生 IO 异常
     */
    void receiveFile(String savePath, int port) throws IOException;
}
