package controlers;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Cursor;

/* Clases propias */
import models.AppProperties;
import models.User;
import models.UserDAO;
import views.MenuView;
import views.SettingsView;
import views.MatchView;

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
    SettingsView settingsView;

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        userDAO = new UserDAO();
        userModel = setNameAndFlag();
        menuView = new MenuView(userModel);
        matchView = new MatchView();
        //menuControler = new MenuControler(userModel, menuView);

        // Listeners de menuView
        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);
        this.menuView.addSettingsButtonListener(this);
        //this.menuView.addReturnButtonListener(this);
        //this.menuView.addMakeMatchButtonListener(this);

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
            menuView.refreshToLanPanels();
        
        } else if (e.getActionCommand().equals("Salir de la partida")) {
            System.out.println("Boton de salir [match]");
            changePanel(menuView);

        } else if (e.getSource() == menuView.getSettingsButton()) {
            System.out.println("Boton de settings");
            saveNewUser();

        } else if (e.getSource().equals("Regresar")) {
            System.out.println("Regresar al menu [desde lan]");
        }
    }

    private void changePanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private void saveNewUser() {
        // Abrir ventana de settings
        SettingsView settingsView = new SettingsView();
        SettingsController settingsController = new SettingsController(settingsView);
        settingsView.setModal(true);
        settingsView.setVisible(true);

        // Establecer usuario consultado
        User userUpdated = settingsController.getUser();
        userDAO.deleteUser();
        userDAO.insertUser(userUpdated);
        this.userModel = userUpdated; 

        this.menuView.setUserModel(userModel);
        this.menuView.refreshHeader();
        System.out.println(this.userModel.toString());
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
