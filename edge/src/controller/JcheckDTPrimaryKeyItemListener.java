package controller;

import java.awt.event.*;
import view.ConvertGUI;

public class JcheckDTPrimaryKeyItemListener implements ItemListener {

    private ConvertGUI convertGUI;

    public JcheckDTPrimaryKeyItemListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void itemStateChanged(ItemEvent e) {

        convertGUI.jcheckDTPrimaryKeyItemStateChanged();

    }

}
