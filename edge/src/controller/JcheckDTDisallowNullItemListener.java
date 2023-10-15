package controller;

import java.awt.event.*;
import view.ConvertGUI;

public class JcheckDTDisallowNullItemListener implements ItemListener {

    private ConvertGUI convertGUI;

    public JcheckDTDisallowNullItemListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void itemStateChanged(ItemEvent e) {

        convertGUI.jcheckDTDisallowNullItemStateChanged();

    }

}
