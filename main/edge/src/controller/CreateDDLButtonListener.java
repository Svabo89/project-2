package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateDDLButtonListener implements ActionListener {

    private ConvertGUI convertGUI;

    public CreateDDLButtonListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.ddlBtnActionPerformed();

    }

}
