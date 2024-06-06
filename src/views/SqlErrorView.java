package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;

public class SqlErrorView extends JFrame {
    AppProperties properties = new AppProperties();
    private JButton cancelButton;

    public SqlErrorView() {
        setTitle("ERROR");
        setBackground(properties.getBackgroundColor());
        setSize(600, 130);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(properties.getBackgroundColor());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createKeyPanel(), BorderLayout.CENTER);
        mainPanel.add(createCancelButton(), BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());
        int padding = 5;
        headerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));


        return headerPanel;
    }

    private JPanel createKeyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(properties.getBackgroundColor());
        JLabel keyLabel = new JLabel("NO ESTA LEVANTADO EL SERVICIO SQL");
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        keyLabel.setForeground(Color.WHITE);
        panel.add(keyLabel);

        int padding = 10;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return panel;
    }

    private JPanel createCancelButton() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getButtonColor());
        panel.setForeground(Color.WHITE);

        return panel;
    }
}
