package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;
import models.User;

public class MenuView extends JPanel {
    private AppProperties properties = new AppProperties();
    private User userModel;
    private JButton pveButton;
    private JButton pvpButton;
    private JTextField title;
    private Image imagenFondo;
    private Font customFontBold;
    private JButton settingsButton;
    private JButton exitButton;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel footerPanel;
    private JButton makeReportButton;

    public MenuView(User userModel) {
        this.userModel = userModel;
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());
        
        headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        ImageIcon imageIcon = new ImageIcon("media/images/background.jpg");
        imagenFondo = imageIcon.getImage();

        validate();
        repaint();
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(new EmptyBorder(10, 15, 0, 0));
        settingsPanel.setBackground(properties.getHeaderColor());
        settingsPanel.setLayout(new FlowLayout());

        UIManager.put("Menu.background", Color.BLUE);
        UIManager.put("Menu.foreground", properties.getHeaderColor());
        UIManager.put("Menu.opaque", true);
        UIManager.put("MenuItem.background", properties.getButtonColor()); 
        UIManager.put("MenuItem.foreground", Color.WHITE);

        settingsButton = createButton("Configuracion", new Font("Arial", Font.PLAIN, 20));
        settingsPanel.add(settingsButton);
        makeReportButton = createButton("Crear reporte", new Font("Arial", Font.PLAIN, 20));
        settingsPanel.add(makeReportButton);

        return settingsPanel;
    }

    private void makeLabel(JPanel fatherPanel, String text, Font font, int[] border) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(border[0], border[1], border[2], border[3])); 
        
        fatherPanel.add(label);
    }

    private JPanel createInfoPanel(String name) {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(properties.getHeaderColor());
    
        // Panel para el icono y el nombre del usuario
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setBackground(properties.getHeaderColor());
    
        // Agregar el label del nombre de usuario
        makeLabel(userPanel, name, new Font("Arial", Font.PLAIN, 30), new int[]{0, 10, 0, 10});
    
        // Cargar la imagen de la bandera del jugador
        ImageIcon flagIcon = null;
        try {
            flagIcon = new ImageIcon(userModel.getFlag());
        } catch (Exception e) {
            flagIcon = new ImageIcon("./media/images/defaultflag.png");
        } 
        Image flagImage = flagIcon.getImage();
        Image scaledFlagImage = flagImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledFlagImage);
        JLabel flagLabel = new JLabel(scaledIcon);
        
        userPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        userPanel.add(flagLabel);
    
        infoPanel.add(userPanel);
    
        return infoPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());
        headerPanel.add(createInfoPanel(userModel.getName()), BorderLayout.EAST);

        return headerPanel;
    }
    
    public JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        Dimension buttonSize = new Dimension(200, 100); 

        // Cargar y escalar imágenes de los botones
        ImageIcon pveImage = new ImageIcon("media/images/PVE.png");
        ImageIcon pvpImage = new ImageIcon("media/images/PVP.png");

        Image pveImageScaled = pveImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        ImageIcon pveIcon = new ImageIcon(pveImageScaled);

        Image pvpImageScaled = pvpImage.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        ImageIcon pvpIcon = new ImageIcon(pvpImageScaled);

        // Botones de partida
        pveButton = new JButton(pveIcon);
        pvpButton = new JButton(pvpIcon);
        pveButton.setPreferredSize(buttonSize);
        pvpButton.setPreferredSize(buttonSize);

        pveButton.setBorderPainted(false);
        pveButton.setContentAreaFilled(false);
        pveButton.setFocusPainted(false);

        pvpButton.setBorderPainted(false);
        pvpButton.setContentAreaFilled(false);
        pvpButton.setFocusPainted(false);

        // Cargar fuente personalizada
        try {
            File fontFile = new File("media/fonts/quickynick.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFontBold = customFont.deriveFont(Font.BOLD, 100); 
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // Campo de texto para el título
        title = new JTextField("BATTLESHIP");
        title.setFont(customFontBold);
        title.setPreferredSize(new Dimension(600, 100)); 
        title.setEditable(false);
        title.setHorizontalAlignment(JTextField.CENTER);
        title.setOpaque(false);
        title.setBorder(null);
        title.setForeground(new Color(255, 182, 55));
        title.setColumns(10);

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new java.awt.Insets(20, 20, 20, 20); 
        panel.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new java.awt.Insets(10, 20, 10, 10); 
        panel.add(pveButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new java.awt.Insets(10, 10, 10, 20); 
        panel.add(pvpButton, gbc);

        return panel;
    }
    
    public JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setBackground(properties.getButtonColor());
        button.setFont(font);
        button.setForeground(Color.WHITE);

        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibuja la imagen de fondo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JPanel createFooterPanel() {
        exitButton = createButton("Salir", new Font("ARIAL", Font.PLAIN, 25));

        JPanel exitPanel = new JPanel(new BorderLayout());
        exitPanel.add(createSettingsPanel(), BorderLayout.WEST);
        exitPanel.setBackground(properties.getHeaderColor());
        exitPanel.setBorder(new EmptyBorder(10, 0, 10, 30));
        exitPanel.add(exitButton, BorderLayout.EAST);

        return exitPanel;
    }

    /* Metodos para refrescar paneles */
    public void refreshHeader() {
        remove(headerPanel); 

        headerPanel = createHeaderPanel(); 
        add(headerPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    /* ActionListeners */
    public void addPveButtonListener(ActionListener listener) {
		pveButton.addActionListener(listener);
	}

    public void addPvpButtonListener(ActionListener listener) {
		pvpButton.addActionListener(listener);
	}

    public void addExitButtonListener(ActionListener listener) {
		this.exitButton.addActionListener(listener);
	}

    public void addSettingsButtonListener(ActionListener listener) {
		settingsButton.addActionListener(listener);
	}

    public void addMakeReportListener(ActionListener listener) {
        makeReportButton.addActionListener(listener);
    }

    /* Getters y setters */
    public JButton getPveButton() {
        return this.pveButton;
    }
    public void setPveButton(JButton pveButton) {
        this.pveButton = pveButton;
    }

    public JButton getPvpButton() {
        return this.pvpButton;
    }
    public void setPvpButton(JButton pvpButton) {
        this.pvpButton = pvpButton;
    }

    public JButton getExitButton() {
        return this.exitButton;
    }
    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public JButton getSettingsButton() {
        return this.settingsButton;
    }
    public void setSettingsButton(JButton settingsButton) {
        this.settingsButton = settingsButton;
    }

    public void setUserModel(User userModel) {
        this.userModel = userModel;
    }
    public User getUserModel() {
        return this.userModel;
    }
}