package controlers.Lan;

import java.awt.Color;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import models.AppProperties;

/* Clases propias */
import models.Cell;
import models.ShipsPos;
import views.MatchView;
import views.MenuView;

public class LanMatchController implements ActionListener {
    private AppProperties properties = new  AppProperties();
    private MatchView matchView;
    private MenuView menuView;
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
    private ShipsPos pos = new ShipsPos();
    private Cor[] fletShips;
    private boolean turn;
    private String message;
    private Cor[] opponentShips;

    public LanMatchController(String ipHost, MatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        this.ipHost = ipHost;
        fletShips = new Cor[17];
        totalShips = 17;
        cellsShips = 0;

        if (this.mode.equals("server")) {
            turn = true;
            message = "TU TURNO";
            serverThread = new Thread(() -> {
                try {
                    runServer();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            serverThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        } else if (this.mode.equals("client")) {
            turn = false;
            message = "TURNO DE RIVAL";
            clientThread = new Thread(() -> {
                try {
                    runClient();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            clientThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        }
    }

    private void initView( MatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft) {
        this.cellsRight = cellsRight;
        this.cellsLeft = cellsLeft;
        if (!turn) {
            lockCells(cellsRight);
        }

        this.matchView = matchView;
        this.matchView.setMessage(message);
        this.matchView.refreshMessagePanel();
        this.matchView.refreshHeaderPanel();
        setPosShips();
        addCellsListener();
    }

    private void refreshMessage(String message) {
        this.matchView.setMessage(message);
        this.matchView.refreshMessagePanel();
        this.matchView.refreshHeaderPanel();
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
    private void runServer() throws ClassNotFoundException {
        serverRunning = true;
        try {
            serverSocket = new ServerSocket(port); 
            System.out.println("Servidor iniciado en el puerto: " + port);
            clientConn = new Socket();
            while (serverRunning) {        
                clientConn = serverSocket.accept(); 

                // Leer mensajes del cliente
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
                    ObjectInputStream inObj = new ObjectInputStream(clientConn.getInputStream());
                ) {
                    Object receivedObj = inObj.readObject();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Cliente dice: " + inputLine);
                        
                        if ("salir".equalsIgnoreCase(inputLine)) {
                            break;
                        } else if ("turno".equalsIgnoreCase(inputLine)){
                            turn = true;
                            refreshMessage("TU TURNO");
                            unlockCells(cellsRight);
                        } else if (receivedObj instanceof Object[]) {
                            System.out.println("Recibido arreglo de objetos");

                        } else if (receivedObj instanceof Object) {
                            System.out.println("Recibido objeto solo");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } 
    }

    private void sendServerPosShips(Cor[] posShips) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientConn.getOutputStream())
        ) {
            out.writeObject(posShips);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al host: " + ipHost);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendServerRequest(String ms) {
        try {
            PrintWriter out = new PrintWriter(clientConn.getOutputStream(), true);
            out.println(ms);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al host: " + ipHost);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendServerCor(Cor posShip) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientConn.getOutputStream())
        ) {
            out.writeObject(posShip);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al host: " + ipHost);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopServer(Boolean message) {
        serverRunning = false;

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();

                if (message) {
                    JOptionPane.showMessageDialog(menuView, "Servidor detenido", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
                System.out.println("Servidor detenido");
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    /* Codigo para cliente */
    private void runClient() throws ClassNotFoundException {
        clientRunning = true;
        try {
            clientSocket = new Socket(ipHost, port); 
            System.out.println("Conectado al servidor en " + ipHost + ":" + port);
            while (clientRunning) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in);
                    ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());
                    ) {
                    Object receivedObj = inObj.readObject();
                    String userInput;

                    // Leer mensages de servidor
                    while ((userInput = scanner.nextLine()) != null) {
                        out.println(userInput); 
                        String response = in.readLine(); 
                        System.out.println("Servidor dice: " + response);
                        
                        if ("salir".equalsIgnoreCase(userInput)) {
                            break;
                        } else if ("turno".equalsIgnoreCase(response)) {
                            turn = true;
                            refreshMessage("TU TURNO");
                            unlockCells(cellsRight);
                        } else if (receivedObj instanceof Object[]) {
                            System.out.println("Recibido arreglo de objetos");
                            Object[] objArray = (Object[]) receivedObj;
                            opponentShips = convertToCor(objArray);
                        } else if (receivedObj instanceof Object) {
                            System.out.println("Recibido objeto solo");
                            Cor posShoot = (Cor) receivedObj;
                            checkShoot(posShoot);
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
        } 
    }

    public void sendClientPosShips(Cor[] posShips) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            out.writeObject(posShips);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al cliente en localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendClientResquest(String ms) {
        try { 
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(ms);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al cliente en localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendClientCor(Cor posCell) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            out.writeObject(posCell);
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al cliente en localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
 

    /* Codigo para acciones de juego */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight[0].length; j++) {  
                if (e.getSource() == cellsRight[i][j].getButton()) {
                    gameActions(i, j);
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    //System.out.println("Barco en: [" + i + ", " + j + "]");
                    return; 
                } 
            }
        }
    }

    private void gameActions(int i, int j) {
        System.out.println("Disparo en: [" + i + ", " + j + "]");
        Cor posCell = new Cor(i, j);
        System.out.println("Posciones guardadas en:" + posCell.x + posCell.y);
        turn = false;
        lockCells(cellsRight);

        if (mode.equals("server")) {
            System.out.println("TURNO DEL CLIENTE");
            sendServerCor(posCell);
            sendServerRequest("turno");
        } else if (mode.equals("client")) {
            System.out.println("TURNO DEL RIVAL");
            sendClientCor(posCell);
            sendClientResquest("turno");
        }
    }

    private void checkShoot(Cor posShot) {
        if (cellsRight[posShot.x][posShot.x].getCellColor().equals(colorShip)) {
            System.out.println("Nos dieron XDXDXD");
                cellsRight[posShot.x][posShot.y].setCellColor(colorRed);
        } else {
            System.out.println("No nos dieron");
            cellsRight[posShot.x][posShot.y].setCellColor(colorWhite);
        }         
    }

    private Cor[] convertToCor(Object[] objArray) {
        if (objArray.length % 2 != 0) {
            throw new IllegalArgumentException("El arreglo no tiene la estructura correcta para convertir a Cor");
        }
    
        int numOfCors = objArray.length / 2;
        Cor[] corArray = new Cor[numOfCors];
    
        for (int i = 0; i < numOfCors; i++) {
            if (!(objArray[2 * i] instanceof Integer) || !(objArray[2 * i + 1] instanceof Integer)) {
                throw new IllegalArgumentException("El arreglo contiene elementos que no son enteros");
            }
            int x = (Integer) objArray[2 * i];
            int y = (Integer) objArray[2 * i + 1];
            corArray[i] = new Cor(x, y);
        }
    
        return corArray;
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

    public void setMenuView(MenuView menuView) {
        this.menuView = menuView;
    }
    public MenuView getMenuView() {
        return this.menuView;
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
