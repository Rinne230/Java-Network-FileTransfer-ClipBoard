import java.net.*;
import java.util.Enumeration;

public class NetworkUtils {
    public static String getLocalIpAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        throw new SocketException("Unable to determine local IP address");
    }
}
