package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* Clases propias */
import views.JoinMatchView;
import models.AppProperties;
import models.User;

public class JoinMatchController implements ActionListener {
    private AppProperties properties = new AppProperties();
    private JoinMatchView joinMatchView;
    private String key;

    public JoinMatchController(JoinMatchView joinMatchView) {
        this.joinMatchView = joinMatchView;
        this.joinMatchView.addJoinButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("APLICAR")) {
            System.out.println("boton de aplicar");

            if (checkFields()) {
                key = joinMatchView.getKeyField().getText();
                joinMatchView.dispose();

            } else {
                JOptionPane.showMessageDialog(joinMatchView, "INGRESE UN NUMERO", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } 

    private boolean checkFields() {
        String keyStr = joinMatchView.getKeyField().getText();
        return keyStr != null && isNumber(keyStr);
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
