package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Clases propias */
import models.Cell;
import views.MatchView;

public class MatchController implements ActionListener {
    private MatchView matchView;
    private Cell[][] cellsRigth;
    private Cell[][] cellsLeft;

    public MatchController(MatchView matchView, Cell[][] cellsRigth, Cell[][] cellsLeft) {
        this.matchView = matchView;
        this.cellsRigth = cellsRigth;
        this.cellsLeft = cellsLeft;
        addCellsListener();
    }

    public void addCellsListener() {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth.length; j++) {
                cellsRigth[i][j].getButton().addActionListener((ActionListener) this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cellsRigth.length; i++) {
            for (int j = 0; j < cellsRigth[i].length; j++) {  
                if (e.getSource() == cellsRigth[i][j].getButton()) {
                    System.out.println("Disparo en: [" + i + ", " + j + "]");
                    return; 
                } 
            }
        }
    }
}