package controlers.test;

import javax.swing.*;
import java.awt.*;

public class LanMenuView extends JPanel {
    private AppProperties properties = new AppProperties();
    private JPanel lanMenuPanel;
    private JButton createGameButton;
    private JButton joinGameButton;

    public LanMenuView() {
        setBackground(properties.getBackgroundColor());
        setLayout(new BorderLayout());

        add(createTitleLabel(), BorderLayout.NORTH);
        add(optionsLanPanel(), BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("PARTIDA LAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Padding around the title

        return titleLabel;
    }

    private JPanel optionsLanPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(properties.getBackgroundColor());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create the "Crear Partida" button
        createGameButton = new JButton("CREAR PARTIDA");
        createGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createGameButton.setBackground(properties.getButtonColor());
        createGameButton.setForeground(Color.WHITE);
        createGameButton.setFocusPainted(false);
        createGameButton.setFont(new Font("Arial", Font.PLAIN, 20));
        createGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding inside the button

        // Create the "Unirse a Partida" button
        joinGameButton = new JButton("UNIRSE A PARTIDA");
        joinGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinGameButton.setBackground(properties.getButtonColor());
        joinGameButton.setForeground(Color.WHITE);
        joinGameButton.setFocusPainted(false);
        joinGameButton.setFont(new Font("Arial", Font.PLAIN, 20));
        joinGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding inside the button

        // Add spacing between buttons
        panel.add(Box.createVerticalStrut(20));
        panel.add(createGameButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(joinGameButton);

        return panel;
    }

    // Getters for buttons to add action listeners
    public JButton getCreateGameButton() {
        return createGameButton;
    }

    public JButton getJoinGameButton() {
        return joinGameButton;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LAN Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new LanMenuView());
        frame.setVisible(true);
    }
}

class AppProperties {
    public Color getBackgroundColor() {
        return new Color(10, 34, 61); // Background color similar to the image
    }

    public Color getButtonColor() {
        return new Color(15, 63, 100); // Button color similar to the image
    }
}

