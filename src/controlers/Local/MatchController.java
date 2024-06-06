
package controlers.Local;


import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import models.Cell;
import models.Cell.CellState;
import models.Match;
import models.MatchDAO;
import views.MatchView;


public class MatchController implements ActionListener {
    private MatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;
    private boolean placingShips;
    private int[] shipSizes = {5, 4, 3, 3, 2};
    private int current = 6;
    private int currentShipIndex =0;
    private boolean horizontal = true;
    private Random random = new Random();

    private int sunkShipRight5 = 0;
    private int sunkShipRight4 = 0;
    private int sunkShipRight3_1 = 0;
    private int sunkShipRight3_2 = 0;
    private int sunkShipRight2 = 0;

    private int sunkShipLeft5 = 0;
    private int sunkShipLeft4 = 0;
    private int sunkShipLeft3_1 = 0;
    private int sunkShipLeft3_2 = 0;
    private int sunkShipLeft2 = 0;

    private int totalShipSunkRight = 0;
    private int totalShipSunkLeft = 0;


    // STATS
    int userHitCount;
    int userMissCount;
    int botHitCount;
    int botMissCount;

    public MatchController(MatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft, Cell cell) {
        this.matchView = matchView;
        this.cellsRigth = cellsRigth;
        this.cellsLeft = cellsLeft;
        this.placingShips = true; 
        addKeyListener(); 
        addRightCellsListener();
        addLeftCellsListener();
        placeRandomShipsRight();
        matchView.addExitButtonListener(this);
        matchView.addStartButtonListener(this);
        updateCursor();

        userHitCount = 0;
        userMissCount = 0;

        botHitCount = 0;
        botMissCount = 0;

    }

    public void addRightCellsListener() {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth[0].length; j++) {
                
                /// Bloquea las celdas de usos indeseables
                cellsRigth[i][j].setSide("right");
                cellsLeft[i][j].setSide("left");

                if (i == 0 && j == 0) {
                    cellsRigth[i][j].setState(CellState.CORD);
                    cellsLeft[i][j].setState(CellState.CORD);

                } else if (i == 0) {
                    cellsRigth[i][j].setState(CellState.CORD);
                    cellsLeft[i][j].setState(CellState.CORD);

                } else if (j == 0) {
                    cellsRigth[i][j].setState(CellState.CORD);
                    cellsLeft[i][j].setState(CellState.CORD);

                }
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
        if (!placingShips) {
            for (int i = 0; i < cellsRigth.length; i++) {
                for (int j = 0; j < cellsRigth[0].length; j++) {
                    if (e.getSource() == cellsRigth[i][j].getButton()) {
                        Cell clickedCell = cellsRigth[i][j];
                        clickedCell.setSide("right");
                        
                        switch (clickedCell.getState()) {
                            case EMPTY:
                                System.out.println("Miss at cell [" + i + ", " + j + "]");
                                clickedCell.setState(CellState.MISS);
                                userMissCount++;
                                updateView();
                                botTurn();

                                break;
    
                            case SHIP_2:
                                System.out.println("Hit at cell [" + i + ", " + j + "] on SHIP_2");
                                clickedCell.setState(CellState.HIT_2);
                                userHitCount++;
                                updateView();
                                botTurn();
                                sunkShipRight2++;
                                break;
    
                            case SHIP_3_1:
                                System.out.println("Hit at cell [" + i + ", " + j + "] on SHIP_3_1");
                                clickedCell.setState(CellState.HIT_3_1);
                                userHitCount++;
                                updateView();
                                botTurn();
                                sunkShipRight3_1++;

                                break;
    
                            case SHIP_3_2:
                                System.out.println("Hit at cell [" + i + ", " + j + "] on SHIP_3_2");
                                clickedCell.setState(CellState.HIT_3_2);
                                userHitCount++;
                                updateView();
                                botTurn();
                                sunkShipRight3_2++;

                                break;
    
                            case SHIP_4:
                                System.out.println("Hit at cell [" + i + ", " + j + "] on SHIP_4");
                                clickedCell.setState(CellState.HIT_4);
                                userHitCount++;
                                updateView();
                                botTurn();
                                sunkShipRight4++;

                                break;
    
                            case SHIP_5:
                                System.out.println("Hit at cell [" + i + ", " + j + "] on SHIP_5");
                                clickedCell.setState(CellState.HIT_5);
                                userHitCount++;
                                updateView();
                                botTurn();
                                sunkShipRight5++;

                                break;
    
                            default:
                                break;
                        }
                        return;
                    }
                }
            }
        }
    }
    

