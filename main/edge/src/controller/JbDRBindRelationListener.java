package controller;

import view.ConvertGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JbDRBindRelationListener implements ActionListener {

    private ConvertGUI convertGUI;

    public JbDRBindRelationListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void actionPerformed(ActionEvent ae) {

        convertGUI.bindRelations();

    }

}
