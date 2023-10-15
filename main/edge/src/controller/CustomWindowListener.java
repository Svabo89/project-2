package controller;

import view.ConvertGUI;
import java.awt.event.*;

public class CustomWindowListener implements WindowListener {

    private ConvertGUI convertGUI;

    public CustomWindowListener(ConvertGUI convertGUI) {

        this.convertGUI = convertGUI;

    }

    public void windowActivated(WindowEvent we) {
        //windowActivated
    }

    public void windowClosed(WindowEvent we) {
        //windowClosed
    }

    public void windowDeactivated(WindowEvent we) {
        //windowDeactivated
    }

    public void windowDeiconified(WindowEvent we) {
        //windowDeiconified
    }

    public void windowIconified(WindowEvent we) {
        //windowIconified
    }

    public void windowOpened(WindowEvent we) {
        //windowOpened
    }

    public void windowClosing(WindowEvent we) {

        convertGUI.windowClosing(we);

    }

}