    private void handleLeftCellClick(ActionEvent e, int row, int col) {
        if (placingShips) {                
            if (currentShipIndex < shipSizes.length) { 
                if (placeShipLeft(row, col)) {
                    System.out.println("Colocando barco en (izquierda): [" + row + ", " + col + "]");
                    currentShipIndex++;
                    if (currentShipIndex == shipSizes.length || currentShipIndex >= shipSizes.length) {
                        placingShips = false;
                        System.out.println("Todos los barcos han sido colocados.");
                        resetCursor();
                        JOptionPane.showMessageDialog(matchView, "Todos los barcos ya han sido colocados en la izquierda. ¡Presiona Empezar partida!", "Info", JOptionPane.INFORMATION_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(matchView, "No hay suficiente espacio para colocar el barco aquí. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Se han colocado todos los barcos
                JOptionPane.showMessageDialog(matchView, "¡Todos los barcos ya han sido colocados!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
        }
        matchView.requestFocusInWindow();
        updateView();

    }

    private void botTurn() {
        System.out.println("ENTRANDO A BOT");
        int randomRow = random.nextInt(cellsLeft.length);
        int randomCol = random.nextInt(cellsLeft[0].length);
        Cell botSelectedCell = cellsLeft[randomRow][randomCol];

            if (botSelectedCell.getState() == CellState.EMPTY || 
                botSelectedCell.getState() == CellState.SHIP_2 ||
                botSelectedCell.getState() == CellState.SHIP_3_1 ||
                botSelectedCell.getState() == CellState.SHIP_3_2 ||
                botSelectedCell.getState() == CellState.SHIP_4 ||
                botSelectedCell.getState() == CellState.SHIP_5) {
                System.out.println("TURNO DEL BOT\n\n\n\n");
                // Si la celda seleccionada por el bot está vacía o contiene un barco,
                // procesa el ataque del bot y actualiza la vista
                switch (botSelectedCell.getState()) {
                    // Maneja los casos de ataque al barco de acuerdo a su estado
                    case EMPTY:
                        botSelectedCell.setState(CellState.MISS);
                        botMissCount++;
                        updateView();
                        break;
                    case SHIP_2:
                        botSelectedCell.setState(CellState.HIT_2);
                        botHitCount++;
                        sunkShipLeft2++;
                        
                        updateView();
                        break;
                    case SHIP_3_1:
                        botSelectedCell.setState(CellState.HIT_3_1);
                        botHitCount++;
                        sunkShipLeft3_1++;

                        updateView();

                        break;
                    case SHIP_3_2:
                        botSelectedCell.setState(CellState.HIT_3_2);
                        botHitCount++;
                        sunkShipLeft3_2++;

                        updateView();

                        break;
                    case SHIP_4:
                        botSelectedCell.setState(CellState.HIT_4);
                        botHitCount++;
                        sunkShipLeft4++;

                        updateView();

                        break;
                    case SHIP_5:
                        botSelectedCell.setState(CellState.HIT_5);
                        botHitCount++;
                        sunkShipLeft5++;

                        updateView();

                        break;
                    default:
                        break;
                }
            } else {
                botTurn();
            }
    }
           
    public void CheckAndIncrementLeft() {
        if (sunkShipLeft5 == 5) {
            sunkShipLeft5 = 0;
            totalShipSunkLeft++;
        }

        if (sunkShipLeft4 == 4) {
            sunkShipLeft4 = 0;
            totalShipSunkLeft++;
        }

        if (sunkShipLeft3_1 == 3) {
            sunkShipLeft3_1 = 0;
            totalShipSunkLeft++;
        }

        if (sunkShipLeft3_2 == 3) {
            sunkShipLeft3_2 = 0;
            totalShipSunkLeft++;
        }

        if (sunkShipLeft2 == 2) {
            sunkShipLeft2 = 0;
            totalShipSunkLeft++;
        }
    }

    public void CheckAndIncrementRight() {
        if (sunkShipRight5 == 5) {
            sunkShipRight5 = 0;
            totalShipSunkRight++;
        }

        if (sunkShipRight4 == 4) {
            sunkShipRight4 = 0;
            totalShipSunkRight++;
        }

        if (sunkShipRight3_1 == 3) {
            sunkShipRight3_1 = 0;
            totalShipSunkRight++;
        }

        if (sunkShipRight3_2 == 3) {
            sunkShipRight3_2 = 0;
            totalShipSunkRight++;
        }

        if (sunkShipRight2 == 2) {
            sunkShipRight2 = 0;
            totalShipSunkRight++;
        }
    }

    public int getTotalShipSunkLeft() {
        CheckAndIncrementLeft();
        System.out.println("totalShipSunkLeft: " + totalShipSunkLeft);
        return totalShipSunkLeft;
    }

    public int getTotalShipSunkRight() {
        CheckAndIncrementRight();
        System.out.println("totalShipSunkRight: " + totalShipSunkRight);
        return totalShipSunkRight;
    }

    private void updateView() {
    System.out.println("userHitCount: " + userHitCount);
    System.out.println("botHitCount: " + botHitCount);
    matchView.updateScoreLabels(getTotalShipSunkRight(), userHitCount, userHitCount + userMissCount);
    matchView.updateOpponentScoreLabels(getTotalShipSunkLeft(), botHitCount, botHitCount + botMissCount);

    // Verificar si el usuario ha hundido 5 barcos
    if (getTotalShipSunkRight() >= 5) {
        JOptionPane.showMessageDialog(matchView, "¡Felicidades! Has hundido 5 barcos. ¡Ganaste la partida!", "Victoria", JOptionPane.INFORMATION_MESSAGE);
        Match match = new Match(true, totalShipSunkRight, 100, botHitCount + botMissCount, "bot");
        MatchDAO matchDAO = new MatchDAO();
        matchDAO.insertMatch(match);    
    }

    // Verificar si el bot ha hundido 5 barcos
    if (getTotalShipSunkLeft() >= 5) {
        JOptionPane.showMessageDialog(matchView, "¡El bot ha hundido 5 barcos! Has perdido la partida.", "Derrota", JOptionPane.INFORMATION_MESSAGE);
        Match match = new Match(false, totalShipSunkRight, 0, botHitCount + botMissCount, "bot");
        MatchDAO matchDAO = new MatchDAO();
        matchDAO.insertMatch(match);  
    }
}
    
    
    private void placeRandomShipsRight() {
        for (int i = 0; i < shipSizes.length; i++) {
            int shipSize = shipSizes[i];
            CellState shipType;
    
            // Asignar el tipo de barco basado en el tamaño del barco
            switch(shipSize) {
                case 2:
                    shipType = CellState.SHIP_2;
                    break;
                case 3:
                    shipType = CellState.SHIP_3_1; 
                    break;
                case 4:
                    shipType = CellState.SHIP_4;
                    break;
                case 5:
                    shipType = CellState.SHIP_5;
                    break;
                default:
                    return; 
            }
    
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(cellsRigth.length);
                int col = random.nextInt(cellsRigth[0].length);
                boolean horizontal = random.nextBoolean();
                if (canPlaceShipRight(row, col, shipSize, horizontal)) {
                    placeShipRight(row, col, shipSize, horizontal, shipType); 
                    placed = true;
                }
            }
        }
    }
    

    private boolean canPlaceShipRight(int row, int col, int shipSize, boolean horizontal) {
        if (horizontal) {
            if (col + shipSize > cellsRigth[0].length) {
                return false;
            }
            for (int i = col; i < col + shipSize; i++) {
                if (cellsRigth[row][i].getState() != CellState.EMPTY) {
                    return false;
                }
            }
        } else {
            if (row + shipSize > cellsRigth.length) {
                return false;
            }
            for (int i = row; i < row + shipSize; i++) {
                if (cellsRigth[i][col].getState() != CellState.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeShipRight(int row, int col, int shipSize, boolean horizontal, CellState shipType) {
        if (horizontal) {
            for (int i = col; i < col + shipSize; i++) {
                cellsRigth[row][i].setState(shipType);
            }
        } else {
            for (int i = row; i < row + shipSize; i++) {
                cellsRigth[i][col].setState(shipType);
            }
        }
    }
    



    private boolean placeShipLeft(int row, int col) {
    int shipSize = shipSizes[currentShipIndex];
    CellState shipType;
    
    // Asignar el tipo de barco basado en el índice actual
    switch(currentShipIndex) {
        case 0:
            shipType = CellState.SHIP_5;
            break;
        case 1:
            shipType = CellState.SHIP_4;
            break;
        case 2:
            shipType = CellState.SHIP_3_2;
            break;
        case 3:
            shipType = CellState.SHIP_3_1;
            break;
        case 4:
            shipType = CellState.SHIP_2;
            break;
        default:
            return false;
    }

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
            cellsLeft[row][i].setState(shipType);
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
            cellsLeft[i][col].setState(shipType);
        }
    }
        current--;
    
        updateCursor();
        
        // Verifica si se ha colocado el último barco
        if (currentShipIndex >= shipSizes.length) {
            placingShips = false;
            System.out.println("Todos los barcos han sido colocados.");
            resetCursor();
        }
        
        return true;
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
    
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(iconPath);
            Image image = icon.getImage();
            int newWidth = image.getWidth(null) / 16;
            int newHeight = image.getHeight(null) / 16;
    
            Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Cursor cursor = toolkit.createCustomCursor(resizedImage, new Point(0, 0), "Custom Cursor");
            matchView.setCursor(cursor);
        } catch (Exception e) {
            System.err.println("Error al cargar imagen del cursor: " + e.getMessage());
            matchView.setCursor(Cursor.getDefaultCursor()); // Set default cursor on error
        }
    }
    
    private void resetCursor() {
        Cursor defaultCursor = Cursor.getDefaultCursor();
        matchView.setCursor(defaultCursor);
    }

    
    public int getUserHitCount(){
        return userHitCount;
    }

    public int getUserMissCount(){
        return userMissCount;
    }

    public int getBotHitCount(){
        return botHitCount;
    }

    public int getBotMissCount(){
        return botMissCount;
    }


    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(matchView.getExitButton())) {
        System.out.println("Saliendo de la partida");
        
    } else if (e.getSource() == matchView.getStartButton()) {
        startGame();
    }
}

public void startGame() {
        this.placingShips = false;
        JOptionPane.showMessageDialog(matchView, "¡El juego ha comenzado!", "Info", JOptionPane.INFORMATION_MESSAGE);
        resetCursor();

    }
}