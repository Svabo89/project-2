package edu.rit.edgeconverter.model;

import edu.rit.edgeconverter.util.DataType;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Field {
  private int numFigure, tableID, tableBound, fieldBound, varcharValue;
  private DataType dataType;
  private String name, defaultValue;
  private boolean disallowNull, isPrimaryKey;
  private final Logger logger = Logger.getLogger(Field.class.getName());

  public Field(String inputString) {
    try {

      StringTokenizer st = new StringTokenizer(inputString, "|");
      numFigure = Integer.parseInt(st.nextToken());
      name = st.nextToken();
    } catch (NullPointerException | NumberFormatException | NoSuchElementException e) {
      logger.log(Level.WARNING, "Error parsing input string.", e);
      numFigure = 0;
      name = "";
    }
    tableID = 0;
    tableBound = 0;
    fieldBound = 0;
    disallowNull = false;
    isPrimaryKey = false;
    defaultValue = "";
    varcharValue = DataType.VARCHAR.getDefaultLength();
    dataType = DataType.VARCHAR;
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

  public boolean isPrimaryKey() {
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

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType type) {
    dataType = type;
  }

  @Override
  public String toString() {
    return numFigure + "|" +
        name + "|" +
        tableID + "|" +
        tableBound + "|" +
        fieldBound + "|" +
        dataType + "|" +
        varcharValue + "|" +
        isPrimaryKey + "|" +
        disallowNull + "|" +
        defaultValue;
  }
}
