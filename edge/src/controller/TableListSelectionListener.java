package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class TableListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public TableListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.tableValueChanged();

    }

}
