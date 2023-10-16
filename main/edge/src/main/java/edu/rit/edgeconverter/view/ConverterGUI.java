package edu.rit.edgeconverter.view;
import edu.rit.edgeconverter.controller.FieldListListener;
import edu.rit.edgeconverter.controller.MenuListener;
import edu.rit.edgeconverter.parsers.EdgeFileConverter;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.InputStream;
import javafx.scene.Scene;
import javafx.scene.control.*;

/**
 * This is where the magic happens, folks! 🎩✨ The ConverterGUI is like the command center
 * for our Edge Converter application. Here, you'll find all the widgets, buttons, and
 * doodads that make our app tick.
 */
public class ConverterGUI {

  Stage stage;

  // UI elements for the converter GUI
  private ListView<String> listViewTables = new ListView<>();
  private ListView<String> listViewFieldList = new ListView<>();
  private MenuItem menuOpenFile = new MenuItem("Open");
  private MenuItem menuSave = new MenuItem("Save");
  private MenuItem menuSaveAs = new MenuItem("Save as");
  private MenuItem menuExit = new MenuItem("Exit");
  private MenuItem menuShowDatabase = new MenuItem(
    "Show Database Products Available"
  );
  private Button buttonCreateDDL = new Button("Create DDL");

  // Listeners for handling events
  private MenuListener menuListener = new MenuListener(this);
  private FieldListListener FieldListListener = new FieldListListener(
    this,
    menuListener
  );

  private EdgeFileConverter efp;

  // Constructor initializes the stage
  public ConverterGUI(Stage primaryStage) {
    this.stage = primaryStage;
  }

  // Start method to set up the UI
  public void start() {
    stage.setTitle("File Converter");

    Pane root = new Pane();
    root.setPrefSize(900, 500);

    // Load and set the background image
    InputStream is = getClass().getResourceAsStream("/style/img/test.jpg"); // Adjusted path
    if (is != null) {
      Image backgroundImage = new Image(is);
      BackgroundImage background = new BackgroundImage(
        backgroundImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT
      );
      root.setBackground(new Background(background));
    } else {
      System.err.println("Loading... Please wait...");
    }

    // Set up the menu bar and menus
    MenuBar menuBar = new MenuBar();
    menuBar.setPrefWidth(902);

    Menu fileMenu = new Menu("File");
    fileMenu.getItems().addAll(menuOpenFile, menuSave, menuSaveAs, menuExit);

    Menu optionsMenu = new Menu("Options");
    optionsMenu
      .getItems()
      .addAll(
        new MenuItem("Show Output File Definition Location"),
        menuShowDatabase
      );

    Menu helpMenu = new Menu("Help");
    helpMenu.getItems().addAll(new MenuItem("About"));

    menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu);

    // Set up list views for tables and fields
    listViewTables.setLayoutX(14);
    listViewTables.setLayoutY(65);
    listViewTables.setPrefHeight(354);
    listViewTables.setPrefWidth(276);

    listViewFieldList.setLayoutX(312);
    listViewFieldList.setLayoutY(65);
    listViewFieldList.setPrefHeight(354);
    listViewFieldList.setPrefWidth(276);

    // Set up buttons for defining relations and creating DDL
    Button buttonDefineRelations = new Button("Define Relations");
    buttonDefineRelations.setLayoutX(80);
    buttonDefineRelations.setLayoutY(444);
    buttonDefineRelations.setPrefHeight(20);
    buttonDefineRelations.setPrefWidth(150);
    buttonDefineRelations.setDisable(true);

    buttonCreateDDL.setLayoutX(369);
    buttonCreateDDL.setLayoutY(444);
    buttonCreateDDL.setPrefHeight(20);
    buttonCreateDDL.setPrefWidth(150);
    buttonCreateDDL.setDisable(true);

    // Buttons to set Varchar length and default value
    Button buttonSetVarcharLength = new Button("Set Varchar Length");
    buttonSetVarcharLength.setLayoutX(717);
    buttonSetVarcharLength.setLayoutY(132);
    buttonSetVarcharLength.setPrefHeight(25);
    buttonSetVarcharLength.setPrefWidth(171);

    Button buttonSetDefaultValue = new Button("Set Default Value");
    buttonSetDefaultValue.setLayoutX(715);
    buttonSetDefaultValue.setLayoutY(394);
    buttonSetDefaultValue.setPrefHeight(25);
    buttonSetDefaultValue.setPrefWidth(171);

    // Checkboxes for Primary Key and Disallow Null options
    CheckBox checkBoxPrimaryKey = new CheckBox("Primary Key");
    checkBoxPrimaryKey.setLayoutX(755);
    checkBoxPrimaryKey.setLayoutY(207);

    CheckBox checkBoxDisallowNull = new CheckBox("Disallow Null");
    checkBoxDisallowNull.setLayoutX(755);
    checkBoxDisallowNull.setLayoutY(261);

