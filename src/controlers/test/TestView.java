package controlers.test;

import java.awt.Color;

import javax.swing.JFrame;

import controlers.Lan.LanMatchController;
/* Clases propias */
import views.SettingsView;
import views.Lan.CreateMatchView;
import views.Lan.JoinMatchView;
import views.Lan.LanView;
import models.UserDAO;
import views.Lan.LanMatchView;
import views.MenuView;
import models.AppProperties;
import models.Cell;
import models.User;

public class TestView extends JFrame{
    AppProperties properties = new AppProperties();

    public static void main(String[] args) {
        TestView t = new TestView();
    }
    
    public TestView() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        Cell[][] cellsRigth = initCells(Color.decode("#A6A6A6"));
        Cell[][] cellsLeft = initCells(Color.decode("#033A84"));

        User user = new User("Uriel");
        LanMatchView v = new LanMatchView(user,cellsRigth, cellsLeft);   
        LanMatchController c = new LanMatchController(v, cellsRigth, cellsLeft);

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
