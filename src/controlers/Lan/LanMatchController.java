package controlers.Lan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import models.AppProperties;

/* Clases propias */
import models.Cell;
import models.ShipsPos;
import views.Lan.LanMatchView;

public class LanMatchController implements ActionListener {
    private AppProperties properties = new  AppProperties();
    private LanMatchView matchView;
    private Cell[][] cellsRight;
    private Cell[][] cellsLeft;

    /* Atributos para conexion */
    private ServerSocket serverSocket;
    private Socket clientConn;
    private Socket clientSocket;
    private String mode;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = AppProperties.getWifiIp(wifiInter);
    private String ipHost;
    private Thread serverThread;
    private Thread clientThread;
    private boolean clientRunning;
    private boolean serverRunning;

    /* Atributos de juego */
    private Color colorRed = Color.decode("#FF0000");
    private Color colorWhite = Color.decode("#FFFFFF");
    private Color colorShip = Color.decode("#343434");
    private int totalShips;
    private int cellsShips;
    private String opponentName;

    /* Barcos */
    private ShipsPos pos = new ShipsPos();
    private Cor[] fletShips;

    public LanMatchController(String ipHost, LanMatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        this.ipHost = ipHost;
        fletShips = new Cor[17];
        totalShips = 17;
        cellsShips = 0;

        if (this.mode.equals("server")) {
            System.out.println("modo servidor");
            serverThread = new Thread(() -> {
                runServer();
            });
            serverThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        } else if (this.mode.equals("client")) {
            System.out.println("modo cliente");
            clientThread = new Thread(() -> {
                runClient();
            });
            clientThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        }
    }

    private void initView( LanMatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft) {
        this.matchView = matchView;
        this.cellsRight = cellsRight;
        this.cellsLeft = cellsLeft;
        setPosShips();
        addCellsListener();
    }

    private void setPosShips() {
        Random random = new Random();
        int num = random.nextInt(5) + 1;

        if (num == 1) {
            pos.pos1(cellsLeft);
            System.out.println("posicion 1");
        } else if (num == 2) {
            pos.pos2(cellsLeft);
            System.out.println("posicion 2");
        } else if (num == 3) {
            pos.pos3(cellsLeft);
            System.out.println("posicion 3");
        } else if (num == 4) {
            pos.pos4(cellsLeft);
            System.out.println("posicion 4");
        } else if (num == 5) {
            pos.pos5(cellsLeft);
            System.out.println("posicion 5");
        } 
    }

    private void lockCells(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].getButton().setEnabled(false);
            }
        }
    }

    private void unlockCells(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].getButton().setEnabled(true);
            }
        }
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight.length; j++) {
                cellsRight[i][j].getButton().addActionListener(this);
            }
        }
    }

    /* Codigo para servidor */
    private void runServer() {
        serverRunning = true;
        try {
            serverSocket = new ServerSocket(port); 
            System.out.println("Servidor iniciado en el puerto: " + port);
            clientConn = new Socket();
            while (serverRunning) {        
                clientConn = serverSocket.accept(); 

                // Leer mensajes del cliente
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Cliente dice: " + inputLine);
                        
                        if ("salir".equalsIgnoreCase(inputLine)) {
                            break;

                        } 
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (clientConn != null) {
                    clientConn.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                System.err.println("Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }

    /* Codigo para cliente */
    private void runClient() {
        clientRunning = true;
 
        try {
            clientSocket = new Socket(ipHost, port); 
            System.out.println("Conectado al servidor en " + ipHost + ":" + port);
            while (clientRunning) {
                // Leer y enviar mensajes al servidor
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)) {
                    String userInput;

                    while ((userInput = scanner.nextLine()) != null) {
                        out.println(userInput); 
                        String response = in.readLine(); 
                        System.out.println("Servidor dice: " + response);
                        
                        if ("salir".equalsIgnoreCase(userInput)) {
                            break;
                        } 
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al host: " + ipHost);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ex) {
                System.err.println("Error al cerrar el cliente: " + ex.getMessage());
            }
        }
    }

    /* Codigo para juego */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight[0].length; j++) {  
                if (e.getSource() == cellsRight[i][j].getButton()) {
                    // System.out.println("Disparo en: [" + i + ", " + j + "]");
                    gameActions(i, j);
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    System.out.println("Barco en: [" + i + ", " + j + "]");
                    
                    return; 
                } 
            }
        }
    }

    public void gameActions(int i, int j) {
        System.out.println("Disparo en: [" + i + ", " + j + "]");
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

class Cor {
    public int x;
    public int y;

    public Cor(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
