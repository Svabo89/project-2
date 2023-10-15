package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class JlDRFieldsTablesRelationsListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public JlDRFieldsTablesRelationsListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.jlDRFieldsTablesRelatedToValueChanged();

    }

}
