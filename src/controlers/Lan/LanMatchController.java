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
    Socket clientSocket;
    String mode;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = AppProperties.getWifiIp(wifiInter);
    private String ipHost;
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
        this.ipHost = ipHost;
        totalShips = 17;
        cellsShips = 0;

        if (this.mode.equals("server")) {
            System.out.println("modo servidor");
            serverThread = new Thread(() -> {
                runServer();
            });
            serverThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.matchView = matchView;
                this.cellsRight = cellsRight;
                this.cellsLeft = cellsLeft;
                addCellsListener();
            });
        } else if (this.mode.equals("client")) {
            System.out.println("modo cliente");
            clientThread = new Thread(() -> {
                runClient();
            });
            clientThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.matchView = matchView;
                this.cellsRight = cellsRight;
                this.cellsLeft = cellsLeft;
                addCellsListener();
            });
        }
    }

    /* Codigo para servidor */
    private void runServer() {
        try {
            serverSocket = new ServerSocket(port); 
            System.out.println("Servidor iniciado en el puerto: " + port);
            clientSocket = serverSocket.accept(); 
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

            // Leer mensajes del cliente
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Cliente dice: " + inputLine);
                    
                    if ("salir".equalsIgnoreCase(inputLine)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
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
        try {
            clientSocket = new Socket(ipHost, port); // Conecta al servidor en la dirección y puerto especificados.
            System.out.println("Conectado al servidor en " + ipHost + ":" + port);

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
            }            
        }
    }

    public void gameActions(int i, int j) {
        if (cellsShips == 17) {
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
