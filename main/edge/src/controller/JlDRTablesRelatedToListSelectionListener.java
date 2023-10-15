package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class JlDRTablesRelatedToListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public JlDRTablesRelatedToListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.jlDRTablesRelatedToValueChanged();

    }

}
