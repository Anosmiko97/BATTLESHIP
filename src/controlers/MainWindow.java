package controlers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import controlers.Lan.LanMatchController;
import controlers.Local.MatchController;
import controlers.Server.ClientControler;
import controlers.Server.ServerControler;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/* Clases propias */
import models.AppProperties;
import models.Cell;
import models.User;
import models.UserDAO;
import views.MenuView;
import views.SettingsView;
import views.Lan.CreateMatchView;
import views.Lan.JoinMatchView;
import views.Lan.LanMatchView;
import views.Lan.LanView;
import views.Local.MatchView;
import views.Lan.LanMatchView;

public class MainWindow extends JFrame implements ActionListener {
    private AppProperties properties = new AppProperties();

    // Modelos
    private User userModel;
    private UserDAO userDAO;

    // Controladores
    private SettingsController settingsController;
    private LanMatchController lanMatchController;
    private ServerControler serverControler;
    private MatchController matchController;

    // Vistas
    private MenuView menuView;
    private LanMatchView lanMatchView;
    private MatchView matchView;
    private LanView lanView;
    private SettingsView settingsView;
    private CreateMatchView createMatchView;
    private JoinMatchView joinMatchView;

    // Atributos para servidor y cliente
    private boolean runningServer;
    private boolean runningClient;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = AppProperties.getWifiIp(wifiInter);
    private Thread serverThread;
    private Thread clientThread;
    private boolean isConnected = false;
    private boolean closeConn = false;

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        userDAO = new UserDAO();
        userModel = setNameAndFlag();
        menuView = new MenuView(userModel);
        lanView = new LanView(userModel);
        serverControler = new ServerControler();

