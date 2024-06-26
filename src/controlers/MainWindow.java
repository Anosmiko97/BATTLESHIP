package controlers;

import controlers.Lan.LanMatchController;
import controlers.Local.MatchController;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.AppProperties;
import models.Cell;
import models.User;
import models.UserDAO;
import views.Lan.CreateMatchView;
import views.Lan.FinishPartyView;
import views.Lan.JoinMatchView;
import views.Lan.LanView;
import views.MatchView;
import views.MenuView;
import views.SettingsView;
import views.SqlErrorView;

public class MainWindow extends JFrame implements ActionListener {
    private AppProperties properties = new AppProperties();

    // Modelos
    private User userModel;
    private UserDAO userDAO;

    // Controladores
    private SettingsController settingsController;
    private LanMatchController lanMatchController;
    private MatchController matchController;

    // Vistas
    private MenuView menuView;
    private MatchView lanMatchView;
    private MatchView matchView;
    private LanView lanView;
    private SettingsView settingsView;
    private CreateMatchView createMatchView;
    private JoinMatchView joinMatchView;
    private SqlErrorView errorSql;
    private FinishPartyView finishPartyView;

    // Atributos para servidor y cliente
    private String ipHost;
    private boolean runningServer;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = AppProperties.getWifiIp(wifiInter);
    private Thread serverThread;
    private Thread clientThread;

    // Atributos
    private String opponentName;

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        initViewsAndModels();
        initListeners();      

        add(menuView);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();

    }

    private void initViewsAndModels() {
        try {
            userDAO = new UserDAO(); 
            userModel = setNameAndFlag();
            menuView = new MenuView(userModel);
            lanView = new LanView(userModel);  
        } catch (Exception e) {
            errorSql = new SqlErrorView();
        }
    }

    private void initListeners() {
        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);
        this.menuView.addSettingsButtonListener(this);
        this.menuView.addMakeReportListener(this);
        this.lanView.addReturnButtonListener(this);
        this.lanView.addJoinMatchButtonListener(this);
        this.lanView.addMakeMatchButtonListener(this); 
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
            lanMatchController.setMenuView(menuView);
            lanMatchController.stopServer(false);
            lanMatchController.stopClient();
            changePanel(menuView);

        }else if (e.getActionCommand().equals("Salir de la partida local")) {
            changePanel(menuView);

        }else if (e.getSource() == menuView.getSettingsButton()) {
            System.out.println("Boton de settings");
            saveNewUser();

        } else if (e.getActionCommand().equals("Regresar")) {
            System.out.println("Regresar al menu [desde lan]");
            stopServer(false);
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
            stopServer(false);
            serverThread.interrupt();

        } else if (e.getActionCommand().equals("UNIRSE")) {
            System.out.println("boton de unirse a partida");
            try {
                LanClient();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public void changePanel(JPanel panel) {
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
        Cell cell = null;
        matchView = new MatchView(userModel, "Ale", cellsRigth, cellsLeft);   
        matchController = new MatchController(matchView, cellsRigth, cellsLeft, cell, this, menuView);
        this.matchView.addExitButtonListener(this); 

        // key
        SwingUtilities.invokeLater(() -> {
            matchView.requestFocusInWindow();
        });
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
                    stopServer(false);
                }
            });
        });
    }

    private void runServer() {
        runningServer = true;
        try {
            serverSocket = new ServerSocket(port);
            while (runningServer) {
                Socket clientConn = serverSocket.accept();
                try (PrintWriter out = new PrintWriter(clientConn.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
                ) {
                    // Enviar ip y nombre
                    String serverIP = clientConn.getLocalAddress().getHostAddress();
                    out.println(serverIP + "," + userModel.getName());
                    
                    // Leer la respuesta del cliente
                    String userResponse = in.readLine();

                    // Esperar a recibir nombre
                    if (!isIp(userResponse)) {
                        opponentName = userResponse;
                        runServerLanMatch();
                        stopServer(false);
                        break;
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(createMatchView, "Error de conexion", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void runServerLanMatch() {
        createMatchView.dispose();
        serverThread.interrupt();
    
        // Desplegar juego
        SwingUtilities.invokeLater(() -> {
            initLanMatch("server"); 
            changePanel(lanMatchView);                  
        });  
    }

    private void initLanMatch(String mode) { 
        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        lanMatchView = new MatchView(userModel, opponentName, cellsRigth, cellsLeft);   
        if (mode.equals("client")) {
            lanMatchController = new LanMatchController(this, ipHost, opponentName,lanMatchView, menuView, cellsRigth, cellsLeft, "client");
            
        } else if (mode.equals("server")) {
            lanMatchController = new LanMatchController(this, ipHost, opponentName, lanMatchView, menuView, cellsRigth, cellsLeft, "server");
            
        }
        this.lanMatchView.addExitButtonListener(this);
    }

    private void stopServer(Boolean message) {
        runningServer = false;

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();

                if (message) {
                    JOptionPane.showMessageDialog(createMatchView, "Servidor detenido", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    /* Parte de cliente */
    public void LanClient() throws IOException {
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
            }); 
        } else {
            JOptionPane.showMessageDialog(joinMatchView, "INGRESE UNA IP", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
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
        try {
            clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Enviar nombre al servidor
            out.println(userModel.getName());
            System.out.println("Nombre enviado: " + userModel.getName());

            // Recibir y almacenar la dirección IP del servidor
            String response = in.readLine();
            String[] splitResponse = response.split(",");
            ipHost = splitResponse[0];
            opponentName = splitResponse[1];
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(createMatchView, "No se puede encontrar el host", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(createMatchView, "Error al comunicar con el host", "ERROR", JOptionPane.ERROR_MESSAGE);
        } 
    }

    public void runLanMatchClient() {
        initLanMatch("client");
        changePanel(lanMatchView);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}