package views.Lan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;

public class FinishPartyView extends JDialog {
    AppProperties properties = new AppProperties();
    private JButton returnMenuButton;
    private boolean win;

    public FinishPartyView(boolean win) {
        this.win = win;
        setTitle("Crear una partida");
        setBackground(properties.getBackgroundColor());
        setSize(600, 300);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(properties.getBackgroundColor());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createKeyPanel(), BorderLayout.CENTER);
        returnMenuButton = createReturnButton();
        mainPanel.add(returnMenuButton, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());
        int padding = 20;
        headerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return headerPanel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(properties.getBackgroundColor());

        if (win) {
            String imagePath = "./media/images/win.png"; 
            ImageIcon imageIcon = new ImageIcon(imagePath);
            JLabel imageLabel = new JLabel(imageIcon);
            panel.add(imageLabel);
        } else {
            JLabel keyLabel = new JLabel("PERDISTE!!!");
            keyLabel.setFont(new Font("Arial", Font.BOLD, 90));
            keyLabel.setForeground(Color.WHITE);
            panel.add(keyLabel);
        }

        int padding = 10;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return panel;
    }

    private JButton createReturnButton() {
        JButton button = new JButton();
        button.setBackground(properties.getButtonColor());
        button.setFont(new Font("ARIAL", Font.PLAIN, 20));
        button.setForeground(Color.WHITE);
        button.setEnabled(false);

        return button;
    }

    public boolean isViewVisible() {
        return isVisible();
    }

    /* Getters y setter */
    public void setWin(boolean win) {
        this.win = win;
    }
    public boolean getWin() {
        return this.win;
    }
}
