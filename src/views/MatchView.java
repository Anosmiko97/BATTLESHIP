package views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;

public class MatchView extends JPanel {
    AppProperties properties = new AppProperties();

    // Texto del panel
    private String userName = "Uriel";
    private String opponentName = "Ale";

    // Botones
    JButton exitButton;

    public MatchView() {
        setLayout(new BorderLayout());
        setBackground(properties.getBackgroundColor());  

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createScorePanel() {
        JPanel scorePanel = new JPanel(new GridLayout(3, 1));
        scorePanel.setBackground(properties.getNamesPanelColor());
        
        int padding = 10;
        scorePanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        // Labels para informacion de partida
        JLabel scoreLabel1 = new JLabel("Barcos hundidos: 2");
        scoreLabel1.setForeground(Color.WHITE);
        JLabel scoreLabel2 = new JLabel("Disparos acertados: 2");
        scoreLabel2.setForeground(Color.WHITE);
        JLabel scoreLabel3 = new JLabel("Total de disparos: 5");
        scoreLabel2.setForeground(Color.WHITE);
        
        scorePanel.add(scoreLabel1);
        scorePanel.add(scoreLabel2);
        scorePanel.add(scoreLabel3);

        return scorePanel;
    }

    /* Metodos para header */
    private JPanel createInfoPanel(String name, String position) {
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBackground(properties.getHeaderColor());

        // Panel de bandera y nombre
        JPanel userPanel = new JPanel(new FlowLayout());
        JLabel namelabel = new JLabel(name);
        namelabel.setFont(new Font("ARIAL", Font.PLAIN, 30));
        namelabel.setForeground(Color.WHITE);
        int padding = 30;
        namelabel.setBorder(new EmptyBorder(0, padding, 0, padding));
        userPanel.add(namelabel);

        if (position.equals("left")) {
            infoPanel.add(createScorePanel());
            infoPanel.add(namelabel);
        } else {
            infoPanel.add(namelabel);
            infoPanel.add(createScorePanel());
        }

        return infoPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(properties.getHeaderColor());
  
        JPanel userPanel = createInfoPanel(userName, "left");
        JPanel opponentPanel = createInfoPanel(opponentName, "rigth");

        JLabel vsLabel = new JLabel("VS", JLabel.CENTER);
        vsLabel.setFont(new Font("ARIAL", Font.BOLD, 50));
        vsLabel.setForeground(Color.WHITE);

        headerPanel.add(userPanel, BorderLayout.WEST);
        headerPanel.add(vsLabel, BorderLayout.CENTER);
        headerPanel.add(opponentPanel, BorderLayout.EAST);

        return headerPanel;
    }

    /* Metodos para el centro el panel central */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.setBackground(properties.getBackgroundColor());

        int padding = 40;
        JPanel leftPanel = createGridPanel("BARCOS", Color.decode("#033A84"));
        leftPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        JPanel rightPanel = createGridPanel("DISPAROS", Color.decode("#A6A6A6"));
        rightPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        return centerPanel;
    }

    private JPanel createGridPanel(String title, Color cellColor) {
        JPanel gridPanel = new JPanel(new BorderLayout());
        gridPanel.setBackground(properties.getBackgroundColor());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#545454"));
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font(properties.getFontApp(), Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        gridPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(11, 11));
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                JLabel cell = new JLabel("", JLabel.CENTER);
                Font fontCells = new Font(properties.getFontApp(), Font.PLAIN, 20);
                cell.setForeground(Color.WHITE);
                cell.setFont(fontCells);
                cell.setOpaque(true);
                cell.setBackground(cellColor);

                cell.setBorder(BorderFactory.createLineBorder(Color.white));
                if (row == 0 && col == 0) {
                    cell.setBackground(Color.decode("#173764"));
                } else if (row == 0) {
                    cell.setText(Integer.toString(col));
                    cell.setBackground(Color.decode("#173764"));
                } else if (col == 0) {
                    cell.setText(Character.toString((char) ('A' + row - 1)));
                    cell.setBackground(Color.decode("#173764"));
                }
                grid.add(cell);
            }
        }
        gridPanel.add(grid, BorderLayout.CENTER);

        return gridPanel;
    }

    public JPanel createFooterPanel() {
        exitButton = new JButton("Salir de la partida");
        exitButton.setBackground(properties.getButtonColor());
        exitButton.setFont(new Font("ARIAL", Font.PLAIN, 25));
        exitButton.setForeground(Color.WHITE);

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(properties.getHeaderColor());
        exitPanel.setBorder(new EmptyBorder(5, 0, 5, 30));
        exitPanel.add(exitButton);

        return exitPanel;
    }

    /* Getters y setters */
    public JButton getExitButton() {
        return this.exitButton;
    }
    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    /* ActionListeners */
    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}


