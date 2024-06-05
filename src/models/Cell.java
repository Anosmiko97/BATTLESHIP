package models;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.awt.Font;

public class Cell {
    AppProperties properties = new AppProperties();
    private Color cellColor;
    private String ship;
    private JButton button;

    public Cell(Color cellColor) {
        button = new JButton();
        Font fontCells = new Font(properties.getFontApp(), Font.PLAIN, 20);
        button.setForeground(Color.WHITE);
        button.setFont(fontCells);
        button.setOpaque(true);
        this.cellColor = cellColor;
        button.setBackground(cellColor);
        button.setBorder(BorderFactory.createLineBorder(Color.white));
    }

    /* Getters y setters */
    public Color getCellColor() {
        return cellColor;
    }
    public void setCellColor(Color color) {
        this.cellColor = color;
        button.setBackground(cellColor);
        button.revalidate();
        button.repaint();
    }

    public JButton getButton() {
        return button;
    }
    public void setButton(JButton button) {
        this.button = button;
    }

    public String getShip() {
        return ship;
    }
    public void setShip(String ship) {
        this.ship = ship;
    }
}
