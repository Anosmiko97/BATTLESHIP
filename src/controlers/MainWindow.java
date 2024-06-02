package controlers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import controlers.socket.ServerControler;
import controlers.socket.ClientControler;

public class MainWindow extends JFrame implements ActionListener {
    AppProperties properties = new AppProperties();

    // Modelos
    User userModel;
    UserDAO userDAO;

    // Controladores
    SettingsController settingsController;
    MatchController matchController;
    ServerControler serverControler;

    // Vistas
    MenuView menuView;
    MatchView matchView;
    LanView lanView;
    SettingsView settingsView;
    CreateMatchView createMatchView;

    // Atributos
    private String keyMatch;
    private boolean showlanView;
    private Thread serverThread;

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
            changePanel(menuView);
        
        } else if (e.getActionCommand().equals("UNIRSE A PARTIDA")) {
            System.out.println("unirse a partida");
            JoinMatchView joinMatchView = new JoinMatchView();
            JoinMatchController joinMatch = new JoinMatchController(joinMatchView);

        } else if (e.getActionCommand().equals("CREAR PARTIDA")) {
            System.out.println("crear partida");
            initLanMatch(e);

        } else if (e.getActionCommand().equals("Crear reporte")) {
            System.out.println("boton de pdf");

        } else if (e.getActionCommand().equals("CANCELAR")) {
            System.out.println("boton de cancelar [CREAR PARTIDA]");
            createMatchView.dispose();
            serverControler.stopServer();
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

    private String generateKey() {
        Random random = new Random();
        int num = 1000 + random.nextInt(9000);

        return String.valueOf(num);
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

    private void initLanMatch(ActionEvent e) {
        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        createMatch();
        if (showlanView) {
            matchView = new MatchView(cellsRigth, cellsLeft);
            matchController = new MatchController(matchView, cellsRigth, cellsLeft);  
            this.matchView.addExitButtonListener(this);
            changePanel(matchView);
        }
    }

    private void createMatch() {
        keyMatch = generateKey();  

        // Ejecutar de manera paralela la vista y el servidor
        serverControler = new ServerControler();
        serverThread = new Thread(() -> {
            boolean result = serverControler.compareKey(keyMatch);
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (result) {
                    JOptionPane.showMessageDialog(null, "La clave coincide", "Resultado", JOptionPane.INFORMATION_MESSAGE);
                    createMatchView.dispose();
                    serverControler.stopServer();
                    serverThread.interrupt();
                    showlanView = true;
                } 
            });
        });
        serverThread.start();
        javax.swing.SwingUtilities.invokeLater(() -> {
            createMatchView = new CreateMatchView(keyMatch);
            createMatchView.addCancelButtonListener(this);
        });
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
