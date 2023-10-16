package edu.rit.edgeconverter.controller;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The SceneListener class provides utility methods for handling window events, 
 * such as when the window is closing or showing. It is particularly useful for 
 * performing specific actions or logging during these events.
 */
public class SceneListener {

    /**
     * Handles the close request event of the primary stage. 
     * This method is invoked when the user attempts to close the window, 
     * allowing for specific actions or logging to be performed at this time.
     *
     * @param primaryStage The primary stage of the application.
     */
    public static void onCloseRequest(Stage primaryStage) {
        primaryStage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // Log or perform actions as needed when the window is closing
                    System.out.println("Window is closing");
                }
            }
        );
    }

    /**
     * Handles the showing event of the primary stage. 
     * This method is invoked when the window is about to be shown, 
     * allowing for specific actions or logging to be performed at this time.
     *
     * @param primaryStage The primary stage of the application.
     */
    public static void onShowing(Stage primaryStage) {
        primaryStage.setOnShowing(
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // Log or perform actions as needed when the window is opening
                    System.out.println("Window is opening");
                }
            }
        );
    }
}
