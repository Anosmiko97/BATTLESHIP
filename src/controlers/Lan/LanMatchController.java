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
import java.util.Scanner;
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
    private int startPlay = 0;
    private String opponentName;

    /* Barcos */
    private int carrier = 5;
    private int battleship = 4;
    private int cruiser = 3;
    private int submarine = 3;
    private int destroyer = 2;
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
        addCellsListener();
        lockCells();
    }

    private void lockCells() {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight.length; j++) {
                cellsRight[i][j].getButton().setEnabled(false);
            }
        }
    }

    private void unlockCells() {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight.length; j++) {
                cellsRight[i][j].getButton().setEnabled(true);
            }
        }
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight.length; j++) {
                cellsRight[i][j].getButton().addActionListener(this);
                cellsLeft[i][j].getButton().addActionListener(this);
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
                System.out.println("Cliente conectado: " + clientConn.getInetAddress().getHostAddress());

                // Leer mensajes del cliente
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Cliente dice: " + inputLine);
                        
                        if ("salir".equalsIgnoreCase(inputLine)) {
                            break;

                        } else if ("jugar".equalsIgnoreCase(inputLine)) {
                            System.out.println("Palabra clave 'jugar' recibida");
                            startPlay += 1;
                        
                        } else if (inputLine.charAt(0) == 'n') {
                            System.out.println("Nombre recibido: ");

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

    private void sendServerStart() {
        if (clientConn != null && !clientConn.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(clientConn.getOutputStream(), true);
                out.println("jugar"); 
                startPlay += 1;
                System.out.println("Mensaje 'jugar' enviado al cliente.");
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        } else {
            System.err.println("Cliente no conectado o socket cerrado.");
        }
    }

    /* Codigo para cliente */
    private void runClient() {
        clientRunning = true;
        System.out.println("Servidor");
        try {
            clientSocket = new Socket(ipHost, port); // Conecta al servidor en la dirección y puerto especificados.
            System.out.println("Conectado al servidor en " + ipHost + ":" + port);
            while (clientRunning) {
                // Leer y enviar mensajes al servidor
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)) {
                    String userInput;
                    while ((userInput = scanner.nextLine()) != null) {
                        out.println(userInput); // Envía entrada del usuario al servidor
                        String response = in.readLine(); // Lee la respuesta del servidor
                        System.out.println("Servidor dice: " + response);
                        if ("salir".equalsIgnoreCase(userInput)) {
                            break;
                        }

                        if ("jugar".equals(response)) {
                            System.out.println("¡A jugar!");
                            startPlay += 1;
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

    private void sendClientStart() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("jugar");
                startPlay += 1;
                System.out.println("Mensaje 'jugar' enviado al servidor.");
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        } else {
            System.err.println("Servidor no conectado o socket cerrado.");
        }   
    }

    /* Codigo para juego */
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
                Cor posShip = new Cor(i, j);
                addPos(posShip);
                cellsShips += 1;
                System.out.println("Largo de flet: " + len());
            }            
        }
    }

    private int len() {
        int num = 0;
        for (int i = 0; i < 18; i++) {
            num = i;    
        }

        return num;
    }

    private void addPos(Cor pos) {
        for (int i = 0; i < fletShips.length; i++) {
            if (fletShips[i] == null) {  
                fletShips[i] = pos;
                break;
            }
        }    
    }

    public void gameActions(int i, int j) {
        if (cellsShips == 17) {
            sendToStart();
            System.out.println("Startplay: " + startPlay);
            unlockCells();

            if (startPlay == 2) {
                matchView.setMessage("VS");
                matchView.refreshMessagePanel();
                matchView.refreshHeaderPanel();
            }
        }
    }

    public void sendToStart() {
        if (mode.equals("server")) {
            sendServerStart();
        } else if (mode.equals("client")) {
            sendClientStart();
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

class Cor {
    public int x;
    public int y;

    public Cor(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