    // Setting the layout and properties for the Create DDL button
    buttonCreateDDL.setLayoutX(369);
    buttonCreateDDL.setLayoutY(444);
    buttonCreateDDL.setPrefHeight(20);
    buttonCreateDDL.setPrefWidth(150);
    buttonCreateDDL.setDisable(true);

    // Labels for the All Tables and Field List sections
    Label labelAllTables = new Label("All Tables");
    labelAllTables.setLayoutX(127);
    labelAllTables.setLayoutY(39);

    Label labelFieldList = new Label("Field List");
    labelFieldList.setLayoutX(428);
    labelFieldList.setLayoutY(39);

    // Radio buttons for selecting the data type
    RadioButton radioButtonVarchar = new RadioButton("Varchar");
    radioButtonVarchar.setLayoutX(618);
    radioButtonVarchar.setLayoutY(133);

    RadioButton radioButtonBoolean = new RadioButton("Boolean");
    radioButtonBoolean.setLayoutX(618);
    radioButtonBoolean.setLayoutY(206);

    RadioButton radioButtonInteger = new RadioButton("Integer");
    radioButtonInteger.setLayoutX(618);
    radioButtonInteger.setLayoutY(277);

    RadioButton radioButtonDouble = new RadioButton("Double");
    radioButtonDouble.setLayoutX(618);
    radioButtonDouble.setLayoutY(351);

    // Text fields for entering Varchar length and default value
    TextField textFieldVarcharLength = new TextField();
    textFieldVarcharLength.setId("textFieldVarcharLength");
    textFieldVarcharLength.setLayoutX(717);
    textFieldVarcharLength.setLayoutY(65);
    textFieldVarcharLength.setPrefHeight(67);
    textFieldVarcharLength.setPrefWidth(171);
    textFieldVarcharLength.setStyle("-fx-alignment: center;");

    TextField textFieldDefaultValue = new TextField();
    textFieldDefaultValue.setId("textFieldDefaultValue");
    textFieldDefaultValue.setLayoutX(715);
    textFieldDefaultValue.setLayoutY(327);
    textFieldDefaultValue.setPrefHeight(67);
    textFieldDefaultValue.setPrefWidth(171);
    textFieldDefaultValue.setStyle("-fx-alignment: center;");

    // TODO: Add remaining UI setup and event handling code

    // Adding all UI elements to the root pane
    root
      .getChildren()
      .addAll(
        menuBar,
        listViewTables,
        listViewFieldList,
        buttonDefineRelations,
        buttonCreateDDL,
        labelAllTables,
        labelFieldList,
        radioButtonVarchar,
        radioButtonBoolean,
        radioButtonInteger,
        radioButtonDouble,
        textFieldVarcharLength,
        textFieldDefaultValue,
        buttonSetVarcharLength,
        buttonSetDefaultValue,
        checkBoxPrimaryKey,
        checkBoxDisallowNull
      );

    // Setting the action for the Open File menu item
    menuOpenFile.setOnAction(menuListener);

    // Adding a listener to update the field list when a table is selected
    listViewTables
      .getSelectionModel()
      .selectedItemProperty()
      .addListener(FieldListListener);

    // Creating the scene and setting the CSS styles
    Scene scene = new Scene(root);
    scene
      .getStylesheets()
      .add(getClass().getResource("../style/style.css").toExternalForm());
    scene.getRoot().getStyleClass().add("title-bar");

    // Setting the scene to the stage and displaying the stage
    stage.setScene(scene);
    stage.show();
  }

  // Getter and setter methods for the UI elements and stage

  public ListView<String> getListViewTables() {
    return listViewTables;
  }

  public void setListViewTables(ListView<String> listViewTables) {
    this.listViewTables = listViewTables;
  }

  public ListView<String> getListViewFieldList() {
    return listViewFieldList;
  }

  public void setListViewFieldList(ListView<String> listViewFieldList) {
    this.listViewFieldList = listViewFieldList;
  }

  public MenuItem getMenuOpenFile() {
    return menuOpenFile;
  }

  public void setMenuOpenEdgeFile(MenuItem menuOpenFile) {
    this.menuOpenFile = menuOpenFile;
  }

  public MenuItem getMenuSave() {
    return menuSave;
  }

  public void setMenuSave(MenuItem menuSave) {
    this.menuSave = menuSave;
  }

  public MenuItem getMenuSaveAs() {
    return menuSaveAs;
  }

  public void setMenuSaveAs(MenuItem menuSaveAs) {
    this.menuSaveAs = menuSaveAs;
  }

  public MenuItem getMenuExit() {
    return menuExit;
  }

  public void setMenuExit(MenuItem menuExit) {
    this.menuExit = menuExit;
  }

  public MenuItem getMenuShowDatabase() {
    return menuShowDatabase;
  }

  public void setMenuShowDatabase(MenuItem menuShowDatabase) {
    this.menuShowDatabase = menuShowDatabase;
  }

  public Button getButtonCreateDDL() {
    return buttonCreateDDL;
  }

  public void setButtonCreateDDL(Button buttonCreateDDL) {
    this.buttonCreateDDL = buttonCreateDDL;
  }

  public Stage getStage() {
    return this.stage;
  }
}
