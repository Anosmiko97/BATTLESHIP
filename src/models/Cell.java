package models;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.awt.Font;

public class Cell {
    AppProperties properties = new AppProperties();
    private Color cellColor;
    private int corX; 
    private int corY;
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
    public int getCorX() {
        return corX;
    }
    public void setCorX(int corX) {
        this.corX = corX;
    }

    public int getCorY() {
        return corY;
    }
    public void setCorY(int corY) {
        this.corY = corY;
    }

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
}
