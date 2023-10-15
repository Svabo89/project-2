package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDTMoveButtonListeners implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDTMoveButtonListeners(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.changeCurrentDTTable(ae);

    }

}
