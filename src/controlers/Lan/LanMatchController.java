package controlers.Lan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

/* Clases propias */
import models.Cell;
import views.Lan.LanMatchView;

public class LanMatchController implements ActionListener {
    private LanMatchView matchView;
    private Cell[][] cellsRight;
    private Cell[][] cellsLeft;

    /* Atributos para conexion */
    ServerSocket serverSocket;
    Socket clientSocket;
    String mode;

    /* Atributos de juego */
    private Color colorRed = Color.decode("#FF0000");
    private Color colorWhite = Color.decode("#FFFFFF");
    private Color colorShip = Color.decode("#343434");
    private int totalShips;
    private int cellsShips;

    /* Barcos */
    private int carrier = 5;
    private int battleship = 4;
    private int cruiser = 3;
    private int submarine = 3;
    private int destroyer = 2;

    public LanMatchController(ServerSocket serverSocket, Socket clientSocket, LanMatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        if (this.mode.equals("server")) {
            this.serverSocket = serverSocket;
            System.out.println("modo servidor");
        } else if (this.mode.equals("client")) {
            this.clientSocket = clientSocket;
            System.out.println("modo cliente");
        }

        totalShips = 17;
        cellsShips = 0;

        this.matchView = matchView;
        this.cellsRight = cellsRight;
        this.cellsLeft = cellsLeft;
        addCellsListener();
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight[0].length; j++) {
                cellsRight[i][j].getButton().addActionListener(this);
                cellsLeft[i][j].getButton().addActionListener(this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight[0].length; j++) {  
                if (e.getSource() == cellsRight[i][j].getButton()) {
                    // System.out.println("Disparo en: [" + i + ", " + j + "]");
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    // System.out.println("Barco en: [" + i + ", " + j + "]");
                    gameActions(i, j);
                    return; 
                } 
            }
        }
    }

    public void gameActions(int i, int j) {
        // Posicion barcos
        if (!isDiagonal(i, j)) {
            if (cellsShips <= 16) {
                System.out.println("Barco colocado en: [" + i + ", " + j + "]");
                cellsLeft[i][j].setCellColor(colorShip);
                cellsShips += 1;
            }
        }
    }

    private void setPositionShip(int ship, int i, int j) {
        int shipCopy = ship;
        if (!cellsLeft[i][j].getCellColor().equals(colorShip)) {   
            posCellShip(ship, shipCopy, j);
        }
    }

    private void posCellShip(int ship, int i, int j) {
        if (ship > 0) {
            System.out.println("Barco colocado en: [" + i + ", " + j + "]");
            cellsLeft[i][j].setCellColor(colorShip);
            cellsShips += 1;
            ship -= 1;
            System.out.println("Cellships = " + cellsShips);
        }
    }

    private boolean isDiagonal(int i, int j) {
        // Comprobar las diagonales principales
        if (i > 0 && j > 0 && cellsLeft[i - 1][j - 1].getCellColor().equals(colorShip)) {
            return true;
        }
        if (i < cellsLeft.length - 1 && j < cellsLeft[0].length - 1 && cellsLeft[i + 1][j + 1].getCellColor().equals(colorShip)) {
            return true;
        }
        // Comprobar las diagonales secundarias
        if (i > 0 && j < cellsLeft[0].length - 1 && cellsLeft[i - 1][j + 1].getCellColor().equals(colorShip)) {
            return true;
        }
        if (i < cellsLeft.length - 1 && j > 0 && cellsLeft[i + 1][j - 1].getCellColor().equals(colorShip)) {
            return true;
        }
        return false;
    }

    /* Getters y setters */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public Socket getClientSocket() {
        return this.clientSocket;
    }
}
