package views.Lan;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/* Clases propias */
import models.AppProperties;
import models.Cell;
import models.UserDAO;
import models.User;

public class LanMatchView extends JPanel {
    AppProperties properties = new AppProperties();
    UserDAO userDAO = new UserDAO();
    User userModel;

    // Texto del panel
    private String userName;
    private String opponentName;

    // Botones
    JButton exitButton;

    // Atributos
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;
    private String message;
    private JLabel messagePanel;
    private JPanel headerPanel;

    public LanMatchView(User userModel, String opponentName, Cell[][] cells1, Cell[][] cells2) {
        setLayout(new BorderLayout());
        setBackground(properties.getBackgroundColor());  

        this.message = "COLOQUEN SUS BARCOS";
        this.opponentName = opponentName;
        this.userModel = userModel;
        this.userName = userModel.getName();
        this.cellsRigth = cells1;
        this.cellsLeft = cells2;

        headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(properties.getHeaderColor());

        JPanel userPanel = createInfoPanel(userName, "left");
        JPanel opponentPanel = createInfoPanel(opponentName, "rigth");

        panel.add(userPanel, BorderLayout.WEST);
        messagePanel = createMessagePanel();
        panel.add(messagePanel, BorderLayout.CENTER);
        panel.add(opponentPanel, BorderLayout.EAST);

        return panel;
    }

    public JLabel createMessagePanel() {
        JLabel label = new JLabel(message, JLabel.CENTER);
        if (message.length() > 5) {
            label.setFont(new Font("ARIAL", Font.BOLD, 20));
        } else {
            label.setFont(new Font("ARIAL", Font.BOLD, 50));
        }
        label.setForeground(Color.WHITE);

        return label;
    }

    public void refreshMessagePanel() {
        remove(messagePanel);

        messagePanel = createMessagePanel();
        headerPanel.add(messagePanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    /* Metodos para el centro el panel central */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.setBackground(properties.getBackgroundColor());

        int padding = 40;
        JPanel leftPanel = createGridPanel("BARCOS", cellsLeft);
        leftPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        JPanel rightPanel = createGridPanel("DISPAROS", cellsRigth);
        rightPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        return centerPanel;
    }

    private JPanel createGridPanel(String title, Cell[][] cells) {
        JPanel gridPanel = new JPanel(new BorderLayout());
        gridPanel.setBackground(properties.getBackgroundColor());
    
        // Panel del título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#545454"));
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font(properties.getFontApp(), Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        gridPanel.add(titlePanel, BorderLayout.NORTH);
    
        // Panel de la cuadrícula
        JPanel grid = new JPanel(new GridLayout(cells.length, cells[0].length));
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
    
                if (i == 0 && j == 0) {
                    cells[i][j].getButton().setBackground(Color.decode("#173764"));
                    cells[i][j].getButton().setEnabled(false); 
                } else if (i == 0) {
                    cells[i][j].getButton().setText(Integer.toString(j));
                    cells[i][j].getButton().setBackground(Color.decode("#173764"));
                    cells[i][j].getButton().setEnabled(false);
                } else if (j == 0) {
                    cells[i][j].getButton().setText(Character.toString((char) ('A' + i - 1)));
                    cells[i][j].getButton().setBackground(Color.decode("#173764"));
                    cells[i][j].getButton().setEnabled(false); 
                }
    
                grid.add(cells[i][j].getButton());
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

    public void refreshHeaderPanel() {
        remove(headerPanel); 

        headerPanel = createHeaderPanel(); 
        add(headerPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    /* Getters y setters */
    public JButton getExitButton() {
        return this.exitButton;
    }
    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public Cell[][] getCellsRigth() {
        return this.cellsRigth;
    }
    public void setCellsRigth(Cell[][] cells) {
        this.cellsRigth = cells;
    }

    public Cell[][] getCellsLeft() {
        return this.cellsLeft;
    }
    public void setCellsLeft(Cell[][] cells) {
        this.cellsLeft = cells;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return this.userModel;
    }
    public void setMessage(User user) {
        this.userModel = user;
    }

    /* ActionListeners */
    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}


