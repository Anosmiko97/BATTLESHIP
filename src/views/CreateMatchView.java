package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
/* Clases propias */
import models.AppProperties;

public class CreateMatchView extends JDialog {
    AppProperties properties = new AppProperties();
    private String key;
    private JButton cancelButton;

    public CreateMatchView(String key) {
        this.key = key;
        setTitle("Crear una partida");
        setBackground(properties.getBackgroundColor());
        setSize(400, 180);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(properties.getBackgroundColor());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createKeyPanel(), BorderLayout.CENTER);
        cancelButton = createCancelButton();
        mainPanel.add(cancelButton, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());

        int padding = 5;
        JLabel titleLabel = new JLabel("COMPARTA LA CLAVE", JLabel.CENTER);
        titleLabel.setFont(new Font("ARIAL", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
         
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(properties.getBackgroundColor());
        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        keyLabel.setForeground(Color.WHITE);
        panel.add(keyLabel);

        int padding = 10;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return panel;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("CANCELAR");
        button.setBackground(properties.getButtonColor());
        button.setFont(new Font("ARIAL", Font.PLAIN, 20));
        button.setForeground(Color.WHITE);
        
        return button;
    }

    /* ActionListeners */
    public void addCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
}