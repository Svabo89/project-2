package controller;

import view.ConvertGUI;
import javax.swing.event.*;

public class FieldTableListSelectionListener implements ListSelectionListener {

    private ConvertGUI convertGUI;

    public FieldTableListSelectionListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void valueChanged(ListSelectionEvent e) {

        convertGUI.fieldTableValueChanged();

    }

}
