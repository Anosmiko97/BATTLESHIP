package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* Clases propias */
import views.SettingsView;
import models.AppProperties;
import models.User;

public class SettingsController implements ActionListener {
    private AppProperties properties = new AppProperties();
    private SettingsView settingsView;
    private User user;

    public SettingsController(SettingsView settingsView) {
        this.settingsView = settingsView;
        this.settingsView.addApplyChangesListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("APLICAR")) {
            System.out.println("boton de aplicar");

            if (checkFields()) {
                String name = settingsView.getNameField().getText();
                String flag = settingsView.getFlagFile();
                user = new User(name);
                user.setFlag(flag);
                settingsView.dispose();

            } else {
                JOptionPane.showMessageDialog(settingsView, "LLENE TODOS LOS CAMPOS", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } 

    private boolean checkFields() {
        return settingsView.getNameField().getText() != null && settingsView.getFlagFile() != null;
    }
    
    /* Getters y setters */
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
