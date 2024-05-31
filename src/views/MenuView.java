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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
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

    public MenuView(User userModel) {
        this.userModel = userModel;
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());
        
        headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        add(mainWindow(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
        
        ImageIcon imageIcon = new ImageIcon("media/images/background.jpg");
        imagenFondo = imageIcon.getImage();

        validate();
        repaint();
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(properties.getHeaderColor());
        settingsPanel.setLayout(new FlowLayout());

        UIManager.put("Menu.background", Color.BLUE);
        UIManager.put("Menu.foreground", properties.getHeaderColor());
        UIManager.put("Menu.opaque", true);
        UIManager.put("MenuItem.background", properties.getButtonColor()); 
        UIManager.put("MenuItem.foreground", Color.WHITE);

        settingsButton = new JButton();
        settingsButton.setBorder(BorderFactory.createEmptyBorder());

        // Crear icono de menu
        ImageIcon gearIcon = new ImageIcon("media/images/gear.png");
        Image gearImage = gearIcon.getImage();
        Image scaledGearImage = gearImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledGearImage);
        settingsButton.setIcon(scaledIcon);
        settingsButton.setBackground(properties.getHeaderColor());
        settingsPanel.add(settingsButton);

        // Label de configuracion
        makeLabel(settingsPanel, "Configuracion", new Font("Arial", Font.PLAIN, 30), new int[]{0, 10, 0, 10});

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
        headerPanel.add(createSettingsPanel(), BorderLayout.WEST);

        return headerPanel;
    }

    public void refreshHeader() {
        remove(headerPanel); 

        headerPanel = createHeaderPanel(); // Crear un nuevo header con la información actualizada
        add(headerPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }
    
    public JPanel mainWindow() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); 
        panel.setOpaque(false);
        Dimension buttonSize = new Dimension(100, 50); 

        ImageIcon pveImage = new ImageIcon("media/images/PVE.png");
        ImageIcon pvpImage = new ImageIcon("media/images/PVP.png");
        
        Image pveImageScaled = pveImage.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
        ImageIcon pveIcon = new ImageIcon(pveImageScaled);
        
        Image pvpImageScaled = pvpImage.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
        ImageIcon pvpIcon = new ImageIcon(pvpImageScaled);

        // Boton de partida lan
        pveButton = new JButton(pveIcon);
        pvpButton = new JButton(pvpIcon);
        pveButton.setPreferredSize(buttonSize);
        pvpButton.setPreferredSize(buttonSize);

        pveButton.setBorderPainted(false);
        pveButton.setContentAreaFilled(false);
        pveButton.setFocusPainted(false);
        
        pvpButton.setBorderPainted(false);
        pvpButton.setContentAreaFilled(false);
        pveButton.setFocusPainted(false);
        
        try {
            // Ruta al archivo de la fuente
            File fontFile = new File("media/fonts/quickynick.ttf");

            // Cargar la fuente desde el archivo
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            // Opcional: Derivar una nueva fuente con el estilo y tamaño deseado
            customFontBold = customFont.deriveFont(Font.BOLD, 60);
          
            // label.setFont(customFontBold); // Asegúrate de que label esté definido y quitar esta línea si no lo está
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        title = new JTextField("BATTLESHIP");
        title.setFont(customFontBold);
        title.setPreferredSize(buttonSize);
        title.setEditable(false); 
        title.setHorizontalAlignment(JTextField.CENTER); 
        title.setEditable(false);
        title.setOpaque(false);
        title.setBorder(null);
        title.setForeground(new Color(255, 182, 55));        
        title.setColumns(10);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;   
        gbc.gridwidth = 2; // Ancho de la celda
        gbc.insets = new java.awt.Insets(5, 5, 5, 5); // Espaciado entre botones
                
        panel.add(title, gbc);

        gbc.gridy = 1; // Siguiente fila
        gbc.gridwidth = 1; // Restauramos el ancho de la celda a 1
        
        gbc.insets = new java.awt.Insets(5, 26, 5, 5); // Espaciado entre botones
        panel.add(pveButton, gbc);

        gbc.gridx = 1;
        panel.add(pvpButton, gbc);

        return panel;
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
        exitButton = new JButton("Salir");
        exitButton.setBackground(properties.getButtonColor());
        exitButton.setFont(new Font("ARIAL", Font.PLAIN, 25));
        exitButton.setForeground(Color.WHITE);

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(properties.getHeaderColor());
        exitPanel.setBorder(new EmptyBorder(10, 0, 10, 30));
        exitPanel.add(exitButton);

        return exitPanel;
    }

    /* ActionListeners */
    public void addPveButtonListener(ActionListener listener) {
		pveButton.addActionListener(listener);
	}

    public void addPvpButtonListener(ActionListener listener) {
		pvpButton.addActionListener(listener);
	}

    public void addExitButtonListener(ActionListener listener) {
		exitButton.addActionListener(listener);
	}

    public void addSettingsButtonListener(ActionListener listener) {
		settingsButton.addActionListener(listener);
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