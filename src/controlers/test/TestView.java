package controlers.test;

import java.awt.Color;

import javax.swing.JFrame;

/* Clases propias */
import views.SettingsView;
import views.JoinMatchView;
import views.LanView;
import models.UserDAO;
import views.CreateMatchView;
import views.MatchView;
import views.MenuView;
import models.AppProperties;
import models.Cell;
import controlers.MatchController;

public class TestView extends JFrame{
    AppProperties properties = new AppProperties();
    /* 
    public TestView() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(properties.getBackgroundColor());
        setTitle("battleship");

        //Cell[][] cellsRigth = initCells();
        //Cell[][] cellsLeft = initCells();

       // MatchView v = new MatchView(cellsRigth, cellsLeft);   
        //MatchController c = new MatchController(v, cellsRigth, cellsLeft);

        add(v);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();
    } */
    public static void main(String[] args) {
        CreateMatchView c = new CreateMatchView("1232");
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
