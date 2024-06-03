package controlers.Lan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Cell;
import views.Lan.LanMatchView;

public class LanMatchController implements ActionListener {
    private LanMatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;

    public LanMatchController(LanMatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft) {
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
}
