import java.util.HashMap;
import java.util.Map;

public class ClipboardManager {
    private Map<IpPort, ClipboardData> clipboardMap = new HashMap<>();

    public void putClipboardData(String ip, int port, String clipboardContent, String language) {
        IpPort key = new IpPort(ip, port);
        ClipboardData value = new ClipboardData(clipboardContent, language);
        clipboardMap.put(key, value);
    }

    public ClipboardData getClipboardData(String ip, int port) {
        IpPort key = new IpPort(ip, port);
        return clipboardMap.get(key);
    }

    public static void main(String[] args) {
        ClipboardManager manager = new ClipboardManager();
        manager.putClipboardData("192.168.1.1", 8080, "Hello, World!", "Plain Text");

        ClipboardData data = manager.getClipboardData("192.168.1.1", 8080);
        System.out.println(data);
    }
}
