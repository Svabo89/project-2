package generator;

import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CreateDDL {

    static String[] products = {"MySQL,Oracle"};
    protected Table[] tables; //master copy of Table objects
    protected Field[] fields; //master copy of Field objects
    protected int[] numBoundTables;
    protected int maxBound;
    protected StringBuilder sb;
    protected int selected;
    private static final Logger logger = LogManager.getLogger();

    protected CreateDDL(Table[] tables, Field[] fields) {
        this.tables = tables;
        this.fields = fields;
        initialize();
    } //ConvertCreateDDL(Table[], Field[])

    protected CreateDDL() { //default constructor with empty arg list for to allow output dir to be set before there are table and field objects
    } //ConvertCreateDDL()

    public void initialize() {
        logger.info("initialize method start");
        numBoundTables = new int[tables.length];
        maxBound = 0;
        sb = new StringBuilder();

        for (int i = 0; i < tables.length; i++) { //step through list of tables
            int numBound = 0; //initialize counter for number of bound tables
            int[] relatedFields = tables[i].getRelatedFieldsArray();
            for (int j = 0; j < relatedFields.length; j++) { //step through related fields list
                if (relatedFields[j] != 0) {
                    numBound++; //count the number of non-zero related fields
                }
            }
            numBoundTables[i] = numBound;
            if (numBound > maxBound) {
                maxBound = numBound;
            }
        }
    }

    protected Table getTable(int numFigure) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (numFigure == tables[tIndex].getNumFigure()) {
                return tables[tIndex];
            }
        }
        return null;
    }

    protected Field getField(int numFigure) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (numFigure == fields[fIndex].getNumFigure()) {
                return fields[fIndex];
            }
        }
        return null;
    }

    public abstract String getDatabaseName();

    public abstract String getProductName();

    public abstract String getSQLString();

    public abstract void createDDL();

}//ConvertCreateDDL
