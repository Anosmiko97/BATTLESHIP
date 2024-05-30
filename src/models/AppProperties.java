/*
 * En este doc, se definen cosas como los colores de fondo,
 * tamano de fuente, etc, cosas de ese estilo para hacer cambios rapidos
 * 
 */

package models;

import java.awt.Color;

public class AppProperties {

    // Estilos del juego
    private Color backgroundColor = Color.decode("#111D2E");
    private Color headerColor = Color.decode("#102644");
    private Color namesPanelColor = Color.decode("#224E8B");
    private String fontApp = "Arial";

    // Parametros de socket
    private int port = 12345;

    public AppProperties() {} 
    
    // Getters y setters
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
    public void setBackgroundColor(String color) {
        this.backgroundColor =  Color.decode(color);
    }

    public Color getHeaderColor() {
        return this.headerColor;
    }
    public void setHeaderColor(String color) {
        this.headerColor =  Color.decode(color);
    }

    public Color getNamesPanelColor() {
        return this.namesPanelColor;
    }public void setNamesPanelColor(String color) {
        this.namesPanelColor = Color.decode(color);
    }

    public String getFontApp() {
        return this.fontApp;
    }public void setFontApp(String font) {
        this.fontApp = font;
    }

    public int getPort() {
        return this.port;
    }public void setPort(int port) {
        this.port = port;
    }
}
