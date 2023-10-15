package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDTVarcharListener implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDTVarcharListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.startInputFilter();

    }

}
