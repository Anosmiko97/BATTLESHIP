
package controlers.Local;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.w3c.dom.events.MouseEvent;

import models.Cell;
import models.Cell.CellState;
import views.Local.MatchView;

public class MatchController implements ActionListener {
    private MatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;
    private boolean placingShips;
    private int[] shipSizes = {5, 4, 3, 3, 2};
    private int current = 6;
    private int currentShipIndex = 0;
    private boolean horizontal = true;


    public MatchController(MatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft) {
        this.matchView = matchView;
        this.cellsRigth = cellsRigth;
        this.cellsLeft = cellsLeft;
        this.placingShips = true; // Comenzar en modo colocación de barcos
        addKeyListener(); // Llamar aquí para asegurarse de que el KeyListener esté activo desde el principio
        addRightCellsListener();
        addLeftCellsListener();
        matchView.addExitButtonListener(this);
        matchView.addStartButtonListener(this);
        updateCursor();

    }

    public void addRightCellsListener() {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth[0].length; j++) {
                cellsRigth[i][j].getButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleRightCellClick(e);
                    }
                });
            }
        }
    }
    
    public void addLeftCellsListener() {
        for (int i = 0; i < cellsLeft.length; i++) {
            for (int j = 0; j < cellsLeft[0].length; j++) {
                final int row = i;
                final int col = j;
                cellsLeft[i][j].getButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleLeftCellClick(e, row, col);
                    }
                });
            }
        }
    }
    

    private void handleRightCellClick(ActionEvent e) {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth[0].length; j++) {
                if (e.getSource() == cellsRigth[i][j].getButton()) {
                    if (placingShips) {
                        // No hacer nada en el tablero derecho durante la colocación de barcos
                    } else {
                        handleCellClick(cellsRigth[i][j]);
                    }
                    return;
                }
            }
        }
    }

    private void handleLeftCellClick(ActionEvent e, int row, int col) {
        
        if (placingShips) {                

            if (placeShip(row, col)) {
                System.out.println("Colocando barco en (izquierda): [" + row + ", " + col + "]");
                currentShipIndex++;
                if (currentShipIndex >= shipSizes.length) {
                    placingShips = false;
                    System.out.println("Todos los barcos han sido colocados.");
                    resetCursor();

                }
            } else {
                JOptionPane.showMessageDialog(matchView, "No hay suficiente espacio para colocar el barco aquí. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
        matchView.requestFocusInWindow(); // Solicitar el foco después de cada click para asegurarse de que el KeyListener sigue activo
    }
    
    

    private boolean placeShip(int row, int col) {
        int shipSize = shipSizes[currentShipIndex];
    
        if (horizontal) {
            if (col + shipSize > cellsLeft[0].length) {
                return false;
            }
            for (int i = col; i < col + shipSize; i++) {
                if (cellsLeft[row][i].getState() != CellState.EMPTY) {
                    return false;
                }
            }
            for (int i = col; i < col + shipSize; i++) {
                cellsLeft[row][i].setState(CellState.SHIP);
            }
        } else {
            if (row + shipSize > cellsLeft.length) {
                return false;
            }
            for (int i = row; i < row + shipSize; i++) {
                if (cellsLeft[i][col].getState() != CellState.EMPTY) {
                    return false;
                }
            }
            for (int i = row; i < row + shipSize; i++) {
                cellsLeft[i][col].setState(CellState.SHIP);
            }
        }
        
        // Llama a updateCursor() después de colocar un barco
        current--;

        updateCursor();
        
        return true;
    }
    
    
    private void toggleOrientation() {
        horizontal = !horizontal;
        System.out.println("Orientación cambiada a " + (horizontal ? "horizontal" : "vertical"));
    }

    private void addKeyListener() {
        matchView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    horizontal = !horizontal;
                    System.out.println("Orientación cambiada a " + (horizontal ? "horizontal" : "vertical"));
                    updateCursor();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (placingShips && currentShipIndex >= shipSizes.length) {
                        startGame();
                    }
                }
            }
        });
        matchView.setFocusable(true);
        matchView.requestFocusInWindow();
        
    }
    

    private void updateCursor() {
        String iconPath = null;
        if (horizontal) {
            iconPath = "media/images/horizontalCursor" + (current) + ".png";
        } else {
            iconPath = "media/images/verticalCursor" + (current) + ".png";
        }
    
        ImageIcon icon = new ImageIcon(iconPath);
        Image image = icon.getImage();
        int newWidth = image.getWidth(null) / 16;
        int newHeight = image.getHeight(null) / 16;
        
        // Redimensionar la imagen a 7x32 píxeles
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(resizedImage, new Point(0, 0), "Custom Cursor");
    
        matchView.setCursor(cursor);

    }
    
    private void resetCursor() {
        Cursor defaultCursor = Cursor.getDefaultCursor();
        matchView.setCursor(defaultCursor);
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == matchView.getExitButton()) {
            System.out.println("Saliendo de la partida");
            // Lógica para salir de la partida
        } else if (e.getSource() == matchView.getStartButton()) {
            startGame();
        }
    }

    private void handleCellClick(Cell cell) {
        if (cell.getSide().equals("right")){
            switch (cell.getState()) {
                case EMPTY:
                    cell.setState(CellState.MISS);
                    break;
                case SHIP:
                    cell.setState(CellState.HIT);
                    break;
                // Puedes agregar más lógica aquí si es necesario
                default:
                    break;
            }
        }{
            // Logica para la derecha owo
        }
    }

    public void startGame() {
        this.placingShips = false;
        JOptionPane.showMessageDialog(matchView, "¡El juego ha comenzado!", "Info", JOptionPane.INFORMATION_MESSAGE);
        resetCursor();

    }
}