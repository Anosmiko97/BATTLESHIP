package controlers;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
import views.MenuView;
import views.MatchView;

public class MainWindow extends JFrame implements ActionListener {
    AppProperties properties = new AppProperties();

    // Vistas
    MenuView menuView;
    MatchView matchView;

    // Controladores
    MenuControler menuControler;

    // Modelos
    User userModel;

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        User userModel = new User(1, "Uriel");
        menuView = new MenuView(userModel);
        matchView = new MatchView();
        menuControler = new MenuControler(userModel, menuView);

        // Listeners de menuView
        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);

        // Listeners de matchView
        this.matchView.addExitButtonListener(this);

        add(menuView);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();
    }

    private void changePanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Salir")) {
            System.out.println("Boton de salir [menu]");

        } else if (e.getSource() == menuView.getPveButton()) {
            System.out.println("BOT button");
            changePanel(matchView);

        } else if (e.getSource() == menuView.getPvpButton()) {
            System.out.println("lan button");
            changePanel(matchView);
        
        } else if (e.getActionCommand().equals("Salir de la partida")) {
            System.out.println("Boton de salir [match]");
            changePanel(menuView);
        }
    }
    
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
