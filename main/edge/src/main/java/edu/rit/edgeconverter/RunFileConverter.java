package edu.rit.edgeconverter;

import edu.rit.edgeconverter.controller.EdgeController;
import edu.rit.edgeconverter.view.ConverterGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class RunFileConverter extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) {
    EdgeController controller = new EdgeController(
      new ConverterGUI(primaryStage)
    );
  }
}
