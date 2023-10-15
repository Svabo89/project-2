package parsers;

import model.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marko Vuckovic
 */
public abstract class FileParser {

    private Table[] tables;
    private Field[] fields;
    static final Logger logger = LogManager.getLogger();
    private FileReader fr;
    private BufferedReader br;
    public static final String EDGE_ID = "EDGE Diagram File";
    public static final String DELIM = "|";

    public void parseFile() {
    }

    public void makeArrays() {

    }

    public Table[] getTables() {
        return tables;
    }

    public Field[] getFields() {
        return fields;
    }

    public FileReader getFileReader() {
        return this.fr;
    }

    public BufferedReader getBufferedReader() {
        return this.br;
    }

    public void openFile(File inputFile) {
        try {
            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException fnfe) {
            logger.error(String.format("Cannot find %s file", inputFile.getName()), fnfe);
            System.exit(0);
        }
    }

}
