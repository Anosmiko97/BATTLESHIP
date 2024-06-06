package models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell {
    AppProperties properties = new AppProperties();
    private Color cellColor;
    private String ship;
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
        SHIP_2,
        SHIP_3_1,
        SHIP_3_2,
        SHIP_4,
        SHIP_5,
        HIT_2,
        HIT_3_1,
        HIT_3_2,
        HIT_4,
        HIT_5,
        CORD
    }

    public Cell(Color cellColor) {
        side = "";
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

    public String getShip() {
        return ship;
    }
    public void setShip(String ship) {
        this.ship = ship;
    }

    // Logica de cambio de estado
    private void updateButtonAppearance() {
        switch (state) {
            case EMPTY:
                button.setBackground(cellColor);
                button.setText("");
                button.setIcon(null);
                break;

            case MISS:
                button.setBackground(cellColor);
                button.setText("");

                if (this.side != null) {
                    Image img;
                    if (this.side.equals("right")) {
                        img = missImage2.getImage();
                    } else {
                        img = missImage1.getImage();
                    }

                    Image newImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(newImg);

                    button.setIcon(scaledIcon);
                }
                break;

            case SHIP_2:
                button.setBackground(cellColor);
                button.setText("");
                if (!"right".equals(this.side)) {
                    button.setIcon(shipChart2Image);
                } else {
                    button.setIcon(null);
                }
                break;

            case SHIP_3_1:
                button.setBackground(cellColor);
                button.setText("");
                if (!"right".equals(this.side)) {
                    button.setIcon(shipChart3_1Image);
                } else {
                    button.setIcon(null);
                }
                break;

            case SHIP_3_2:
                button.setBackground(cellColor);
                button.setText("");
                if (!"right".equals(this.side)) {
                    button.setIcon(shipChart3_2Image);
                } else {
                    button.setIcon(null);
                }
                break;

            case SHIP_4:
                button.setBackground(cellColor);
                button.setText("");
                if (!"right".equals(this.side)) {
                    button.setIcon(shipChart4Image);
                } else {
                    button.setIcon(null);
                }
                break;

            case SHIP_5:
                button.setBackground(cellColor);
                button.setText("");
                if (!"right".equals(this.side)) {
                    button.setIcon(shipChart5Image);
                } else {
                    button.setIcon(null);
                }
                break;

            case HIT_2:
                button.setBackground(Color.RED);
                button.setText("X");
                button.setIcon(shipChartShoted2Image);
                break;

            case HIT_3_1:
                button.setBackground(Color.RED);
                button.setText("X");
                button.setIcon(shipChartShoted3_1Image);

                break;

            case HIT_3_2:
                button.setBackground(Color.RED);
                button.setText("X");
                button.setIcon(shipChartShoted3_2Image);

                break;

            case HIT_4:
                button.setBackground(Color.RED);
                button.setText("X");
                button.setIcon(shipChartShoted4Image);

                break;

            case HIT_5:
                button.setBackground(Color.RED);
                button.setText("X");
                button.setIcon(shipChartShoted5Image);

                break;

            case CORD:
                break;
        }
    }

    
}