        // Listeners 
        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);
        this.menuView.addSettingsButtonListener(this);
        this.menuView.addMakeReportListener(this);
        this.lanView.addReturnButtonListener(this);
        this.lanView.addJoinMatchButtonListener(this);
        this.lanView.addMakeMatchButtonListener(this);      

        add(menuView);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();
    }

    private User setNameAndFlag() {
        User user = null;

        if (!userDAO.isEmptyTable()) {
            user = userDAO.getUser();
        } else {
            user = new User("User");
            user.setFlag("./media/images/defaultflag.png");
        }

        return user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Salir")) {
            System.out.println("Boton de salir [menu]");
            dispose();

        } else if (e.getSource() == menuView.getPveButton()) {
            System.out.println("BOT button");
            initMatch();
            changePanel(matchView);

        } else if (e.getSource() == menuView.getPvpButton()) {
            System.out.println("lan button");
            changePanel(lanView);
        
        } else if (e.getActionCommand().equals("Salir de la partida")) {
            System.out.println("Boton de salir [match]");
            isRunningServer();
            changePanel(menuView);

        } else if (e.getSource() == menuView.getSettingsButton()) {
            System.out.println("Boton de settings");
            saveNewUser();

        } else if (e.getActionCommand().equals("Regresar")) {
            System.out.println("Regresar al menu [desde lan]");
            isRunningServer();
            changePanel(menuView);
        
        } else if (e.getActionCommand().equals("UNIRSE A PARTIDA")) {
            System.out.println("unirse a partida");
            joinMatchView = new JoinMatchView();
            this.joinMatchView.addJoinButtonListener(this);

        } else if (e.getActionCommand().equals("CREAR PARTIDA")) {
            System.out.println("crear partida");
            LanServer();

        } else if (e.getActionCommand().equals("Crear reporte")) {
            System.out.println("boton de pdf");

        } else if (e.getActionCommand().equals("CANCELAR")) {
            System.out.println("boton de cancelar [CREAR PARTIDA]");
            createMatchView.dispose();
            isRunningClient();
            isRunningServer();
            serverThread.interrupt();

        } else if (e.getActionCommand().equals("UNIRSE")) {
            System.out.println("boton de unirse a partida");
            LanClient(); 
        }
    }

    private void changePanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private void saveNewUser() {
        SettingsView settingsView = new SettingsView();
        SettingsController settingsController = new SettingsController(settingsView);
        settingsView.setModal(true);
        settingsView.setVisible(true);

        // Establecer usuario consultado
        User userUpdated = settingsController.getUser();
        if (userUpdated != null) {
            userDAO.deleteUser();
            userDAO.insertUser(userUpdated);
            this.userModel = userUpdated; 

            this.menuView.setUserModel(userModel);
            this.menuView.refreshHeader();
        }
    }

    private void initMatch() { 
        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        matchView = new MatchView(userModel, cellsRigth, cellsLeft);   
        matchController = new MatchController(matchView, cellsRigth, cellsLeft);
        this.matchView.addExitButtonListener(this); 
    }

    private void initLanMatch() { 
        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        lanMatchView = new LanMatchView(userModel,cellsRigth, cellsLeft);   
        lanMatchController = new LanMatchController(lanMatchView, cellsRigth, cellsLeft);
        this.lanMatchView.addExitButtonListener(this); 
    }

    private Cell[][] initCells(Color color) {
        Cell[][] cells = new Cell[11][11];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(color);
            }
        }

        return cells;
    }

    /* Parte de servidor */
    public void LanServer() {
        runningServer = true;
        
        // Ejecutar servidor y vistas de manera paralela
        serverThread = new Thread(() -> {
            runServer();
        });
        serverThread.start();
        javax.swing.SwingUtilities.invokeLater(() -> {
            createMatchView = new CreateMatchView(host);
            createMatchView.addCancelButtonListener(this);
            createMatchView.addWindowCloseListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    stopServer();
                }
            });
        });
    }

    private void runServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor escuchando en el puerto " + port);
            while (runningServer) {
                try (Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        isConnected = true;
                        System.out.println("Usuario conectado: " + clientSocket.getRemoteSocketAddress());
                        JOptionPane.showMessageDialog(createMatchView, "Conexion establecida", "Status", JOptionPane.INFORMATION_MESSAGE);
                        runServerLanMatch();

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("Recibido del cliente: " + inputLine);
                            //out.println("Servidor: " + inputLine); // Enviar respuesta al cliente
        
                            // Verificar si se debe cerrar la conexión
                            if (closeConn) {
                                sendResponse(out, "close");
                                isConnected = false;
                                break;
                            }
                            
                        }         
                } catch (IOException e) {
                    isConnected = false;
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(createMatchView, "Error al iniciar el servidor", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runServerLanMatch() {
        createMatchView.dispose();
        serverThread.interrupt();

        // Desplegar juego
        SwingUtilities.invokeLater(() -> {
            initLanMatch(); 
            changePanel(lanMatchView);                   
        });
    }

    private void sendResponse(PrintWriter out, String message) {
        out.println(message);
    }

    private void isRunningServer() {
        if (runningServer == true) {
            stopServer();
        } else if (isConnected == true) {
            closeConn = true;
        }
    }

    private void stopServer() {
        runningServer = false;

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Servidor detenido");
                JOptionPane.showMessageDialog(createMatchView, "Servidor detenido", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    public void stopClient() {
        runningClient = false;
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Servidor detenido");
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    /* Parte de cliente */
    public void LanClient() {
        if (checkFields()) {
            System.out.println("pasamos al cliente");
            joinMatchView.dispose();

            // Ejecutar cliente y vistas de manera paralela
            clientThread = new Thread(() -> {
                runClient();
            });
            clientThread.start();
            javax.swing.SwingUtilities.invokeLater(() -> {
                runLanMatchClient();
                /*joinMatchView.addWindowCloseListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        stopServer();
                    }
                });*/
            }); 
        } else {
            JOptionPane.showMessageDialog(joinMatchView, "INGRESE UN NUMERO", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // CAMBIAR ESTO A IP, EL TRUE ES SOLO PA PRUEBAS
    private boolean checkFields() {
        String ip = joinMatchView.getKeyField().getText();
        return ip != null && isIp(ip); 
    }

    private boolean isIp(String ip) {
        // Expresión regular para validar direcciones IPv4
        String ipPattern = 
            "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
    
        return ip.matches(ipPattern);
    }
    
    private void runClient() {
        runningClient = true;
        String host = joinMatchView.getKeyField().getText();
        try {clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            System.out.println("Introduce un texto (\"exit\" para salir): ");
            while ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
                out.println(userInput);
                String response = in.readLine();
                System.out.println("Respuesta del servidor: " + response);
                
                if (response == "close") {
                    JOptionPane.showMessageDialog(createMatchView, "El host abandono la partida", "Status", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("CONEXION CON HOST CERRADA");
                }
            
            }
        } catch (UnknownHostException e) {
            System.err.println("No se puede encontrar el host: " + host);
            JOptionPane.showMessageDialog(createMatchView, "No se puede encontrar el host", "ERROR", JOptionPane.ERROR_MESSAGE);

        } catch (IOException e) {
            System.err.println("Error al comunicar con el host: " + host);
            JOptionPane.showMessageDialog(createMatchView, "Error al comunicar con el host", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runLanMatchClient() {
        JOptionPane.showMessageDialog(createMatchView, "Conexion establecida con el host", "Estado", JOptionPane.INFORMATION_MESSAGE);
        initLanMatch(); 
        changePanel(lanMatchView);                   
    }

    public void isRunningClient() {
        if (runningClient == true) {
            stopServer();
        }
    }  

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
