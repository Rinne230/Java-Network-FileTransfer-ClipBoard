import java.io.*;
import java.net.*;

public class ClipboardClient {
    private static final String SERVER_IP = "192.168.31.177";  // 替换为实际的服务器IP地址
    private static final int SERVER_PORT = 8889;

    public static void setClipboardContent(String content, String language) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            dos.writeUTF("SET_CLIPBOARD");
            dos.writeUTF(content);
            dos.writeUTF(language);
        }
    }

    public static String[] getClipboardContent() throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeUTF("GET_CLIPBOARD");
            String content = dis.readUTF();
            String language = dis.readUTF();
            return new String[]{content, language};
        }
    }
}
