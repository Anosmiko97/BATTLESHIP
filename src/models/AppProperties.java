/*
 * En este doc, se definen cosas como los colores de fondo,
 * tamano de fuente, etc, cosas de ese estilo para hacer cambios rapidos
 * 
 */

package models;

import java.awt.Color;

public class AppProperties {

    // Estilos del juego
    private Color backgroundColor = hexToColor("#111D2E");

    // Parametros de socket
    private int port = 12345;

    public AppProperties() {} 
    
    // Getters y setters
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public int getPort() {
        return this.port;
    }public void setPort(int port) {
        this.port = port;
    }

    public Color hexToColor(String hexColor) {
        if (hexColor.startsWith("#") && hexColor.length() == 7) {
            int rgb = Integer.parseInt(hexColor.substring(1), 16);
            return new Color(rgb);
        } else {
            throw new IllegalArgumentException("El formato del color hexadecimal es inválido. Debe ser #RRGGBB.");
        }
    }
}
