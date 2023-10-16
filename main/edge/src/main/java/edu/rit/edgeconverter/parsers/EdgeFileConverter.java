package edu.rit.edgeconverter.parsers;

import edu.rit.edgeconverter.model.Field;
import edu.rit.edgeconverter.model.Table;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeFileConverter extends FileParser {

  private String filename = "test.edg";
  private File parseFile;
  private FileReader fr;
  private BufferedReader bufferedReader;
  private String currentLine;
  private ArrayList<Table> allTables;
  private ArrayList<Field> allFields;
  private ArrayList<Table> allConnectors;
  private Table[] tables;
  private Field[] fields;
  private Field tempField;
  private Table[] connectors;
  private String style;
  private String text;
  private String tableName;
  private String fieldName;
  private boolean isEntity, isAttribute, isUnderlined = false;
  private int numFigure, numConnector, numFields, numTables, numNativeRelatedFields;
  private int endPoint1, endPoint2;
  private int numLine;
  private String endStyle1, endStyle2;
  public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
  public static final String SAVE_ID = "EdgeConvert Save File"; //first line of save files should be this
  public static final String DELIM = "|";
  private static final Logger log = LogManager.getLogger("edu.rit");

  public EdgeFileConverter(File fileToParse) {
    log.info("Constructing EdgeFileConverter . . .");
    numFigure = 0;
    numConnector = 0;
    allTables = new ArrayList<>();
    allFields = new ArrayList<>();
    allConnectors = new ArrayList<>();
    isEntity = false;
    isAttribute = false;
    parseFile = fileToParse;
    numLine = 0;
    this.openFile(parseFile);
  }

  @Override
  public void makeArrays() {
    if (allTables != null) {
      tables = (Table[]) allTables.toArray(new Table[allTables.size()]);
    }
    if (allFields != null) {
      fields = (Field[]) allFields.toArray(new Field[allFields.size()]);
    }
    if (allConnectors != null) {
      connectors =
        (Table[]) allConnectors.toArray(new Table[allConnectors.size()]);
    }
  }

  @Override
  public void parseFile() throws IOException {
    log.info("Parsing file . . .");
    while ((currentLine = bufferedReader.readLine()) != null) {
      currentLine = currentLine.trim();
      if (currentLine.startsWith("Figure ")) { //this is the start of a Figure entry
        numFigure =
          Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
        currentLine = bufferedReader.readLine().trim(); // this should be "{"
        currentLine = bufferedReader.readLine().trim();
        currentLine = bufferedReader.readLine().trim(); // this should be "SheetNumber 1"
        if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
          continue;
        } else {
          style =
            currentLine.substring(
              currentLine.indexOf("\"") + 1,
              currentLine.lastIndexOf("\"")
            ); //get the Style parameter
          if (style.startsWith("Relation")) { //presence of Relations implies lack of normalization
            JOptionPane.showMessageDialog(
              null,
              "The Edge Diagrammer file\n" +
              parseFile +
              "\ncontains relations.  Please resolve them and try again."
            );
            //                        ConverterGUI.setReadSuccess(false);
            log.warn(
              "The file\"" +
              parseFile +
              "\" contains relations. Resolve them and try again."
            );
            break;
          }
          if (style.startsWith("Entity")) {
            isEntity = true;
          }
          if (style.startsWith("Attribute")) {
            isAttribute = true;
          }
          if (!(isEntity || isAttribute)) { //these are the only Figures we're interested in
            continue;
          }
          currentLine = bufferedReader.readLine().trim(); //this should be Text
          text =
            currentLine
              .substring(
                currentLine.indexOf("\"") + 1,
                currentLine.lastIndexOf("\"")
              )
              .replaceAll(" ", ""); //get the Text parameter
          if (text.equals("")) {
            JOptionPane.showMessageDialog(
              null,
              "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again."
            );
            //                        ConverterGUI.setReadSuccess(false);
            log.warn(
              "Entities or attributes found without a name. Please fill out and try again."
            );
            break;
          }
          int escape = text.indexOf("\\");
          if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
            text = text.substring(0, escape);
          }

          do { //advance to end of record, look for whether the text is underlined
            currentLine = bufferedReader.readLine().trim();
            if (currentLine.startsWith("TypeUnderl")) {
              isUnderlined = true;
            }
          } while (!currentLine.equals("}")); // this is the end of a Figure entry

          if (isEntity) { //create a new EdgeTable object and add it to the allTables ArrayList
            if (isTableDup(text)) {
              JOptionPane.showMessageDialog(
                null,
                "There are multiple tables called " +
                text +
                " in this diagram.\nPlease rename all but one of them and try again."
              );
              //                            ConverterGUI.setReadSuccess(false);
              log.warn(
                "Duplicate tables \"" +
                text +
                "\" found in this diagram. Rename them and try again.\""
              );
              break;
            }
            allTables.add(new Table(numFigure + DELIM + text));
          }
          if (isAttribute) { //create a new EdgeField object and add it to the allFields ArrayList
            tempField = new Field(numFigure + DELIM + text);
            tempField.setIsPrimaryKey(isUnderlined);
            allFields.add(tempField);
          }
          //reset flags
          isEntity = false;
          isAttribute = false;
          isUnderlined = false;
        }
      } // if("Figure")
      if (currentLine.startsWith("Connector ")) { //this is the start of a Connector entry
        numConnector =
          Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Connector number
        currentLine = bufferedReader.readLine().trim(); // this should be "{"
        currentLine = bufferedReader.readLine().trim(); // not interested in SheetNumber 1
        currentLine = bufferedReader.readLine().trim(); // not interested in Style
        currentLine = bufferedReader.readLine().trim(); // Figure1
        endPoint1 =
          Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
        currentLine = bufferedReader.readLine().trim(); // Figure2
        endPoint2 =
          Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
        currentLine = bufferedReader.readLine().trim(); // not interested in EndPoint1
        currentLine = bufferedReader.readLine().trim(); // not interested in EndPoint2
        currentLine = bufferedReader.readLine().trim(); // not interested in SuppressEnd1
        currentLine = bufferedReader.readLine().trim(); // not interested in SuppressEnd2
        currentLine = bufferedReader.readLine().trim(); // End1
        endStyle1 =
          currentLine.substring(
            currentLine.indexOf("\"") + 1,
            currentLine.lastIndexOf("\"")
          ); //get the End1 parameter
        currentLine = bufferedReader.readLine().trim(); // End2
        endStyle2 =
          currentLine.substring(
            currentLine.indexOf("\"") + 1,
            currentLine.lastIndexOf("\"")
          ); //get the End2 parameter

        do { //advance to end of record
          currentLine = bufferedReader.readLine().trim();
        } while (!currentLine.equals("}")); // this is the end of a Connector entry

        allConnectors.add(
          new Table(
            numConnector +
            DELIM +
            endPoint1 +
            DELIM +
            endPoint2 +
            DELIM +
            endStyle1 +
            DELIM +
            endStyle2,
            ""
          )
        );
      } // if("Connector")
    } // while()
    log.info("File parsed.");
  }

  @Override
  public void resolveConnectors() {
    int endPoint1, endPoint2;
    int fieldIndex = 0, table1Index = 0, table2Index = 0;
    for (int cIndex = 0; cIndex < connectors.length; cIndex++) {
      endPoint1 = connectors[cIndex].getEndPoint1();
      endPoint2 = connectors[cIndex].getEndPoint2();
      fieldIndex = -1;
      for (int fIndex = 0; fIndex < fields.length; fIndex++) { //search fields array for endpoints
        if (endPoint1 == fields[fIndex].getNumFigure()) { //found endPoint1 in fields array
          connectors[cIndex].setIsEP1Field(true); //set appropriate flag
          fieldIndex = fIndex; //identify which element of the fields array that endPoint1 was found in
        }
        if (endPoint2 == fields[fIndex].getNumFigure()) { //found endPoint2 in fields array
          connectors[cIndex].setIsEP2Field(true); //set appropriate flag
          fieldIndex = fIndex; //identify which element of the fields array that endPoint2 was found in
        }
      }
      for (int tIndex = 0; tIndex < tables.length; tIndex++) { //search tables array for endpoints
        if (endPoint1 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
          connectors[cIndex].setIsEP1Table(true); //set appropriate flag
          table1Index = tIndex; //identify which element of the tables array that endPoint1 was found in
        }
        if (endPoint2 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
          connectors[cIndex].setIsEP2Table(true); //set appropriate flag
          table2Index = tIndex; //identify which element of the tables array that endPoint2 was found in
        }
      }

      if (connectors[cIndex].isEP1Field() && connectors[cIndex].isEP2Field()) { //both endpoints are fields, implies lack of normalization
        JOptionPane.showMessageDialog(
          null,
          "The Edge Diagrammer file\n" +
          parseFile +
          "\ncontains composite attributes. Please resolve them and try again."
        );
        //                ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
        log.warn(
          "The file \"" +
          parseFile +
          "\" contains composite attributes. Resolve and try again."
        );
        break; //stop processing list of Connectors
      }

      if (connectors[cIndex].isEP1Table() && connectors[cIndex].isEP2Table()) { //both endpoints are tables
        if (
          (connectors[cIndex].getEndStyle1().indexOf("many") >= 0) &&
          (connectors[cIndex].getEndStyle2().indexOf("many") >= 0)
        ) { //the connector represents a many-many relationship, implies lack of normalization
          JOptionPane.showMessageDialog(
            null,
            "There is a many-many relationship between tables\n\"" +
            tables[table1Index].getTableName() +
            "\" and \"" +
            tables[table2Index].getTableName() +
            "\"" +
            "\nPlease resolve this and try again."
          );
          //                    ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
          log.warn(
            "MANY-MANY relationship found between: TABLE 1: \"" +
            tables[table1Index].getTableName() +
            "\" - TABLE 2: \"" +
            tables[table2Index].getTableName() +
            "\". Resolve and try again."
          );
          break; //stop processing list of Connectors
        } else { //add Figure number to each table's list of related tables
          tables[table1Index].addRelatedTable(
              tables[table2Index].getNumFigure()
            );
          tables[table2Index].addRelatedTable(
              tables[table1Index].getNumFigure()
            );
          continue; //next Connector
        }
      }

      if (fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0) { //field has not been assigned to a table yet
        if (connectors[cIndex].isEP1Table()) { //endpoint1 is the table
          tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
          fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
        } else { //endpoint2 is the table
          tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
          fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
        }
      } else if (fieldIndex >= 0) { //field has already been assigned to a table
        JOptionPane.showMessageDialog(
          null,
          "The attribute " +
          fields[fieldIndex].getName() +
          " is connected to multiple tables.\nPlease resolve this and try again."
        );
        //                ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
        log.warn(
          "The attribute " +
          fields[fieldIndex].getName() +
          " is connected to multiple tables. Resolve and try again."
        );
        break; //stop processing list of Connectors
      }
    } // connectors for() loop
  }

  @Override
  public void parseSaveFile() {
    
  }

  @Override
  public Field[] getFields() {
    return fields;
  }

  @Override
  public Table[] getTables() {
    return tables;
  }

  @Override
  public boolean isTableDup(String testTableName) {
    for (Table table : allTables) {
      if (table.getTableName().equals(testTableName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void openFile(File file) {
    try {
      log.info("Opening file");
      bufferedReader = new BufferedReader(new FileReader(file));
      //test for what kind of file we have
      currentLine = bufferedReader.readLine().trim();

      if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
        this.parseFile(); //parse the file
        bufferedReader.close();
        this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
        this.resolveConnectors(); //Identify nature of Connector endpoints
      } else {
        if (currentLine.startsWith(SAVE_ID)) { //the file chosen is a Save file created by this application
          this.parseSaveFile(); //parse the file
          bufferedReader.close();
          this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
        } else { //the file chosen is something else
          log.warn("Unrecognized file format");
          JOptionPane.showMessageDialog(null, "Unrecognized file format");
        }
      }
      log.info("File Opened");
    } catch (FileNotFoundException fnfe) { // try
      log.fatal("Cannot find file specified: \"" + file.getName() + "\".");
      System.exit(0);
    } catch (IOException ioe) { // catch FileNotFoundException
      log.fatal("Issue with reading the file: " + ioe.getMessage());
      System.exit(0);
    } // catch IOException
  }
}
