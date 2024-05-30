package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Clases propias */
import views.MenuView;
import models.User;

public class MenuControler implements ActionListener {

    private MenuView menuView; 
    private User userModel;

    public MenuControler(User userModel ,MenuView menuView) {   
        this.menuView = menuView;
        this.userModel = userModel;

        this.menuView.addPvpButtonListener(this);
        this.menuView.addPveButtonListener(this);
        this.menuView.addExitButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Salir")) {
            System.out.println("Boton de salir");
        } else if (e.getSource() == menuView.getPveButton()) {
            System.out.println("BOT button");
        } else if (e.getSource() == menuView.getPvpButton()) {
            System.out.println("lan button");
        }
    }
}
