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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import models.AppProperties;

public class MenuView extends JPanel {

    AppProperties properties = new AppProperties();
    private JButton pveButton;
    private JButton pvpButton;
    private JTextField title;
    private Image imagenFondo;
    private Font customFontBold;

    public MenuView() {
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());

        add(header(), BorderLayout.NORTH);
        add(mainWindow(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        
        ImageIcon imageIcon = new ImageIcon("media/images/background.jpg");
        imagenFondo = imageIcon.getImage();
    }

    public JPanel header() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getHeaderColor());
        panel.setLayout(new FlowLayout());

        return panel;
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
            customFontBold = customFont.deriveFont(Font.BOLD, 40);

            // Usar la fuente personalizada en tus componentes

            // label.setFont(customFontBold); // Asegúrate de que label esté definido y quitar esta línea si no lo está
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        title = new JTextField("BATTLESHIP");
        title.setFont(customFontBold);
        title.setPreferredSize(buttonSize);
        title.setEditable(false); // Hacemos el campo de texto no editable
        title.setHorizontalAlignment(JTextField.CENTER); // Centramos el texto horizontalmente
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

    public JPanel footer() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getHeaderColor());
        panel.setLayout(new FlowLayout());

        return panel;
    }

    // Asignar listeners en el controlador
    public void buttonActionListener(ActionListener listener) {
		pveButton.addActionListener(listener);
		pvpButton.addActionListener(listener);
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
}