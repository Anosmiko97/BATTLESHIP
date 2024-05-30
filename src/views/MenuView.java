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
    private JButton lanButton;
    private JButton exitButton;

    public MenuView(User userModel) {
        this.userModel = userModel;
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(mainWindow(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
        
        ImageIcon imageIcon = new ImageIcon("media/images/background.jpg");
        imagenFondo = imageIcon.getImage();
    }

    private JPanel createInfoPanel(String name) {
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBackground(properties.getHeaderColor());

        // Panel de bandera y nombre
        JPanel userPanel = new JPanel(new FlowLayout());
        JLabel namelabel = new JLabel(name);
        namelabel.setFont(new Font("ARIAL", Font.PLAIN, 30));
        namelabel.setForeground(Color.WHITE);
        int padding = 30;
        namelabel.setBorder(new EmptyBorder(10, 0, 10, padding));
        userPanel.add(namelabel);
        infoPanel.add(namelabel);    

        return infoPanel;
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

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        // Crear icono de menu
        ImageIcon gearIcon = new ImageIcon("media/images/gear.png");
        Image gearImage = gearIcon.getImage();
        Image scaledGearImage = gearImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledGearImage);
        
        JMenu settingsMenu = new JMenu();
        settingsMenu.setIcon(scaledIcon);
        settingsMenu.setBackground(properties.getHeaderColor());

        // Opciones de menu
        JMenuItem item1 = new JMenuItem("Opción 1");
        JMenuItem item2 = new JMenuItem("Opción 2");
        JMenuItem item3 = new JMenuItem("Opción 3");
        settingsMenu.add(item1);
        settingsMenu.add(item2);
        settingsMenu.add(item3);
        menuBar.add(settingsMenu);

        // Label de "Configuracion"
        JLabel settingsLabel = new JLabel("Configuracion"); 
        settingsLabel.setFont(new Font("ARIAL", Font.PLAIN, 30)); 
        settingsLabel.setForeground(Color.WHITE);  

        // Agregar al panel padre
        settingsPanel.add(menuBar);
        settingsPanel.add(settingsLabel);

        return settingsPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());
  
        headerPanel.add(createInfoPanel(userModel.getName()), BorderLayout.EAST);
        headerPanel.add(createSettingsPanel(), BorderLayout.WEST);

        return headerPanel;
    }
    
    public JPanel mainWindow() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar los botones
        panel.setOpaque(false);
        Dimension buttonSize = new Dimension(100, 50); // Tamaño personalizado

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
            // Escala la imagen para que se ajuste al tamaño del JPanel
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JPanel createFooterPanel() {
        int padding = 30;

        exitButton = new JButton("Salir");
        exitButton.setBackground(properties.getButtonColor());
        exitButton.setFont(new Font("ARIAL", Font.PLAIN, 25));
        exitButton.setForeground(Color.WHITE);

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(properties.getHeaderColor());
        exitPanel.setBorder(new EmptyBorder(10, 0, 10, padding));
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
}