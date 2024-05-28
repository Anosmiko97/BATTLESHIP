package views;

import java.awt.Color;
import javax.swing.JPanel;

/* Clases propias */
import models.AppProperties;

public class MenuView extends JPanel{

    AppProperties properties = new AppProperties();
    
    public MenuView() {
        setBackground(properties.getBackgroundColor());
    }

}
