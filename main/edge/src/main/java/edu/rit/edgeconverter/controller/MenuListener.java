package edu.rit.edgeconverter.controller;

import edu.rit.edgeconverter.model.Field;
import edu.rit.edgeconverter.model.Table;
import edu.rit.edgeconverter.parsers.EdgeFileConverter;
import edu.rit.edgeconverter.view.ConverterGUI;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The MenuListener class is responsible for handling events triggered by menu actions, particularly those associated with file operations.
 * It encapsulates the logic for opening, processing, and displaying the contents of selected files within the application's user interface.
 */
public class MenuListener implements EventHandler<ActionEvent> {

  private ConverterGUI gui;
  private ObservableList<String> itemsField = FXCollections.observableArrayList();
  private ObservableList<String> itemsTable = FXCollections.observableArrayList();

  private static final Logger log = LogManager.getLogger("edu.rit");

  private EdgeFileConverter efp;

  private Map<String, ArrayList<Field>> mappedTable = new HashMap<String, ArrayList<Field>>();

  public MenuListener(ConverterGUI gui) {
    this.gui = gui;
  }

  /**
   * Handles the ActionEvent triggered by menu actions.
   * This method facilitates the file opening process, including file selection, validation, and content processing.
   * @param event The ActionEvent to be handled.
   */
  @Override
  public void handle(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    String currentDirectory = System.getProperty("user.dir");

    //opening resource file 
     fileChooser.setInitialDirectory(
      new File(System.getProperty("user.dir") + "/src/main/resources/resources")
    );

    fileChooser
      .getExtensionFilters()
      .addAll(
        new FileChooser.ExtensionFilter("Edge Files (*.edg)", "*.edg"),
        new FileChooser.ExtensionFilter("Save Files (*.sav)", "*.sav")
      );
    // Set the title for the file chooser dialog
    fileChooser.setTitle("Opening File");
    log.info("User is choosing a file.");

    // Show the file open dialog and get the selected file
    File selectedFile = fileChooser.showOpenDialog(gui.getStage()); // Replace 'yourStage' with the actual stage
    log.info("User has selected a file.");

    // The conditional statement evaluates the validity of the selected file based on its extension.
    if (selectedFile != null) {
      String fileName = selectedFile.getName();
      // The conditional block processes files with the ".edg" extension, invoking the parseFile method for content extraction.

      if (fileName.endsWith(".edg")) {
        // Call the parseFile method to parse the selected file
        efp = new EdgeFileConverter(selectedFile); // Initialize the instance variable
        System.out.println("Number of tables: " + efp.getTables().length);
        System.out.println("Number of fields: " + efp.getFields().length);
        // key - Table_id, value - array fields
        for (Table table : efp.getTables()) {
          ArrayList<Field> allFields = new ArrayList<Field>();
          for (Field field : efp.getFields()) {
            if (table.getNumFigure() == field.getTableID()) {
              allFields.add(field);
            }
          }
          mappedTable.put(table.getTableName(), allFields);
        }
        populateListViewTables(efp.getTables());
        // Log statement indicating an invalid file selection due to an unsupported file extension.

      } else {
        // The selected file does not have the ".edg" extension.
        System.out.println("Selected file is not a valid .edg file.");
      }
    }
  }
 /**
     * Populates the ListView component with table names extracted from the provided array of Table objects.
     * @param tables An array of Table objects containing table information to be displayed.
     */
  public void populateListViewTables(Table[] tables) {
    gui.getListViewTables().getItems().clear(); // Clear previous items
    for (Table table : tables) {
      itemsTable.add(table.getTableName()); // Add the table names
    }
    gui.getListViewTables().setItems(itemsTable);
  }

  /**
     * Populates the ListView component with field names extracted from the provided array of Field objects.
     * @param fields An array of Field objects containing field information to be displayed.
     */
  public void populateListViewFieldList(Field[] fields) {
    gui.getListViewFieldList().getItems().clear(); 

    // Create an observable list for the fields
    ObservableList<String> itemsField = FXCollections.observableArrayList();

    for (Field field : fields) {
      itemsField.add(field.getName()); 
    }

    gui.getListViewFieldList().setItems(itemsField);
  }

  public Map<String, ArrayList<Field>> getMappedTable() {
    return this.mappedTable;
  }
} // MenuListener out
