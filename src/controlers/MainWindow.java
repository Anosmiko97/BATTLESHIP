package controlers;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Cursor;

public class MainWindow extends JFrame{

    public MainWindow() {
        setBounds(500, 100, 900, 675);
        setResizable(true);
        getContentPane().setBackground(Color.BLACK);
        setTitle("battleship");

    


    

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }   
}
