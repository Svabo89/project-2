package starter;

import view.ConvertGUI;
import controller.ConvertGUIController;

public class RunEdgeConvert {

    public static void main(String[] args) {

        ConvertGUI cgui = new ConvertGUI();
        new ConvertGUIController(cgui);

    }
}
