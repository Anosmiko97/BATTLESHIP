package views.Lan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;
import models.User;

public class LanView extends JPanel {
    private AppProperties properties = new AppProperties();
    private User userModel;
    private JButton settingsButton;
    private JButton returnButton;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel lanMenuPanel;
    private JButton makeMatchButton;
    private JButton joinMatchButton;
    private JPanel footerPanel;

    public LanView(User userModel) {
        this.userModel = userModel;
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());
        
        headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        mainPanel = createLanMenu();
        add(mainPanel, BorderLayout.CENTER);
        footerPanel = createFooterReturn();
        add(footerPanel, BorderLayout.SOUTH);

        validate();
        repaint();
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

    public JPanel createLanMenu() {
        lanMenuPanel = new JPanel();
        lanMenuPanel.setBackground(properties.getBackgroundColor());
        lanMenuPanel.setLayout(new BorderLayout());
        int padding = 70;
        lanMenuPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        // Crear titulo
        JLabel titleLabel = new JLabel("PARTIDA LAN", JLabel.CENTER);
        titleLabel.setFont(new Font("ARIAL", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 20, 40 , 20));

        // Crear panel para titulo y botones
        JPanel panel = new JPanel();
        panel.setBackground(properties.getBackgroundColor());
        panel.setLayout(new BorderLayout());

        // Imagenes de los lados
        ImageIcon warshipImageRigth = new ImageIcon("./media/images/warshipLan.png");
        ImageIcon warshipImageLeft = new ImageIcon("./media/images/warshipLan1.png");
        Image scaledImageRigth = warshipImageRigth.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        Image scaledImageLeft = warshipImageLeft.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon warshipIconRigth = new ImageIcon(scaledImageRigth);
        ImageIcon warshipIconLeft = new ImageIcon(scaledImageLeft);
        JLabel imgLabel = new JLabel(warshipIconRigth);
        JLabel imgLabel1 = new JLabel(warshipIconLeft);
        imgLabel.setOpaque(false);
        imgLabel1.setOpaque(false);
        imgLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        imgLabel1.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Agregar al panel los elementos
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(createOptionsLanPanel(), BorderLayout.CENTER);
        panel.add(imgLabel, BorderLayout.EAST);
        panel.add(imgLabel1, BorderLayout.WEST);

        lanMenuPanel.add(panel);

        return lanMenuPanel;
    }

    public JPanel createOptionsLanPanel() {
        JPanel optionsLanPanel = new JPanel();
        int padding = 40;  
        optionsLanPanel.setBackground(properties.getHeaderColor());
        optionsLanPanel.setLayout(new GridLayout(2, 1, 20, 20));  
        optionsLanPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        
        // Reducción del tamaño de la fuente de los botones
        makeMatchButton = createButton("UNIRSE A PARTIDA", new Font("ARIAL", Font.PLAIN, 25));
        joinMatchButton = createButton("CREAR PARTIDA", new Font("ARIAL", Font.PLAIN, 25));  
        
        // Agregar paneles
        JPanel container = createContainer();
        optionsLanPanel.add(makeMatchButton);
        optionsLanPanel.add(joinMatchButton);
        container.add(optionsLanPanel);
    
        return container;
    }
    
    public JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setBackground(properties.getButtonColor());
        button.setFont(font);
        button.setForeground(Color.WHITE);

        return button;
    }

    private JPanel createContainer() {
        JPanel container = new JPanel();
        container.setBackground(properties.getBackgroundColor());
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        return container;
    }

    public JPanel createFooterReturn() {
        returnButton = createButton("Regresar", new Font("ARIAL", Font.PLAIN, 25));

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(properties.getHeaderColor());
        exitPanel.setBorder(new EmptyBorder(10, 0, 10, 30));
        exitPanel.add(returnButton);

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
    public void addMakeMatchButtonListener(ActionListener listener) {
		makeMatchButton.addActionListener(listener);
	}

    public void addJoinMatchButtonListener(ActionListener listener) {
		joinMatchButton.addActionListener(listener);
	}

    public void addReturnButtonListener(ActionListener listener) {
	    returnButton.addActionListener(listener);
	}

    /* Getters y setters */
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

    public JButton getMakeMatchButton() {
        return this.makeMatchButton;
    }
    public void setMakeMatchButton(JButton button) {
       this.makeMatchButton = button; 
    }
}
