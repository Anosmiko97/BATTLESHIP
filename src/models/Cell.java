package models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell {
    AppProperties properties = new AppProperties();
    private Color colorRed = Color.decode("#FF0000");
    private Color colorWhite = Color.decode("#FFFFFF");
    private Color cellColor;
    private int corX;
    private int corY;
    private JButton button;
    private CellState state;
    private String side;
    
    // Imagenes de las celdas y barcos
    private ImageIcon missImage1 = new ImageIcon("media/images/missChart1.png");
    private ImageIcon missImage2 = new ImageIcon("media/images/missChart2.png");

    private ImageIcon shipChart2Image = new ImageIcon("media/images/shipChart2.png");
    private ImageIcon shipChart3_1Image = new ImageIcon("media/images/shipChart3_1.png");
    private ImageIcon shipChart3_2Image = new ImageIcon("media/images/shipChart3_2.png");
    private ImageIcon shipChart4Image = new ImageIcon("media/images/shipChart4.png");
    private ImageIcon shipChart5Image = new ImageIcon("media/images/shipChart5.png");

    private ImageIcon shipChartShoted2Image = new ImageIcon("media/images/shipChartShoted2.png");
    private ImageIcon shipChartShoted3_1Image = new ImageIcon("media/images/shipChartShoted3_1.png");
    private ImageIcon shipChartShoted3_2Image = new ImageIcon("media/images/shipChartShoted3_2.png");
    private ImageIcon shipChartShoted4Image = new ImageIcon("media/images/shipChartShoted4.png");
    private ImageIcon shipChartShoted5Image = new ImageIcon("media/images/shipChartShoted5.png");

    
    // Posibles estados de cada celda
    public enum CellState {
        EMPTY,     
        MISS,     
        SHIP,     
        HIT       
    }

    public Cell(Color cellColor) {
        button = new JButton();
        Font fontCells = new Font(properties.getFontApp(), Font.PLAIN, 20);
        button.setForeground(Color.WHITE);
        button.setFont(fontCells);
        button.setOpaque(true);
        this.cellColor = cellColor;
        button.setBackground(cellColor);
        button.setBorder(BorderFactory.createLineBorder(Color.white));
        this.state = CellState.EMPTY;
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
    }

    public Color getColorRed() {
        return colorRed;
    }
    public void setColorRed(Color colorRed) {
        this.colorRed = colorRed;
        button.setBackground(colorRed);
    }

    public Color getColorWhite() {
        return colorWhite;
    }
    public void setColorWhite(Color colorWhite) {
        this.colorWhite = colorWhite;
        button.setBackground(colorWhite);
    }

    public JButton getButton() {
        return button;
    }
    public void setButton(JButton button) {
        this.button = button;
    }

    public CellState getState() {
        return state;
    }
    public void setState(CellState state) {
        this.state = state;
        updateButtonAppearance();
    }

    public String getSide() {
        return side;
    }
    public void setSide(String side) {
        this.side = side;
    }

    private void updateButtonAppearance() {
        switch (state) {
            case EMPTY:
                button.setBackground(cellColor); 
                button.setText("");
                button.setIcon(null); // Eliminar cualquier icono existente
                break;

            case MISS:
                button.setBackground(cellColor);
                button.setText("");


                Image img = missImage1.getImage();

                if (this.side.equals("left")){
                    img = missImage1.getImage();
                }
                else{
                    img = missImage2.getImage();
                }
                Image newImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
                missImage1 = new ImageIcon(newImg);
                button.setIcon(missImage1);
                break;

            case SHIP:
                button.setBackground(Color.GRAY);
                button.setText("");
                // Configura la imagen de barco según sea necesario
                // Por ejemplo:
                button.setIcon(shipChart2Image);
                break;

            case HIT:
                button.setBackground(Color.RED);
                button.setText("X");
                // Configura la imagen de barco disparado según sea necesario
                // Por ejemplo:
                button.setIcon(shipChartShoted2Image);
                break;
        }
    }
}
