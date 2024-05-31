package views;

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

public class SettingsView extends JDialog {
    AppProperties properties = new AppProperties();
    private JTextField nameField;
    private String flagFile;
    private JButton applyChangesButton;

    public SettingsView() {
        setTitle("Settings");
        setBackground(properties.getBackgroundColor());
        setSize(400, 250);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(properties.getBackgroundColor());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createSettingsPanel(), BorderLayout.CENTER);
        mainPanel.add(createApplyChangesButton(), BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());

        int padding = 5;
        JLabel titleLabel = new JLabel("Settings", JLabel.CENTER);
        titleLabel.setFont(new Font("ARIAL", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
         
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(properties.getBackgroundColor());
        settingsPanel.setLayout(new GridLayout(2, 1, 0, 0));

        settingsPanel.add(createLabelNameUser());
        settingsPanel.add(createUploadFlagPanel());;

        int padding = 10;
        settingsPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        return settingsPanel;
    }

    private JPanel createLabelNameUser() {
        JPanel nameUserPanel = new JPanel();
        nameUserPanel.setBackground(properties.getBackgroundColor());
        nameUserPanel.setLayout(new FlowLayout());
    
        JLabel nameLabel = new JLabel("Nombre");
        nameField = new JTextField(20);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("ARIAL", Font.PLAIN, 20));
        nameUserPanel.add(nameLabel);
        nameUserPanel.add(nameField);

        return nameUserPanel;
    }

    private JPanel createUploadFlagPanel() {
        JPanel uploadFlagPanel = new JPanel();
        uploadFlagPanel.setBackground(properties.getBackgroundColor());
        uploadFlagPanel.setLayout(new FlowLayout());

        // Label para el subir archivo        
        JLabel nameLabel = new JLabel("Bandera");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("ARIAL", Font.PLAIN, 20));

        // Configuración del FileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        JButton btnUpload = new JButton("Subir");
        btnUpload.setBackground(properties.getButtonColor());
        btnUpload.setFont(new Font("ARIAL", Font.PLAIN, 20));
        btnUpload.setForeground(Color.WHITE);
        btnUpload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String selectedPath = fileChooser.getSelectedFile().getPath();
                    flagFile = selectedPath;
                }
            }
        });

        uploadFlagPanel.add(nameLabel);
        uploadFlagPanel.add(btnUpload);

        return uploadFlagPanel;
    }

    private JButton createApplyChangesButton() {
        applyChangesButton = new JButton("APLICAR");
        applyChangesButton.setBackground(properties.getButtonColor());
        applyChangesButton.setFont(new Font("ARIAL", Font.PLAIN, 20));
        applyChangesButton.setForeground(Color.WHITE);
        
        return applyChangesButton;
    }

    /* Getters y setters */
    public JTextField getNameField() {
        return this.nameField;
    }
    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public String getFlagFile() {
        return this.flagFile;
    }
    public void setFlagFile(String file) {
        this.flagFile = file;
    }

    /* ActionListeners */
    public void addApplyChangesListener(ActionListener listener) {
        applyChangesButton.addActionListener(listener);
    }
}