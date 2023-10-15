package model;

import parsers.ConvertFileParser;
import java.util.StringTokenizer;

public class Field {

    private int numFigure;
    private int tableID;
    private int dataType;
    private int tableBound;
    private int fieldBound;
    private int varcharValue;
    private String name;
    private String defaultValue;
    private boolean disallowNull;
    private boolean isPrimaryKey;
    private static String[] strDataType = {"Varchar", "Boolean", "Integer", "Double"};
    public static final int VARCHAR_DEFAULT_LENGTH = 1;

    public Field(String inputString) {
        StringTokenizer st = new StringTokenizer(inputString, ConvertFileParser.DELIM);
        numFigure = Integer.parseInt(st.nextToken());
        name = st.nextToken();
        tableID = 0;
        tableBound = 0;
        fieldBound = 0;
        disallowNull = false;
        isPrimaryKey = false;
        defaultValue = "";
        varcharValue = VARCHAR_DEFAULT_LENGTH;
        dataType = 0;

    }

    public int getNumFigure() {

        return numFigure;
    }

    public String getName() {

        return name;
    }

    public int getTableID() {

        return tableID;
    }

    public void setTableID(int value) {

        tableID = value;
    }

    public int getTableBound() {
        return tableBound;
    }

    public void setTableBound(int value) {
        tableBound = value;
    }

    public int getFieldBound() {
        return fieldBound;
    }

    public void setFieldBound(int value) {

        fieldBound = value;
    }

    public boolean getDisallowNull() {

        return disallowNull;
    }

    public void setDisallowNull(boolean value) {

        disallowNull = value;
    }

    public boolean getIsPrimaryKey() {

        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean value) {

        isPrimaryKey = value;
    }

    public String getDefaultValue() {

        return defaultValue;
    }

    public void setDefaultValue(String value) {

        defaultValue = value;
    }

    public int getVarcharValue() {

        return varcharValue;
    }

    public void setVarcharValue(int value) {

        if (value > 0) {
            varcharValue = value;
        }
    }

    public int getDataType() {

        return dataType;
    }

    public void setDataType(int value) {

        if (value >= 0 && value < strDataType.length) {
            dataType = value;
        }
    }

    public static String[] getStrDataType() {

        return strDataType;
    }

    public String toString() {
        return numFigure + ConvertFileParser.DELIM
                + name + ConvertFileParser.DELIM
                + tableID + ConvertFileParser.DELIM
                + tableBound + ConvertFileParser.DELIM
                + fieldBound + ConvertFileParser.DELIM
                + dataType + ConvertFileParser.DELIM
                + varcharValue + ConvertFileParser.DELIM
                + isPrimaryKey + ConvertFileParser.DELIM
                + disallowNull + ConvertFileParser.DELIM
                + defaultValue;
    }
}
