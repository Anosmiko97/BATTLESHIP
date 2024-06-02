package controlers;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import models.Cell;
import views.MatchView;

public class MatchController implements ActionListener {
    private MatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;
    private boolean placingShip; // Indicates if the player is currently placing a ship
    private String currentShip; // The type of ship the player is currently placing
    private ImageIcon[] shipImages; // Array to hold ship images


    public MatchController(MatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft) {
        this.matchView = matchView;
        this.cellsRigth = cellsRigth;
        this.cellsLeft = cellsLeft;
        addCellsListener();
        this.placingShip = true; // Start the game in ship placing mode
        loadShipImages(); // Load ship images
        
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth.length; j++) {
                cellsRigth[i][j].getButton().addActionListener((ActionListener) this);
                cellsLeft[i][j].getButton().addActionListener((ActionListener) this);
            }
        }
    }

    private void loadShipImages() {
        shipImages = new ImageIcon[5];
        shipImages[0] = new ImageIcon("media/images/ship2.png");
        shipImages[1] = new ImageIcon("media/images/ship3_1.png");
        shipImages[2] = new ImageIcon("media/images/ship3_2.png");
        shipImages[3] = new ImageIcon("media/images/ship4.png");
        shipImages[4] = new ImageIcon("media/images/ship5.png");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (placingShip) {
            for (int i = 0; i < cellsRigth.length; i++) {
                for (int j = 0; j < cellsRigth[i].length; j++) {
                    if (e.getSource() == cellsRigth[i][j].getButton()) {
                        // Set the ship image on the selected cells based on ship size
                        setShipImageOnCells(i, j);
                        // Save the ship position or handle it accordingly
                        // For example: saveShipPosition(i, j);
                        placingShip = false; // End ship placing mode
                        return;
                    }
                }
            }
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth.length; j++) {  
                if (e.getSource() == cellsRigth[i][j].getButton()) {
                    System.out.println("Disparo en: [" + i + ", " + j + "]");
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    System.out.println("Barco en: [" + i + ", " + j + "]");
                    return; 
                } 
            }
        }
    }
}
// Set ship image on the selected cells based on ship size
private void setShipImageOnCells(int row, int col) {
    Image shipImage = shipImages[getShipIndex(currentShip)].getImage();
    int shipSize = getShipSize(currentShip);
    for (int i = 0; i < shipSize; i++) {
        if (col + i < cellsRigth[row].length) { // Check bounds
            cellsRigth[row][col + i].getButton().setIcon(new ImageIcon(shipImage));
        }
    }
}

// Get the index of the ship image array based on ship type
private int getShipIndex(String shipType) {
    switch (shipType) {
        case "ship2":
            return 0;
        case "ship3_1":
            return 1;
        case "ship3_2":
            return 2;
        case "ship4":
            return 3;
        case "ship5":
            return 4;
        default:
            return -1;
    }
}

// Get the size of the ship based on ship type
private int getShipSize(String shipType) {
    switch (shipType) {
        case "ship2":
            return 2;
        case "ship3_1":
        case "ship3_2":
            return 3;
        case "ship4":
            return 4;
        case "ship5":
            return 5;
        default:
            return -1;
    }
}

// Method to set the current ship being placed
public void setCurrentShip(String shipType) {
    this.currentShip = shipType;
}
}