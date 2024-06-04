package models;

import java.awt.Color;

public class ShipsPos {
    private Color colorShip = Color.decode("#343434");

    public ShipsPos() {}
    
    public void pos1(Cell[][] cells) {
        // Establecer carrier
        for (int i = 2; i < 2+5; i++) {
            cells[i][2].setCellColor(colorShip);
            cells[i][2].setShip("carrier"); 
        }
    
        // Establecer battleship
        for (int i = 1; i < 1+4; i++) {
            cells[i][5].setCellColor(colorShip);
            cells[i][5].setShip("battleship"); 
        }
    
        // Establecer cruiser
        for (int i = 3; i < 3+3; i++) {
            cells[i][9].setCellColor(colorShip);
            cells[i][9].setShip("cruiser"); 
        }
    
        // Establecer submarine
        for (int i = 5; i < 5+3; i++) {
            cells[i][6].setCellColor(colorShip);
            cells[i][6].setShip("submarine"); 
        }
    
        // Establecer destroyer
        for (int i = 9; i < 9+2; i++) {
            cells[i][2].setCellColor(colorShip);
            cells[i][2].setShip("destroyer"); 
        }
    }
    
    public void pos2(Cell[][] cells) {
        // Establecer carrier
        for (int i = 1; i < 1+5; i++) {
            cells[i][1].setCellColor(colorShip);
            cells[i][1].setShip("carrier"); 
        }
    
        // Establecer battleship
        for (int i = 3; i < 3+4; i++) {
            cells[i][4].setCellColor(colorShip);
            cells[i][4].setShip("battleship"); 
        }
    
        // Establecer cruiser
        for (int i = 6; i < 6+3; i++) {
            cells[i][8].setCellColor(colorShip);
            cells[i][8].setShip("cruiser"); 
        }
    
        // Establecer submarine
        for (int i = 7; i < 7+3; i++) {
            cells[i][3].setCellColor(colorShip);
            cells[i][3].setShip("submarine"); 
        }
    
        // Establecer destroyer
        for (int i = 5; i < 5+2; i++) {
            cells[i][6].setCellColor(colorShip);
            cells[i][6].setShip("destroyer"); 
        }
    }
    
    public void pos3(Cell[][] cells) {
        // Establecer carrier
        for (int i = 4; i < 4+5; i++) {
            cells[i][1].setCellColor(colorShip);
            cells[i][1].setShip("carrier"); 
        }
    
        // Establecer battleship
        for (int i = 1; i < 1+4; i++) {
            cells[i][7].setCellColor(colorShip);
            cells[i][7].setShip("battleship"); 
        }
    
        // Establecer cruiser
        for (int i = 6; i < 6+3; i++) {
            cells[i][5].setCellColor(colorShip);
            cells[i][5].setShip("cruiser"); 
        }
    
        // Establecer submarine
        for (int i = 2; i < 2+3; i++) {
            cells[i][9].setCellColor(colorShip);
            cells[i][9].setShip("submarine"); 
        }
    
        // Establecer destroyer
        for (int i = 7; i < 7+2; i++) {
            cells[i][2].setCellColor(colorShip);
            cells[i][2].setShip("destroyer"); 
        }
    }
    
    public void pos4(Cell[][] cells) {
        // Establecer carrier
        for (int i = 1; i < 1+5; i++) {
            cells[i][3].setCellColor(colorShip);
            cells[i][3].setShip("carrier"); 
        }
    
        // Establecer battleship
        for (int i = 5; i < 5+4; i++) {
            cells[i][6].setCellColor(colorShip);
            cells[i][6].setShip("battleship"); 
        }
    
        // Establecer cruiser
        for (int i = 0; i < 0+3; i++) {
            cells[i][8].setCellColor(colorShip);
            cells[i][8].setShip("cruiser"); 
        }
    
        // Establecer submarine
        for (int i = 4; i < 4+3; i++) {
            cells[i][4].setCellColor(colorShip);
            cells[i][4].setShip("submarine"); 
        }
    
        // Establecer destroyer
        for (int i = 8; i < 8+2; i++) {
            cells[i][1].setCellColor(colorShip);
            cells[i][1].setShip("destroyer"); 
        }
    }
    
    public void pos5(Cell[][] cells) {
        // Establecer carrier
        for (int i = 3; i < 3+5; i++) {
            cells[i][1].setCellColor(colorShip);
            cells[i][1].setShip("carrier"); 
        }
    
        // Establecer battleship
        for (int i = 2; i < 2+4; i++) {
            cells[i][5].setCellColor(colorShip);
            cells[i][5].setShip("battleship"); 
        }
    
        // Establecer cruiser
        for (int i = 1; i < 1+3; i++) {
            cells[i][9].setCellColor(colorShip);
            cells[i][9].setShip("cruiser"); 
        }
    
        // Establecer submarine
        for (int i = 7; i < 7+3; i++) {
            cells[i][2].setCellColor(colorShip);
            cells[i][2].setShip("submarine"); 
        }
    
        // Establecer destroyer
        for (int i = 9; i < 9+2; i++) {
            cells[i][7].setCellColor(colorShip);
            cells[i][7].setShip("destroyer"); 
        }
    }
}
