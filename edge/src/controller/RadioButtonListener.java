package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioButtonListener implements ActionListener {

    private ConvertGUI convertGUI;

    public RadioButtonListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.radioButtonListenerActionPerformed();

    }

}
