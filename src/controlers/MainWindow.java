package controlers;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Cursor;

/* Clases propias */
import models.AppProperties;
import models.User;
import views.MenuView;
import views.MatchView;

public class MainWindow extends JFrame{

    AppProperties properties = new AppProperties();

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        // Modelos
        User userModel = new User(1, "Uriel");

        // Vistas
        MenuView menuView = new MenuView(userModel);
        MatchView matchView = new MatchView();
        add(menuView);
        MenuControler menuControler = new MenuControler(userModel, menuView);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }   
}
