package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Clases propias */
import views.CreateMatchView;
import controlers.socket.ServerControler;

public class CreateMatchControler implements ActionListener{
    private String keyMatch;
    private CreateMatchView createMatchView;
    private ServerControler serverControler;
    private Thread serverThread;

    public CreateMatchControler(String keyMatch) {
        // Ejecutar de manera paralela la vista y el servidor
        this.keyMatch = keyMatch;
        serverControler = new ServerControler();
        serverThread = new Thread(() -> {
            serverControler.compareKey(this.keyMatch);
        });
        serverThread.start();
        javax.swing.SwingUtilities.invokeLater(() -> {
            createMatchView = new CreateMatchView(this.keyMatch);
            createMatchView.addCancelButtonListener(this);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CANCELAR")) {
            System.out.println("boton de cancelar [CREAR PARTIDA]");
            createMatchView.dispose();
            serverThread.interrupt();
        }
    }
}
