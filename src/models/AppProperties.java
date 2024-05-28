/*
 * En este doc, se definen cosas como los colores de fondo,
 * tamano de fuente, etc, cosas de ese estilo para hacer cambios rapidos
 * 
 */

package models;

import java.awt.Color;

public class AppProperties {

    Color backgroundColor;

    public AppProperties() {
        this.backgroundColor = hexToColor("#111D2E");

    } 
    
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public static Color hexToColor(String hexColor) {
        if (hexColor.startsWith("#") && hexColor.length() == 7) {
            int rgb = Integer.parseInt(hexColor.substring(1), 16);
            return new Color(rgb);
        } else {
            throw new IllegalArgumentException("El formato del color hexadecimal es inválido. Debe ser #RRGGBB.");
        }
    }
}
