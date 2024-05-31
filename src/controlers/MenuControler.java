package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Clases propias */
import views.MenuView;
import views.SettingsView;
import models.User;

public class MenuControler implements ActionListener {

    private MenuView menuView; 
    private User userModel;

    public MenuControler(User userModel ,MenuView menuView) {   
        this.menuView = menuView;
        this.userModel = userModel;

        this.menuView.addSettingsButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuView.getSettingsButton()) {
            System.out.println("Boton de settings");
            SettingsView settingsMenu = new SettingsView();
        } 
    }
}
