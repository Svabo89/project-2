package parsers;

import model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFileParser extends FileParser {

    private static final Logger logger = LogManager.getLogger();
    private Table[] tables;
    private Field[] fields;
    private File parseFile;
    private BufferedReader br;
    private int numLine;
    public static final String SAVE_ID = "Save Diagram File";
    private String currentLine;
    private ArrayList<Table> alTables;
    private ArrayList<Field> alFields;

    public SaveFileParser(File constructorFile) {
        alTables = new ArrayList<>();
        alFields = new ArrayList<>();
        this.parseFile = constructorFile;
        numLine = 0;
        this.openFile(this.parseFile);
    }

    @Override
    public void parseFile() {
        int numFigure;
        int numFields;
        int numTables;
        String tableName;
        String fieldName;
        StringTokenizer stTables;
        StringTokenizer stNatFields;
        StringTokenizer stRelFields;
        StringTokenizer stField;
        Table tempTable;
        Field tempField;
        try {
            currentLine = br.readLine();
            currentLine = br.readLine(); //this should be "Table: "
            while (currentLine.startsWith("Table: ")) {
                numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Table number
                currentLine = br.readLine(); //this should be "{"

                currentLine = br.readLine(); //this should be "TableName"

                tableName = currentLine.substring(currentLine.indexOf(" ") + 1);
                tempTable = new Table(numFigure + DELIM + tableName);

                currentLine = br.readLine(); //this should be the NativeFields list

                stNatFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
                numFields = stNatFields.countTokens();
                for (int i = 0; i < numFields; i++) {
                    tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
                }

                currentLine = br.readLine(); //this should be the RelatedTables list
                stTables = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
                numTables = stTables.countTokens();
                for (int i = 0; i < numTables; i++) {
                    tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
                }
                tempTable.makeArrays();
                currentLine = br.readLine();
                stRelFields = new StringTokenizer(currentLine.substring(currentLine.indexOf(" ") + 1), DELIM);
                numFields = stRelFields.countTokens();

                for (int i = 0; i < numFields; i++) {
                    tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
                }

                alTables.add(tempTable);
                currentLine = br.readLine(); //this should be "}"
                currentLine = br.readLine(); //this should be "\n"
                currentLine = br.readLine(); //this should be either the next "Table: ", #Fields#
            }
            while ((currentLine = br.readLine()) != null) {
                stField = new StringTokenizer(currentLine, DELIM);
                numFigure = Integer.parseInt(stField.nextToken());
                fieldName = stField.nextToken();
                tempField = new Field(numFigure + DELIM + fieldName);
                tempField.setTableID(Integer.parseInt(stField.nextToken()));
                tempField.setTableBound(Integer.parseInt(stField.nextToken()));
                tempField.setFieldBound(Integer.parseInt(stField.nextToken()));
                tempField.setDataType(Integer.parseInt(stField.nextToken()));
                tempField.setVarcharValue(Integer.parseInt(stField.nextToken()));
                tempField.setIsPrimaryKey(Boolean.parseBoolean(stField.nextToken()));
                tempField.setDisallowNull(Boolean.parseBoolean(stField.nextToken()));
                if (stField.hasMoreTokens()) { //Default Value may not be defined
                    tempField.setDefaultValue(stField.nextToken());
                }
                alFields.add(tempField);

            }
        } catch (IOException ioe) {
            logger.error("Exception occurred", ioe);
        }
    } // parseSaveFile() 

    @Override
    public Table[] getTables() {
        return tables;
    }

    @Override
    public Field[] getFields() {
        return fields;
    }

    @Override
    public void makeArrays() { //convert ArrayList objects into arrays of the appropriate Class type

        if (alTables != null) {

            tables = alTables.toArray(new Table[alTables.size()]);
        }
        if (alFields != null) {
            fields = (Field[]) alFields.toArray(new Field[alFields.size()]);
        }
    }

    @Override
    public void openFile(File inputFile) {
        try {
            super.openFile(inputFile);
            super.getFileReader();
            br = super.getBufferedReader();
            //test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(SAVE_ID)) {
                this.parseFile(); //parse the file
                br.close();
                this.makeArrays();
            } else {
                JOptionPane.showMessageDialog(null, "Unrecognized file format");

            }

        } // try
        catch (IOException ioe) {
            logger.error("Exception occurred", ioe);
            System.exit(0);
        }// catch IOException
    } // openFile()
}
