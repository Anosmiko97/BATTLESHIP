package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/* Clases propias */
import views.SettingsView;

public class SettingsController implements ActionListener {
    private SettingsView settingsView;
    private String name;
    private String flagFile;

    public SettingsController(SettingsView settingsView) {
        this.settingsView = settingsView;
        this.settingsView.addApplyChangesListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("APLICAR")) {
            System.out.println("boton de aplicar");

            if (checkFields()) {
                name = settingsView.getNameField().getText();
                settingsView.getFlagFile();
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFlagFile() {
        return this.flagFile;
    }
    public void setFlagFile(String file) {
        this.flagFile = file;
    }
}
