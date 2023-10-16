package edu.rit.edgeconverter.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rit.edgeconverter.model.Field;
import edu.rit.edgeconverter.model.Table;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONFileParser extends FileParser {
    private File parseFile;
    private BufferedReader bufferedReader;
    private int numFigure, numConnector;
    private String line;
    private boolean isEntity, isAttribute, isUnderlined = false;
    private ArrayList<Table> allTables;
    private ArrayList<Field> allFields;
    private ArrayList<Table> allConnectors;
    private Table[] tables;
    private Field[] fields;
    private Table[] connectors;
    private Field tempField;
    public static final String DELIM = "|";

    public JSONFileParser(File fileToParse){
        numFigure = 0;
        numConnector = 0;
        allTables = new ArrayList<>();
        allFields = new ArrayList<>();
        allConnectors = new ArrayList<>();
        isEntity = false;
        isAttribute = false;
        parseFile = fileToParse;
        this.openFile(parseFile);
    }

    @Override
    public void makeArrays() {
        if (allTables != null) {
            tables = (Table[])allTables.toArray(new Table[allTables.size()]);
        }
        if (allFields != null) {
            fields = (Field[])allFields.toArray(new Field[allFields.size()]);
        }
        if (allConnectors != null) {
            connectors = (Table[])allConnectors.toArray(new Table[allConnectors.size()]);
        }
    }

    @Override
    public void parseFile() throws IOException {
        JsonArray jsonArray = JsonParser.parseReader(bufferedReader).getAsJsonArray();

        Pattern figurePattern = Pattern.compile("Figure (\\d+)"); // for getting the figure number
        Pattern connectorPattern = Pattern.compile("Connector (\\d+)"); // for getting the figure number


        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String jsonKey = jsonObject.keySet().iterator().next();

            if (jsonKey.startsWith("Figure ")) {
                System.out.println("This is a figure!");

                // Extract the figure number from the key
                Matcher matcher = figurePattern.matcher(jsonKey);

                if (matcher.find()) {
                    numFigure = Integer.parseInt(matcher.group(1));
                }
                String style;
                String text;
                if (jsonObject.has("Text")) {
                    text = jsonObject.get("Text").getAsString();
                    System.out.println(text);
                } else {
                    text = " ";
                }

                if (jsonObject.has("Style")) {
                    style = jsonObject.get("Style").getAsString();
                } else {
                    style = " ";
                }

                if (style.equals("Label")) {
                    continue;
                } else {
                    if (style.equals("Entity")) {
                        isEntity = true;
                    }
                    if (style.equals("Attribute")) {
                        isAttribute = true;
                    }
                    if (!(isEntity || isAttribute)) {
                        continue;
                    }
                    if (text.isEmpty() || text.isBlank()) {
//                    JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
//                    ConverterGUI.setReadSuccess(false);
                        System.out.println("There are entities or attributes with blank names!!");
                        break;
                    }

                    int escape = text.indexOf("\\");
                    if (escape > 0) { //Edge denotes a line break as "\line", disregard anything after a backslash
                        text = text.substring(0, escape);
                    }

//                do { //advance to end of record, look for whether the text is underlined
//                    currentLine = bufferedReader.readLine().trim();
//                    if (currentLine.startsWith("TypeUnderl")) {
//                        isUnderlined = true;
//                    }
//                } while (!currentLine.equals("}")); // this is the end of a Figure entry



                    if (isEntity) {
                        if (isTableDup(text)) {
//                        JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
//                        ConverterGUI.setReadSuccess(false);
                            System.out.println("The table is a duplicate");
                            break;
                        }
                        allTables.add(new Table(numFigure + DELIM + text));
                    }

                    if (isAttribute) {
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

            if (jsonKey.startsWith("Connector ")) {
                System.out.println("This is a connector!");

                // Extract the figure number from the key
                Matcher matcher = connectorPattern.matcher(jsonKey);

                if (matcher.find()) {
                    numConnector = Integer.parseInt(matcher.group(1));
                }

                if (jsonObject.has("Figure1") && jsonObject.has("Figure2") &&
                        jsonObject.has("End1") && jsonObject.has("End2")) {
                    String endPoint1 = jsonObject.get("Figure1").getAsString();
                    String endPoint2 = jsonObject.get("Figure2").getAsString();
                    String endStyle1 = jsonObject.get("End1").getAsString();
                    String endStyle2 = jsonObject.get("End2").getAsString();

                    allConnectors.add(new Table(numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2,""));
                }}
        }
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
                JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains composite attributes. Please resolve them and try again.");
//                ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }

            if (connectors[cIndex].isEP1Table() && connectors[cIndex].isEP2Table()) { //both endpoints are tables
                if ((connectors[cIndex].getEndStyle1().indexOf("many") >= 0) &&
                        (connectors[cIndex].getEndStyle2().indexOf("many") >= 0)) { //the connector represents a many-many relationship, implies lack of normalization
                    JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getTableName() + "\" and \"" + tables[table2Index].getTableName() + "\"" + "\nPlease resolve this and try again.");
//                    ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                    break; //stop processing list of Connectors
                } else { //add Figure number to each table's list of related tables
                    tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
                    tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
                    continue; //next Connector
                }
            }

            if (fieldIndex >=0 && fields[fieldIndex].getTableID() == 0) { //field has not been assigned to a table yet
                if (connectors[cIndex].isEP1Table()) { //endpoint1 is the table
                    tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
                } else { //endpoint2 is the table
                    tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
                    fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
                }
            } else if (fieldIndex >=0) { //field has already been assigned to a table
                JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
//                ConverterGUI.setReadSuccess(false); //this tells GUI not to populate JList components
                break; //stop processing list of Connectors
            }

        }


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

            bufferedReader = new BufferedReader(new FileReader(file));
            this.parseFile();
            bufferedReader.close();
            this.makeArrays();
            this.resolveConnectors();
            for (int i = 0; i < tables.length; i++) {
                System.out.println(tables[i].getTableName());
                System.out.println(Arrays.toString(fields));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
