package controlers.Lan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlers.Server.ClientControler;
import models.AppProperties;
import models.User;
import views.Lan.JoinMatchView;

public class JoinMatchController implements ActionListener {
    private AppProperties properties = new AppProperties();
    private JoinMatchView joinMatchView;
    private ClientControler clientControler;
    private String key;

    public JoinMatchController(JoinMatchView joinMatchView) {
        this.joinMatchView = joinMatchView;
        clientControler = new ClientControler("localhost");
        this.joinMatchView.addJoinButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("UNIRSE")) {
            System.out.println("boton de unirse a partida");

            if (checkFields()) {
                clientControler.setHostName(key);
                sendData(e);
            } else {
                JOptionPane.showMessageDialog(joinMatchView, "INGRESE UN NUMERO", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } 

    private void sendData(ActionEvent e) {
        if (e.getActionCommand().equals("UNIRSE")) {
            System.out.println("boton de unirse [UNIRSE PARTIDA]");
            clientControler.connect();
        }
    }

    private boolean checkFields() {
        String keyStr = joinMatchView.getKeyField().getText();
        //return keyStr != null && isNumber(keyStr);
        return true;
    }

    private boolean isNumber(String str) {
        String regex = "^-?\\d+(\\.\\d+)?$";
        return str.matches(regex);
    }
    
    /* Getters y setters */
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
