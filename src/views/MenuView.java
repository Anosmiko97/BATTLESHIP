package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.border.Border;

/* Clases propias */
import models.AppProperties;

public class MenuView extends JPanel{

    AppProperties properties = new AppProperties();
    
    public MenuView() {
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());

        add(header(), BorderLayout.NORTH);
        add(mainWindow(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
    }

    public JPanel header() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getHeaderColor());
        panel.setLayout(new FlowLayout());

        return panel;
    }

    public JPanel mainWindow() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getBackgroundColor());
        return panel;
    }

    public JPanel footer() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getHeaderColor());
        panel.setLayout(new FlowLayout());

        return panel;
    }

}
