package controlers.Lan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import models.AppProperties;
/* Clases propias */
import models.Cell;
import views.Lan.LanMatchView;

public class LanMatchController implements ActionListener {
    private AppProperties properties = new  AppProperties();
    private LanMatchView matchView;
    private Cell[][] cellsRight;
    private Cell[][] cellsLeft;

    /* Atributos para conexion */
    private ServerSocket serverSocket;
    Socket clientSocket;
    String mode;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = AppProperties.getWifiIp(wifiInter);
    private Thread serverThread;
    private Thread clientThread;

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

    public LanMatchController(String ipHost, LanMatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        totalShips = 17;
        cellsShips = 0;

        if (this.mode.equals("server")) {
            System.out.println("modo servidor");
           // this.serverSocket = new ServerSocket();
            

        } else if (this.mode.equals("client")) {
            //this.clientSocket = new Socket();
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
                    posShips(i, j);
                    gameActions(i, j);
                    return; 
                } 
            }
        }
    }

    public void posShips(int i, int j) {
        // Posicion barcos
        if (!isDiagonal(i, j)) {
            if (cellsShips <= 16) {
                //System.out.println("Barco colocado en: [" + i + ", " + j + "]");
                cellsLeft[i][j].setCellColor(colorShip);
                cellsShips += 1;
                System.out.println("Cellships: " + cellsShips);
            }            
        }
    }

    public void gameActions(int i, int j) {
        if (cellsShips >= 17) {
            System.out.println("A JUGAR");
           

        }
    }

    public void sendBoats() {
    if (mode.equals("server")) {
        // Asegúrate de que el cliente está conectado y listo para recibir mensajes
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("jugar"); // Envía la palabra clave "jugar" al cliente
                System.out.println("Mensaje 'jugar' enviado al cliente.");
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        } else {
            System.err.println("Cliente no conectado o socket cerrado.");
        }
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
