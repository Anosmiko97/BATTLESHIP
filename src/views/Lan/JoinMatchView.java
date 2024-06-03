package views.Lan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;
import javax.management.loading.PrivateClassLoader;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/* Clases propias */
import models.AppProperties;

public class JoinMatchView extends JDialog {
    AppProperties properties = new AppProperties();
    private JTextField keyField;
    private JButton joinButton;

    public JoinMatchView() {
        setTitle("Unirse a partida");
        setBackground(properties.getBackgroundColor());
        setSize(400, 180);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(properties.getBackgroundColor());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createKeyPanel(), BorderLayout.CENTER);
        joinButton = createJoinButton();
        mainPanel.add(joinButton, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());

        int padding = 5;
        JLabel titleLabel = new JLabel("INGRESE LA CLAVE", JLabel.CENTER);
        titleLabel.setFont(new Font("ARIAL", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
         
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getBackgroundColor());
        panel.setLayout(new BorderLayout());
        keyField = new JTextField();
        panel.add(keyField, BorderLayout.CENTER);

        int padding = 20;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return panel;
    }

    private JButton createJoinButton() {
        JButton button = new JButton("UNIRSE");
        button.setBackground(properties.getButtonColor());
        button.setFont(new Font("ARIAL", Font.PLAIN, 20));
        button.setForeground(Color.WHITE);
        
        return button;
    }

    /* Getters y setters */
    public JTextField getKeyField() {
        return this.keyField;
    }
    public void setNameField(JTextField keyField) {
        this.keyField = keyField;
    }

    /* ActionListeners */
    public void addJoinButtonListener(ActionListener listener) {
        joinButton.addActionListener(listener);
    }
}