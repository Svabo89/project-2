package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDTDefineRelationsListener implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDTDefineRelationsListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.showDefineRelationsScreen();

    }

}
