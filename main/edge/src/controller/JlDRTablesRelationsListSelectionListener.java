package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class JlDRTablesRelationsListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public JlDRTablesRelationsListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.tablesRelationsValueChanged();

    }

}
