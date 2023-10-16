package edu.rit.edgeconverter.model;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Table {
  private int numFigure;
  private String tableName;
  private List<Integer> nativeFieldsList;
  private List<Integer> relatedTablesList;
  private int[] nativeFields, relatedTables, relatedFields;
  private final Logger logger = Logger.getLogger(Field.class.getName());

  private int numConnector, endPoint1, endPoint2;
  private String endStyle1, endStyle2;
  private boolean isEP1Field, isEP2Field, isEP1Table, isEP2Table;

  // Add connector functionality with constructor
  public Table(String inputString) {
    // TODO: change the "|"
    try {
      StringTokenizer st = new StringTokenizer(inputString, "|");
      numFigure = Integer.parseInt(st.nextToken());
      tableName = st.nextToken();
    } catch (NullPointerException | NumberFormatException | NoSuchElementException e) {
      logger.log(Level.WARNING, "Error parsing input string.", e);
      numFigure = 0;
      tableName = "";
    }

    relatedTablesList = new ArrayList<>();
    nativeFieldsList = new ArrayList<>();
    isEP1Field = false;
    isEP2Field = false;
    isEP1Table = false;
    isEP2Table = false;
  }

  public Table(String inputString, String connector) {
    try {
      StringTokenizer st = new StringTokenizer(inputString, "|");
      numConnector = Integer.parseInt(st.nextToken());
      endPoint1 = Integer.parseInt(st.nextToken());
      endPoint2 = Integer.parseInt(st.nextToken());
      endStyle1 = st.nextToken();
      endStyle2 = st.nextToken();
    } catch (NullPointerException | NumberFormatException | NoSuchElementException e) {
      logger.log(Level.WARNING, "Error parsing input string.", e);
      numConnector = 0;
      endPoint1 = 0;
      endPoint2 = 0;
      endStyle1 = "";
      endStyle2 = "";
    }

    isEP1Field = false;
    isEP2Field = false;
    isEP1Table = false;
    isEP2Table = false;
  }

  public int getNumFigure() {
    return numFigure;
  }

  public String getTableName() {
    return tableName;
  }

  public List<Integer> getAllNativeFields() {
    return nativeFieldsList;
  }

  public List<Integer> getAllRelatedTables() {
    return relatedTablesList;
  }

  public void setAllRelatedTables(List<Integer> relatedTablesList) {
    this.relatedTablesList = relatedTablesList;
  }

  public int[] getNativeFields() {
    return nativeFields;
  }

  public void setNativeFields(int[] nativeFields) {
    this.nativeFields = nativeFields;
  }

  public void addNativeField(int value) {
    nativeFieldsList.add(value);
  }

  public int[] getRelatedTables() {
    return relatedTables;
  }

  public void setRelatedTables(int[] relatedTables) {
    this.relatedTables = relatedTables;
  }

  public void addRelatedTable(int relatedTable) {
    relatedTablesList.add(relatedTable);
  }

  public int[] getRelatedFields() {
    return relatedFields;
  }

  public void setRelatedFields(int[] relatedFields) {
    this.relatedFields = relatedFields;
  }

  public void setRelatedField(int index, int relatedValue) {
    relatedFields[index] = relatedValue;
  }

  public void makeArrays() { // convert the ArrayLists into int[]
    Integer[] temp;
    temp = (Integer[]) nativeFieldsList.toArray(new Integer[nativeFieldsList.size()]);
    nativeFields = new int[temp.length];
    for (int i = 0; i < temp.length; i++) {
      nativeFields[i] = temp[i];
    }

    temp = (Integer[]) relatedTablesList.toArray(new Integer[relatedTablesList.size()]);
    relatedTables = new int[temp.length];
    for (int i = 0; i < temp.length; i++) {
      relatedTables[i] = temp[i];
    }

    relatedFields = new int[nativeFields.length];
    Arrays.fill(relatedFields, 0);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Table: ").append(numFigure).append("\r\n");
    sb.append("{\r\n");
    sb.append("TableName: ").append(tableName).append("\r\n");
    sb.append("NativeFields: ");
    for (int i = 0; i < nativeFields.length; i++) {
      sb.append(nativeFields[i]);
      if (i < (nativeFields.length - 1)) {
        sb.append("|");
      }
    }
    sb.append("\r\nRelatedTables: ");
    for (int i = 0; i < relatedTables.length; i++) {
      sb.append(relatedTables[i]);
      if (i < (relatedTables.length - 1)) {
        sb.append("|");
      }
    }
    sb.append("\r\nRelatedFields: ");
    for (int i = 0; i < relatedFields.length; i++) {
      sb.append(relatedFields[i]);
      if (i < (relatedFields.length - 1)) {
        sb.append("|");
      }
    }
    sb.append("\r\n}\r\n");

    return sb.toString();

  }

  /*
   * Connector - methods
   *
   */
  public int getNumConnector() {
    return numConnector;
  }

  public void setNumConnector(int numConnector) {
    this.numConnector = numConnector;
  }

  public int getEndPoint1() {
    return endPoint1;
  }

  public void setEndPoint1(int endPoint1) {
    this.endPoint1 = endPoint1;
  }

  public int getEndPoint2() {
    return endPoint2;
  }

  public void setEndPoint2(int endPoint2) {
    this.endPoint2 = endPoint2;
  }

  public String getEndStyle1() {
    return endStyle1;
  }

  public void setEndStyle1(String endStyle1) {
    this.endStyle1 = endStyle1;
  }

  public String getEndStyle2() {
    return endStyle2;
  }

  public void setEndStyle2(String endStyle2) {
    this.endStyle2 = endStyle2;
  }

  public boolean isEP1Field() {
    return isEP1Field;
  }

  public void setIsEP1Field(boolean field1) {
    isEP1Field = field1;
  }

  public boolean isEP2Field() {
    return isEP2Field;
  }

  public void setIsEP2Field(boolean field2) {
    isEP2Field = field2;
  }

  public boolean isEP1Table() {
    return isEP1Table;
  }

  public void setIsEP1Table(boolean table1) {
    isEP1Table = table1;
  }

  public boolean isEP2Table() {
    return isEP2Table;
  }

  public void setIsEP2Table(boolean table2) {
    isEP2Table = table2;
  }
}