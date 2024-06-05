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
import javax.swing.JOptionPane;

/* Clases propias */
import models.Cell;
import models.ShipsPos;
import models.AppProperties;
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
    private boolean shipsSended;

    /* Atributos de juego */
    private Color colorRed = Color.decode("#FF0000");
    private Color colorWhite = Color.decode("#FFFFFF");
    private Color colorShip = Color.decode("#343434");
    private int totalShips = 17;
    private int cellsShips = 0;
    private String opponentName;
    private ShipsPos pos = new ShipsPos();
    private String fletShips = "";
    private Cor[] posShips;
    private boolean turn;
    private String message;
    private Cor[] opponentShips;
    private int shipsSunked = 0;
    private int totalShots = 0;
    private int successfulShots = 0;

    /* Barcos */
    private int carrier = 5;
    private int battleship = 4;
    private int cruiser = 3;
    private int submarine = 3;
    private int destroyer = 2;

    public LanMatchController(String ipHost, MatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        shipsSended = false;
        this.ipHost = ipHost;
        setPosShips(cellsRight, cellsLeft);

        // Dependiendo del modo estblecido, correr hilos
        if (this.mode.equals("server")) {
            turn = true;
            message = "TU TURNO";
            serverThread = new Thread(() -> {
                runServer();
            });
            serverThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        } else if (this.mode.equals("client")) {
            turn = false;
            message = "TURNO DE RIVAL";
            clientThread = new Thread(() -> {
                runClient();
            });
            clientThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                initView(matchView, cellsRight, cellsLeft);
            });
        }
    }

    private void initView( MatchView matchView, Cell[][] cellsRight, Cell[][] cellsLeft) {
        lockCells(this.cellsLeft);
        if (!turn) {
            lockCells(this.cellsRight);
        }

        this.matchView = matchView;
        this.matchView.setMessage(message);
        this.matchView.refreshMessagePanel();
        this.matchView.refreshHeaderPanel();
    }

    private void refreshMessage(String message) {
        this.matchView.setMessage(message);
        this.matchView.refreshMessagePanel();
        this.matchView.refreshHeaderPanel();
    }

    private void setPosShips(Cell[][] cellsRight, Cell[][] cellsLeft) {
        this.cellsRight = cellsRight;
        this.cellsLeft = cellsLeft;
        
        // Seleccionar una de las 5 plantillas de posiciones de barco
        Random random = new Random();
        int num = random.nextInt(5) + 1;
        if (num == 1) {
            pos.pos1(this.cellsLeft);
            System.out.println("posicion 1");
        } else if (num == 2) {
            pos.pos2(this.cellsLeft);
            System.out.println("posicion 2");
        } else if (num == 3) {
            pos.pos3(this.cellsLeft);
            System.out.println("posicion 3");
        } else if (num == 4) {
            pos.pos4(this.cellsLeft);
            System.out.println("posicion 4");
        } else if (num == 5) {
            pos.pos5(this.cellsLeft);
            System.out.println("posicion 5");
        } 

        // Guardar pocsiones de barco 
        for (int i = 0; i < this.cellsLeft.length; i++) {
            for (int j = 0; j < this.cellsLeft.length; j++) {
                if (this.cellsLeft[i][j].getCellColor().equals(colorShip)) {
                    Cor pos = new Cor(i, j);
                    fletShips += "[" + String.valueOf(pos.x) + "|" + String.valueOf(pos.y) + "]" + ",";
                }
            }
        }
        addCellsListener();
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
                if (!shipsSended) {
                    sendShips();
                    shipsSended = true;
                } 
                processDataServer();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } 
    }

    private void processDataServer() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("salir".equalsIgnoreCase(inputLine)) {
                    serverRunning = false;
                    break;
                } else if ("turno".equalsIgnoreCase(inputLine)) {
                    turn = true;
                    refreshMessage("TU TURNO");
                    unlockCells(cellsRight);
                } else if ('d' == inputLine.charAt(0)) {
                    System.out.println("Nos dieron, fack");
                    filterRecivedCors(inputLine);
                } else {
                    opponentShips = strToCorArray(inputLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al manejar la conexión del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendServerRequest(String ms) {
        try {
            PrintWriter out = new PrintWriter(clientConn.getOutputStream(), true);
            out.println(ms); 
        } catch (UnknownHostException e) {
            System.err.println("No se puede conectar al cliente en localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el cliente: " + e.getMessage());
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
    private void runClient() {
        clientRunning = true;
        try {
            clientSocket = new Socket(ipHost, port);   
            processDataClient();
            
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al conectar con el servidor: " + e.getMessage());
            e.printStackTrace();
        } 
    }

    private void processDataClient()  {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {  
            String serverMessage;
            if (!shipsSended) {
                sendShips();
                shipsSended = true;
            }
    
            while (clientRunning && (serverMessage = in.readLine()) != null) {
                if ("salir".equalsIgnoreCase(serverMessage)) {
                    clientRunning = false;
                } else if ("turno".equalsIgnoreCase(serverMessage)) {
                    turn = true;
                    refreshMessage("TU TURNO");
                    unlockCells(cellsRight);
                } else if ('d' ==serverMessage.charAt(0)) {
                        System.out.println("Nos dieron, fack");
                        filterRecivedCors(serverMessage);
                } else {
                    opponentShips = strToCorArray(serverMessage);
                    System.out.println("Coordenadas recibidas");
                    printCorArray(opponentShips);
                }
            }
        } catch (IOException e) {
            System.err.println("No se puede conectar al host: " + ipHost);
            e.printStackTrace();
        }
    }

    public void sendClientRequest(String ms) {
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

    /* Codigo para acciones de juego */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRight.length; i++) {
            for (int j = 0; j < cellsRight[0].length; j++) {  
                if (e.getSource() == cellsRight[i][j].getButton()) {
                    gameActions(i, j);
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    return; 
                } 
            }
        }
    }

    private void gameActions(int i, int j) {
        if (turn) {
            shoot(i, j);
        } 
    }

    private void sendShips() {
        System.out.println("Cadena de => " + fletShips);
        if (mode.equals("server")) {
            sendServerRequest(fletShips);
        } else {
            sendClientRequest(fletShips);
        }
    }

    private void shoot(int i, int j) {
        System.out.println("Disparo en: [" + i + ", " + j + "]");
        Cor posCell = new Cor(i, j);
        System.out.println("Posciones guardadas en:" + posCell.x + posCell.y);
        checkShoot(posCell);

        // Cambio de turno
        turn = false;
        refreshMessage("TURNO DEL RIVAL");
        lockCells(cellsRight);

        if (mode.equals("server")) {
            System.out.println("TURNO DEL CLIENTE");
            sendServerRequest("turno");
        } else if (mode.equals("client")) {
            System.out.println("TURNO DEL RIVAL");
            sendClientRequest("turno");
        }
    }

    private void checkShoot(Cor posShot) {
        for (int i = 0; i < opponentShips.length; i++) {
            if (posShot.x == opponentShips[i].x && posShot.y == opponentShips[i].y) {
                System.out.println("DISPARRO ACERTADO");
                cellsRight[posShot.x][posShot.y].setCellColor(colorRed);
                updateScores(posShot.x, posShot.y);
                sendResquestShot(posShot);
                break;
            } else {
                System.out.println("NO DIO EN EL BALNCO");
                cellsRight[posShot.x][posShot.y].setCellColor(colorWhite);
            }
        }          
    }

    private void updateScores(int x, int y) {
        successfulShots += 1;
        totalShots += 1;
        checkShips(x, y);
        matchView.setShots(successfulShots);
        matchView.setShots(totalShots);
        matchView.refreshHeaderPanel();
    }

    private void checkCells(int x, int y) {
        posShips = strToCorArray(fletShips);
        for (int i = 0; i < posShips.length; i ++) {
            if (posShips[i].x == x && posShips[i].y == y) {
                System.out.println("nos dieron, cambio a rojo");
                cellsLeft[x][y].setCellColor(colorRed);
            } else {
                System.out.println("NO DIO EN EL BALNCO");
                cellsLeft[x][y].setCellColor(colorWhite);
            }  
        }
    }

    private void checkShips(int x, int y) {
        if (cellsLeft[x][y].getShip() == "carrier") {
            updateSunkedShips(carrier, 5);
        } else if (cellsLeft[x][y].getShip() == "battleship") {
            updateSunkedShips(battleship, 4);
        } else if (cellsLeft[x][y].getShip() == "cruiser") {
            updateSunkedShips(cruiser, 3);
        } else if (cellsLeft[x][y].getShip() == "submarine") {
            updateSunkedShips(submarine, 3);
        } else if (cellsLeft[x][y].getShip() == "destroyer") {
            updateSunkedShips(destroyer, 2);
        }
    }

    private void updateSunkedShips(int ship, int num) {
        if (ship <= num) {
            ship += 1;
        } else {
            shipsSunked += 1;
            matchView.setShipsSunked(shipsSunked);
        }
    }

    private void sendResquestShot(Cor posShot) {
        String cor = "d:"+ String.valueOf(posShot.x) + "," + String.valueOf(posShot.y);

        if (mode.equals("server")) {
            sendServerRequest(cor);
        } else if (mode.equals("client")) {
            sendClientRequest(cor);
        }
    }

    private void filterRecivedCors(String cors) {
        String[] filter1 = cors.split(":");
        String filter2 = String.join("", filter1[1]);
        String[] filter3 = filter2.split(",");
        int x = Integer.parseInt(filter3[0]);
        int y = Integer.parseInt(filter3[1]);
        checkCells(x, y);
    }

    public Cor[] strToCorArray(String str) {
        Cor[] corArray = new Cor[17];
        str = str.substring(0, str.length() - 1);
        str = str.replace("[", "").replace("]", "");
        String[] pairs = str.split(",");

        for (int i = 0; i < pairs.length; i++) {
            String[] nums = pairs[i].split("\\|");
            int x = Integer.parseInt(nums[0]);
            int y = Integer.parseInt(nums[1]);
            addTo(corArray, x, y);
        }

        return corArray;
    }

    private void addTo(Cor[] cors, int x, int y) {
        for (int i = 0; i < cors.length; i++) {
            if (cors[i] == null) {
                Cor cor = new Cor(x, y);
                setShipIn(cor, i);
                cors[i] = cor;
                break;
            }
        }
    }

    private void setShipIn(Cor cor, int i) {
        if (i <= 5) {
            cor.ship = "carrier";
        } else if (i > 5 && i <= 9) {
            cor.ship = "battleship";
        } else if (i > 9 && i <= 12) {
            cor.ship = "cruiser";
        } else if (i > 12 && i <= 15) {
            cor.ship = "submarine";
        } else if (i > 15 && i <= 17) {
            cor.ship = "destroyer";
        }
    }

    public void printCorArray(Cor[] array) {
        for (Cor cor : array) {
            System.out.println(cor);
        }
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
    public String ship;

    public Cor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "Cor{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
