package controlers.test.img;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class TestScript {
    public static void main(String[] args) {
        String ip = obtenerIPWifi("wlan3");
        if (ip != null) {
            System.out.println("La dirección IP de wlan3 es: " + ip);
        } else {
            System.out.println("No se pudo obtener la dirección IP de wlan3.");
        }
    }

    public static String obtenerIPWifi(String interfaz) {
        try {
            // Obtiene la interfaz de red especificada
            NetworkInterface ni = NetworkInterface.getByName(interfaz);
            if (ni != null && ni.isUp()) {
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // Filtra solo las direcciones IPv4
                    if (address instanceof java.net.Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra la IP
    }
}

