package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class JlDRFieldsTablesRelatedToListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public JlDRFieldsTablesRelatedToListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.fieldsTablesRelationsValueChanged();

    }

}
