package parsers;

import model.Connector;
import view.ConvertGUI;
import model.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertFileParser extends FileParser {

    private int numFigure;
    private String text;
    private String endStyle1;
    private String endStyle2;
    private int endPoint1;
    private int endPoint2;
    private static final Logger logger = LogManager.getLogger();
    private final File parseFile;
    private BufferedReader br;
    private String currentLine;
    private Table[] tables;
    private Field[] fields;
    private Connector[] connectors;
    private boolean isEntity = false;
    private boolean isAttribute = false;
    private boolean isUnderlined = false;
    private final ArrayList<Table> alTables = new ArrayList<>();
    private final ArrayList<Field> alFields = new ArrayList<>();
    private final ArrayList<Connector> alConnectors = new ArrayList<>();
    private int numConnector;
    private int numLine;
    public static final String EDGE_ID = "EDGE Diagram File"; //first line of .edg files should be this
    public static final String SAVE_ID = "Save Diagram File"; //first line of save files should be this

    public ConvertFileParser(File constructorFile) {
        numFigure = 0;
        numConnector = 0;
        isEntity = false;
        isAttribute = false;
        parseFile = constructorFile;
        numLine = 0;
        this.openFile(parseFile);
    }

    @Override
    public void parseFile() {
        boolean figureCheck = false;
        boolean connectorCheck = false;
        int index = 0;
        Map<Integer, String> section = new HashMap<>();
        try {
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.startsWith("Figure ")) {
                    figureCheck = true;
                } else if (currentLine.startsWith("Connector ")) {
                    connectorCheck = true;
                }
                if (figureCheck || connectorCheck) {
                    section.put(index, currentLine);
                    index++;
                }
                if (currentLine.startsWith("}")) {
                    if (figureCheck) {
                        figureCheck = false;
                        this.figureParse(section);
                    } else if (connectorCheck) {
                        connectorCheck = false;
                        this.connectorParse(section);
                    }
                    section.clear();
                    index = 0;
                }
            }
        } catch (IOException ioe) {
            logger.error("Exception occurred", ioe);
        }
    } // parseFile()

    private void resolveConnectors() {
        endPoint1 = 0;
        endPoint2 = 0;
        int fieldIndex = 0;
        int table1Index = 0;
        int table2Index = 0;
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

            if (connectors[cIndex].getIsEP1Field() && connectors[cIndex].getIsEP2Field()) { //both endpoints are fields, implies lack of normalization
                JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains composite attributes. Please resolve them and try again.");
                ConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }

            if (connectors[cIndex].getIsEP1Table() && connectors[cIndex].getIsEP2Table()) { //both endpoints are tables
                if ((connectors[cIndex].getEndStyle1().indexOf("many") >= 0)
                        && (connectors[cIndex].getEndStyle2().indexOf("many") >= 0)) { //the connector represents a many-many relationship, implies lack of normalization
                    JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getName() + "\" and \"" + tables[table2Index].getName() + "\"" + "\nPlease resolve this and try again.");
                    ConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                    break; //stop processing list of Connectors
                } else { //add Figure number to each table's list of related tables
                    tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
                    tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
                    continue; //next Connector
                }
            }

            if (fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0) { //field has not been assigned to a table yet
                if (connectors[cIndex].getIsEP1Table()) { //endpoint1 is the table
                    tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
                } else { //endpoint2 is the table
                    tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
                }
            } else if (fieldIndex >= 0) { //field has already been assigned to a table
                JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
                ConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }
        }
    }

    public void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type
        tables = (Table[]) alTables.toArray(new Table[alTables.size()]);
        fields = alFields.toArray(new Field[alFields.size()]);
        connectors = alConnectors.toArray(new Connector[alConnectors.size()]);

    }

    private boolean isTableDup(String testTableName) {

        for (int i = 0; i < alTables.size(); i++) {
            Table tempTable = (Table) alTables.get(i);
            if (tempTable.getName().equals(testTableName)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public Table[] getTables() {

        return tables;

    }

    @Override
    public Field[] getFields() {

        return fields;

    }

    @Override
    public void openFile(File inputFile) {

        try {
            super.openFile(inputFile);
            br = super.getBufferedReader();
            //test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(EDGE_ID)) { //the file chosen is an Edge Diagrammer file
                this.parseFile(); //parse the file
                br.close();
                this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
                this.resolveConnectors(); //Identify nature of Connector endpoints
            } else {
                JOptionPane.showMessageDialog(null, "Unrecognized file format");

            }

        } // try
        catch (FileNotFoundException fnfe) {
            logger.error(String.format("Cannot find %s file", inputFile.getName()), fnfe);
            System.exit(0);
        } // catch FileNotFoundException
        catch (IOException ioe) {
            logger.error("Exception occurred", ioe);
            System.exit(0);
        }// catch IOException
    } // openFile()

    private void figureParse(Map<Integer, String> section) {
        String style;
        numFigure = 0;
        text = null;
        for (Map.Entry<Integer, String> entry : section.entrySet()) {
            if (entry.getKey() == 0) {
                numFigure = Integer.parseInt(entry.getValue().substring(entry.getValue().indexOf(" ") + 1));
            }
            if (entry.getValue().trim().startsWith("Style")) {
                style = entry.getValue().substring(entry.getValue().indexOf("\"") + 1, entry.getValue().lastIndexOf("\""));
                if (style.startsWith("Entity")) {
                    isEntity = true;
                } else if (style.startsWith("Attribute")) {
                    isAttribute = true;
                } else if (style.startsWith("Relation")) {
                    JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains relations.  Please resolve them and try again.");
                    ConvertGUI.setReadSuccess(false);
                    break;
                }
            }
            if (entry.getKey() == 4 && entry.getValue().trim().startsWith("Text")) {
                text = entry.getValue().substring(entry.getValue().indexOf("\"") + 1, entry.getValue().lastIndexOf("\"")).replace(" ", "");
                this.checkText();
            }
            if (entry.getValue().trim().startsWith("TypeUnderl")) {
                isUnderlined = true;
            }
        }
        this.createObjects();
        isEntity = false;
        isAttribute = false;
        isUnderlined = false;
    }

    private void checkText() {
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
            ConvertGUI.setReadSuccess(false);
            return;
        }
        int escape = text.indexOf("\\");
        if (escape > 0) {
            text = text.substring(0, escape);
        }

    }

    private void createObjects() {
        if (isEntity) {
            if (isTableDup(text)) {
                JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
                ConvertGUI.setReadSuccess(false);
                return;
            }
            alTables.add(new Table(numFigure + FileParser.DELIM + text));
        } else if (isAttribute) {
            Field tempField = new Field(numFigure + FileParser.DELIM + text);
            tempField.setIsPrimaryKey(isUnderlined);
            alFields.add(tempField);
        }
    }

    private void connectorParse(Map<Integer, String> section) {

        for (Map.Entry<Integer, String> entry : section.entrySet()) {
            if (entry.getKey() == 0) {
                numConnector = Integer.parseInt(entry.getValue().substring(entry.getValue().indexOf(" ") + 1));
            }
            if (entry.getKey() == 4 && entry.getValue().trim().startsWith("Figure1")) {
                endPoint1 = Integer.parseInt(entry.getValue().substring(entry.getValue().lastIndexOf(" ") + 1));

            }
            if (entry.getKey() == 5 && entry.getValue().trim().startsWith("Figure2")) {
                endPoint2 = Integer.parseInt(entry.getValue().substring(entry.getValue().lastIndexOf(" ") + 1));

            }
            if (entry.getKey() == 10 && entry.getValue().trim().startsWith("End1")) {
                endStyle1 = entry.getValue().substring(entry.getValue().indexOf("\"") + 1, entry.getValue().lastIndexOf("\""));

            }
            if (entry.getKey() == 11 && entry.getValue().trim().startsWith("End2")) {
                endStyle2 = entry.getValue().substring(entry.getValue().indexOf("\"") + 1, entry.getValue().lastIndexOf("\""));
            }
        }
        alConnectors.add(new Connector(numConnector + FileParser.DELIM + endPoint1 + FileParser.DELIM + endPoint2 + FileParser.DELIM + endStyle1 + FileParser.DELIM + endStyle2));
    }

} // EdgeConvertFileHandler
