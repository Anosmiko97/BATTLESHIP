/*
 * En este doc, se definen cosas como los colores de fondo,
 * tamano de fuente, etc, cosas de ese estilo para hacer cambios rapidos
 * 
 */

package models;

import java.awt.Color;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AppProperties {

    // Estilos del juego
    private Color backgroundColor = Color.decode("#111D2E");
    private Color headerColor = Color.decode("#102644");
    private Color namesPanelColor = Color.decode("#224E8B");
    private Color buttonColor = Color.decode("#173764");
    private String fontApp = "Arial";

    // Parametros de socket
    private int port = 12345;
    private String wlan = "wlan3";

    public AppProperties() {}

    public static String getWifiIp(String inter) {
        try {
            NetworkInterface ni = NetworkInterface.getByName(inter);
            if (ni != null && ni.isUp()) {
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof java.net.Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    // Getters y setters
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
    public void setBackgroundColor(String color) {
        this.backgroundColor =  Color.decode(color);
    }

    public String getWlan() {
        return this.wlan;
    }
    public void setWlan(String wlan) {
        this.wlan =  wlan;
    }

    public Color getHeaderColor() {
        return this.headerColor;
    }
    public void setHeaderColor(String color) {
        this.headerColor =  Color.decode(color);
    }

    public Color getNamesPanelColor() {
        return this.namesPanelColor;
    }
    public void setNamesPanelColor(String color) {
        this.namesPanelColor = Color.decode(color);
    }

    public String getFontApp() {
        return this.fontApp;
    }
    public void setFontApp(String font) {
        this.fontApp = font;
    }

    public int getPort() {
        return this.port;
    } 
    public void setPort(int port) {
        this.port = port;
    }

    public Color getButtonColor() {
        return this.buttonColor;
    }
    public void setButtonColor(String color) {
        this.buttonColor = Color.decode(color);
    }
}
