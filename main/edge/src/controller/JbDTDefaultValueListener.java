package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDTDefaultValueListener implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDTDefaultValueListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.addDefaultValues();

    }

}
