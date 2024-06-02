package controlers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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
import java.util.Random;

/* Clases propias */
import models.AppProperties;
import models.Cell;
import models.User;
import models.UserDAO;
import views.MenuView;
import views.SettingsView;
import views.MatchView;
import views.LanView;
import views.CreateMatchView;
import views.JoinMatchView;

public class MainWindow extends JFrame implements ActionListener {
    private AppProperties properties = new AppProperties();

    // Modelos
    private User userModel;
    private UserDAO userDAO;

    // Controladores
    private SettingsController settingsController;
    private MatchController matchController;
    private ServerControler serverControler;

    // Vistas
    private MenuView menuView;
    private MatchView matchView;
    private LanView lanView;
    private SettingsView settingsView;
    private CreateMatchView createMatchView;

    // Atributos
    private String keyMatch;
    private boolean showlanView;
    private Thread serverThread;

    // Atributos para servidor
    private boolean running;
    private ServerSocket serverSocket;
    private int port = properties.getPort();
    private String wifiInter = properties.getWlan();
    private String host = "localhost"; //properties.getWifiIp(wifiInter);

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
            //initMatch();
            changePanel(lanView);
        
        } else if (e.getActionCommand().equals("Salir de la partida")) {
            System.out.println("Boton de salir [match]");
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
            JoinMatchView joinMatchView = new JoinMatchView();
            JoinMatchController joinMatch = new JoinMatchController(joinMatchView);

        } else if (e.getActionCommand().equals("CREAR PARTIDA")) {
            System.out.println("crear partida");
            LanServer();

        } else if (e.getActionCommand().equals("Crear reporte")) {
            System.out.println("boton de pdf");

        } else if (e.getActionCommand().equals("CANCELAR")) {
            System.out.println("boton de cancelar [CREAR PARTIDA]");
            createMatchView.dispose();
            stopServer();
            serverThread.interrupt();
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

        matchView = new MatchView(cellsRigth, cellsLeft);   
        matchController = new MatchController(matchView, cellsRigth, cellsLeft);
        this.matchView.addExitButtonListener(this); 
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
        running = true;
        
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
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        System.out.println("Usuario conectado: " + clientSocket.getRemoteSocketAddress());
                        JOptionPane.showMessageDialog(createMatchView, "CONECCION ESTABLECIDA", "Status", JOptionPane.INFORMATION_MESSAGE);
                        runLanMatch();

            
                } catch (IOException e) {
                    System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private void runLanMatch() {
        createMatchView.dispose();
        serverThread.interrupt();

        // Desplegar juego
        SwingUtilities.invokeLater(() -> {
            initMatch(); 
            changePanel(matchView);                   
        });
    }

    public void isRunningServer() {
        if (running == true) {
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Servidor detenido");
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
