package views;

import java.awt.Color;
import javax.swing.JPanel;

/* Clases propias */
import models.AppProperties;

public class ViewMenu extends JPanel{

    AppProperties properties = new AppProperties();
    
    public ViewMenu() {
        setBackground(properties.getBackgroundColor());
    }

}
