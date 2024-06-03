package controlers.Lan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

import models.Cell;
import views.Lan.LanMatchView;

public class LanMatchController implements ActionListener {
    private LanMatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;

    /* Atributos para conexion */
    ServerSocket serverSocket;
    Socket clientSocket;
    String mode;

    public LanMatchController(ServerSocket serverSocket, Socket clientSocket, LanMatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft, String mode) {
        this.mode = mode;
        if (this.mode == "server") {
            this.serverSocket = serverSocket;
        } else if (this.mode == "client") {
            this.clientSocket = clientSocket;
        }
        
        this.matchView = matchView;
        this.cellsRigth = cellsRigth;
        this.cellsLeft = cellsLeft;
        addCellsListener();
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth.length; j++) {
                cellsRigth[i][j].getButton().addActionListener((ActionListener) this);
                cellsLeft[i][j].getButton().addActionListener((ActionListener) this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth.length; j++) {  
                if (e.getSource() == cellsRigth[i][j].getButton()) {
                    System.out.println("Disparo en: [" + i + ", " + j + "]");
                    return; 
                } else if (e.getSource() == cellsLeft[i][j].getButton()) {
                    System.out.println("Barco en: [" + i + ", " + j + "]");
                    return; 
                } 
            }
        }
    }

    /* Getters y setters */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public Socket getClientSocket() {
        return this.clientSocket;
    }
}
