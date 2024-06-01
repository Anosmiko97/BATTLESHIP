package controlers;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/* Clases propias */
import models.AppProperties;
import models.User;
import models.UserDAO;
import views.MenuView;
import views.SettingsView;
import views.MatchView;
import views.LanView;
import views.CreateMatchView;
import views.JoinMatchView;

public class MainWindow extends JFrame implements ActionListener {
    AppProperties properties = new AppProperties();

    // Modelos
    User userModel;
    UserDAO userDAO;

    // Controladores
    SettingsController settingsController;

    // Vistas
    MenuView menuView;
    MatchView matchView;
    LanView lanView;
    SettingsView settingsView;

    // Atributos
    private String keyMatch;

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        userDAO = new UserDAO();
        userModel = setNameAndFlag();
        menuView = new MenuView(userModel);
        matchView = new MatchView();
        lanView = new LanView(userModel);
        //menuControler = new MenuControler(userModel, menuView);

        // Listeners 
        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);
        this.menuView.addSettingsButtonListener(this);
        this.menuView.addMakeReportListener(this);
        this.lanView.addReturnButtonListener(this);
        this.lanView.addJoinMatchButtonListener(this);
        this.lanView.addMakeMatchButtonListener(this);

        // Listeners de matchView
        this.matchView.addExitButtonListener(this);        

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
            changePanel(matchView);

        } else if (e.getSource() == menuView.getPvpButton()) {
            System.out.println("lan button");
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

        } else if (e.getActionCommand().equals("CREAR PARTIDA")) {
            System.out.println("crear partida");
            keyMatch = generateKey();
            CreateMatchView createMatchView = new CreateMatchView(keyMatch);
        
        } else if (e.getActionCommand().equals("Crear reporte")) {
            System.out.println("boton de pdf");
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

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
