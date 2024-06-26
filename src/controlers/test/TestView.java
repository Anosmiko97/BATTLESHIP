package controlers.test;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import controlers.Lan.LanMatchController;
/* Clases propias */
import views.SettingsView;
import views.Lan.CreateMatchView;
import views.Lan.FinishPartyView;
import views.Lan.JoinMatchView;
import views.Lan.LanView;
import views.SqlErrorView;
import models.UserDAO;
import views.MatchView;
import views.MenuView;
import models.AppProperties;
import models.Cell;
import models.User;
import views.MatchView;
import controlers.Local.MatchController;

public class TestView extends JFrame{
    AppProperties properties = new AppProperties();

    public static void main(String[] args) throws IOException {
        FinishPartyView s = new FinishPartyView(false);
    }
    
    public TestView() throws IOException {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        Socket client = new Socket();
        ServerSocket server = new ServerSocket();
        User user = new User("Uriel");
        MatchView v = new MatchView(user, "Ale", cellsRigth, cellsLeft);   
        MatchController c = new MatchController(v, cellsRigth, cellsLeft);

        add(v);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();
    } 

    public Cell[][] initCells(Color color) {
        Cell[][] cells = new Cell[11][11];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(color);
            }
        }

        return cells;
    }

   
}
