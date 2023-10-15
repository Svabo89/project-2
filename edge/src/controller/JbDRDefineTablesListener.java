package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDRDefineTablesListener implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDRDefineTablesListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.defineTables();

    }

}
